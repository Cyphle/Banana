'use strict';

export default class AjaxBuilder {
  send(path, method, data) {
    return new Promise((resolve, reject) => {
      $.ajax({
        url: path,
        type: method,
        data: data,
        success: response => {
          resolve(response);
        }
      });
    });
  }
}