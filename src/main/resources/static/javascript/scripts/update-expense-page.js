'use strict';

import AjaxBuilder from './utils/AjaxBuilder';
import MenuBuilder from './components/MenuBuilder';

import {HOST} from './config/config';

$(document).ready(() => {
  let currentURLParts = window.location.href.split('?');
  currentURLParts = currentURLParts[0].split('/');
  let isBudgetExpense = currentURLParts.filter(part => part.toLowerCase() === 'budgets');
  let currentId = -1;
  if (isBudgetExpense.length > 0)
    currentId = currentURLParts[currentURLParts.length - 3];
  else
    currentId = currentURLParts[currentURLParts.length - 2];

  let ajaxBuilder = new AjaxBuilder();
  let menuBuilder = new MenuBuilder();

  ajaxBuilder
      .send(HOST + 'api/accounts/?id=' + currentId, 'GET')
      .then(accountData => {
        let sideMenu = $('.side-nav');
        sideMenu.append(menuBuilder.buildMenuLinks(accountData.id, accountData.slug));
      });
});
