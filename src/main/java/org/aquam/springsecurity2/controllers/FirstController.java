package org.aquam.springsecurity2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping()
public class FirstController {

    @GetMapping()
    public ModelAndView getFirstView() {
        ModelAndView modelAndView = new ModelAndView();
        //modelAndView.setViewName("register_login/first");
        modelAndView.setViewName("person/htmls/first");
        return modelAndView;
    }

}
