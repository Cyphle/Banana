package com.banana.view.services;

import com.banana.domain.adapters.*;
import com.banana.domain.calculators.AccountCalculator;
import com.banana.domain.calculators.ChargeCalculator;
import com.banana.domain.models.Account;
import com.banana.domain.models.Charge;
import com.banana.domain.models.User;
import com.banana.domain.ports.AccountPort;
import com.banana.domain.ports.ChargePort;
import com.banana.infrastructure.connector.adapters.*;
import com.banana.infrastructure.connector.pivots.UserPivot;
import com.banana.infrastructure.connector.repositories.*;
import com.banana.infrastructure.orm.repositories.*;
import com.banana.utils.Moment;
import com.banana.view.forms.ChargeForm;
import com.banana.view.pivots.ChargeFormPivot;
import org.springframework.stereotype.Service;

@Service
public class ChargeService {
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
  private ChargePort chargePort;

  public ChargeService(SUserRepository sUserRepository,
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
    this.chargePort = new ChargeCalculator(this.accountFetcher, this.chargeFetcher);
    this.userService = userService;
  }

  public Charge getCharge(long accountId, long chargeId) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    return this.chargePort.getChargeById(user, accountId, chargeId);
  }

  public Account createCharge(ChargeForm chargeForm) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Charge charge = ChargeFormPivot.fromViewToDomain(chargeForm);
    Charge createdCharge = this.chargePort.createCharge(user, chargeForm.getAccountId(), charge);
    if (createdCharge != null)
      return this.accountPort.getAccountByUserAndAccountId(user, chargeForm.getAccountId());
    return null;
  }

  public Account updateCharge(ChargeForm chargeForm) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Charge charge = ChargeFormPivot.fromViewToDomain(chargeForm);
    Charge updatedCharge = this.chargePort.updateCharge(user, chargeForm.getAccountId(), charge);
    if (updatedCharge != null)
      return this.accountPort.getAccountByUserAndAccountId(user, chargeForm.getAccountId());
    return null;
  }

  public boolean deleteCharge(long accountId, long chargeId, String endDate) {
    User user = UserPivot.fromInfrastructureToDomain(this.userService.getAuthenticatedUser());
    Charge charge = this.getCharge(accountId, chargeId);
    if (endDate != null && endDate.length() > 0) charge.setEndDate(new Moment(endDate).getDate());
    else charge.setEndDate(new Moment().getLastDayOfPreviousMonth().getDate());
    Charge deletedCharge = this.chargePort.deleteCharge(user, accountId, charge);
    if (deletedCharge != null)
      return true;
    return false;
  }
}
