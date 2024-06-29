package com.yelzhan.capstoneproject.service.implementation;

import com.yelzhan.capstoneproject.exception.ResourceNotFoundException;
import com.yelzhan.capstoneproject.model.entity.Gender;
import com.yelzhan.capstoneproject.repository.GenderRepository;
import com.yelzhan.capstoneproject.service.GenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenderServiceImpl implements GenderService {

    private final GenderRepository genderRepository;


    @Override
    public Map<String, Gender> fetchGender() {
        List<Gender> genders = genderRepository.findAll();
        return genders.stream()
                .collect(Collectors.toMap(Gender::getGender, Function.identity()));
    }

    @Override
    public Gender fetchGender(String gender) {
        return genderRepository.findByGender(gender.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Gender not found"));
    }
}
