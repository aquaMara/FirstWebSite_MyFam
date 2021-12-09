package org.aquam.springsecurity2.controllers;

import org.aquam.springsecurity2.models.DefaultUser;
import org.aquam.springsecurity2.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class RegisterController {

    private final DefaultUserService defaultUserService;

    @Autowired
    public RegisterController(DefaultUserService defaultUserService) {
        this.defaultUserService = defaultUserService;
    }

    @GetMapping("register")
    public ModelAndView getRegisterView() {
        ModelAndView modelAndView = new ModelAndView();
        DefaultUser defaultUser = new DefaultUser();
        modelAndView.addObject("defaultuser", defaultUser);
        modelAndView.setViewName("register_login/htmls/register");
        return modelAndView;
    }

    @PostMapping("register")
    public ModelAndView registerUser(@Valid DefaultUser defaultUser, BindingResult bindingResult, ModelMap modelMap) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register_login/htmls/register");

        if(bindingResult.hasErrors()) {
            // modelAndView.addObject("successMessage", "Please correct the errors!");
            modelMap.addAttribute("bindingResult", bindingResult); // displ validation messages
        } else if(defaultUserService.defaultUserExists(defaultUser)) {
            modelAndView.addObject("successMessage", "Username already taken!");
        } else {
            defaultUserService.saveUser(defaultUser);
            modelAndView.addObject("successMessage", "You are registered successfully!");
        }

        modelAndView.addObject("defaultuser", new DefaultUser());
        //modelAndView.setViewName("register_login/register");

        return modelAndView;
    }

}

