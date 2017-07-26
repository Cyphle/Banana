package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.Credit;
import com.banana.infrastructure.orm.models.SCredit;
import com.banana.utils.Moment;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CreditPivotTests {
  @Test
  public void should_pivot_credit_from_infrastructure_to_domain() {
    SCredit sCredit = new SCredit("Salaire", 2400, (new Moment("2017-06-30")).getDate());
    sCredit.setId(1);

    Credit credit = CreditPivot.fromInfrastructureToDomain(sCredit);
    Moment creditDate = new Moment(credit.getCreditDate());

    assertThat(credit.getId()).isEqualTo(1);
    assertThat(credit.getAmount()).isEqualTo(2400);
    assertThat(credit.getDescription()).isEqualTo("Salaire");
    assertThat(creditDate.getDayOfMonth()).isEqualTo(30);
    assertThat(creditDate.getMonthNumber()).isEqualTo(6);
    assertThat(creditDate.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_pivot_credits_from_infrastructure_to_domain() {
    SCredit sCreditOne = new SCredit("Salaire", 2400, (new Moment("2017-06-30")).getDate());
    sCreditOne.setId(1);
    SCredit sCreditTwo = new SCredit("Maman", 500, new Moment("2017-06-25").getDate());
    sCreditTwo.setId(2);
    List<SCredit> sCredits = new ArrayList<>();
    sCredits.add(sCreditOne);
    sCredits.add(sCreditTwo);

    List<Credit> credits = CreditPivot.fromInfrastructureToDomain(sCredits);

    assertThat(credits.get(0).getId()).isEqualTo(1);
    assertThat(credits.get(0).getAmount()).isEqualTo(2400);
    assertThat(credits.get(0).getDescription()).isEqualTo("Salaire");
    assertThat(credits.get(1).getId()).isEqualTo(2);
    assertThat(credits.get(1).getAmount()).isEqualTo(500);
    assertThat(credits.get(1).getDescription()).isEqualTo("Maman");
  }

  @Test
  public void should_pivot_credit_from_domain_to_infrastructure() {
    Credit credit = new Credit("Salaire", 2400, (new Moment("2017-06-30")).getDate());
    credit.setId(1);

    SCredit sCredit = CreditPivot.fromDomainToInfrastructure(credit);
    Moment creditDate = new Moment(sCredit.getCreditDate());

    assertThat(sCredit.getId()).isEqualTo(1);
    assertThat(sCredit.getAmount()).isEqualTo(2400);
    assertThat(sCredit.getDescription()).isEqualTo("Salaire");
    assertThat(creditDate.getDayOfMonth()).isEqualTo(30);
    assertThat(creditDate.getMonthNumber()).isEqualTo(6);
    assertThat(creditDate.getYear()).isEqualTo(2017);
  }
}
