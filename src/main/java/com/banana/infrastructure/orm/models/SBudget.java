package com.banana.infrastructure.orm.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "budgets")
public class SBudget {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @ManyToOne(fetch = FetchType.EAGER)
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
    this.isDeleted = false;
  }

  public long getId() { return this.id; }

  public void setId(long id) { this.id = id; }

  public SAccount getAccount() { return this.account; }

  public void setAccount(SAccount account) { this.account = account; }

  public String getName() { return this.name; }

  public double getInitialAmount() { return this.initialAmount; }

  public Date getStartDate() { return this.startDate; }

  public void setStartDate(Date startDate) { this.startDate = startDate; }

  public Date getEndDate() { return this.endDate; }

  public void setEndDate(Date endDate) { this.endDate = endDate; }

  public Date getCreationDate() { return this.creationDate; }

  public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

  public Date getUpdateDate() { return this.updateDate; }

  public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }

  public boolean isDelete() { return this.isDeleted; }

  public void setDeleted(boolean deleted) { this.isDeleted = deleted; }
}
