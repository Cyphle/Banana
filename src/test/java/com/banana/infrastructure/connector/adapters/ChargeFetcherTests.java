package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IChargeFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.Charge;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.IChargeRepository;
import com.banana.utilities.FakeAccountRepository;
import com.banana.utilities.FakeChargeRepository;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ChargeFetcherTests {
  private IAccountRepository accountRepository;
  private IChargeRepository chargeRepository;
  private IChargeFetcher chargeFetcher;

  private User user;
  private Account account;

  @Before
  public void setup() {
    this.user = new User(1, "Doe", "John", "john@doe.fr");
    this.account = new Account(1, this.user, "My account", "my-account", 1000);

    this.accountRepository = new FakeAccountRepository();
    this.chargeRepository = new FakeChargeRepository();

    this.chargeFetcher = new ChargeFetcher(this.accountRepository, this.chargeRepository);
  }

  @Test
  public void should_create_charge() {
    Charge newCharge = new Charge("Loyer", 1200, (new Moment("2017-01-03")).getDate());

    Charge createdCharge = this.chargeFetcher.createCharge(1, newCharge);

    assertThat(createdCharge).isNotNull();
  }
}
