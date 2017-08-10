'use strict';

export default class AccountPage {
  constructor(ajaxBuilder, menuBuilder) {
    this.ajaxBuilder = ajaxBuilder;
    this.menuBuilder = menuBuilder;
    this.accountData = {};
  }

  getAccountDataFromServer(accountURL) {
    this.ajaxBuilder
        .send(accountURL, 'GET')
        .then(accountData => this.dispatchDataOnView(accountData));
  }

  dispatchDataOnView(accountData) {
    this.accountData = accountData;
    this.populateMenu(accountData.id);
  }

  populateMenu(accountId) {
    let sideMenu = $('.side-nav');
    sideMenu.append(this.menuBuilder.buildMenuLinks(accountId));
  }
}