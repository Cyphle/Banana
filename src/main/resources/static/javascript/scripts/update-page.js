'use strict';

import AjaxBuilder from './utils/AjaxBuilder';
import MenuBuilder from './components/MenuBuilder';

import {CONFIG, CURRENT_ENVIRONMENT} from './config/config';

$(document).ready(() => {
  let currentURLParts = window.location.href.split('?');
  let currentId = currentURLParts[0].split('/')[currentURLParts[0].split('/').length - 2];

  let ajaxBuilder = new AjaxBuilder();
  let menuBuilder = new MenuBuilder();

  ajaxBuilder
      .send(CONFIG[CURRENT_ENVIRONMENT].host + 'api/accounts/?id=' + currentId, 'GET')
      .then(accountData => {
        let sideMenu = $('.side-nav');
        sideMenu.append(menuBuilder.buildMenuLinks(accountData.id, accountData.slug));
      });
});
