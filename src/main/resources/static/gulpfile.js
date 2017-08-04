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

gulp.task('uglify-homepage', function () {
  gulp.src('javascript/build/homepage.bundle.js')
      .pipe(uglify())
      .pipe(rename({
        suffix: '.min'
      }))
      .pipe(gulp.dest('javascript/dist'))
});

// gulp.task('uglify-home-main', function () {
//   gulp.src('src/build/home-main.bundle.js')
//       .pipe(uglify())
//       .pipe(rename({
//         suffix: '.min'
//       }))
//       .pipe(gulp.dest('src/dist'))
// });
//
// gulp.task('uglify-home-second', function () {
//   gulp.src('src/build/home-second.bundle.js')
//       .pipe(uglify())
//       .pipe(rename({
//         suffix: '.min'
//       }))
//       .pipe(gulp.dest('src/dist'))
// });
//
// gulp.task('uglify-medias-main', function () {
//   gulp.src('src/build/medias-main.bundle.js')
//       .pipe(uglify())
//       .pipe(rename({
//         suffix: '.min'
//       }))
//       .pipe(gulp.dest('src/dist'))
// });
//
// gulp.task('uglify-projects-main', function () {
//   gulp.src('src/build/projects-main.bundle.js')
//       .pipe(uglify())
//       .pipe(rename({
//         suffix: '.min'
//       }))
//       .pipe(gulp.dest('src/dist'))
// });

gulp.task('watch', ['webpack', 'uglify-homepage', 'sass', 'minify-css'], function () {
  gulp.watch('src/scripts/**/*.js', ['webpack']);
  gulp.watch('src/build/*.js', ['uglify-homepage']);
  gulp.watch('./sass/**/*.scss', ['sass']);
  gulp.watch('css/scripts/build/**.css', ['minify-css']);
});