package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.exceptions.NoElementFoundException;
import com.banana.domain.models.Account;
import com.banana.domain.models.Charge;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.domain.ports.ChargePort;
import com.banana.utilities.FakeAccountFetcher;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ChargeCalculatorTests {
  private IAccountFetcher accountFetcher;
  private ChargePort chargePort;

  private User user;
  private Account account;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
    this.account = new Account(1, this.user, "My account", "my-account", 2000);

    this.accountFetcher = new FakeAccountFetcher();

    this.chargePort = new ChargeCalculator(this.accountFetcher);
  }

  @Test
  public void should_throw_error_if_account_does_not_exist_for_new_charge() {
    Charge newCharge = new Charge("Loyer", 1200, (new Moment("2013-02-10")).getDate());

    try {
      Charge createdCharge = this.chargePort.createCharge(this.user, this.account.getId(), newCharge);
      fail("Should throw error if account does not exists");
    } catch (NoElementFoundException e) {
      assertThat(e.getMessage()).contains("No account found with id");
    }
  }

  @Test
  public void should_create_a_new_charge() {
    Charge newCharge = new Charge("Loyer", 1200, (new Moment("2013-02-10")).getDate());

    Charge createdCharge = this.chargePort.createCharge(this.user, this.account.getId(), newCharge);

    assertThat(createdCharge.getId()).isGreaterThan(0);
  }
}
