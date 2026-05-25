package com.example.fleamarket.controller;

import com.example.fleamarket.dto.FleaMarketDTO;
import com.example.fleamarket.form.FleaMarketForm;
import com.example.fleamarket.service.AccountService;
import com.example.fleamarket.service.FleaMarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")

public class AccountController {
    private final AccountService accountService;
    private final FleaMarketService fleaMarketService;

    @GetMapping
    public String showAccount(Authentication authentication,Model model) {
        String username = authentication.getName();
        model.addAttribute("username",username);
        return "account";
    }

    @GetMapping("/listing")
    public String list(Authentication authentication, Model model){
        String username = authentication.getName();
        var shoppingList = accountService.findAll(username);
        model.addAttribute("shoppingList",shoppingList);
        return "account/listing";
    }

    @GetMapping("/sold-list")
    public String listSold(Authentication authentication, Model model){
        String username = authentication.getName();
        var shoppingList = accountService.findSoldAll(username);
        model.addAttribute("shoppingList",shoppingList);
        return "account/sold-list";
    }

    @GetMapping("/{id}")
    public String showEditForm(@PathVariable("id") Long id,Model model) {
        FleaMarketDTO dto = fleaMarketService.findById(id);
        //売り切れ商品の編集を禁止
        if (dto.status().equals("SOLD_OUT")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        FleaMarketForm form = FleaMarketForm.fromEntity(dto);
        model.addAttribute("fleaMarketForm", form);
        model.addAttribute("mode", "EDIT");

        return "form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id, @Validated @ModelAttribute FleaMarketForm form,
            BindingResult bindingResult, Authentication authentication, Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "EDIT");
            return "form";
        }
        String username = authentication.getName();
        accountService.update(id, form, username);
        return "redirect:/account/listing";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, Authentication authentication) {
        String username = authentication.getName();
        accountService.delete(id, username);
        return "redirect:/account/listing";
    }

    @GetMapping("/purchases")
    public String purchases(Authentication authentication, Model model){
        String username = authentication.getName();
        var shoppingList = accountService.findPurchasedItems(username);
        model.addAttribute("mode", "EDIT");
        model.addAttribute("shoppingList", shoppingList);
        return "account/purchases";
    }

}