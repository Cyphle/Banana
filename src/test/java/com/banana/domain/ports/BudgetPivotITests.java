package com.banana.domain.ports;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.calculators.AccountCalculator;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.adapters.AccountFetcher;
import com.banana.infrastructure.connector.repositories.AccountRepository;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.IUserRepository;
import com.banana.infrastructure.connector.repositories.UserRepository;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SAccountRepository;
import com.banana.infrastructure.orm.repositories.SUserRepository;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BudgetPivotITests {
  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private SAccountRepository sAccountRepository;
  @Autowired
  private SUserRepository sUserRepository;

  private IAccountRepository accountRepository;
  private IUserRepository userRepository;
  private IAccountFetcher accountFetcher;
  private AccountPort accountPort;
  private BudgetPort budgetPort;

  private User user;
  private SUser fakeUser;
  private SAccount accountOne;

  @Before
  public void setup() {
    this.user = new User(1, "Doe", "John", "john@doe.fr");
    this.fakeUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.entityManager.persist(fakeUser);
    Moment today = new Moment();
    this.accountOne = new SAccount("Account one", 100);
    this.accountOne.setSlug("account-one");
    this.accountOne.setUser(this.fakeUser);
    this.accountOne.setCreationDate(today.getDate());
    this.accountOne.setUpdateDate(today.getDate());
    this.entityManager.persist(this.accountOne);

    this.accountRepository = new AccountRepository(this.sAccountRepository);
    this.userRepository = new UserRepository(this.sUserRepository);
    this.accountFetcher = new AccountFetcher(this.userRepository, this.accountRepository);
    this.accountPort = new AccountCalculator(this.accountFetcher);
  }

  @Test
  public void should_create_new_budget() {
    Moment today = new Moment();
    Account myAccount = this.accountPort.getAccountByUserAndAccountSlug(this.user, "my-account");
    Budget newBudget = new Budget("My budget", 200, today.getFirstDateOfMonth().getDate());

    Budget createdBudget = this.budgetPort.createBudget(myAccount, newBudget);
    Moment startDate = new Moment(createdBudget.getStartDate());

    assertThat(createdBudget.getId()).isGreaterThan(0);
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(startDate.getYear()).isEqualTo(today.getYear());
  }

  /*
    Should add budget
    -> check that budget initial amount is not negative
    -> Get all budgets of account Id and User user
    -> check if budget name does not already exists
    -> if not, add budget
   */


  /*
    Should update budget
    -> check that budget initial amount is not negative
    -> check that budget id belongs to account id and user user
    -> check that budget name does not already exists
    -> update
   */

  /*
    Should add expense
    -> check that total budget expenses + expense is not higher that budget initial amount (for the month)
    -> add expense
   */

  /*
    Should delete budget
    -> modify endDate in infrastructure
   */
}
