package com.banana.view.services;

import com.banana.domain.adapters.*;
import com.banana.domain.calculators.AccountCalculator;
import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.domain.ports.AccountPort;
import com.banana.infrastructure.connector.adapters.*;
import com.banana.infrastructure.connector.pivots.UserPivot;
import com.banana.infrastructure.connector.repositories.*;
import com.banana.infrastructure.orm.models.SExpense;
import com.banana.infrastructure.orm.repositories.*;
import com.banana.utils.Moment;
import com.banana.view.forms.AccountForm;
import com.banana.view.models.AccountView;
import com.banana.view.pivots.AccountFormPivot;
import com.banana.view.pivots.AccountViewPivot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
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
  private AccountPort banker;

  @Autowired
  public AccountService(
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

    this.banker = new AccountCalculator(this.accountFetcher, this.budgetFetcher, this.chargeFetcher, this.creditFetcher, this.expenseFetcher);
    this.userService = userService;
  }

  public List<AccountView> getAccountsOfUser() {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    List<AccountView> accounts = AccountViewPivot.fromDomainToView(this.banker.getAccountsOfUser(user));
    // TODO
    /*
    for (AccountView account : accounts) {
      calculate current month start amount
      calculate current month current amount
      calculate current month free amount
         -> This should be part of account calculator port calculateX(Account account, Date date)
    }
     */
    return accounts;
  }

  public Account getAccountBySlug(String slug) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    return this.banker.getAccountByUserAndAccountSlug(user, slug);
  }

  public Account createAccount(AccountForm accountForm) {
    if (accountForm.getStartDate() == null) accountForm.setStartDate(new Moment().getFirstDateOfMonth().getDate());
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    if (accountForm.getStartDate() == null) accountForm.setStartDate(new Moment().getFirstDateOfMonth().getDate());
    Account account = AccountFormPivot.fromViewToDomain(user, accountForm);
    return this.banker.createAccount(account);
  }

  public Account updateAccount(AccountForm accountForm) {
    if (accountForm.getStartDate() == null) accountForm.setStartDate(new Moment().getFirstDateOfMonth().getDate());
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Account account = AccountFormPivot.fromViewToDomain(user, accountForm);
    return this.banker.updateAccount(account);
  }

  public boolean deleteAccount(long accountId) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    return this.banker.deleteAccount(user, accountId);
  }
}
