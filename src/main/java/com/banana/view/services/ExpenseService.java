package com.banana.view.services;

import com.banana.domain.adapters.*;
import com.banana.domain.calculators.AccountCalculator;
import com.banana.domain.calculators.ExpenseCalculator;
import com.banana.domain.models.Account;
import com.banana.domain.models.Expense;
import com.banana.domain.models.User;
import com.banana.domain.ports.AccountPort;
import com.banana.domain.ports.ExpensePort;
import com.banana.infrastructure.connector.adapters.*;
import com.banana.infrastructure.connector.pivots.UserPivot;
import com.banana.infrastructure.connector.repositories.*;
import com.banana.infrastructure.orm.repositories.*;
import com.banana.view.forms.ExpenseForm;
import com.banana.view.pivots.ExpenseFormPivot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
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
  private ExpensePort expensePort;
  private AccountPort accountPort;

  @Autowired
  public ExpenseService(
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
    this.expensePort = new ExpenseCalculator(this.accountFetcher, this.budgetFetcher, this.expenseFetcher);
    this.userService = userService;
  }

  public Expense getExpense(long accountId, long budgetId, long expenseId) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    return this.expensePort.getExpenseById(user, accountId, budgetId, expenseId);
  }

  public Account createExpense(ExpenseForm expenseForm) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Expense expense = ExpenseFormPivot.fromViewToDomain(expenseForm);
    Expense createdExpense = this.expensePort.createExpense(user, expenseForm.getAccountId(), expenseForm.getBudgetId(), expense);
    if (createdExpense != null)
      return this.accountPort.getAccountByUserAndAccountId(user, expenseForm.getAccountId());
    return null;
  }

  public Account updateExpense(ExpenseForm expenseForm) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Expense expense = ExpenseFormPivot.fromViewToDomain(expenseForm);
    Expense updatedExpense = this.expensePort.updateExpense(user, expenseForm.getAccountId(), expenseForm.getBudgetId(), expense);
    if (updatedExpense != null)
      return this.accountPort.getAccountByUserAndAccountId(user, expenseForm.getAccountId());
    return null;
  }

  public boolean deleteExpense(long accountId, long budgetId, long expenseId) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Expense expense = this.getExpense(accountId, budgetId, expenseId);
    return this.expensePort.deleteExpense(user, accountId, budgetId, expense);
  }
}
