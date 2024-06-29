package com.yelzhan.capstoneproject.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface Page {

    int DEFAULT_PAGE = 0;
    int MAX_ELEMENTS_PER_PAGE = 5;
    String DEFAULT_SORING_ATTRIBUTE = "id";

    static Pageable getPageable(Optional<Integer> page, Optional<Integer> size, Optional<String> sort) {
        Integer _page = page.orElse(DEFAULT_PAGE);
        Integer _size = size.orElse(MAX_ELEMENTS_PER_PAGE);
        String _sort = sort.orElse(DEFAULT_SORING_ATTRIBUTE);

        return PageRequest.of(_page, _size, Sort.by(Sort.Direction.ASC, _sort));
    }
}
