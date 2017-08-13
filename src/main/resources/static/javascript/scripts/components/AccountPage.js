'use strict';

export default class AccountPage {
  constructor(ajaxBuilder, menuBuilder, accountPageBuilder) {
    this.ajaxBuilder = ajaxBuilder;
    this.menuBuilder = menuBuilder;
    this.accountPageBuilder = accountPageBuilder;
    this.accountURL = '';
  }

  getAccountDataFromServer(accountURL) {
    this.accountURL = accountURL;
    this.ajaxBuilder
        .send(accountURL, 'GET')
        .then(accountData => this.dispatchDataOnView(accountData));
  }

  dispatchDataOnView(accountData) {
    this.populateMenu(accountData.id, accountData.slug);
    this.accountPageBuilder.setAccountData(accountData);
    this.accountPageBuilder.buildPage();
    this.initListeners();
  }

  populateMenu(accountId, accountSlug) {
    let sideMenu = $('.side-nav');
    sideMenu.append(this.menuBuilder.buildMenuLinks(accountId, accountSlug));
  }

  initListeners() {
    this.showPart('button-allaccount');
    $('#next-month').click(e => this.onClickNextMonth());
    $('#previous-month').click(e => this.onClickPreviousMonth());
    $('#button-allaccount').click(e => this.showPart(e.currentTarget.getAttribute('id')));
    $('#button-budgets').click(e => this.showPart(e.currentTarget.getAttribute('id')));
    $('#button-charges').click(e => this.showPart(e.currentTarget.getAttribute('id')));
    $('#button-expenses').click(e => this.showPart(e.currentTarget.getAttribute('id')));
    $('#button-credits').click(e => this.showPart(e.currentTarget.getAttribute('id')));
    $('.delete-button').click(e => this.deleteElement(e.currentTarget.getAttribute('data-link')));
  }

  onClickNextMonth() {
    this.accountPageBuilder.changeToNextMonth();
  }

  onClickPreviousMonth() {
    this.accountPageBuilder.changeToPreviousMonth();
  }

  hideParts() {
    $('.allaccount-content').hide();
    $('.budgets-content').hide();
    $('.charges-content').hide();
    $('.expenses-content').hide();
    $('.credits-content').hide();
  }

  showPart(element) {
    let id = element.split('-')[1];
    this.hideParts();
    $('.' + id + '-content').show();
  }

  deleteElement(deletePath) {
    this.ajaxBuilder
        .send(deletePath, 'GET')
        .then(accountData => this.getAccountDataFromServer(this.accountURL));
  }
}
