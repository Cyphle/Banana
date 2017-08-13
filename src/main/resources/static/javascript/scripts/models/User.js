'use strict';

export class User {
  constructor(id, lastname, firstname, username) {
    this.id = id;
    this.lastname = lastname;
    this.firstname = firstname;
    this.username = username;
  }

  clone() {
    return new User(this.id, this.lastname, this.firstname, this.username);
  }
}