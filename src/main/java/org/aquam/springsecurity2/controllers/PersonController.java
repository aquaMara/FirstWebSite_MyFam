package org.aquam.springsecurity2.controllers;

import org.aquam.springsecurity2.models.*;
import org.aquam.springsecurity2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/person")
public class PersonController {

    private final DefaultUserService defaultUserService;
    private final DefaultHomeService defaultHomeService;
    private final DefaultRoomService defaultRoomService;
    private final DefaultNoteService defaultNoteService;
    private final DefaultFeedbackService defaultFeedbackService;

    @Autowired
    public PersonController(DefaultUserService defaultUserService, DefaultHomeService defaultHomeService, DefaultRoomService defaultRoomService, DefaultNoteService defaultNoteService, DefaultFeedbackService defaultFeedbackService) {
        this.defaultUserService = defaultUserService;
        this.defaultHomeService = defaultHomeService;
        this.defaultRoomService = defaultRoomService;
        this.defaultNoteService = defaultNoteService;
        this.defaultFeedbackService = defaultFeedbackService;
    }

    public DefaultUser loggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        UserDetails loggedInUser = defaultUserService.loadUserByUsername(username);
        DefaultUser defaultUser = (DefaultUser) loggedInUser;

        return defaultUser;
    }

    @GetMapping()
    public ModelAndView getHomeView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("person/htmls/homes");

        List<Home> allHomes = loggedInUser().getHomesForDefaultUser();
        modelAndView.addObject("allhomes", allHomes);
        modelAndView.addObject("home", new Home());

        return modelAndView;
    }

    @PostMapping
    public ModelAndView sendHomeView(@Valid Home home, BindingResult bindingResult, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("person/htmls/homes");

        if (home.getHomename() != null) {
            if (home.getHomename().isBlank()) {
                modelAndView.addObject("successMessage", "Homename should not be empty!");
            } else if (defaultHomeService.homeExists(home)) {
                modelAndView.addObject("successMessage", "Homename already taken!");
            } else {
                defaultUserService.addHomeForUser(loggedInUser(), home);
                modelAndView.addObject("successMessage", "Home is registered successfully!");
            }
        }

        List<Home> allHomes = loggedInUser().getHomesForDefaultUser();
        modelAndView.addObject("allhomes", allHomes);
        modelAndView.addObject("home", new Home());

        return modelAndView;
    }

    @GetMapping("/feedback")
    public ModelAndView getFeedbackView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("person/htmls/feedback");

        Feedback feedback = new Feedback();
        modelAndView.addObject("emptyfeedback", feedback);

        return modelAndView;
    }
    @PostMapping("/feedback")
    public ModelAndView sendFeedback(@ModelAttribute("emptyfeedback") Feedback feedback) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("person/htmls/feedback");

        defaultFeedbackService.addFeedback(feedback);

        return modelAndView;
    }


    @GetMapping("/{homename}")
    public ModelAndView exactHome(@PathVariable("homename") String homename) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("person/htmls/main");

        modelAndView.addObject("emptyroom", new Room());
        modelAndView.addObject("emptyrelative", new DefaultUser());

        Home oneHome = defaultHomeService.loadHomeByHomename(homename);
        modelAndView.addObject("onehome", oneHome);

        List<DefaultUser> allHomeUsers = oneHome.getDefaultUsersForHome();
        modelAndView.addObject("allhomeusers", allHomeUsers);

        List<Room> allHomeRooms = oneHome.getRoomsForHome();
        modelAndView.addObject("allhomerooms", allHomeRooms);

        // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
        List<Home> allHomes = loggedInUser().getHomesForDefaultUser();
        modelAndView.addObject("allhomes", allHomes);
        modelAndView.addObject("home", new Home());
        // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

        return modelAndView;
    }

    @PostMapping("/{homename}")
    public ModelAndView getUpdatedExactHome(@PathVariable("homename") String homename, @ModelAttribute("emptyroom") Room thisRoom, @ModelAttribute("emptyrelative") DefaultUser thisRelative) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("person/htmls/main");

        Home oneHome = defaultHomeService.loadHomeByHomename(homename);
        modelAndView.addObject("onehome", oneHome);

        if (thisRoom.getRoomname() != null && thisRelative.getUsername() == null) {
            Room room = new Room(thisRoom.getRoomname());
            if (defaultHomeService.roomIsInHome(oneHome, thisRoom.getRoomname())) {
                modelAndView.addObject("successMessageRoom", "Room is already in home!");
            } else if (thisRoom.getRoomname().isBlank()) {
                modelAndView.addObject("successMessageRoom", "Can't be blank!");
            } else {
                defaultHomeService.addRoomForHome(oneHome, room);
            }
        }

        if (thisRelative.getUsername() != null && thisRoom.getRoomname() == null) {
            if (thisRelative.getUsername().isBlank()) {
                modelAndView.addObject("successMessagePerson", "Can't be blank!");
            } else if (!defaultUserService.defaultUserExists(thisRelative)) {
                modelAndView.addObject("successMessagePerson", "User does not exist!");
            } else if (defaultHomeService.userIsInHome(oneHome, thisRelative.getUsername())) {
                modelAndView.addObject("successMessagePerson", "User already lives in home!");
            } else {
                defaultHomeService.addUserForHome(thisRelative, oneHome);
            }
        }


        List<DefaultUser> allHomeUsers = oneHome.getDefaultUsersForHome();
        modelAndView.addObject("allhomeusers", allHomeUsers);

        List<Room> allHomeRooms = oneHome.getRoomsForHome();
        modelAndView.addObject("allhomerooms", allHomeRooms);

        modelAndView.addObject("emptyroom", new Room());
        modelAndView.addObject("emptyrelative", new DefaultUser());

        return modelAndView;
    }

    @GetMapping("/{homename}/{roomname}")
    public ModelAndView addNote(@PathVariable("homename") String homename, @PathVariable("roomname") String roomname, @ModelAttribute("emptynote") Note thisNote) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("person/htmls/note");

        // **********************************************************
        Home oneHome = defaultHomeService.loadHomeByHomename(homename);
        modelAndView.addObject("onehome", oneHome);

        List<Room> allHomeRooms = oneHome.getRoomsForHome();
        modelAndView.addObject("allhomerooms", allHomeRooms);

        List<DefaultUser> allHomeUsers = oneHome.getDefaultUsersForHome();
        modelAndView.addObject("allhomeusers", allHomeUsers);

        modelAndView.addObject("emptyroom", new Room());
        modelAndView.addObject("emptyrelative", new DefaultUser());

        Room oneRoom = defaultRoomService.loadRoomByRoomname(oneHome, roomname);
        modelAndView.addObject("oneroom", oneRoom);

        // **********************************************************

        List allNotes = oneRoom.getNotesForRoom();
        modelAndView.addObject("allnotes", allNotes);

        //modelAndView.addObject("note", new Note());
        modelAndView.addObject("emptynote", new Note());


        return modelAndView;
    }

    @PostMapping("/{homename}/{roomname}")
    public ModelAndView sendAddedNote(@PathVariable("homename") String homename, @PathVariable("roomname") String roomname, @ModelAttribute("emptynote") Note thisNote) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("person/htmls/note");

        // **********************************************************
        Home oneHome = defaultHomeService.loadHomeByHomename(homename);
        modelAndView.addObject("onehome", oneHome);

        List<Room> allHomeRooms = oneHome.getRoomsForHome();
        modelAndView.addObject("allhomerooms", allHomeRooms);

        List<DefaultUser> allHomeUsers = oneHome.getDefaultUsersForHome();
        modelAndView.addObject("allhomeusers", allHomeUsers);

        modelAndView.addObject("emptyroom", new Room());
        modelAndView.addObject("emptyrelative", new DefaultUser());

        Room oneRoom = defaultRoomService.loadRoomByRoomname(oneHome, roomname);
        modelAndView.addObject("oneroom", oneRoom);
        // **********************************************************

        if (thisNote != null) {
            defaultRoomService.addNoteForRoom(thisNote, oneRoom);
        }


        List allNotes = oneRoom.getNotesForRoom();
        modelAndView.addObject("allnotes", allNotes);

        return modelAndView;
    }

    @DeleteMapping("/{homename}/{roomname}/{id}")
    public ModelAndView getDeletedNote(@PathVariable("homename") String homename, @PathVariable("roomname") String roomname, @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("person/htmls/note");

        defaultNoteService.deleteNoteById(id);

        // **********************************************************
        Home oneHome = defaultHomeService.loadHomeByHomename(homename);
        modelAndView.addObject("onehome", oneHome);

        List<Room> allHomeRooms = oneHome.getRoomsForHome();
        modelAndView.addObject("allhomerooms", allHomeRooms);

        List<DefaultUser> allHomeUsers = oneHome.getDefaultUsersForHome();
        modelAndView.addObject("allhomeusers", allHomeUsers);

        modelAndView.addObject("emptyroom", new Room());
        modelAndView.addObject("emptyrelative", new DefaultUser());

        Room oneRoom = defaultRoomService.loadRoomByRoomname(oneHome, roomname);
        modelAndView.addObject("oneroom", oneRoom);
        // **********************************************************

        List allNotes = oneRoom.getNotesForRoom();
        modelAndView.addObject("allnotes", allNotes);
        modelAndView.addObject("emptynote", new Note());

        return modelAndView;
    }
    @PutMapping("/{homename}/{roomname}/{id}")
    public ModelAndView getMarkedNote(@PathVariable("homename") String homename, @PathVariable("roomname") String roomname, @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("person/htmls/note");

        defaultNoteService.completeNoteById(id);

        // **********************************************************
        Home oneHome = defaultHomeService.loadHomeByHomename(homename);
        modelAndView.addObject("onehome", oneHome);

        List<Room> allHomeRooms = oneHome.getRoomsForHome();
        modelAndView.addObject("allhomerooms", allHomeRooms);

        List<DefaultUser> allHomeUsers = oneHome.getDefaultUsersForHome();
        modelAndView.addObject("allhomeusers", allHomeUsers);

        modelAndView.addObject("emptyroom", new Room());
        modelAndView.addObject("emptyrelative", new DefaultUser());

        Room oneRoom = defaultRoomService.loadRoomByRoomname(oneHome, roomname);
        modelAndView.addObject("oneroom", oneRoom);
        // **********************************************************

        List allNotes = oneRoom.getNotesForRoom();
        modelAndView.addObject("allnotes", allNotes);
        modelAndView.addObject("emptynote", new Note());

        return modelAndView;
    }





}
















