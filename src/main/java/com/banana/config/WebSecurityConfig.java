package com.banana.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

import com.banana.spring.user.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = CustomUserDetailsService.class)
@Import(PersistentTokenConfig.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private PersistentTokenConfig persistentTokenConfig;

  @Autowired
  DataSource dataSource;

  @Autowired
  public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
		/*
		 * http .authorizeRequests() .anyRequest().permitAll() .and() .formLogin()
		 * .loginPage("/login") .permitAll() .and() .logout() .permitAll();
		 */

    http.authorizeRequests()
            .antMatchers("/hello").access("hasRole('ROLE_ADMIN')")
            .regexMatchers("/accounts.*").hasRole("USER")
            .regexMatchers("/budgets.*").access("hasRole('ROLE_USER')")
            .regexMatchers("/expenses.*").access("hasRole('ROLE_USER')")
            .anyRequest().permitAll()
            .and()
            .formLogin().loginPage("/login")
            .usernameParameter("username").passwordParameter("password")
            .and()
            .logout().logoutSuccessUrl("/login?logout")
            .deleteCookies("remember-me")
            .and()
            .exceptionHandling().accessDeniedPage("/403")
            .and()
            .rememberMe()
            .rememberMeParameter("remember-me")
            .tokenRepository(persistentTokenConfig.persistentTokenRepository()).tokenValiditySeconds(86400)
            .and()
            .csrf();
  }

  @Bean(name = "passwordEncoder")
  public PasswordEncoder passwordencoder() {
    return new BCryptPasswordEncoder();
  }
}
