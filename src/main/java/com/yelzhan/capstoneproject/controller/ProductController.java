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

import static com.yelzhan.capstoneproject.config.ApplicationConstants.PAGE_TITLE;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{adult}")
    public String productsPage(@PathVariable("adult") Boolean adult,
                               @RequestParam("gender") Optional<String> gender,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size,
                               @RequestParam("sort") Optional<String> sort,
                               Model model) {

        Page<Product> products = productService.fetchAll(adult, gender, page, size, sort);

        model.addAttribute("products", products);
        model.addAttribute(PAGE_TITLE.getValue(), "Products");
        model.addAttribute("isAdult", adult);

        return "product";
    }


}
