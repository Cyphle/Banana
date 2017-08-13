'use strict';

import AccountDataSorter from "./AccountDataSorter";
import {Instant} from "../utils/Instant";

export default class AccountPageBuilder {
  constructor() {
    this.currentMonth = new Instant();
    this.accountDataSorter = new AccountDataSorter();
  }

  setAccountData(data) {
    this.accountDataSorter.buildAccountData(data);
  }

  buildPage() {
    let currentMonthData = this.accountDataSorter.getDataOfMonth(this.currentMonth);
    this.buildAccountInfosPart(currentMonthData);
    this.buildGlobalPart(currentMonthData);
    this.buildBudgetsPart(currentMonthData);
    this.buildChargesPart(currentMonthData);
    this.buildExpensesPart(currentMonthData);
    this.buildCreditsPart(currentMonthData);
  }

  buildAccountInfosPart(currentMonthData) {
    $('h1').html('Compte : ' + currentMonthData.accountName + ' au ' + this.currentMonth.toString());
    $('#account-initial-amount').html(currentMonthData.startAmount + '€');
    $('#account-free-amount').html(currentMonthData.freeAmount + '€');
    $('#account-current-amount').html(currentMonthData.currentAmount + '€');
  }

  buildGlobalPart(currentMonthData) {
    let formattedDataForTableAll = [];
    currentMonthData.budgets.forEach(budget => {
      budget.expenses.forEach(expense => formattedDataForTableAll.push({
        updateLink: '/budgets/expenses/update/' + currentMonthData.accountId + '/' + budget.id + '/' + expense.id,
        deleteLink: '/api/budgets/expenses/delete/' + currentMonthData.accountId + '/' + budget.id + '/' + expense.id,
        firstDate: expense.expenseDate.toShortString(),
        secondDate: expense.debitDate !== null ? expense.debitDate.toShortString() : '',
        type: 'Budget',
        description: expense.description,
        amount: -expense.amount
      }));
    });
    currentMonthData.charges.forEach(charge => formattedDataForTableAll.push({
      updateLink: '/charges/update/' + currentMonthData.accountId + '/' + charge.id,
      deleteLink: '/api/charges/delete/' + currentMonthData.accountId + '/' + charge.id,
      firstDate: (charge.startDate.getDayNumber() < 10 ? '0' + charge.startDate.getDayNumber() : charge.startDate.getDayNumber()) + '/' + (this.currentMonth.getMonthNumber() < 10 ? '0' + this.currentMonth.getMonthNumber() : this.currentMonth.getMonthNumber()) + '/' + this.currentMonth.getYear(),
      secondDate: charge.endDate !== null ? charge.endDate.toShortString() : '',
      type: 'Charge',
      description: charge.description,
      amount: -charge.amount
    }));
    currentMonthData.expenses.forEach(expense => formattedDataForTableAll.push({
      updateLink: '/expenses/update/' + currentMonthData.accountId + '/' + expense.id,
      deleteLink: '/api/expenses/delete/' + currentMonthData.accountId + '/' + expense.id,
      firstDate: expense.expenseDate.toShortString(),
      secondDate: expense.debitDate !== null ? expense.debitDate.toShortString() : '',
      type: 'Dépense',
      description: expense.description,
      amount: -expense.amount
    }));
    currentMonthData.credits.forEach(credit => formattedDataForTableAll.push({
      updateLink: '/credits/update/' + currentMonthData.accountId + '/' + credit.id,
      deleteLink: '/api/credits/delete/' + currentMonthData.accountId + '/' + credit.id,
      firstDate: credit.creditDate.toShortString(),
      secondDate: credit.creditDate.toShortString(),
      type: 'Crédit',
      description: credit.description,
      amount: credit.amount
    }));
    let allAccountTableBody = $('#all-account-table tbody');
    allAccountTableBody.empty();
    formattedDataForTableAll.forEach(data => {
      allAccountTableBody.append(
        `<tr>
           <td>${data.firstDate}</td>
           <td>${data.secondDate}</td>
           <td>${data.type}</td>
           <td>${data.description}</td>
           <td>${data.amount}€</td>
           <td><a href="${data.updateLink}"><i class="material-icons">mode_edit</i></a> <button class="delete-button btn-flat" data-link="${data.deleteLink}"><i class="material-icons">delete</i></button></td>
         </tr>`
      );
    });
    $('#all-account-table').tablesorter();
  }

  buildBudgetsPart(currentMonthData) {
    let budgetsContainer = $('#budgets-container');
    budgetsContainer.empty();
    currentMonthData.budgets.forEach(budget => {
      let budgetExpensesAmount = budget.expenses
          .map(expense => expense.amount)
          .reduce((a, b) => a + b, 0);

      budgetsContainer.append(`
        <div id="budget-wrapper-${budget.id}" class="budget-wrapper">
          <h2>Nom : ${budget.name}</h2>
          <div class="budget-amounts container">
            <div class="row">
              <div class="col s4"><b>Montant initial :</b> ${budget.initialAmount}€</div>
              <div class="col s4"><b>Montant actuel :</b> ${budgetExpensesAmount}€</div>
              <div class="col s4">
                <a href="/budgets/expenses/create/${currentMonthData.accountId}/${budget.id}" class="btn-floating btn-large waves-effect waves-light red"><i class="material-icons">add</i></a> 
                <a href="/budgets/update/${currentMonthData.accountId}/${budget.id}"><i class="material-icons">mode_edit</i></a> 
                <button class="delete-button btn-flat" data-link="/api/budgets/delete/${currentMonthData.accountId}/${budget.id}"><i class="material-icons">delete</i></button>
              </div>
            </div>
          </div>
          <table id="budget-table-${budget.id}" class="striped centered tablesorter">
            <thead>
              <tr>
                <th>Date dépense</th>
                <th>Date débit</th>
                <th>Description</th>
                <th>Montant</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
      `);

      let budgetTableBody = $('#budget-table-' + budget.id + ' tbody');
      budget.expenses.forEach(expense => {
        budgetTableBody.append(`
          <tr>
						<td>${expense.expenseDate.toShortString()}</td>
						<td>${expense.debitDate !== null ? expense.debitDate.toShortString() : ''}</td>
						<td>${expense.description}</td>
						<td>${expense.amount}€</td>
						<td><a href="/budgets/expenses/update/${currentMonthData.accountId}/${budget.id}/${expense.id}"><i class="material-icons">mode_edit</i></a> <button class="delete-button btn-flat" data-link="/api/budgets/expenses/delete/${currentMonthData.accountId}/${budget.id}/${expense.id}"><i class="material-icons">delete</i></button></td>
					</tr>`
        );
      });

      $('#budget-table-' + budget.id).tablesorter();
    });
  }

  buildChargesPart(currentMonthData) {
    let chargeTableBody = $('#charge-table tbody');
    chargeTableBody.empty();
    currentMonthData.charges.forEach(charge => {
      chargeTableBody.append(`
        <tr>
					<td>${charge.startDate.getDayNumber() + '/' + (this.currentMonth.getMonthNumber() < 10 ? '0' + this.currentMonth.getMonthNumber() : this.currentMonth.getMonthNumber()) + '/' + this.currentMonth.getYear()}</td>
					<td>${charge.description}</td>
					<td>${charge.amount}€</td>
					<td><a href="/charges/update/${currentMonthData.accountId}/${charge.id}"><i class="material-icons">mode_edit</i></a> <button class="delete-button btn-flat" data-link="/api/charges/delete/${currentMonthData.accountId}/${charge.id}"><i class="material-icons">delete</i></button></td>
				</tr>`
      );
    });
    $('#charge-table').tablesorter();
  }

  buildExpensesPart(currentMonthData) {
    let expenseTableBody = $('#expenses-table tbody');
    expenseTableBody.empty();
    currentMonthData.expenses.forEach(expense => {
      expenseTableBody.append(`
        <tr>
					<td>${expense.expenseDate.toShortString()}</td>
					<td>${expense.debitDate !== null ? expense.debitDate.toShortString() : ''}</td>
					<td>${expense.description}</td>
					<td>${expense.amount}€</td>
					<td><a href="/expenses/update/${currentMonthData.accountId}/${expense.id}"><i class="material-icons">mode_edit</i></a> <button class="delete-button btn-flat" data-link="/api/expenses/delete/${currentMonthData.accountId}/${expense.id}"><i class="material-icons">delete</i></button></td>
				</tr>`
      );
    });
    $('#expenses-table').tablesorter();
  }

  buildCreditsPart(currentMonthData) {
    let creditTableBody = $('#credits-table tbody');
    creditTableBody.empty();
    currentMonthData.credits.forEach(credit => {
      creditTableBody.append(
          `<tr>
						<td>${credit.creditDate.toShortString()}</td>
						<td>${credit.description}</td>
						<td>${credit.amount}€</td>
						<td><a href="/credits/update/${currentMonthData.accountId}/${credit.id}"><i class="material-icons">mode_edit</i></a> <button class="delete-button btn-flat" data-link="/api/credits/delete/${currentMonthData.accountId}/${credit.id}"><i class="material-icons">delete</i></button></td>
					</tr>`
      );
    });
    $('#credits-table').tablesorter();
  }

  changeToNextMonth() {
    this.currentMonth = this.currentMonth.getLastDateOfNextMonth();
    this.buildPage();
  }

  changeToPreviousMonth() {
    this.currentMonth = this.currentMonth.getLastDateOfPreviousMonth();
    this.buildPage();
  }
}
