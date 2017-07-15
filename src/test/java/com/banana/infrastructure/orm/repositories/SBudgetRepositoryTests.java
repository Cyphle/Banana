package com.banana.infrastructure.orm.repositories;

import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SBudgetRepositoryTests {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SBudgetRepository budgetRepository;

  @Autowired
  private SAccountRepository accountRepository;

  private SUser user;
  private SAccount account;
  private SBudget budgetOne;
  private SBudget budgetTwo;

  @Before
  public void setup() {
    this.user = new SUser("Doe", "John", "john@doe.fr");
    this.entityManager.persist(this.user);

    this.account = new SAccount(this.user, "My Account", "my-account", 2000);
    this.entityManager.persist(this.account);

    this.budgetOne = new SBudget("Budget one", 200, (new Moment()).getFirstDateOfMonth().getDate());
    this.budgetOne.setAccount(this.account);
    this.entityManager.persist(this.budgetOne);
    this.budgetTwo = new SBudget("Budget two", 300, (new Moment()).getFirstDateOfMonth().getDate());
    this.budgetTwo.setAccount(this.account);
    this.entityManager.persist(this.budgetTwo);
  }

  @Test
  public void should_find_budgets_by_username_and_account_id() {
    SAccount myAccount = this.accountRepository.findByUserUsernameAndSlug(this.user.getUsername(), this.account.getSlug());
    List<SBudget> budgets = this.budgetRepository.findByUserUsernameAndAccountId(this.user.getUsername(), myAccount.getId());

    assertThat(budgets.size()).isEqualTo(2);
    assertThat(budgets.get(0).getId()).isGreaterThan(0);
    assertThat(budgets.get(0).getName()).isEqualTo("Budget one");
    assertThat(budgets.get(0).getInitialAmount()).isEqualTo(200);
    assertThat(budgets.get(1).getId()).isGreaterThan(0);
    assertThat(budgets.get(1).getName()).isEqualTo("Budget two");
    assertThat(budgets.get(1).getInitialAmount()).isEqualTo(300);
  }

  @Test
  public void should_save_new_budget() {
    SBudget newBudget = new SBudget("New budget", 400, (new Moment()).getFirstDateOfMonth().getDate());
    newBudget.setAccount(this.account);

    SBudget createdBudget = this.budgetRepository.save(newBudget);

    assertThat(createdBudget.getId()).isGreaterThan(0);
  }
}
