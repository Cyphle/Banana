var gulp = require('gulp');
var babel = require('gulp-babel');
var uglify = require('gulp-uglify');
var webpack = require('webpack-stream');
var sass = require('gulp-sass');
var cleanCSS = require('gulp-clean-css');
var rename = require('gulp-rename');
var uglify = require('gulp-uglifyjs');

gulp.task('webpack', function () {
  return gulp.src('javascript/scripts/**/*.js')
      .pipe(webpack(require('./webpack.config.js')))
      .pipe(gulp.dest('javascript/build/'));
});

gulp.task('sass', function () {
  return gulp.src('./sass/**/*.scss')
      .pipe(sass().on('error', sass.logError))
      .pipe(gulp.dest('./css/scripts'));
});

gulp.task('minify-css', function () {
  return gulp.src('css/scripts/pages/**.css')
      .pipe(cleanCSS({compatibility: 'ie8'}))
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('css/dist'));
});

gulp.task('uglify-layout', function () {
  gulp.src('javascript/build/layout.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('javascript/dist'))
});

gulp.task('uglify-homepage', function () {
  gulp.src('javascript/build/homepage.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('javascript/dist'))
});

gulp.task('uglify-account', function () {
  gulp.src('javascript/build/account.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('javascript/dist'))
});

gulp.task('uglify-accounts', function () {
  gulp.src('javascript/build/accounts.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('javascript/dist'))
});

gulp.task('uglify-create-page', function () {
  gulp.src('javascript/build/create-page.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('javascript/dist'))
});

gulp.task('uglify-create-expense-page', function () {
  gulp.src('javascript/build/create-expense-page.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('javascript/dist'))
});

gulp.task('uglify-update-page', function () {
  gulp.src('javascript/build/update-page.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('javascript/dist'))
});

gulp.task('uglify-update-expense-page', function () {
  gulp.src('javascript/build/update-expense-page.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('javascript/dist'))
});

gulp.task('watch', [
  'webpack',
  'uglify-homepage',
  'uglify-layout',
  'uglify-account',
  'uglify-accounts',
  'uglify-create-page',
  'uglify-create-expense-page',
  'uglify-update-page',
  'uglify-update-expense-page',
  'sass',
  'minify-css'
], function () {
  gulp.watch('src/scripts/**/*.js', ['webpack']);
  gulp.watch('src/build/*.js', ['uglify-homepage']);
  gulp.watch('src/build/*.js', ['uglify-layout']);
  gulp.watch('src/build/*.js', ['uglify-account']);
  gulp.watch('src/build/*.js', ['uglify-accounts']);
  gulp.watch('src/build/*.js', ['uglify-create-page']);
  gulp.watch('src/build/*.js', ['uglify-create-expense-page']);
  gulp.watch('src/build/*.js', ['uglify-update-page']);
  gulp.watch('src/build/*.js', ['uglify-update-expense-page']);
  gulp.watch('./sass/**/*.scss', ['sass']);
  gulp.watch('css/scripts/build/**.css', ['minify-css']);
});