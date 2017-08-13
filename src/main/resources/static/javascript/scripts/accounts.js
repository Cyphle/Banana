'use strict';

import AjaxBuilder from './utils/AjaxBuilder';
import AccountPage from './components/AccountPage';
import MenuBuilder from './components/MenuBuilder';
import AccountPageBuilder from './components/AccountPageBuilder';

import { HOST } from './config/config';

$(document).ready(() => {
  let ajaxBuilder = new AjaxBuilder();

  $('.button-delete').click(e => {
    ajaxBuilder
        .send(HOST + '/api/accounts/delete/' + e.currentTarget.getAttribute('data-id'), 'GET')
        .then(accountData => window.location = HOST + 'accounts');
  });
});
