package com.banana.infrastructure.orm.repositories;

import com.banana.infrastructure.orm.models.SAccount;
import com.banana.infrastructure.orm.models.SUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SAccountRepositoryTests {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SAccountRepository accountRepository;

  private SUser fakeUser;
  private SAccount accountOne;
  private SAccount accountTwo;
  private SAccount accountThree;

  @Before
  public void setUp() {
    this.fakeUser = new SUser("Doe", "John", "john@doe.fr", "johndoe");
    this.entityManager.persist(fakeUser);

    this.accountOne = new SAccount("Account one", 100);
    this.accountOne.setSlug("account-one");
    this.accountOne.setUser(this.fakeUser);
    this.accountTwo = new SAccount("Account two", 200);
    this.accountTwo.setUser(this.fakeUser);
    this.accountThree = new SAccount("Account three", 300);
    this.accountThree.setSlug("account-three");
    this.entityManager.persist(this.accountOne);
    this.entityManager.persist(this.accountTwo);
    this.entityManager.persist(this.accountThree);
  }

  @Test
  public void should_get_accounts_of_user_by_user_id() {
    // WHEN
    List<SAccount> accounts = this.accountRepository.findByUserId(this.fakeUser.getId());
    // THEN
    assertThat(accounts.size()).isEqualTo(2);
    assertThat(accounts.get(0).getName()).isEqualTo("Account one");
    assertThat(accounts.get(0).getInitialAmount()).isEqualTo(100);
    assertThat(accounts.get(1).getName()).isEqualTo("Account two");
    assertThat(accounts.get(1).getInitialAmount()).isEqualTo(200);
  }

  @Test
  public void should_get_accounts_of_user_by_username() {
    List<SAccount> accounts = this.accountRepository.findByUserUsername(this.fakeUser.getUsername());

    assertThat(accounts.size()).isEqualTo(2);
    assertThat(accounts.get(0).getName()).isEqualTo("Account one");
    assertThat(accounts.get(0).getInitialAmount()).isEqualTo(100);
    assertThat(accounts.get(1).getName()).isEqualTo("Account two");
    assertThat(accounts.get(1).getInitialAmount()).isEqualTo(200);
  }

  @Test
  public void should_get_account_of_user_by_username_and_account_name() {
    SAccount account = this.accountRepository.findByUserUsernameAndName(this.fakeUser.getUsername(), "Account one");

    assertThat(account.getName()).isEqualTo("Account one");
    assertThat(account.getInitialAmount()).isEqualTo(100);
    assertThat(account.getUser().getUsername()).isEqualTo("john@doe.fr");
  }

  @Test
  public void should_get_null_if_account_is_not_found_when_search_by_name() {
    SAccount account = this.accountRepository.findByUserUsernameAndName(this.fakeUser.getUsername(), "Unexisting account");

    assertThat(account).isNull();
  }

  @Test
  public void should_get_account_of_user_by_username_and_account_slug() {
    SAccount account = this.accountRepository.findByUserUsernameAndSlug(this.fakeUser.getUsername(), "account-one");

    assertThat(account.getName()).isEqualTo("Account one");
    assertThat(account.getInitialAmount()).isEqualTo(100);
    assertThat(account.getUser().getUsername()).isEqualTo("john@doe.fr");
  }

  @Test
  public void should_create_a_new_account() {
    SAccount newAccount = new SAccount("Account created", 1000.0);
    newAccount.setUser(this.fakeUser);
    newAccount.setSlug("account-created");

    assertThat(false).isTrue();
  }
}
