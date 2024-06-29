package com.yelzhan.capstoneproject.service.userServiceTests;

import com.yelzhan.capstoneproject.exception.ResourceAlreadyExistException;
import com.yelzhan.capstoneproject.exception.ResourceNotFoundException;
import com.yelzhan.capstoneproject.model.dto.request.auth.RegistrationRequest;
import com.yelzhan.capstoneproject.model.dto.request.user.ChangePasswordRequest;
import com.yelzhan.capstoneproject.model.dto.request.user.UserUpdateRequest;
import com.yelzhan.capstoneproject.model.entity.Role;
import com.yelzhan.capstoneproject.model.entity.User;
import com.yelzhan.capstoneproject.repository.UserRepository;
import com.yelzhan.capstoneproject.service.RoleService;
import com.yelzhan.capstoneproject.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.yelzhan.capstoneproject.service.implementation.AuthServiceImpl.DEFAULT_ROLE;
import static com.yelzhan.capstoneproject.service.implementation.UserServiceImpl.ADMIN_ROLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchUserByEmail() {
        String email = "test@example.com";
        User expectedUser = new User();
        expectedUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        User fetchedUser = userService.fetchUserByEmail(email);

        assertNotNull(fetchedUser);
        assertEquals(email, fetchedUser.getEmail());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFetchUserByEmail_ThrowsResourceNotFoundException() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.fetchUserByEmail(email);
        });

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testChangePassword_InvalidEmail() {
        String email = "nonexistent@example.com";
        ChangePasswordRequest updateRequest = new ChangePasswordRequest();
        updateRequest.setCurrentPassword("currentPassword");
        updateRequest.setNewPassword("newPassword");

        when(userRepository.existsByEmail(email)).thenReturn(false);

        boolean result = userService.changePassword(email, updateRequest);

        assertFalse(result);

        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, never()).findByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangePassword_ValidRequest() {
        String email = "test@example.com";
        ChangePasswordRequest updateRequest = new ChangePasswordRequest();
        updateRequest.setCurrentPassword("currentPassword");
        updateRequest.setNewPassword("newPassword");

        User userFromDatabase = new User();
        userFromDatabase.setEmail(email);
        userFromDatabase.setPassword(passwordEncoder.encode("currentPassword"));

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userFromDatabase));
        when(passwordEncoder.encode(updateRequest.getNewPassword())).thenReturn("encodedPassword");
        when(passwordEncoder.matches(updateRequest.getCurrentPassword(), userFromDatabase.getPassword())).thenReturn(true);


    }


    @Test
    void testActivateUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        userService.activateUser(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);

        assertEquals(true, user.isActive());
    }

    @Test
    void testDeactivateUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        userService.deActivateUser(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);

        assertEquals(false, user.isActive());
    }

    @Test
    void testFetchById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        User result = userService.fetchById(userId);

        verify(userRepository, times(1)).findById(userId);

        assertEquals(user, result);
    }

    @Test
    void testFetchAll() {
        Optional<Integer> page = Optional.of(0);
        Optional<Integer> size = Optional.of(10);
        Optional<String> sort = Optional.of("firstName");

        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(10), Sort.by(sort.orElse("id")));
        List<User> userList = Arrays.asList(
                new User(1L, "John", "Doe", "johnDoe@mail.com", "password", "123345", LocalDate.now(), LocalDate.now(), true, null, null, null),
                new User(1L, "Jane", "Smith", "janeSmith@mail.com", "password", "123345", LocalDate.now(), LocalDate.now(), true, null, null, null )
        );
        Page<User> userPage = new PageImpl<>(userList, pageable, userList.size());

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<User> result = userService.fetchAll(page, size, sort);

        verify(userRepository, times(1)).findAll(pageable);

        assertEquals(userPage, result);
    }

    @Test
    void testFetchById_ValidId() {
        Long id = 1L;
        User user = new User(1L, "John", "Doe", "johnDoe@mail.com", "password", "123345", LocalDate.now(), LocalDate.now(), true, null, null, null);
        ;

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.fetchById(id);

        verify(userRepository, times(1)).findById(id);

        assertEquals(user, result);
    }

    @Test
    void testFetchById_InvalidId() {
        Long id = 3L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.fetchById(id);
        });

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testIsActive_ActiveUser() {
        Long id = 1L;

        when(userRepository.findByIdAndIsActive(id)).thenReturn(true);

        boolean result = userService.isActive(id);

        verify(userRepository, times(1)).findByIdAndIsActive(id);

        assertTrue(result);
    }

    @Test
    void testIsActive_InactiveUser() {
        Long id = 1L;

        when(userRepository.findByIdAndIsActive(id)).thenReturn(false);

        boolean result = userService.isActive(id);

        verify(userRepository, times(1)).findByIdAndIsActive(id);

        assertFalse(result);
    }

    @Test
    void testRegisterAdmin() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setEmail("john.doe@example.com");
        registrationRequest.setPassword("password");
        registrationRequest.setMatchingPassword("password");
        registrationRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(roleService.fetchByRoleNames(DEFAULT_ROLE, ADMIN_ROLE)).thenReturn(getDummyRoles());

        userService.registerAdmin(registrationRequest);

        verify(userRepository, times(1)).save(any(User.class));
        verify(roleService, times(1)).fetchByRoleNames(DEFAULT_ROLE, ADMIN_ROLE);
    }

    @Test
    void testExistsByEmail() {
        String email = "john.doe@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean result = userService.existsByEmail(email);

        assertTrue(result);

        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void testUpdate() {
        String email = "john.doe@example.com";
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setFirstName("John");
        updateRequest.setLastName("Doe");
        updateRequest.setPhoneNumber("1234567890");
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        updateRequest.setDateOfBirth(dateOfBirth);

        User userFromDatabase = new User();
        userFromDatabase.setEmail(email);

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userFromDatabase));
        when(userRepository.save(userFromDatabase)).thenReturn(userFromDatabase);

        boolean result = userService.update(email, updateRequest);

        assertTrue(result);
        assertEquals(updateRequest.getFirstName(), userFromDatabase.getFirstName());
        assertEquals(updateRequest.getLastName(), userFromDatabase.getLastName());
        assertEquals(updateRequest.getPhoneNumber(), userFromDatabase.getPhoneNumber());
        assertEquals(updateRequest.getDateOfBirth(), userFromDatabase.getDateOfBirth());

        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(userFromDatabase);
    }

    @Test
    void testCheckExistenceForCreation() {
        String email = "john.doe@example.com";
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(email);

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(ResourceAlreadyExistException.class,
                () -> userService.checkExistenceForCreation(registrationRequest));

        verify(userRepository, times(1)).existsByEmail(email);
    }

    private List<Role> getDummyRoles() {
        // Return a list of dummy roles
        return Arrays.asList(new Role("ROLE_USER"), new Role("ROLE_ADMIN"));
    }
}
