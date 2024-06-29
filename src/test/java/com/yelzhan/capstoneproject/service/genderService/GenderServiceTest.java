package com.yelzhan.capstoneproject.service.genderService;

import com.yelzhan.capstoneproject.exception.ResourceNotFoundException;
import com.yelzhan.capstoneproject.model.entity.Gender;
import com.yelzhan.capstoneproject.repository.GenderRepository;
import com.yelzhan.capstoneproject.service.implementation.GenderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GenderServiceTest {

    @Mock
    private GenderRepository genderRepository;

    @InjectMocks
    private GenderServiceImpl genderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchGender() {
        List<Gender> genders = Arrays.asList(
                new Gender("Male"),
                new Gender("Female")
        );

        when(genderRepository.findAll()).thenReturn(genders);

        genderService = new GenderServiceImpl(genderRepository);

        Map<String, Gender> expectedGenderMap = new HashMap<>();
        expectedGenderMap.put("Male", new Gender("Male"));
        expectedGenderMap.put("Female", new Gender("Female"));

        Map<String, Gender> result = genderService.fetchGender();

        assertEquals(expectedGenderMap, result);

        verify(genderRepository, times(1)).findAll();
    }

    @Test
    void testFetchGender_GenderFound() {
        String genderName = "Male";
        Gender maleGender = new Gender(genderName);

        when(genderRepository.findByGender(genderName.trim())).thenReturn(Optional.of(maleGender));

        genderService = new GenderServiceImpl(genderRepository);

        Gender result = genderService.fetchGender(genderName);

        assertEquals(maleGender, result);

        verify(genderRepository, times(1)).findByGender(genderName.trim());
    }

    @Test
    void testFetchGender_GenderNotFound() {
        String genderName = "Unknown";

        when(genderRepository.findByGender(genderName.trim())).thenReturn(Optional.empty());

        genderService = new GenderServiceImpl(genderRepository);

        assertThrows(ResourceNotFoundException.class, () -> genderService.fetchGender(genderName));

        verify(genderRepository, times(1)).findByGender(genderName.trim());
    }

}
