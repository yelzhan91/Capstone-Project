package com.yelzhan.capstoneproject.controller;


import com.yelzhan.capstoneproject.config.security.SecurityUtils;
import com.yelzhan.capstoneproject.model.dto.request.auth.RegistrationRequest;
import com.yelzhan.capstoneproject.model.dto.request.category.CategoryRequest;
import com.yelzhan.capstoneproject.model.dto.request.product.ProductRequest;
import com.yelzhan.capstoneproject.model.entity.Category;
import com.yelzhan.capstoneproject.model.entity.User;
import com.yelzhan.capstoneproject.service.CategoryService;
import com.yelzhan.capstoneproject.service.ProductService;
import com.yelzhan.capstoneproject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.yelzhan.capstoneproject.config.ApplicationConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductService productService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Model model;

    @BeforeEach
    void setupAuthentication() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testAdminPage() {
        UserService userService = mock(UserService.class);
        CategoryService categoryService = mock(CategoryService.class);
        ProductService productService = mock(ProductService.class);
        AdminController adminController = new AdminController(userService, categoryService, productService);
        Model model = mock(Model.class);

        String expectedView = "admin";

        String result = adminController.adminPage(model);

        verify(model).addAttribute(eq(PAGE_TITLE.getValue()), eq(ADMIN.getValue()));
        verifyNoMoreInteractions(model);

        assertEquals(expectedView, result);
    }

    @Test
    void testUserListPage() {
        UserService userService = mock(UserService.class);
        CategoryService categoryService = mock(CategoryService.class);
        ProductService productService = mock(ProductService.class);
        AdminController adminController = new AdminController(userService, categoryService, productService);
        Model model = mock(Model.class);
        Page<User> userList = Mockito.mock(Page.class);
        Long userId = 1L;

        when(userService.fetchAll(any(Optional.class), any(Optional.class), any(Optional.class))).thenReturn(userList);
        when(userService.fetchUserByEmail(anyString())).thenReturn(User.builder().id(userId).build());

        String expectedView = "userList";

        String result = adminController.userListPage(Optional.of(1), Optional.of(10), Optional.of("name"), model);

        verify(userService).fetchAll(eq(Optional.of(1)), eq(Optional.of(10)), eq(Optional.of("name")));
        verify(userService).fetchUserByEmail(eq(SecurityContextHolder.getContext().getAuthentication().getName()));
        verify(model).addAttribute(eq(PAGE_TITLE.getValue()), eq(USER_LIST.getValue()));
        verify(model).addAttribute(eq("users"), eq(userList));
        verify(model).addAttribute(eq("currentId"), eq(userId));
        verifyNoMoreInteractions(userService, model);

        assertEquals(expectedView, result);
    }

    @Test
    void testSingleUserPage() {
        MockitoAnnotations.openMocks(this);
        AdminController adminController = new AdminController(userService, categoryService, productService);

        String expectedView = "singleUser";

        String result = adminController.singleUserPage(model);

        verify(model).addAttribute(eq(PAGE_TITLE.getValue()), eq(SINGLE_USER.getValue()));
        verify(model).addAttribute(eq(USER.getValue()), any(User.class));
        verifyNoMoreInteractions(model);

        assertEquals(expectedView, result);
    }

    @Test
    void testCategoriesPage() {
        MockitoAnnotations.openMocks(this);
        AdminController adminController = new AdminController(userService, categoryService, productService);
        Page<Category> categories = mock(Page.class);

        when(categoryService.fetchAllPage(any(Optional.class), any(Optional.class), any(Optional.class))).thenReturn(categories);

        String expectedView = "categories";

        String result = adminController.categoriesPage(Optional.of(1), Optional.of(10), Optional.of("name"), model);

        verify(model).addAttribute(eq("categories"), eq(categories));
        verify(model).addAttribute(eq(REQUEST.getValue()), any(CategoryRequest.class));
        verifyNoMoreInteractions(model);

        assertEquals(expectedView, result);
    }

    @Test
    void testNewAdminPage() {
        MockitoAnnotations.openMocks(this);
        AdminController adminController = new AdminController(userService, categoryService, productService);

        String expectedView = "addAdmin";

        String result = adminController.newAdminPage(model);

        verify(model).addAttribute(eq(PAGE_TITLE.getValue()), eq(CREATE_NEW_ADMIN.getValue()));
        verify(model).addAttribute(eq(REQUEST.getValue()), any(RegistrationRequest.class));
        verifyNoMoreInteractions(model);

        assertEquals(expectedView, result);
    }

    @Test
    void testNewProductPage() {
        MockitoAnnotations.openMocks(this);
        AdminController adminController = new AdminController(userService, categoryService, productService);

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Category 1", "Description 1"));
        categories.add(new Category("Category 2", "Description 2"));

        when(categoryService.fetchAll()).thenReturn(categories);

        String expectedView = "addProduct";

        String result = adminController.newProductPage(model);

        verify(model).addAttribute(eq(PAGE_TITLE.getValue()), eq(NEW_PRODUCT.getValue()));
        verify(model).addAttribute(eq("request"), any(ProductRequest.class));
        verify(model).addAttribute(eq("categories"), eq(categories));
        verifyNoMoreInteractions(model);

        assertEquals(expectedView, result);
    }

    @Test
    void testNewAdmin() {
        MockitoAnnotations.openMocks(this);
        AdminController adminController = new AdminController(userService, categoryService, productService);

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("admin@example.com");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.existsByEmail(registrationRequest.getEmail())).thenReturn(true);

        String expectedView = "redirect:addAdmin";

        String result = adminController.newAdmin(registrationRequest, bindingResult, redirectAttributes);

        verify(userService).registerAdmin(registrationRequest);
        verify(userService).existsByEmail(registrationRequest.getEmail());
        verify(redirectAttributes).addAttribute("newAdmin", true);
        verifyNoMoreInteractions(userService, redirectAttributes);

        assertEquals(expectedView, result);
    }

    @Test
    void testAddCategory() {
        MockitoAnnotations.openMocks(this);
        AdminController adminController = new AdminController(userService, categoryService, productService);

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setCategory("Category 1");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(categoryService.exists(categoryRequest.getCategory())).thenReturn(true);

        String expectedView = "redirect:categoryList";

        String result = adminController.addCategory(categoryRequest, bindingResult, redirectAttributes);

        verify(categoryService).create(categoryRequest);
        verify(categoryService).exists(categoryRequest.getCategory());
        verify(redirectAttributes).addAttribute("newCategory", true);
        verifyNoMoreInteractions(categoryService, redirectAttributes);

        assertEquals(expectedView, result);
    }

    @Test
    void testDeleteCategory() {
        MockitoAnnotations.openMocks(this);
        AdminController adminController = new AdminController(userService, categoryService, productService);

        Long categoryId = 1L;

        String expectedView = "redirect:../categoryList";

        String result = adminController.deleteCategory(categoryId, redirectAttributes);

        verify(categoryService).delete(categoryId);
        verify(categoryService).existsById(categoryId);
        verify(redirectAttributes).addAttribute("deleted", true);
        verifyNoMoreInteractions(categoryService, redirectAttributes);

        assertEquals(expectedView, result);
    }

    @Test
    void testGetSingleUser() {
        MockitoAnnotations.openMocks(this);
        AdminController adminController = new AdminController(userService, categoryService, productService);

        String userEmail = "user@example.com";

        User user = new User();

        Long currentUserId = 1L;

        when(userService.fetchUserByEmail(userEmail)).thenReturn(user);
        when(userService.fetchUserByEmail(SecurityUtils.getUsername())).thenReturn(
                new User(1L,
                        "John",
                        "Doe",
                        "johnDoe@mail.com",
                        "password",
                        "123345",
                        LocalDate.now(),
                        LocalDate.now(),
                        true,
                        null,
                        null,
                        null));

        String expectedView = "singleUser";

        String result = adminController.getSingleUser(userEmail, model);

        verify(userService).fetchUserByEmail(userEmail);
        verify(userService).fetchUserByEmail(SecurityUtils.getUsername());
        verify(model).addAttribute("currentId", currentUserId);
        verify(model).addAttribute("user", user);
        verifyNoMoreInteractions(userService, model);

        assertEquals(expectedView, result);
    }

    @Test
    void testActivateUser() {
        MockitoAnnotations.openMocks(this);
        AdminController adminController = new AdminController(userService, categoryService, productService);

        Long userId = 1L;

        String expectedView = "redirect:../userList";

        String result = adminController.activateUser(userId, redirectAttributes);

        verify(userService).activateUser(userId);
        verify(userService).isActive(userId);
        verify(redirectAttributes).addAttribute("user", userId);
        verify(redirectAttributes).addAttribute("is_active", false);
        verifyNoMoreInteractions(userService, redirectAttributes);

        assertEquals(expectedView, result);
    }

    @Test
    void testDeactivateUser() {
        MockitoAnnotations.openMocks(this);
        AdminController adminController = new AdminController(userService, categoryService, productService);

        Long userId = 1L;

        String expectedView = "redirect:../userList";

        String result = adminController.deActivateUser(userId, redirectAttributes);

        verify(userService).deActivateUser(userId);
        verify(userService).isActive(userId);
        verify(redirectAttributes).addAttribute("user", userId);
        verify(redirectAttributes).addAttribute("is_active", false);
        verifyNoMoreInteractions(userService, redirectAttributes);

        assertEquals(expectedView, result);
    }

}
