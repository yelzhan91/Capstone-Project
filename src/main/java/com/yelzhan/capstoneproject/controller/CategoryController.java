package com.yelzhan.capstoneproject.controller;


import com.yelzhan.capstoneproject.model.entity.Product;
import com.yelzhan.capstoneproject.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.yelzhan.capstoneproject.config.ApplicationConstants.CATEGORY;
import static com.yelzhan.capstoneproject.config.ApplicationConstants.PAGE_TITLE;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final ProductService productService;

    @GetMapping("/{category}")
    public String getCategoryItemsPage(@PathVariable String category,
                                       @RequestParam("page") Optional<Integer> page,
                                       @RequestParam("size") Optional<Integer> size,
                                       @RequestParam("sort") Optional<String> sort,
                                       Model model){

        Page<Product> products = productService
                .fetchAllByCategory(category, page, size, sort);

        model.addAttribute(PAGE_TITLE.getValue(), CATEGORY.getValue());
        model.addAttribute("products", products);

        return "category";
    }

}
