package com.banana.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class UserController {
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(Model model) {
    return "user/login";
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String getRegister(Model model) {
    return "user/registration";
  }
}
