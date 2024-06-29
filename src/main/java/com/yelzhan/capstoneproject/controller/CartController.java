package com.yelzhan.capstoneproject.controller;

import com.yelzhan.capstoneproject.config.SessionCart;
import com.yelzhan.capstoneproject.config.security.SecurityUtils;
import com.yelzhan.capstoneproject.model.entity.CartItem;
import com.yelzhan.capstoneproject.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static com.yelzhan.capstoneproject.config.ApplicationConstants.*;
import static org.springframework.http.HttpHeaders.REFERER;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    public final static String CART_ATTRIBUTE = "Cart";


    private final CartService cartService;

    @GetMapping
    public String cartPage(Model model,
                           HttpSession session,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           @RequestParam("sort") Optional<String> sort) {


        String email = SecurityUtils.getUsername();
        Page<CartItem> items = cartService.fetchAllForUser(email, page, size, sort);

        model.addAttribute("cartItems", items);
        model.addAttribute(PAGE_TITLE.getValue(), CART.getValue());

        return "cart";


    }

    @PostMapping("/addToCart/{id}")
    public String addToCart(@PathVariable Long id,
                            Model model,
                            HttpSession session,
                            HttpServletRequest request,
                            RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityUtils.getAuthentication();
        if (authentication == null || SecurityUtils.isAnonymousAuthenticationToken(authentication)) {
            SessionCart sessionCart = cartService.addProduct(id, session);
            boolean isAdded = cartService.inSessionCart(id, session);
            session.setAttribute(CART_ATTRIBUTE, sessionCart);

            redirectAttributes.addAttribute("addToCart", isAdded);
            return "redirect:" + request.getHeader(REFERER);
        }

        cartService.addProductForUser(id, SecurityUtils.getUsername());

        boolean isAdded = cartService.inCart(id);
        model.addAttribute("addToCart", isAdded);
        redirectAttributes.addAttribute("addToCart", isAdded);
        return "redirect:" + request.getHeader(REFERER);
    }

    @PostMapping("/deleteItem/{id}")
    public String deleteItem(@PathVariable Long id, HttpServletRequest request,
                             Model model){

        String email = SecurityUtils.getUsername();
        cartService.deleteProductFromCartItem(id, email);
        return "redirect:" + request.getHeader(REFERER);
    }
}
