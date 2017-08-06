var path = require('path');
var webpack = require('webpack');

module.exports = {
    entry: {
        layout: './javascript/scripts/layout.js',
        homepage: './javascript/scripts/homepage.js',
        // 'home-main': './src/scripts/home-main.js',
        // 'home-second': './src/scripts/home-second.js',
        // 'medias-main': './src/scripts/medias-main.js',
        // 'projects-main': './src/scripts/projects-main.js',
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