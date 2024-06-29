package com.yelzhan.capstoneproject.service;

import com.yelzhan.capstoneproject.model.entity.Gender;

import java.util.Map;

public interface GenderService {

    Map<String, Gender> fetchGender();
    Gender fetchGender(String gender);
}
