package com.banana.infrastructure.connector.repositories;

import com.banana.BananaApplication;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SBudgetRepository;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={BananaApplication.class})
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class BudgetRepositoryTests {
  @MockBean
  private SBudgetRepository sBudgetRepository;

  private IBudgetRepository budgetRepository;
  private SUser sUser;
  private SAccount sAccount;
  private List<SBudget> sBudgets;
  private SBudget sBudgetOne;
  private SBudget sBudgetTwo;

  @Before
  public void setup() {
    this.sUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.sAccount = new SAccount("My account", 2000);
    this.sAccount.setUser(this.sUser);
    this.sAccount.setId(1);
    this.sAccount.setSlug("my-account");
    this.sBudgets = new ArrayList<>();
    this.sBudgetOne = new SBudget("Budget one", 200, (new Moment()).getFirstDateOfMonth().getDate());
    this.sBudgetOne.setId(1);
    this.sBudgetOne.setAccount(this.sAccount);
    this.sBudgets.add(this.sBudgetOne);
    this.sBudgetTwo = new SBudget("Budget two", 300, (new Moment()).getFirstDateOfMonth().getDate());
    this.sBudgetTwo.setId(2);
    this.sBudgetTwo.setAccount(this.sAccount);
    this.sBudgets.add(this.sBudgetTwo);

    this.budgetRepository = new BudgetRepository(this.sBudgetRepository);
  }

  @Test
  public void should_get_budgets_of_account() {
    given(this.sBudgetRepository.findByUserUsernameAndAccountId(any(String.class), any(long.class))).willReturn(this.sBudgets);

    List<SBudget> fetchedBudgets = this.budgetRepository.getBudgetsOfUserAndAccount(this.sUser, 1);

    assertThat(fetchedBudgets.size()).isEqualTo(2);
    assertThat(fetchedBudgets.get(0).getName()).isEqualTo("Budget one");
    assertThat(fetchedBudgets.get(0).getInitialAmount()).isEqualTo(200);
    assertThat(fetchedBudgets.get(1).getName()).isEqualTo("Budget two");
    assertThat(fetchedBudgets.get(1).getInitialAmount()).isEqualTo(300);
  }

  @Test
  public void should_get_budget_of_user_by_account_id_and_budget_id() {
    given(this.sBudgetRepository.findByUserUsernameAndAccountIdAndBudgetId(any(String.class), any(long.class), any(long.class))).willReturn(this.sBudgetOne);

    SBudget fetchedBudget = this.budgetRepository.getBudgetOfUserAndAccountById(this.sUser, 1, 1);

    assertThat(fetchedBudget.getId()).isEqualTo(1);
    assertThat(fetchedBudget.getName()).isEqualTo("Budget one");
    assertThat(fetchedBudget.getInitialAmount()).isEqualTo(200);
  }

  @Test
  public void should_create_new_budget() {
    SBudget newBudget = new SBudget("New budget", 400, (new Moment()).getFirstDateOfMonth().getDate());
    newBudget.setAccount(this.sAccount);
    Moment today = new Moment();
    given(this.sBudgetRepository.save(any(SBudget.class))).willReturn(newBudget);

    SBudget createdBudget = this.budgetRepository.createBudget(newBudget);
    Moment creationDate = new Moment(createdBudget.getCreationDate());
    Moment updateDate = new Moment(createdBudget.getUpdateDate());

    assertThat(createdBudget.getName()).isEqualTo("New budget");
    assertThat(createdBudget.getInitialAmount()).isEqualTo(400);
    assertThat(creationDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(creationDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(creationDate.getYear()).isEqualTo(today.getYear());
    assertThat(updateDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(updateDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(updateDate.getYear()).isEqualTo(today.getYear());
  }

  @Test
  public void should_update_budget() {
    SBudget budgetToUpdate = new SBudget("Budget to update", 300, (new Moment("2017-01-01")).getDate());
    budgetToUpdate.setAccount(this.sAccount);
    Moment today = new Moment();
    given(this.sBudgetRepository.save(any(SBudget.class))).willReturn(budgetToUpdate);

    SBudget updatedBudget = this.budgetRepository.updateBudget(budgetToUpdate);
    Moment updateDate = new Moment(updatedBudget.getUpdateDate());

    assertThat(updatedBudget.getName()).isEqualTo("Budget to update");
    assertThat(updatedBudget.getInitialAmount()).isEqualTo(300);
    assertThat(updateDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(updateDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(updateDate.getYear()).isEqualTo(today.getYear());
  }
}
