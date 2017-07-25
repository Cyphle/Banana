package com.banana.infrastructure.connector.repositories;

import com.banana.BananaApplication;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SCharge;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.infrastructure.orm.repositories.SChargeRepository;
import com.banana.utils.Moment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={BananaApplication.class})
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ChargeRepositoryTests {
  @MockBean
  private SChargeRepository sChargeRepository;

  private IChargeRepository chargeRepository;
  private SUser sUser;
  private SAccount sAccount;

  @Before
  public void setup() {
    this.sUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.sAccount = new SAccount("My account", 2000);
    this.sAccount.setUser(this.sUser);
    this.sAccount.setId(1);
    this.sAccount.setSlug("my-account");

    this.chargeRepository = new ChargeRepository(this.sChargeRepository);
  }

  @Test
  public void should_create_charge() {
    SCharge sCharge = new SCharge("Loyer", 1200, new Moment(("2013-01-01")).getDate());
    sCharge.setAccount(this.sAccount);
    Moment today = new Moment();
    given(this.sChargeRepository.save(any(SCharge.class))).willReturn(sCharge);

    SCharge createdCharge = this.chargeRepository.createCharge(sCharge);
    Moment createdDate = new Moment(createdCharge.getCreationDate());
    Moment updateDate = new Moment(createdCharge.getUpdateDate());

    assertThat(createdCharge.getAmount()).isEqualTo(1200);
    assertThat(createdCharge.getDescription()).isEqualTo("Loyer");
    assertThat(createdDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(createdDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(createdDate.getYear()).isEqualTo(today.getYear());
    assertThat(updateDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(updateDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(updateDate.getYear()).isEqualTo(today.getYear());
  }
}
