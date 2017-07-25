package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.Charge;
import com.banana.infrastructure.orm.models.SCharge;
import com.banana.utils.Moment;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ChargePivotTests {
  @Test
  public void should_pivot_charge_from_infrastructure_to_domain() {
    SCharge sCharge = new SCharge("Loyer", 1200, (new Moment("2013-01-01")).getDate());
    sCharge.setId(1);
    sCharge.setEndDate((new Moment("2017-12-31")).getDate());

    Charge charge = ChargePivot.fromInfrastructureToDomain(sCharge);
    Moment startDate = new Moment(charge.getStartDate());
    Moment endDate = new Moment(charge.getEndDate());

    assertThat(charge.getId()).isEqualTo(1);
    assertThat(charge.getAmount()).isEqualTo(1200);
    assertThat(charge.getDescription()).isEqualTo("Loyer");
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(1);
    assertThat(startDate.getYear()).isEqualTo(2013);
    assertThat(endDate.getDayOfMonth()).isEqualTo(31);
    assertThat(endDate.getMonthNumber()).isEqualTo(12);
    assertThat(endDate.getYear()).isEqualTo(2017);
  }

  @Test
  public void should_pivot_charge_from_domain_to_infrastructure() {
    Charge charge = new Charge("Loyer", 1200, (new Moment("2013-01-01")).getDate());
    charge.setId(1);
    charge.setEndDate((new Moment("2017-12-31")).getDate());

    SCharge sCharge = ChargePivot.fromDomainToInfrastructure(charge);
    Moment startDate = new Moment(sCharge.getStartDate());
    Moment endDate = new Moment(sCharge.getEndDate());

    assertThat(sCharge.getId()).isEqualTo(1);
    assertThat(sCharge.getAmount()).isEqualTo(1200);
    assertThat(sCharge.getDescription()).isEqualTo("Loyer");
    assertThat(startDate.getDayOfMonth()).isEqualTo(1);
    assertThat(startDate.getMonthNumber()).isEqualTo(1);
    assertThat(startDate.getYear()).isEqualTo(2013);
    assertThat(endDate.getDayOfMonth()).isEqualTo(31);
    assertThat(endDate.getMonthNumber()).isEqualTo(12);
    assertThat(endDate.getYear()).isEqualTo(2017);
  }
}
