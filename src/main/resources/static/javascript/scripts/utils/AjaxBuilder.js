'use strict';

export default class AjaxBuilder {
  send(path, data) {
    $.ajax({
      url: this.host + path,
      type: 'POST',
      data: data,
      success: response => {
        console.log(response)
      }
    });
  }
}