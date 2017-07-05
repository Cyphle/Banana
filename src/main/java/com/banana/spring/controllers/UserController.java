package com.banana.spring.controllers;

import com.banana.spring.forms.UserForm;
import com.banana.spring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class UserController {
  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(Model model) {
    return "user/login";
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String getRegister(Model model) {
    return "user/registration";
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String postRegister(@Valid UserForm userForm, RedirectAttributes redirectAttributes, Errors errors)
          throws IllegalStateException, IOException {
    if (errors.hasErrors()) {
      return "user/registration";
    }

    boolean isUserCreated = this.userService.createUserWithUserRole(userForm.toModel(), userForm.getPicture());
    if (isUserCreated)
      redirectAttributes.addFlashAttribute("flashMessage", "Registration successfull !");
    else
      redirectAttributes.addFlashAttribute("flashMessage", "Error creating the user.");
    return "redirect:/";
  }

  @RequestMapping(value="/profile", method=RequestMethod.GET)
  public String getProfile(Model model) {
    model.addAttribute("user", this.userService.getAuthenticatedUser());
    model.addAttribute("picture", this.userService.getUserPicture());

    return "user/profile";
  }
}
