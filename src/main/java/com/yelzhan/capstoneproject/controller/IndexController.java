package com.yelzhan.capstoneproject.controller;

import com.yelzhan.capstoneproject.model.entity.Category;
import com.yelzhan.capstoneproject.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.yelzhan.capstoneproject.config.ApplicationConstants.APPLICATION_NAME;
import static com.yelzhan.capstoneproject.config.ApplicationConstants.PAGE_TITLE;


@Controller
@RequiredArgsConstructor
public class IndexController {

    private final CategoryService categoryService;

    @GetMapping("/")
    public String index(Model model) {
        List<Category> categories = categoryService.fetchAll();

        model.addAttribute(PAGE_TITLE.getValue(), APPLICATION_NAME.getValue());
        model.addAttribute("categories", categories);

        return "index";
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }
}
