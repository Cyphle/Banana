package com.banana.view.controllers;

import com.banana.BananaApplication;
import com.banana.domain.models.*;
import com.banana.infrastructure.connector.pivots.UserPivot;
import com.banana.utils.Moment;
import com.banana.view.forms.BudgetForm;
import com.banana.view.forms.ChargeForm;
import com.banana.view.services.BudgetService;
import com.banana.view.services.ChargeService;
import com.banana.view.services.ExpenseService;
import com.banana.view.services.UserService;
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
@WebMvcTest(ChargeController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ChargeControllerTests {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private UserService userService;

  @MockBean
  private ChargeService chargeService;

  private User user;
  private Account account;
  private Charge charge;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();

    this.user = new User(1, "John", "Doe", "john@doe.fr");
    this.account = new Account(1, this.user, "My account", "my-account", 2000, new Moment("2013-01-01").getDate());
    this.charge = new Charge(1, "Loyer", 1200, new Moment("2013-01-01").getDate());
    given(this.userService.getAuthenticatedUser()).willReturn(UserPivot.fromDomainToInfrastructure(this.user));
  }

  @Test
  public void should_get_charge_creation_page() throws  Exception {
    this.mvc.perform(get("/charges/create/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("charge/form-create"));
  }

  @Test
  public void should_create_charge() throws Exception {
    given(this.chargeService.createCharge(any(ChargeForm.class))).willReturn(this.account);

    this.mvc.perform(post("/charges/create")
            .param("description", this.charge.getDescription())
            .param("amount", new Double(this.charge.getAmount()).toString())
            .param("startDate", "2017-01-01")
            .param("endDate", "2018-06-01")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));
  }

  @Test
  public void should_get_update_charge_page() throws Exception {
    given(this.chargeService.getCharge(any(long.class), any(long.class))).willReturn(this.charge);

    this.mvc.perform(get("/charges/update/1/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("charge/form-update"));
  }

  @Test
  public void should_update_charge() throws Exception {
    given(this.chargeService.updateCharge(any(ChargeForm.class))).willReturn(this.account);

    this.mvc.perform(post("/charges/update")
            .param("id", "1")
            .param("accountId", "1")
            .param("description", this.charge.getDescription())
            .param("amount", new Double(this.charge.getAmount()).toString())
            .param("startDate", "2017-01-01")
            .param("endDate", "2018-06-01")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/accounts/my-account"));
  }
}
