package com.banana.view.services;

import com.banana.domain.adapters.*;
import com.banana.domain.calculators.AccountCalculator;
import com.banana.domain.calculators.CreditCalculator;
import com.banana.domain.models.Account;
import com.banana.domain.models.Credit;
import com.banana.domain.models.User;
import com.banana.domain.ports.AccountPort;
import com.banana.domain.ports.CreditPort;
import com.banana.infrastructure.connector.adapters.*;
import com.banana.infrastructure.connector.pivots.UserPivot;
import com.banana.infrastructure.connector.repositories.*;
import com.banana.infrastructure.orm.repositories.*;
import com.banana.view.forms.CreditForm;
import com.banana.view.pivots.CreditFormPivot;
import org.springframework.stereotype.Service;

@Service
public class CreditService {
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
  private CreditPort creditPort;

  public CreditService(
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
    this.creditPort = new CreditCalculator(this.accountFetcher, this.creditFetcher);
    this.userService = userService;
  }

  public Credit getCredit(long accountId, long creditId) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    return this.creditPort.getCreditById(user, accountId, creditId);
  }

  public Account createCredit(CreditForm creditForm) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Credit credit = CreditFormPivot.fromViewToDomain(creditForm);
    Credit createdCredit = this.creditPort.createCredit(user, creditForm.getAccountId(), credit);
    if (createdCredit != null)
      return this.accountPort.getAccountByUserAndAccountId(user, creditForm.getAccountId());
    return null;
  }

  public Account updateCredit(CreditForm creditForm) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Credit credit = CreditFormPivot.fromViewToDomain(creditForm);
    Credit updatedCredit = this.creditPort.updateCredit(user, creditForm.getAccountId(), credit);
    if (updatedCredit != null)
      return this.accountPort.getAccountByUserAndAccountId(user, creditForm.getAccountId());
    return null;
  }

  public boolean deleteCredit(long accountId, long creditId) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Credit credit = this.getCredit(accountId, creditId);
    return this.creditPort.deleteCredit(user, accountId, credit);
  }
}
