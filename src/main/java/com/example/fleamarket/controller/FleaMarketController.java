package com.example.fleamarket.controller;

import com.example.fleamarket.dto.FleaMarketDTO;
import com.example.fleamarket.form.FleaMarketForm;
import com.example.fleamarket.form.FleaMarketSearchForm;
import com.example.fleamarket.service.FleaMarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shopping")
public class FleaMarketController {
    private final FleaMarketService fleaMarketService;

    @GetMapping
    public String list(FleaMarketSearchForm searchForm, Authentication authentication, Model model){
        String username = authentication.getName();
        var shoppingList = fleaMarketService.findAll(username, searchForm.toEntity());
        model.addAttribute("shoppingList",shoppingList);
        model.addAttribute("searchDTO", searchForm.toDTO());
        return "shopping";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") long shoppingId, @RequestParam(required = false) String from, Model model){
        FleaMarketDTO dto = fleaMarketService.findById(shoppingId);
        model.addAttribute("shopping", dto);
        model.addAttribute("from", from);
        return "shopping/detail";
    }

    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute FleaMarketForm form, Model model) {
        model.addAttribute("mode", "CREATE");
        return "form";
    }

    @PostMapping
    public String create(@Validated @ModelAttribute FleaMarketForm form,
                         BindingResult bindingResult, Authentication authentication, Model model) {
        //画像が選択されてない場合バリデーション
        if (form.id() == null && (form.imageFile() == null || form.imageFile().isEmpty())) {
            bindingResult.rejectValue("imageFile", "required", "画像を選択してください");
        }

        if (bindingResult.hasErrors()) {
            return showCreationForm(form,model);
        }

        String username = authentication.getName();
        fleaMarketService.create(form, username);
        return "redirect:/shopping";
    }

    @PostMapping("/{id}/purchase")
    public String purchase(@PathVariable("id") Long id, Authentication authentication) {
        String username = authentication.getName();
        fleaMarketService.purchase(id, username);
        return "redirect:/shopping/" + id;
    }


}
