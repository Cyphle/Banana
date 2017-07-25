package com.banana.infrastructure.orm.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "charges")
public class SCharge {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "account_id")
  private SAccount account;
  @Column(name = "description")
  public String description;
  @Column(name = "amount")
  public double amount;
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

  public SCharge(String description, double amount, Date startDate) {
    this.description = description;
    this.amount = amount;
    this.startDate = startDate;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public SAccount getAccount() {
    return account;
  }

  public void setAccount(SAccount account) {
    this.account = account;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean deleted) {
    isDeleted = deleted;
  }
}
