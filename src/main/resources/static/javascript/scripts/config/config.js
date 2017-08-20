'use strict';

export const CURRENT_ENVIRONMENT = 'dev';
export const CONFIG = {
  dev: {
    host: 'http://localhost:8080/',
    context: ''
  },
  prod: {
    host: 'https://banana.cyrilphamle.fr/banana-0.0.1/',
    context: '/banana-0.0.1'
  }
};