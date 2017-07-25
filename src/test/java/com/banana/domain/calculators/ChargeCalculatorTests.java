package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IChargeFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.models.Account;
import com.banana.domain.models.Charge;
import com.banana.domain.models.User;
import com.banana.domain.ports.ChargePort;
import com.banana.utilities.FakeAccountFetcher;
import com.banana.utilities.FakeChargeFetcher;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;

public class ChargeCalculatorTests {
  private IAccountFetcher accountFetcher;
  private IChargeFetcher chargeFetcher;
  private ChargePort chargePort;

  private User user;
  private Account account;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
    this.account = new Account(1, this.user, "My account", "my-account", 2000);

    this.accountFetcher = new FakeAccountFetcher();
    this.accountFetcher = Mockito.spy(this.accountFetcher);

    this.chargeFetcher = new FakeChargeFetcher();
    this.chargeFetcher = Mockito.spy(this.chargeFetcher);

    this.chargePort = new ChargeCalculator(this.accountFetcher, this.chargeFetcher);
  }

  @Test
  public void should_throw_error_if_account_does_not_exist_for_new_charge() {
    Mockito.doReturn(null).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));

    Charge newCharge = new Charge("Loyer", 1200, (new Moment("2013-02-10")).getDate());

    try {
      Charge createdCharge = this.chargePort.createCharge(this.user, this.account.getId(), newCharge);
      fail("Should throw error if account does not exists");
    } catch (CreationException e) {
      assertThat(e.getMessage()).contains("No account for user and id");
    }
  }

  @Test
  public void should_create_a_new_charge() {
    Mockito.doReturn(this.account).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));

    Charge newCharge = new Charge("Loyer", -1200, (new Moment("2013-02-10")).getDate());

    Charge createdCharge = this.chargePort.createCharge(this.user, this.account.getId(), newCharge);

    assertThat(createdCharge.getId()).isGreaterThan(0);
    assertThat(createdCharge.getAmount()).isEqualTo(1200);
  }
}
