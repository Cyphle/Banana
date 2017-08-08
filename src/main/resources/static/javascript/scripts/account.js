'use strict';

import AjaxBuilder from './utils/AjaxBuilder';

$(document).ready(() => {
  let ajaxBuilder = new AjaxBuilder();

  let currentURLParts = window.location.href.split('?');
  let currentSlug = currentURLParts[0].split('/')[currentURLParts[0].split('/').length - 1];
  console.log(currentSlug);
  ajaxBuilder.send('http://localhost:8080/api/accounts/' + currentSlug, 'GET');
})