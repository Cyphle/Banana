package com.banana.spring.user;

import java.util.List;

import com.banana.spring.models.SUser;
import com.banana.spring.repositories.SUserRepository;
import com.banana.spring.repositories.SUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;



@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
  private final SUserRepository userRepository;
  private final SUserRoleRepository userRoleRepository;

  @Autowired
  public CustomUserDetailsService(SUserRepository userRepository, SUserRoleRepository userRolesRepository) {
    this.userRepository = userRepository;
    this.userRoleRepository = userRolesRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    SUser user = userRepository.findByUsername(username);
    if (null == user) {
      throw new UsernameNotFoundException("No user present with username: " + username);
    } else {
      List<String> userRoles = userRoleRepository.findRoleByUserName(username);
      return new CustomUserDetails(user, userRoles);
    }
  }
}
