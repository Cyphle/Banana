'use strict';

import AjaxBuilder from './utils/AjaxBuilder';
import AccountPage from './components/AccountPage';
import MenuBuilder from './components/MenuBuilder';

import { HOST } from './config/config';

$(document).ready(() => {
  let currentURLParts = window.location.href.split('?');
  let currentSlug = currentURLParts[0].split('/')[currentURLParts[0].split('/').length - 1];

  let ajaxBuilder = new AjaxBuilder();
  let menuBuilder = new MenuBuilder();
  let accountPage = new AccountPage(ajaxBuilder, menuBuilder);

  accountPage.getAccountDataFromServer(HOST + 'api/accounts/' + currentSlug);
})