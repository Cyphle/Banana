var gulp = require('gulp');
var babel = require('gulp-babel');
var uglify = require('gulp-uglify');
var webpack = require('webpack-stream');
var sass = require('gulp-sass');
var cleanCSS = require('gulp-clean-css');
var rename = require('gulp-rename');
var uglify = require('gulp-uglifyjs');

gulp.task('webpack', function () {
  return gulp.src('src/scripts/**/*.js')
      .pipe(webpack(require('./webpack.config.js')))
      .pipe(gulp.dest('src/build/'));
});

gulp.task('sass', function () {
  return gulp.src('./sass/**/*.scss')
      .pipe(sass().on('error', sass.logError))
      .pipe(gulp.dest('./css/scripts'));
});

gulp.task('minify-css', function () {
  return gulp.src('css/scripts/build/**.css')
      .pipe(cleanCSS({compatibility: 'ie8'}))
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('css/dist'));
});

gulp.task('uglify-main', function () {
  gulp.src('src/build/main.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('src/dist'))
});

gulp.task('uglify-home-main', function () {
  gulp.src('src/build/home-main.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('src/dist'))
});

gulp.task('uglify-home-second', function () {
  gulp.src('src/build/home-second.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('src/dist'))
});

gulp.task('uglify-medias-main', function () {
  gulp.src('src/build/medias-main.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('src/dist'))
});

gulp.task('uglify-projects-main', function () {
  gulp.src('src/build/projects-main.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('src/dist'))
});

gulp.task('watch', ['webpack', 'uglify-main', 'uglify-home-main', 'uglify-home-second', 'uglify-medias-main', 'uglify-projects-main', 'sass', 'minify-css'], function () {
  gulp.watch('src/scripts/**/*.js', ['webpack']);
  gulp.watch('src/build/*.js', ['uglify-main']);
  gulp.watch('src/build/*.js', ['uglify-home-main']);
  gulp.watch('src/build/*.js', ['uglify-home-second']);
  gulp.watch('src/build/*.js', ['uglify-medias-main']);
  gulp.watch('src/build/*.js', ['uglify-projects-main']);
  gulp.watch('./sass/**/*.scss', ['sass']);
  gulp.watch('css/scripts/build/**.css', ['minify-css']);
});