package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.Budget;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SBudget;
import com.banana.infrastructure.orm.models.SUser;
import com.banana.utils.Moment;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BudgetPivotTests {
  @Test
  public void should_pivot_budget_from_infrastructure_to_domain() {
    SUser sUser = new SUser("Doe", "John", "john&doe.fr");
    SAccount sAccount = new SAccount("Account", 2000);
    sAccount.setId(1);
    sAccount.setUser(sUser);
    sAccount.setSlug("account");
    Moment today = (new Moment()).getFirstDateOfMonth();
    SBudget sBudget = new SBudget("Budget", 200, today.getDate());
    sBudget.setAccount(sAccount);
    sBudget.setId(1);

    Budget budget = BudgetPivot.fromInfrastructureToDomain(sBudget);
    Moment startDate = new Moment(budget.getStartDate());

    assertThat(budget.getId()).isEqualTo(1);
    assertThat(budget.getName()).isEqualTo("Budget");
    assertThat(budget.getInitialAmount()).isEqualTo(200);
    assertThat(startDate.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
    assertThat(startDate.getMonthNumber()).isEqualTo(today.getMonthNumber());
    assertThat(startDate.getYear()).isEqualTo(today.getYear());
  }

  @Test
  public void should_pivot_budgets_from_infrastructure_to_domain() {
    SUser sUser = new SUser("Doe", "John", "john&doe.fr");
    SAccount sAccount = new SAccount("Account", 2000);
    sAccount.setId(1);
    sAccount.setUser(sUser);
    sAccount.setSlug("account");
    Moment today = (new Moment()).getFirstDateOfMonth();
    SBudget sBudget = new SBudget("Budget", 200, today.getDate());
    sBudget.setAccount(sAccount);
    sBudget.setId(1);
    SBudget sBudgetTxo = new SBudget("Budget two", 300, today.getDate());
    sBudgetTxo.setAccount(sAccount);
    sBudgetTxo.setId(2);
    List<SBudget> sBudgets = new ArrayList<>();
    sBudgets.add(sBudget);
    sBudgets.add(sBudgetTxo);

    List<Budget> budgets = BudgetPivot.fromInfrastructureToDomain(sBudgets);

    assertThat(budgets.size()).isEqualTo(2);
    assertThat(budgets.get(0).getId()).isEqualTo(1);
    assertThat(budgets.get(0).getName()).isEqualTo("Budget");
    assertThat(budgets.get(0).getInitialAmount()).isEqualTo(200);
    assertThat(budgets.get(1).getId()).isEqualTo(2);
    assertThat(budgets.get(1).getName()).isEqualTo("Budget two");
    assertThat(budgets.get(1).getInitialAmount()).isEqualTo(300);
  }
}
