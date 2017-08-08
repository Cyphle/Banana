'use strict';

export default class AjaxBuilder {
  send(path, method, data) {
    $.ajax({
      url: path,
      type: method,
      data: data,
      success: response => {
        console.log(response)
      }
    });
  }
}