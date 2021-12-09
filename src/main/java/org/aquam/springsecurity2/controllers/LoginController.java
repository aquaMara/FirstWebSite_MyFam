package org.aquam.springsecurity2.controllers;

import org.aquam.springsecurity2.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/")
public class LoginController {

    private final DefaultUserService defaultUserService;

    @Autowired
    public LoginController(DefaultUserService defaultUserService) {
        this.defaultUserService = defaultUserService;
    }


    @GetMapping("login")    // address in browser ("/" + "login" = "/login")
    public ModelAndView getLoginView() {
        ModelAndView modelAndView = new ModelAndView();
        //modelAndView.setViewName("register_login/login"); // resources/templates/login.html
        modelAndView.setViewName("register_login/htmls/login");
        return modelAndView;
    }

}
