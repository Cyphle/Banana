package com.banana.infrastructure.connector.adapters;

import com.banana.domain.adapters.ICreditFetcher;
import com.banana.domain.models.Account;
import com.banana.domain.models.Credit;
import com.banana.domain.models.User;
import com.banana.infrastructure.connector.repositories.IAccountRepository;
import com.banana.infrastructure.connector.repositories.ICreditRepository;
import com.banana.utilities.FakeAccountRepository;
import com.banana.utilities.FakeCreditRepository;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CreditFetcherTests {
  private IAccountRepository accountRepository;
  private ICreditRepository creditRepository;
  private ICreditFetcher creditFetcher;

  private User user;
  private Account account;

  @Before
  public void setup() {
    this.user = new User(1, "Doe", "John", "john@doe.fr");
    this.account = new Account(1, this.user, "My account", "my-account", 1000, new Moment("2016-01-01").getDate());

    this.accountRepository = new FakeAccountRepository();
    this.creditRepository = new FakeCreditRepository();

    this.creditFetcher = new CreditFetcher(this.accountRepository, this.creditRepository);
  }

  @Test
  public void should_get_credits_of_account_of_user() {
    List<Credit> credits = this.creditFetcher.getCreditsOfUserAndAccount(this.user, 1);

    assertThat(credits.size()).isEqualTo(2);
    assertThat(credits.get(0).getDescription()).isEqualTo("Salaire");
    assertThat(credits.get(0).getAmount()).isEqualTo(2400);
    assertThat(credits.get(1).getDescription()).isEqualTo("Maman");
    assertThat(credits.get(1).getAmount()).isEqualTo(500);
  }

  @Test
  public void should_create_credit() {
    Credit newCredit = new Credit("Salaire", 2400, (new Moment("2017-06-30")).getDate());

    Credit createdCredit = this.creditFetcher.createCredit(1, newCredit);

    assertThat(createdCredit).isNotNull();
  }

  @Test
  public void should_return_credit_after_its_update() {
    Credit creditToUpdate = new Credit(1, "Salaire to update", 2500, new Moment("2017-06-25").getDate());

    Credit updatedCredit = this.creditFetcher.updateCredit(this.account.getId(), creditToUpdate);

    assertThat(updatedCredit.getId()).isEqualTo(1);
    assertThat(updatedCredit.getDescription()).isEqualTo("Salaire to update");
    assertThat(updatedCredit.getAmount()).isEqualTo(2500);
  }

  @Test
  public void should_delete_credit() {
    Credit creditToDelete = new Credit(1, "Salaire", 2400, new Moment("2017-06-30").getDate());

    boolean isDeleted = this.creditFetcher.deleteCredit(creditToDelete);

    assertThat(isDeleted).isTrue();
  }
}
