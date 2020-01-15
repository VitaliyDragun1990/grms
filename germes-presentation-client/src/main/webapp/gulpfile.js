var gulp = require('gulp');
var concat = require('gulp-concat');
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');

var jsFile = 'js/*.js',
    jsDest = 'dist/scripts';

gulp.task('scripts', function() {
    return gulp.src(jsFile)
                    .pipe(concat('app.js'))
                    .pipe(gulp.dest(jsDest))
                    .pipe(rename('app.min.js'))
                    .pipe(uglify())
                    .pipe(gulp.dest(jsDest));
});