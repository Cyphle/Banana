package com.banana.infrastructure.orm.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "expenses")
public class SExpense {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "account_id")
  private SAccount account;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "budget_id")
  private SBudget budget;
  @Column(name = "description")
  private String description;
  @Column(name = "amount")
  private double amount;
  @Column(name = "expense_date")
  private Date expenseDate;
  @Column(name = "debit_date")
  private Date debitDate;
  @Column(name = "creation_date")
  private Date creationDate;
  @Column(name = "update_date")
  private Date updateDate;
  @Column(name = "is_deleted")
  private boolean isDeleted;

  public SExpense(String description, double amount, Date expenseDate) {
    this.description = description;
    this.amount = amount;
    this.expenseDate = expenseDate;
    this.isDeleted = false;
  }

  public long getId() { return this.id; }

  public void setId(long id) { this.id = id; }

  public void setAccount(SAccount account) { this.account = account; }

  public void setBudget(SBudget budget) { this.budget = budget; }

  public String getDescription() { return this.description; }

  public double getAmount() { return this.amount; }

  public Date getExpenseDate() { return this.expenseDate; }

  public Date getDebitDate() { return this.debitDate; }

  public void setDebitDate(Date debitDate) { this.debitDate = debitDate; }
}
