var testsContext = require.context('./src/__tests__', true, /-test\.js$/);
testsContext.keys().forEach(testsContext);

var srcContext = require.context('./src/__tests__', true, /^((?!__tests__).)*.js$/);
srcContext.keys().forEach(srcContext);
