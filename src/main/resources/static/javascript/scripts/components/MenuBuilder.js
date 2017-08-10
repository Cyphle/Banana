'use strict';

export default class MenuBuilder {
  buildMenuLinks(accountId) {
    let menuLinks = this.buildBudgetCreateLink(accountId);
    menuLinks += this.buildChargeCreateLink(accountId);
    menuLinks += this.buildExpenseCreateLink(accountId);
    menuLinks += this.buildCreditCreateLink(accountId);
    return menuLinks;
  }

  buildBudgetCreateLink(accountId) {
    return '<li><a href="/budgets/create/' + accountId + '" class="waves-effect">Créer un budget</a></li>';
  }

  buildChargeCreateLink(accountId) {
    return '<li><a href="/charges/create/' + accountId + '" class="waves-effect">Ajouter une charge</a></li>';
  }

  buildExpenseCreateLink(accountId) {
    return '<li><a href="/expenses/create/' + accountId + '" class="waves-effect">Ajouter une dépense</a></li>';
  }

  buildCreditCreateLink(accountId) {
    return '<li><a href="/credits/create/' + accountId + '" class="waves-effect">Ajouter un crédit</a></li>';
  }
}