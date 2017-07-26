package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.ICreditFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.exceptions.UpdateException;
import com.banana.domain.models.Account;
import com.banana.domain.models.Credit;
import com.banana.domain.models.User;
import com.banana.domain.ports.CreditPort;
import com.banana.utilities.FakeAccountFetcher;
import com.banana.utilities.FakeCreditFetcher;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;

public class CreditCalculatorTests {
  private IAccountFetcher accountFetcher;
  private ICreditFetcher creditFetcher;
  private CreditPort creditPort;

  private User user;
  private Account account;
  private Credit creditOne;
  private Credit creditTwo;
  private List<Credit> credits;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
    this.account = new Account(1, this.user, "My account", "my-account", 2000, new Moment("2016-01-01").getDate());

    this.creditOne = new Credit(1, "Salaire", 2400, new Moment("2017-05-30").getDate());
    this.creditTwo = new Credit(2, "Livret A", 500, new Moment("2017-05-20").getDate());
    this.credits = new ArrayList<>();
    this.credits.add(this.creditOne);
    this.credits.add(this.creditTwo);

    this.accountFetcher = new FakeAccountFetcher();
    this.accountFetcher = Mockito.spy(this.accountFetcher);

    this.creditFetcher = new FakeCreditFetcher();
    this.creditFetcher = Mockito.spy(this.creditFetcher);

    this.creditPort = new CreditCalculator(this.accountFetcher, this.creditFetcher);
  }

  @Test
  public void should_throw_error_if_account_does_not_exist_for_new_credit() {
    Mockito.doReturn(null).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));

    Credit newCredit = new Credit("Salaire", 2400, (new Moment("2017-06-30")).getDate());

    try {
      this.creditPort.createCredit(this.user, this.account.getId(), newCredit);
      fail("Should throw error if account does not exists");
    } catch (CreationException e) {
      assertThat(e.getMessage()).contains("No account for user and id");
    }
  }

  @Test
  public void should_create_a_new_credit() {
    Mockito.doReturn(this.account).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));

    Credit newCredit = new Credit("Salaire", 2400, (new Moment("2017-06-30")).getDate());
    Credit createdCredit = this.creditPort.createCredit(this.user, this.account.getId(), newCredit);

    assertThat(createdCredit.getId()).isGreaterThan(0);
    assertThat(createdCredit.getAmount()).isEqualTo(2400);
  }

  @Test
  public void should_throw_error_if_update_credit_does_not_exist() {
    Credit creditToUpdate = new Credit(3, "Salaire", 2400, (new Moment("2017-06-30")).getDate());
    Mockito.doReturn(this.account).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));
    Mockito.doReturn(this.credits).when(this.creditFetcher).getCreditsOfUserAndAccount(any(User.class), any(long.class));

    try {
      this.creditPort.updateCredit(this.user, 1, creditToUpdate);
      fail("Should throw update exception if there is no credit with this id for the given account and user");
    } catch (UpdateException e) {
      assertThat(e.getMessage()).contains("No credit found with id");
    }
  }

  @Test
  public void should_update_credit() {
    Credit creditToUpdate = new Credit(1, "Salaire update", 2500, (new Moment("2017-06-29")).getDate());
    Mockito.doReturn(this.account).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));
    Mockito.doReturn(this.credits).when(this.creditFetcher).getCreditsOfUserAndAccount(any(User.class), any(long.class));

    Credit updatedCredit = this.creditPort.updateCredit(this.user, 1, creditToUpdate);
    Moment creditDate = new Moment(updatedCredit.getCreditDate());

    assertThat(updatedCredit.getId()).isEqualTo(creditToUpdate.getId());
    assertThat(updatedCredit.getDescription()).isEqualTo("Salaire update");
    assertThat(updatedCredit.getAmount()).isEqualTo(2500);
    assertThat(creditDate.getDayOfMonth()).isEqualTo(29);
    assertThat(creditDate.getMonthNumber()).isEqualTo(6);
    assertThat(creditDate.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_delete_credit() {
    Credit creditToDelete = new Credit(1, "Salaire", 2400, (new Moment("2017-06-30")).getDate());
    Mockito.doReturn(this.account).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));
    Mockito.doReturn(this.credits).when(this.creditFetcher).getCreditsOfUserAndAccount(any(User.class), any(long.class));

    boolean isDeleted = this.creditPort.deleteCredit(this.user, 1, creditToDelete);

    assertThat(isDeleted).isTrue();
  }
}
