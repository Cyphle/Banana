package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.domain.models.Account;
import com.banana.domain.models.Credit;
import com.banana.domain.models.User;
import com.banana.utils.Moment;
import com.banana.view.forms.CreditForm;
import com.banana.view.services.CreditService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BananaApplication.class})
@WebMvcTest(CreditController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CreditControllerTests {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private CreditService creditService;

  private User user;
  private Account account;
  private Credit credit;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();

    this.user = new User(1, "John", "Doe", "john@doe.fr");
    this.account = new Account(1, this.user, "My account", "my-account", 2000, new Moment("2013-01-01").getDate());
    this.credit = new Credit(1, "Salaire", 2400, new Moment("2017-07-31").getDate());
  }

  @Test
  public void should_get_credit_creation_page() throws  Exception {
    this.mvc.perform(get("/credits/create/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("credit/create-credit"));
  }

  @Test
  public void should_create_credit() throws Exception {
    given(this.creditService.createCredit(any(CreditForm.class))).willReturn(this.account);

    this.mvc.perform(post("/credits/create")
            .param("accountId", "1")
            .param("description", this.credit.getDescription())
            .param("amount", new Double(this.credit.getAmount()).toString())
            .param("creditDate", "2017-07-31")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));
  }

  @Test
  public void should_get_update_credit_page() throws Exception {
    given(this.creditService.getCredit(any(long.class), any(long.class))).willReturn(this.credit);

    this.mvc.perform(get("/credits/update/1/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("credit/update-credit"));
  }

  @Test
  public void should_update_credit() throws Exception {
    given(this.creditService.updateCredit(any(CreditForm.class))).willReturn(this.account);

    this.mvc.perform(post("/credits/update")
            .param("id", "1")
            .param("accountId", "1")
            .param("description", this.credit.getDescription())
            .param("amount", new Double(this.credit.getAmount()).toString())
            .param("creditDate", "2017-06-30")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));
  }
}
