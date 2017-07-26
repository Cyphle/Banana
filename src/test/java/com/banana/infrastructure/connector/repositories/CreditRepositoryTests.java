package com.banana.infrastructure.connector.repositories;

import com.banana.BananaApplication;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SCredit;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SCreditRepository;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={BananaApplication.class})
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CreditRepositoryTests {
  @MockBean
  private SCreditRepository sCreditRepository;

  private ICreditRepository creditRepository;
  private SUser sUser;
  private SAccount sAccount;
  private List<SCredit> credits;
  private SCredit creditOne;
  private SCredit creditTwo;

  @Before
  public void setup() {
    this.sUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.sAccount = new SAccount("My account", 2000, new Moment("2016-01-01").getDate());
    this.sAccount.setUser(this.sUser);
    this.sAccount.setId(1);
    this.sAccount.setSlug("my-account");

    this.creditOne = new SCredit("Salaire", 2400, new Moment("2017-06-30").getDate());
    this.creditTwo = new SCredit("Maman", 500, new Moment("2017-06-25").getDate());
    this.credits = new ArrayList<>();
    this.credits.add(this.creditOne);
    this.credits.add(this.creditTwo);

    this.creditRepository = new CreditRepository(this.sCreditRepository);
  }

  @Test
  public void should_get_credits_of_account_of_user() {
    given(this.sCreditRepository.findByUserUsernameAndAccountId(any(String.class), any(long.class))).willReturn(this.credits);

    List<SCredit> fetchedCredits = this.creditRepository.getCreditsOfUserAndAccount(this.sUser, 1);

    assertThat(fetchedCredits.size()).isEqualTo(2);
    assertThat(fetchedCredits.get(0).getDescription()).isEqualTo("Salaire");
    assertThat(fetchedCredits.get(1).getDescription()).isEqualTo("Maman");
  }

  @Test
  public void should_create_credit() {
    SCredit sCredit = new SCredit("Salaire", 2400, new Moment(("2017-06-30")).getDate());
    sCredit.setAccount(this.sAccount);
    Moment today = new Moment();
    given(this.sCreditRepository.save(any(SCredit.class))).willReturn(sCredit);

    SCredit createdCredit = this.creditRepository.createCredit(sCredit);
    Moment createdDate = new Moment(createdCredit.getCreationDate());
    Moment updateDate = new Moment(createdCredit.getUpdateDate());

    assertThat(createdCredit.getAmount()).isEqualTo(2400);
    assertThat(createdCredit.getDescription()).isEqualTo("Salaire");
    assertThat(createdDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(createdDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(createdDate.getYear()).isEqualTo(today.getYear());
    assertThat(updateDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(updateDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(updateDate.getYear()).isEqualTo(today.getYear());
  }

  @Test
  public void should_update_credit() {
    SCredit creditToUpdate = new SCredit("Salaire update", 2400, (new Moment("2017-06-30")).getDate());
    creditToUpdate.setAccount(this.sAccount);
    Moment today = new Moment();
    given(this.sCreditRepository.save(any(SCredit.class))).willReturn(creditToUpdate);

    SCredit updatedCredit = this.creditRepository.updateCredit(creditToUpdate);
    Moment updateDate = new Moment(updatedCredit.getUpdateDate());

    assertThat(updatedCredit.getDescription()).isEqualTo("Salaire update");
    assertThat(updatedCredit.getAmount()).isEqualTo(2400);
    assertThat(updateDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(updateDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(updateDate.getYear()).isEqualTo(today.getYear());
  }
}
