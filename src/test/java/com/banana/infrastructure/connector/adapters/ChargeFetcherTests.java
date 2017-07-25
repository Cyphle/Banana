package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.IChargeFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.Charge;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.IChargeRepository;
import com.banana.utilities.FakeAccountRepository;
import com.banana.utilities.FakeChargeRepository;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
  public void should_get_charges_of_account_of_user() {
    List<Charge> charges = this.chargeFetcher.getChargesOfUserAndAccount(this.user, 1);

    assertThat(charges.size()).isEqualTo(2);
    assertThat(charges.get(0).getDescription()).isEqualTo("Loyer");
    assertThat(charges.get(0).getAmount()).isEqualTo(1200);
    assertThat(charges.get(1).getDescription()).isEqualTo("Internet");
    assertThat(charges.get(1).getAmount()).isEqualTo(40);
  }

  @Test
  public void should_create_charge() {
    Charge newCharge = new Charge("Loyer", 1200, (new Moment("2017-01-03")).getDate());

    Charge createdCharge = this.chargeFetcher.createCharge(1, newCharge);

    assertThat(createdCharge).isNotNull();
  }

  @Test
  public void should_return_charge_after_its_update() {
    Charge chargeToUpdate = new Charge(1, "Loyer to update", 1400, (new Moment()).getFirstDateOfMonth().getDate());

    Charge updatedCharge = this.chargeFetcher.updateCharge(this.account.getId(), chargeToUpdate);

    assertThat(updatedCharge.getId()).isEqualTo(1);
    assertThat(updatedCharge.getDescription()).isEqualTo("Loyer to update");
    assertThat(updatedCharge.getAmount()).isEqualTo(1400);
  }

  @Test
  public void should_delete_charge() {
    Charge chargeToDelete = new Charge(1, "Loyer to update", 1400, (new Moment()).getFirstDateOfMonth().getDate());

    boolean isDeleted = this.chargeFetcher.deleteCharge(chargeToDelete);

    assertThat(isDeleted).isTrue();
  }
}
