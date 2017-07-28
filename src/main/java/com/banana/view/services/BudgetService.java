package com.banana.view.services;

import com.banana.domain.adapters.*;
import com.banana.domain.calculators.AccountCalculator;
import com.banana.domain.calculators.BudgetCalculator;
import com.banana.domain.models.Account;
import com.banana.domain.models.Budget;
import com.banana.domain.models.User;
import com.banana.domain.ports.AccountPort;
import com.banana.domain.ports.BudgetPort;
import com.banana.infrastructure.connector.adapters.*;
import com.banana.infrastructure.connector.pivots.UserPivot;
import com.banana.infrastructure.connector.repositories.*;
import com.banana.infrastructure.orm.repositories.*;
import com.banana.view.forms.BudgetForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BudgetService {
  private SUserRepository sUserRepository;
  private SAccountRepository sAccountRepository;
  private SBudgetRepository sBudgetRepository;
  private SCreditRepository sCreditRepository;
  private SChargeRepository sChargeRepository;
  private SExpenseRepository sExpenseRepository;

  private IUserRepository userRepository;
  private IAccountRepository accountRepository;
  private IBudgetRepository budgetRepository;
  private ICreditRepository creditRepository;
  private IChargeRepository chargeRepository;
  private IExpenseRepository expenseRepository;

  private IAccountFetcher accountFetcher;
  private IBudgetFetcher budgetFetcher;
  private IChargeFetcher chargeFetcher;
  private ICreditFetcher creditFetcher;
  private IExpenseFetcher expenseFetcher;

  private UserService userService;
  private AccountPort accountPort;
  private BudgetPort budgetPort;

  @Autowired
  public BudgetService(
          SUserRepository sUserRepository,
          SAccountRepository sAccountRepository,
          SBudgetRepository sBudgetRepository,
          SChargeRepository sChargeRepository,
          SCreditRepository sCreditRepository,
          SExpenseRepository sExpenseRepository,
          UserService userService
  ) {
    this.sUserRepository = sUserRepository;
    this.sAccountRepository = sAccountRepository;
    this.sBudgetRepository = sBudgetRepository;
    this.sChargeRepository = sChargeRepository;
    this.sCreditRepository = sCreditRepository;
    this.sExpenseRepository = sExpenseRepository;

    this.userRepository = new UserRepository(this.sUserRepository);
    this.accountRepository = new AccountRepository(this.sAccountRepository);
    this.budgetRepository = new BudgetRepository(this.sBudgetRepository);
    this.chargeRepository = new ChargeRepository(this.sChargeRepository);
    this.creditRepository = new CreditRepository(this.sCreditRepository);
    this.expenseRepository = new ExpenseRepository(this.sExpenseRepository);

    this.accountFetcher = new AccountFetcher(this.userRepository, this.accountRepository);
    this.budgetFetcher = new BudgetFetcher(this.accountRepository, this.budgetRepository);
    this.chargeFetcher = new ChargeFetcher(this.accountRepository, this.chargeRepository);
    this.creditFetcher = new CreditFetcher(this.accountRepository, this.creditRepository);
    this.expenseFetcher = new ExpenseFetcher(this.accountRepository, this.budgetRepository, this.expenseRepository);

    this.accountPort = new AccountCalculator(this.accountFetcher, this.budgetFetcher, this.chargeFetcher, this.creditFetcher, this.expenseFetcher);
    this.budgetPort = new BudgetCalculator(this.accountFetcher, this.budgetFetcher,this.expenseFetcher);
    this.userService = userService;
  }

  public Budget getBudget(long accountId, long budgetId) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    return this.budgetPort.getBudgetById(user, accountId, budgetId);
  }

  public Account createBudget(BudgetForm budgetForm) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Budget budget = new Budget(budgetForm.getName(), budgetForm.getInitialAmount(), budgetForm.getStartDate());
    if (budgetForm.getEndDate() != null) budget.setEndDate(budgetForm.getEndDate());
    Budget createdBudget = this.budgetPort.createBudget(user, budgetForm.getAccountId(), budget);
    if (createdBudget != null)
      return this.accountPort.getAccountByUserAndAccountId(user, budgetForm.getAccountId());
    return null;
  }

  public Account updateBudget(BudgetForm budgetForm) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Budget budget = new Budget(budgetForm.getName(), budgetForm.getInitialAmount(), budgetForm.getStartDate());
    budget.setId(budgetForm.getId());
    if (budgetForm.getEndDate() != null) budget.setEndDate(budgetForm.getEndDate());
    Budget updatedBudget = this.budgetPort.updateBudget(user, budgetForm.getAccountId(), budget);
    if (updatedBudget != null)
      return this.accountPort.getAccountByUserAndAccountId(user, budgetForm.getAccountId());
    return null;
  }

  public boolean deleteBudget(long accountId, long budgetId, Date endDate) {
    // TODO for budgetAPI /budgets/id?end-date=....
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Budget budget = this.budgetFetcher.getBudgetOfUserAndAccountById(user, accountId, budgetId);
    budget.setEndDate(endDate);
    Budget deletedBudget = this.budgetPort.deleteBudget(user, accountId, budget);
    if (deletedBudget != null)
      return true;
    else
      return false;
  }
}
