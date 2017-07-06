package com.banana.view.services;

import java.util.List;

import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.models.SUserRole;
import com.banana.infrastructure.orm.repositories.SUserRepository;
import com.banana.infrastructure.orm.repositories.SUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.banana.view.controllers.FileUploadController;
import com.banana.infrastructure.user.Roles;
import com.banana.view.storage.StorageException;
import com.banana.view.storage.StorageService;
import com.github.slugify.Slugify;

@Service
public class UserService {
  private StorageService storageService;
  private SUserRepository userRepository;
  private SUserRoleRepository userRoleRepository;

  @Autowired
  public UserService(StorageService storageService, SUserRepository userRepository, SUserRoleRepository userRoleRepository) {
    this.storageService = storageService;
    this.userRepository = userRepository;
    this.userRoleRepository = userRoleRepository;
  }

  public boolean isAuthenticated() {
    if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
      return true;
    }
    return false;
  }

  public SUser getAuthenticatedUser() {
    if (this.isAuthenticated())
      return (SUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    else
      return null;
  }

  public SUser createUser(SUser user, MultipartFile profilePicture) throws StorageException {
    System.out.println(user);
    String profilePictureName = this.storageService.store(profilePicture);
    user = this.setAttributes(user, profilePictureName);
    return this.userRepository.save(user);
  }

  private SUser setAttributes(SUser user, String profilePictureName) {
    user = this.encodeUserPassword(user);
    user.setPicture(profilePictureName);
    user.setUsername(user.getEmail());
    user = this.setSlugFromLastnameAndFirstname(user);
    return user;
  }

  private SUser encodeUserPassword(SUser user) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return user;
  }

  private SUser setSlugFromLastnameAndFirstname(SUser user) {
    Slugify slg = new Slugify();
    String userSlug = slg.slugify(user.getFirstname()) + "." + slg.slugify(user.getLastname());
    List<SUser> otherUsersWithSameSlug = this.userRepository.findAllBySlugLike(userSlug);
    if (otherUsersWithSameSlug.size() > 0) userSlug = userSlug + "-" + otherUsersWithSameSlug.size();
    user.setSlug(userSlug);
    return user;
  }

  public SUserRole createUserRoleUser(SUser user) {
    SUserRole role = new SUserRole();
    role.setUserId(user.getId());
    role.setRole(Roles.ROLE_USER.toString());
    return this.userRoleRepository.save(role);
  }

  public boolean createUserWithUserRole(SUser user, MultipartFile profilePicture) {
    SUser createdUser = this.createUser(user, profilePicture);
    SUserRole createdRole = this.createUserRoleUser(createdUser);
    if (createdUser != null && createdRole != null)
      return true;
    else
      return false;
  }

  public String getUserPicture() {
    SUser user = this.getAuthenticatedUser();
    if (user != null)
      return MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "serveFile", this.storageService.load(user.getPicture()).getFileName().toString()).build().toString();
    else
      return "";
  }
}