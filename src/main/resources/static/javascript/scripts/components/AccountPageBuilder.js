'use strict';

import AccountDataSorter from "./AccountDataSorter";
import {Instant} from "../utils/Instant";
import {CONFIG, CURRENT_ENVIRONMENT} from "../config/config";

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
        updateLink: CONFIG[CURRENT_ENVIRONMENT].context + '/budgets/expenses/update/' + currentMonthData.accountId + '/' + budget.id + '/' + expense.id,
        deleteLink: CONFIG[CURRENT_ENVIRONMENT].context + '/api/budgets/expenses/' + currentMonthData.accountId + '/' + budget.id + '/' + expense.id,
        firstDate: expense.expenseDate.toShortString(),
        secondDate: expense.debitDate !== null ? expense.debitDate.toShortString() : '',
        type: 'Budget',
        description: expense.description,
        amount: -expense.amount
      }));
    });
    currentMonthData.charges.forEach(charge => formattedDataForTableAll.push({
      updateLink: CONFIG[CURRENT_ENVIRONMENT].context + '/charges/update/' + currentMonthData.accountId + '/' + charge.id,
      deleteLink: CONFIG[CURRENT_ENVIRONMENT].context + '/api/charges/' + currentMonthData.accountId + '/' + charge.id,
      firstDate: (charge.startDate.getDayNumber() < 10 ? '0' + charge.startDate.getDayNumber() : charge.startDate.getDayNumber()) + '/' + (this.currentMonth.getMonthNumber() < 10 ? '0' + this.currentMonth.getMonthNumber() : this.currentMonth.getMonthNumber()) + '/' + this.currentMonth.getYear(),
      secondDate: charge.endDate !== null ? charge.endDate.toShortString() : '',
      type: 'Charge',
      description: charge.description,
      amount: -charge.amount
    }));
    currentMonthData.expenses.forEach(expense => formattedDataForTableAll.push({
      updateLink: CONFIG[CURRENT_ENVIRONMENT].context + '/expenses/update/' + currentMonthData.accountId + '/' + expense.id,
      deleteLink: CONFIG[CURRENT_ENVIRONMENT].context + '/api/expenses/' + currentMonthData.accountId + '/' + expense.id,
      firstDate: expense.expenseDate.toShortString(),
      secondDate: expense.debitDate !== null ? expense.debitDate.toShortString() : '',
      type: 'Dépense',
      description: expense.description,
      amount: -expense.amount
    }));
    currentMonthData.credits.forEach(credit => formattedDataForTableAll.push({
      updateLink: CONFIG[CURRENT_ENVIRONMENT].context + '/credits/update/' + currentMonthData.accountId + '/' + credit.id,
      deleteLink: CONFIG[CURRENT_ENVIRONMENT].context + '/api/credits/' + currentMonthData.accountId + '/' + credit.id,
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
           <td>
             <div class="container">
              <div class="row valign-wrapper">
                <div class="col s5"> </div>
                <div class="col s1"><a href="${data.updateLink}"><i class="material-icons">mode_edit</i></a></div>
                <div class="col s1"><button class="delete-button btn-flat" data-link="${data.deleteLink}"><i class="material-icons">delete</i></button></div>
                <div class="col s5"> </div>
              </div>
             </div>
           </td>
         </tr>`
      );
    });
    $('#all-account-table').tablesorter();

    let allAccountTableFoot = $('#all-account-table tfoot');
    allAccountTableFoot.empty();
    let totalTransactions = formattedDataForTableAll.map(transaction => transaction.amount).reduce((a, b) => a + b, 0);
    allAccountTableFoot.append(`<tr><td></td><td></td><td></td><td>Total</td><td>${totalTransactions}€</td><td></td></tr>`);
  }

  buildBudgetsPart(currentMonthData) {
    let budgetsContainer = $('#budgets-wrapper');
    budgetsContainer.empty();
    currentMonthData.budgets.forEach(budget => {
      let budgetExpensesAmount = budget.expenses
          .map(expense => expense.amount)
          .reduce((a, b) => a + b, 0);

      budgetsContainer.append(`
        <div id="budget-wrapper-${budget.id}" class="budget-wrapper">
          <h3>
            <div class="container">
              <div class="row valign-wrapper">
                <div class="col s8">
                  Nom : ${budget.name}
                </div>
                <div class="col s1">
                    <a href="${CONFIG[CURRENT_ENVIRONMENT].context}/budgets/expenses/create/${currentMonthData.accountId}/${budget.id}" class="btn-floating btn-large waves-teal btn-flat waves-light"><i class="material-icons">add</i></a>
                </div>
                <div class="col s1">
                  <a href="${CONFIG[CURRENT_ENVIRONMENT].context}/budgets/update/${currentMonthData.accountId}/${budget.id}"><i class="material-icons">mode_edit</i></a>
                </div>
                <div class="col s1">
                  <button class="delete-button btn-flat" data-link="${CONFIG[CURRENT_ENVIRONMENT].context}/api/budgets/${currentMonthData.accountId}/${budget.id}"><i class="material-icons">delete</i></button>
                </div>
                <div class="col s1"> </div>
              </div>
             </div>
          </h3>
          <div class="budget-amounts container">
            <div class="row">
              <div class="col s12 l6"><b>Montant initial :</b> ${budget.initialAmount}€</div>
              <div class="col s12 l6"><b>Montant actuel :</b> ${budget.initialAmount - budgetExpensesAmount}€</div>
            </div>
          </div>
          <table id="budget-table-${budget.id}" class="striped centered tablesorter responsive-table">
            <thead>
              <tr>
                <th>Date dépense</th>
                <th>Date débit</th>
                <th>Description</th>
                <th>Montant</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tfoot></tfoot>
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
						<td>
              <div class="container">
                <div class="row valign-wrapper">
                  <div class="col s5"> </div>
                  <div class="col s1"><a href="${CONFIG[CURRENT_ENVIRONMENT].context}/budgets/expenses/update/${currentMonthData.accountId}/${budget.id}/${expense.id}"><i class="material-icons">mode_edit</i></a></div>
                  <div class="col s1"><button class="delete-button btn-flat" data-link="${CONFIG[CURRENT_ENVIRONMENT].context}/api/budgets/expenses/${currentMonthData.accountId}/${budget.id}/${expense.id}"><i class="material-icons">delete</i></button></div>
                  <div class="col s5"> </div>
                </div>
              </div>
						</td>
					</tr>`
        );
      });

      $('#budget-table-' + budget.id).tablesorter();

      let budgetTableFoot = $('#budget-table-' + budget.id + ' tfoot');
      budgetTableFoot.empty();
      budgetTableFoot.append(`<tr><td></td><td></td><td>Total</td><td>${budgetExpensesAmount}€</td><td></td></tr>`);
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
					<td>
					  <div class="container">
              <div class="row valign-wrapper">
                <div class="col s5"> </div>
                <div class="col s1"><a href="${CONFIG[CURRENT_ENVIRONMENT].context}/charges/update/${currentMonthData.accountId}/${charge.id}"><i class="material-icons">mode_edit</i></a></div>
                <div class="col s1"><button class="delete-button btn-flat" data-link="${CONFIG[CURRENT_ENVIRONMENT].context}/api/charges/${currentMonthData.accountId}/${charge.id}"><i class="material-icons">delete</i></button></div>
                <div class="col s5"> </div>
              </div>
            </div>
					</td>
				</tr>`
      );
    });
    $('#charge-table').tablesorter();

    let chargeTableFoot = $('#charge-table tfoot');
    chargeTableFoot.empty();
    let totalCharges = currentMonthData.charges.map(charge => charge.amount).reduce((a, b) => a + b, 0);
    chargeTableFoot.append(`<tr><td></td><td>Total</td><td>${totalCharges}€</td><td></td></tr>`);
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
					<td>
					  <div class="container">
              <div class="row valign-wrapper">
                <div class="col s5"> </div>
                <div class="col s1"><a href="${CONFIG[CURRENT_ENVIRONMENT].context}/expenses/update/${currentMonthData.accountId}/${expense.id}"><i class="material-icons">mode_edit</i></a></div>
                <div class="col s1"><button class="delete-button btn-flat" data-link="${CONFIG[CURRENT_ENVIRONMENT].context}/api/expenses/${currentMonthData.accountId}/${expense.id}"><i class="material-icons">delete</i></button></div>
                <div class="col s5"> </div>
              </div>
            </div>
					</td>
				</tr>`
      );
    });
    $('#expenses-table').tablesorter();

    let expenseTableFoot = $('#expenses-table tfoot');
    expenseTableFoot.empty();
    let totalExpenses = currentMonthData.expenses.map(expense => expense.amount).reduce((a, b) => a + b, 0);
    expenseTableFoot.append(`<tr><td></td><td></td><td>Total</td><td>${totalExpenses}€</td><td></td></tr>`);
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
						<td>
              <div class="container">
                <div class="row valign-wrapper">
                  <div class="col s5"> </div>
                  <div class="col s1"><a href="${CONFIG[CURRENT_ENVIRONMENT].context}/credits/update/${currentMonthData.accountId}/${credit.id}"><i class="material-icons">mode_edit</i></a></div>
                  <div class="col s1"><button class="delete-button btn-flat" data-link="${CONFIG[CURRENT_ENVIRONMENT].context}/api/credits/${currentMonthData.accountId}/${credit.id}"><i class="material-icons">delete</i></button></div>
                  <div class="col s5"> </div>
                </div>
              </div>
            </td>
					</tr>`
      );
    });
    $('#credits-table').tablesorter();

    let creditTableFoot = $('#credits-table tfoot');
    creditTableFoot.empty();
    let totalCredits = currentMonthData.credits.map(credit => credit.amount).reduce((a, b) => a + b, 0);
    creditTableFoot.append(`<tr><td></td><td>Total</td><td>${totalCredits}€</td><td></td></tr>`);
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
