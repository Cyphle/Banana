package com.banana.infrastructure.orm.models;

import javax.persistence.*;
import java.util.Date;

public class SBudget {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  private SAccount account;
  @Column(name = "name")
  private String name;
  @Column(name = "initial_amount")
  private double initialAmount;
  @Column(name = "start_date")
  private Date startDate;
  @Column(name = "end_date")
  private Date endDate;
  @Column(name = "creation_date")
  private Date creationDate;
  @Column(name = "update_date")
  private Date updateDate;
  @Column(name = "is_deleted")
  private boolean isDeleted;

  public SBudget(String name, double initialAmount, Date startDate) {
    this.name = name;
    this.initialAmount = initialAmount;
    this.startDate = startDate;
  }

  public long getId() { return this.id; }

  public void setId(long id) { this.id = id; }

  public SAccount getAccount() { return this.account; }

  public void setAccount(SAccount account) { this.account = account; }

  public String getName() { return this.name; }

  public double getInitialAmount() { return this.initialAmount; }

  public Date getStartDate() { return this.startDate; }
}
