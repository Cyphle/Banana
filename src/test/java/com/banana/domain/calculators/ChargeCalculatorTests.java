package com.banana.domain.calculators;

import com.banana.domain.adapters.IAccountFetcher;
import com.banana.domain.adapters.IChargeFetcher;
import com.banana.domain.exceptions.CreationException;
import com.banana.domain.exceptions.UpdateException;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;

public class ChargeCalculatorTests {
  private IAccountFetcher accountFetcher;
  private IChargeFetcher chargeFetcher;
  private ChargePort chargePort;

  private User user;
  private Account account;
  private Charge chargeOne;
  private Charge chargeTwo;
  private List<Charge> charges;

  @Before
  public void setup() {
    this.user = new User("Doe", "John", "john@doe.fr");
    this.account = new Account(1, this.user, "My account", "my-account", 2000, new Moment("2016-01-01").getDate());

    this.chargeOne = new Charge(1, "Loyer", 1200, new Moment("2013-01-01").getDate());
    this.chargeTwo = new Charge(2, "Internet", 40, new Moment("2017-01-01").getDate());
    this.charges = new ArrayList<>();
    this.charges.add(this.chargeOne);
    this.charges.add(this.chargeTwo);

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
      this.chargePort.createCharge(this.user, this.account.getId(), newCharge);
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

  @Test
  public void should_throw_error_if_update_charge_does_not_exist() {
    Charge chargeToUpdate = new Charge(3, "Loyer", 1400, (new Moment("2017-08-01")).getDate());
    Mockito.doReturn(this.account).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));
    Mockito.doReturn(this.charges).when(this.chargeFetcher).getChargesOfUserAndAccount(any(User.class), any(long.class));

    try {
      this.chargePort.updateCharge(this.user, 1, chargeToUpdate);
      fail("Should throw update exception if there is no charge with this id for the given account and user");
    } catch (UpdateException e) {
      assertThat(e.getMessage()).contains("No charge found with id");
    }
  }

  @Test
  public void should_update_charge_amount() {
    Charge chargeToUpdate = new Charge(1, "Loyer", 1400, (new Moment("2017-08-01")).getDate());
    Mockito.doReturn(this.account).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));
    Mockito.doReturn(this.charges).when(this.chargeFetcher).getChargesOfUserAndAccount(any(User.class), any(long.class));

    Charge updatedCharge = this.chargePort.updateCharge(this.user, 1, chargeToUpdate);
    Moment startDate = new Moment(updatedCharge.getStartDate());

    assertThat(updatedCharge.getId()).isNotEqualTo(chargeToUpdate.getId());
    assertThat(updatedCharge.getDescription()).isEqualTo("Loyer");
    assertThat(updatedCharge.getAmount()).isEqualTo(1400);
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(8);
    assertThat(startDate.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_update_charge_other_properties_than_amount() {
    Charge chargeToUpdate = new Charge(1, "Loyer update", 1200, (new Moment("2017-08-01")).getDate());
    chargeToUpdate.setEndDate(new Moment("2018-03-20").getDate());
    Mockito.doReturn(this.account).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));
    Mockito.doReturn(this.charges).when(this.chargeFetcher).getChargesOfUserAndAccount(any(User.class), any(long.class));

    Charge updatedCharge = this.chargePort.updateCharge(this.user, 1, chargeToUpdate);
    Moment startDate = new Moment(updatedCharge.getStartDate());
    Moment endDate = new Moment(updatedCharge.getEndDate());

    assertThat(updatedCharge.getId()).isEqualTo(chargeToUpdate.getId());
    assertThat(updatedCharge.getDescription()).isEqualTo("Loyer update");
    assertThat(updatedCharge.getAmount()).isEqualTo(1200);
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(8);
    assertThat(startDate.getYear()).isEqualTo(2017);
    assertThat(endDate.getDayOfMonth()).isEqualTo(31);
    assertThat(endDate.getMonthNumber()).isEqualTo(3);
    assertThat(endDate.getYear()).isEqualTo(2018);
  }

  @Test
  public void should_delete_charge() {
    Charge chargeToUpdate = new Charge(1, "Loyer update", 1200, (new Moment("2017-08-01")).getDate());
    chargeToUpdate.setEndDate(new Moment("2018-03-10").getDate());
    Mockito.doReturn(this.account).when(this.accountFetcher).getAccountByUserAndId(any(User.class), any(long.class));
    Mockito.doReturn(this.charges).when(this.chargeFetcher).getChargesOfUserAndAccount(any(User.class), any(long.class));

    Charge deletedCharge = this.chargePort.deleteCharge(this.user, 1, chargeToUpdate);
    Moment endDate = new Moment(deletedCharge.getEndDate());

    assertThat(endDate.getDayOfMonth()).isEqualTo(31);
    assertThat(endDate.getMonthNumber()).isEqualTo(3);
    assertThat(endDate.getYear()).isEqualTo(2018);
  }
}
