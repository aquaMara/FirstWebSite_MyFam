package org.aquam.springsecurity2.controllers;

import org.aquam.springsecurity2.models.DefaultUser;
import org.aquam.springsecurity2.models.Feedback;
import org.aquam.springsecurity2.models.Home;
import org.aquam.springsecurity2.service.DefaultFeedbackService;
import org.aquam.springsecurity2.service.DefaultHomeService;
import org.aquam.springsecurity2.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DefaultUserService defaultUserService;
    private final DefaultHomeService defaultHomeService;
    private final DefaultFeedbackService defaultFeedbackService;

    @Autowired
    public AdminController(DefaultUserService defaultUserService, DefaultHomeService defaultHomeService, DefaultFeedbackService defaultFeedbackService) {
        this.defaultUserService = defaultUserService;
        this.defaultHomeService = defaultHomeService;
        this.defaultFeedbackService = defaultFeedbackService;
    }

    @GetMapping()
    public ModelAndView getAdminViewFeedback() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/htmls/feedback");
        List<Feedback> allFeedbacks = defaultFeedbackService.getAllFeedbacks();
        modelAndView.addObject("allfeeds", allFeedbacks);
        return modelAndView;
    }

    @GetMapping("/print")
    public ModelAndView writeFeedback() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/htmls/feedback");

        List<Feedback> allFeedbacks = defaultFeedbackService.getAllFeedbacks();

        String fileName = "src/main/resources/templates/feedback.txt";
        FileWriter myWriter = null;

        try(PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

            for (int i = 0; i < allFeedbacks.size(); i++) {
                writer.append(allFeedbacks.get(i).getFeedback());
                writer.append("\n");
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        for (int i = 0; i < allFeedbacks.size(); i++) {


            PrintWriter out = new PrintWriter(fileName);
            out.println(allFeedbacks.get(i).getFeedback());
            out.close();
            try {
                myWriter = new FileWriter(fileName);
                myWriter.write("Files in Java might be tricky, but it is fun enough!");
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        */

        modelAndView.addObject("allfeeds", allFeedbacks);
        return modelAndView;
    }

    @GetMapping("/user")
    public ModelAndView getAdminViewUsers() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/htmls/admin");
        List<DefaultUser> allPeople = defaultUserService.getAllDefaultUsers();
        //allPeople.remove(84);
        modelAndView.addObject("allpeople", allPeople);
        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView getAdminViewHomes() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/htmls/home");
        List<Home> allHomes = defaultHomeService.getAllHomes();
        //allPeople.remove(84);
        modelAndView.addObject("allhomes", allHomes);
        return modelAndView;
    }




}
