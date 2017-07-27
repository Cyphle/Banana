package com.banana.view.controllers;

import com.banana.view.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class MainController {
  private UserService userService;

  @Autowired
  public MainController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(method= RequestMethod.GET)
  public String home(Model model) {
    model.addAttribute("picture", this.userService.getUserPicture());
    model.addAttribute("username", this.userService.getAuthenticatedUser() != null ? this.userService.getAuthenticatedUser().getUsername() : "Anonyme");
    model.addAttribute("toto", "Hello world");
    return "home";
  }
}
