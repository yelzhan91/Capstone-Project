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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static com.yelzhan.capstoneproject.config.ApplicationConstants.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;


    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute(PAGE_TITLE.getValue(), ADMIN.getValue());
        return "admin";
    }
    @GetMapping("/userList")
    public String userListPage(@RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size,
                               @RequestParam("sort") Optional<String> sort,
                               Model model) {
        Page<User> userList = userService.fetchAll(page, size, sort);
        Long id = userService.fetchUserByEmail(SecurityUtils.getUsername()).getId();

        model.addAttribute(PAGE_TITLE.getValue(), USER_LIST.getValue());
        model.addAttribute("users", userList);
        model.addAttribute("currentId", id);

        return "userList";
    }

    @GetMapping("/singleUser")
    public String singleUserPage(Model model) {

        model.addAttribute(PAGE_TITLE.getValue(), SINGLE_USER.getValue());
        model.addAttribute(USER.getValue(), new User());

        return "singleUser";
    }

    @GetMapping("/categoryList")
    public String categoriesPage(@RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size,
                                 @RequestParam("sort") Optional<String> sort,
                                 Model model) {
        Page<Category> categories = categoryService.fetchAllPage(page, size, sort);
        model.addAttribute("categories", categories);
        model.addAttribute(REQUEST.getValue(), new CategoryRequest());
        return "categories";
    }

    @GetMapping("/addAdmin")
    public String newAdminPage(Model model) {

        model.addAttribute(PAGE_TITLE.getValue(), CREATE_NEW_ADMIN.getValue());
        model.addAttribute(REQUEST.getValue(), new RegistrationRequest());

        return "addAdmin";
    }

    @GetMapping("/addProduct")
    public String newProductPage(Model model) {
        List<Category> categories = categoryService.fetchAll();

        model.addAttribute(PAGE_TITLE.getValue(), NEW_PRODUCT.getValue());
        model.addAttribute("request", new ProductRequest());
        model.addAttribute("categories", categories);

        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String newProduct(@ModelAttribute("request") @Valid ProductRequest productRequest,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute(FAILED.getValue(), true);
            bindingResult.getFieldErrors()
                    .forEach(fieldError -> {
                        redirectAttributes.addAttribute(fieldError.getField(),
                                fieldError.getDefaultMessage());
                    });
            return "redirect:addProduct";
        }
        productService.create(productRequest);
        boolean savedInDB = productService.existsByProductName(productRequest.getProductName());

        redirectAttributes.addAttribute("newProduct", savedInDB);
        return "redirect:addProduct";
    }

    @PostMapping("/addAdmin")
    public String newAdmin(@ModelAttribute("request") @Valid RegistrationRequest registrationRequest,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute(FAILED.getValue(), true);
            bindingResult.getFieldErrors()
                    .forEach(fieldError -> {
                        redirectAttributes.addAttribute(fieldError.getField(),
                                fieldError.getDefaultMessage());
                    });
            return "redirect:addAdmin";
        }
        userService.registerAdmin(registrationRequest);
        boolean savedInDB = userService.existsByEmail(registrationRequest.getEmail());

        redirectAttributes.addAttribute("newAdmin", savedInDB);
        return "redirect:addAdmin";
    }

    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute("request") @Valid CategoryRequest categoryRequest,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute(FAILED.getValue(), true);
            bindingResult.getFieldErrors()
                    .forEach(fieldError -> {
                        redirectAttributes.addAttribute(fieldError.getField(),
                                fieldError.getDefaultMessage());
                    });
            return "redirect:categoryList";
        }

        categoryService.create(categoryRequest);
        boolean categoryExist = categoryService.exists(categoryRequest.getCategory());

        redirectAttributes.addAttribute("newCategory", categoryExist);
        return "redirect:categoryList";
    }

    @PostMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        categoryService.delete(id);
        boolean deleted = !categoryService.existsById(id);

        redirectAttributes.addAttribute("deleted", deleted);
        return "redirect:../categoryList";
    }

    @PostMapping("/singleUser")
    public String getSingleUser(@RequestParam("email") String email,
                                Model model) {

        User user = userService.fetchUserByEmail(email);
        Long id = userService.fetchUserByEmail(SecurityUtils.getUsername()).getId();

        model.addAttribute("currentId", id);

        if (user != null) {
            model.addAttribute(USER.getValue(), user);
        } else {
            model.addAttribute("error", "User not found");
        }

        return "singleUser";
    }


    @PostMapping("/activate/{id}")
    public String activateUser(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {

        userService.activateUser(id);
        boolean isActive = userService.isActive(id);

        redirectAttributes.addAttribute(USER.getValue(), id);
        redirectAttributes.addAttribute("is_active", isActive);

        return "redirect:../userList";
    }

    @PostMapping("/deactivate/{id}")
    public String deActivateUser(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {

        userService.deActivateUser(id);
        boolean isActive = userService.isActive(id);

        redirectAttributes.addAttribute(USER.getValue(), id);
        redirectAttributes.addAttribute("is_active", isActive);

        return "redirect:../userList";
    }



}
