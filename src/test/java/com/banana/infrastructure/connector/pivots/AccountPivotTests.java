package com.banana.infrastructure.connector.pivots;

import com.banana.domain.models.Account;
import com.banana.domain.models.User;
import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountPivotTests {
  @Test
  public void should_pivot_infrastructure_account_to_domain_account() {
    SUser sUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    SAccount sAccount = new SAccount("Account one", 100);
    sAccount.setSlug("account-one");
    sAccount.setId(1);
    sAccount.setUser(sUser);

    Account account = AccountPivot.fromInfrastructureToDomain(sAccount);

    assertThat(account.getId()).isEqualTo(1);
    assertThat(account.getName()).isEqualTo("Account one");
    assertThat(account.getSlug()).isEqualTo("account-one");
    assertThat(account.getInitialAmount()).isEqualTo(100);
  }

  @Test
  public void should_pivot_infrastructure_accounts_to_domain_accounts() {
    SUser sUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    SAccount sAccount = new SAccount("Account one", 100);
    sAccount.setId(1);
    sAccount.setUser(sUser);
    SAccount sAccountTwo = new SAccount("Account two", 200);
    sAccountTwo.setId(2);
    sAccountTwo.setUser(sUser);
    List<SAccount> sAccounts = new ArrayList<>();
    sAccounts.add(sAccount);
    sAccounts.add(sAccountTwo);

    List<Account> accounts = AccountPivot.fromInfrastructureToDomain(sAccounts);

    assertThat(accounts.size()).isEqualTo(2);
    assertThat(accounts.get(0).getId()).isEqualTo(1);
    assertThat(accounts.get(0).getName()).isEqualTo("Account one");
    assertThat(accounts.get(0).getInitialAmount()).isEqualTo(100);
    assertThat(accounts.get(1).getId()).isEqualTo(2);
    assertThat(accounts.get(1).getName()).isEqualTo("Account two");
    assertThat(accounts.get(1).getInitialAmount()).isEqualTo(200);
  }

  @Test
  public void should_pivot_domain_account_to_infrastructure_account() {
    User user = new User(1, "Doe", "John", "john@doe.fr");
    Account account = new Account(1, user, "Account", "account", 200.0);

    SAccount sAccount = AccountPivot.fromDomainToInfrastructure(account);

    assertThat(sAccount.getName()).isEqualTo("Account");
    assertThat(sAccount.getInitialAmount()).isEqualTo(200.0);
    assertThat(sAccount.getSlug()).isEqualTo("account");
    assertThat(sAccount.isDeleted()).isFalse();
  }
}
