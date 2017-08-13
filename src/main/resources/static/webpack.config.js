var path = require('path');
var webpack = require('webpack');

module.exports = {
  entry: {
    layout: './javascript/scripts/layout.js',
    homepage: './javascript/scripts/homepage.js',
    account: './javascript/scripts/account.js',
    accounts: './javascript/scripts/accounts.js',
    'create-page': './javascript/scripts/create-page.js',
    'create-expense-page': './javascript/scripts/create-expense-page.js',
    'update-page': './javascript/scripts/update-page.js',
    'update-expense-page': './javascript/scripts/update-expense-page.js'
  },
  output: {
    path: path.resolve(__dirname, 'javascript/build'),
    filename: '[name].bundle.js'
  },
  module: {
    loaders: [
      {
        test: /\.js$/,
        loader: 'babel-loader',
        query: {
          presets: ['es2015']
        }
      }
    ]
  },
  stats: {
    colors: true
  },
  devtool: 'source-map'
};