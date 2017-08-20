'use strict';

import AjaxBuilder from './utils/AjaxBuilder';

import {CONFIG, CURRENT_ENVIRONMENT} from './config/config';

$(document).ready(() => {
  let ajaxBuilder = new AjaxBuilder();

  $('.button-delete').click(e => {
    ajaxBuilder
        .send(CONFIG[CURRENT_ENVIRONMENT].host + '/api/accounts/delete/' + e.currentTarget.getAttribute('data-id'), 'GET')
        .then(accountData => window.location = CONFIG[CURRENT_ENVIRONMENT].host + 'accounts');
  });
});
