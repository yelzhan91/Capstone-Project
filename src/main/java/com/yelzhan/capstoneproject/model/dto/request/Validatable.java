package com.yelzhan.capstoneproject.model.dto.request;

import com.yelzhan.capstoneproject.exception.ResourceAlreadyExistException;

public interface Validatable <T extends CreateRequest>{

    String EMPTY_STRING = "";

    void checkExistenceForCreation(T request) throws ResourceAlreadyExistException;
}
