package com.banana.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@Import(BoneCPConfig.class)
public class PersistentTokenConfig {
  @Autowired
  BoneCPConfig boneCPConfig;

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
    tokenRepositoryImpl.setDataSource(boneCPConfig.dataSource());
    return tokenRepositoryImpl;
  }
}
