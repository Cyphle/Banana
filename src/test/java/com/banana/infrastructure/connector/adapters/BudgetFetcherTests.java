package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IBudgetFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.IBudgetRepository;
import com.banana.infrastructure.connector.repositories.IUserRepository;
import com.banana.utilities.FakeAccountRepository;
import com.banana.utilities.FakeBudgetRepository;
import com.banana.utilities.FakeUserRepository;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BudgetFetcherTests {
  private IBudgetFetcher budgetFetcher;
  private IAccountRepository accountRepository;
  private IBudgetRepository budgetRepository;
  private IUserRepository userRepository;
  private User user;
  private Account account;
  private Budget budget;

  @Before
  public void setup() {
    this.user = new User(1, "Doe", "John", "john@doe.fr");
    this.account = new Account(this.user, "My account", 1000);
    this.account.setId(1);

    this.budget = new Budget(1, "My budget", 300, (new Moment()).getFirstDateOfMonth().getDate());

    this.accountRepository = new FakeAccountRepository();
    this.userRepository = new FakeUserRepository();
    this.budgetRepository = new FakeBudgetRepository();

    this.budgetFetcher = new BudgetFetcher(this.userRepository, this.accountRepository, this.budgetRepository);
  }

  @Test
  public void should_get_budgets_of_user_and_account_id() {
    List<Budget> budgets = this.budgetFetcher.getBudgetsOfUserAndAccount(this.user, this.account.getId());

    assertThat(budgets.size()).isEqualTo(2);
    assertThat(budgets.get(0).getName()).isEqualTo("Budget one");
    assertThat(budgets.get(0).getInitialAmount()).isEqualTo(200);
    assertThat(budgets.get(1).getName()).isEqualTo("Budget two");
    assertThat(budgets.get(1).getInitialAmount()).isEqualTo(300);
  }

  @Test
  public void should_get_budget_of_user_and_account_id_by_id() {
    Budget myBudget = this.budgetFetcher.getBudgetOfUserAndAccountById(this.user, this.account.getId(), 1);

    assertThat(myBudget.getId()).isEqualTo(1);
    assertThat(myBudget.getName()).isEqualTo("My budget");
    assertThat(myBudget.getInitialAmount()).isEqualTo(300);
  }

  @Test
  public void should_get_create_budget() {
    Budget newBudget = new Budget("My budget", 200, (new Moment()).getFirstDateOfMonth().getDate());
    Budget createdBudget = this.budgetFetcher.createBudget(this.account, newBudget);

    assertThat(createdBudget).isNotNull();
  }

  @Test
  public void should_return_budget_after_its_update() {
    Budget budgetToUpdate = new Budget(1, "Budget to update", 300, (new Moment()).getFirstDateOfMonth().getDate());

    Budget updatedBudget = this.budgetFetcher.updateBudget(this.account, budgetToUpdate);

    assertThat(updatedBudget.getId()).isEqualTo(1);
    assertThat(updatedBudget.getName()).isEqualTo("Budget to update");
    assertThat(updatedBudget.getInitialAmount()).isEqualTo(300);
  }
}
