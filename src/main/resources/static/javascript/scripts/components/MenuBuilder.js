'use strict';
import {CONFIG, CURRENT_ENVIRONMENT} from "../config/config";

export default class MenuBuilder {
  buildMenuLinks(accountId, accountSlug) {
    let menuLinks = this.buildAccountViewLink(accountSlug);
    menuLinks += this.buildBudgetCreateLink(accountId);
    menuLinks += this.buildChargeCreateLink(accountId);
    menuLinks += this.buildExpenseCreateLink(accountId);
    menuLinks += this.buildCreditCreateLink(accountId);
    return menuLinks;
  }

  buildAccountViewLink(accountSlug) {
    return '<li><a href="' + CONFIG[CURRENT_ENVIRONMENT].context + '/accounts/' + accountSlug + '" class="waves-effect">Voir le compte</a></li>';
  }

  buildBudgetCreateLink(accountId) {
    return '<li><a href="' + CONFIG[CURRENT_ENVIRONMENT].context + '/budgets/create/' + accountId + '" class="waves-effect">Ajouter un budget</a></li>';
  }

  buildChargeCreateLink(accountId) {
    return '<li><a href="' + CONFIG[CURRENT_ENVIRONMENT].context + '/charges/create/' + accountId + '" class="waves-effect">Ajouter une charge</a></li>';
  }

  buildExpenseCreateLink(accountId) {
    return '<li><a href="' + CONFIG[CURRENT_ENVIRONMENT].context + '/expenses/create/' + accountId + '" class="waves-effect">Ajouter une dépense</a></li>';
  }

  buildCreditCreateLink(accountId) {
    return '<li><a href="' + CONFIG[CURRENT_ENVIRONMENT].context + '/credits/create/' + accountId + '" class="waves-effect">Ajouter un crédit</a></li>';
  }
}