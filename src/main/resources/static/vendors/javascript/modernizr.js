/*! modernizr 3.3.1 (Custom Build) | MIT *
 * https://modernizr.com/download/?-backgroundblendmode-backgroundcliptext-backgroundsize-bgpositionshorthand-bgpositionxy-bgrepeatspace_bgrepeatround-bgsizecover-borderimage-borderradius-boxshadow-boxsizing-canvas-canvastext-checked-createelementattrs_createelement_attrs-cssall-cssanimations-csscalc-csschunit-csscolumns-cssfilters-csspositionsticky-csstransforms-csstransitions-ellipsis-es5array-es5date-es5function-es5object-es6array-eventlistener-eventsource-fileinput-flexbox-fontface-input-inputtypes-json-multiplebgs-nthchild-opacity-overflowscrolling-strictmode-svg-textalignlast-video-websockets-setclasses !*/
!function(e,t,n){function r(e,t){return typeof e===t}function o(){var e,t,n,o,s,a,i;for(var d in b)if(b.hasOwnProperty(d)){if(e=[],t=b[d],t.name&&(e.push(t.name.toLowerCase()),t.options&&t.options.aliases&&t.options.aliases.length))for(n=0;n<t.options.aliases.length;n++)e.push(t.options.aliases[n].toLowerCase());for(o=r(t.fn,"function")?t.fn():t.fn,s=0;s<e.length;s++)a=e[s],i=a.split("."),1===i.length?Modernizr[i[0]]=o:(!Modernizr[i[0]]||Modernizr[i[0]]instanceof Boolean||(Modernizr[i[0]]=new Boolean(Modernizr[i[0]])),Modernizr[i[0]][i[1]]=o),h.push((o?"":"no-")+i.join("-"))}}function s(e){var t=S.className,n=Modernizr._config.classPrefix||"";if(k&&(t=t.baseVal),Modernizr._config.enableJSClass){var r=new RegExp("(^|\\s)"+n+"no-js(\\s|$)");t=t.replace(r,"$1"+n+"js$2")}Modernizr._config.enableClasses&&(t+=" "+n+e.join(" "+n),k?S.className.baseVal=t:S.className=t)}function a(){return"function"!=typeof t.createElement?t.createElement(arguments[0]):k?t.createElementNS.call(t,"http://www.w3.org/2000/svg",arguments[0]):t.createElement.apply(t,arguments)}function i(e){return e.replace(/([a-z])-([a-z])/g,function(e,t,n){return t+n.toUpperCase()}).replace(/^-/,"")}function d(){var e=t.body;return e||(e=a(k?"svg":"body"),e.fake=!0),e}function c(e,n,r,o){var s,i,c,l,u="modernizr",p=a("div"),f=d();if(parseInt(r,10))for(;r--;)c=a("div"),c.id=o?o[r]:u+(r+1),p.appendChild(c);return s=a("style"),s.type="text/css",s.id="s"+u,(f.fake?f:p).appendChild(s),f.appendChild(p),s.styleSheet?s.styleSheet.cssText=e:s.appendChild(t.createTextNode(e)),p.id=u,f.fake&&(f.style.background="",f.style.overflow="hidden",l=S.style.overflow,S.style.overflow="hidden",S.appendChild(f)),i=n(p,e),f.fake?(f.parentNode.removeChild(f),S.style.overflow=l,S.offsetHeight):p.parentNode.removeChild(p),!!i}function l(e,t){return!!~(""+e).indexOf(t)}function u(e,t){return function(){return e.apply(t,arguments)}}function p(e,t,n){var o;for(var s in e)if(e[s]in t)return n===!1?e[s]:(o=t[e[s]],r(o,"function")?u(o,n||t):o);return!1}function f(e){return e.replace(/([A-Z])/g,function(e,t){return"-"+t.toLowerCase()}).replace(/^ms-/,"-ms-")}function y(t,r){var o=t.length;if("CSS"in e&&"supports"in e.CSS){for(;o--;)if(e.CSS.supports(f(t[o]),r))return!0;return!1}if("CSSSupportsRule"in e){for(var s=[];o--;)s.push("("+f(t[o])+":"+r+")");return s=s.join(" or "),c("@supports ("+s+") { #modernizr { position: absolute; } }",function(e){return"absolute"==getComputedStyle(e,null).position})}return n}function m(e,t,o,s){function d(){u&&(delete M.style,delete M.modElem)}if(s=r(s,"undefined")?!1:s,!r(o,"undefined")){var c=y(e,o);if(!r(c,"undefined"))return c}for(var u,p,f,m,g,v=["modernizr","tspan","samp"];!M.style&&v.length;)u=!0,M.modElem=a(v.shift()),M.style=M.modElem.style;for(f=e.length,p=0;f>p;p++)if(m=e[p],g=M.style[m],l(m,"-")&&(m=i(m)),M.style[m]!==n){if(s||r(o,"undefined"))return d(),"pfx"==t?m:!0;try{M.style[m]=o}catch(h){}if(M.style[m]!=g)return d(),"pfx"==t?m:!0}return d(),!1}function g(e,t,n,o,s){var a=e.charAt(0).toUpperCase()+e.slice(1),i=(e+" "+$.join(a+" ")+a).split(" ");return r(t,"string")||r(t,"undefined")?m(i,t,o,s):(i=(e+" "+I.join(a+" ")+a).split(" "),p(i,t,n))}function v(e,t,r){return g(e,n,n,t,r)}var h=[],b=[],x={_version:"3.3.1",_config:{classPrefix:"",enableClasses:!0,enableJSClass:!0,usePrefixes:!0},_q:[],on:function(e,t){var n=this;setTimeout(function(){t(n[e])},0)},addTest:function(e,t,n){b.push({name:e,fn:t,options:n})},addAsyncTest:function(e){b.push({name:null,fn:e})}},Modernizr=function(){};Modernizr.prototype=x,Modernizr=new Modernizr,Modernizr.addTest("eventlistener","addEventListener"in e),Modernizr.addTest("json","JSON"in e&&"parse"in JSON&&"stringify"in JSON),Modernizr.addTest("svg",!!t.createElementNS&&!!t.createElementNS("http://www.w3.org/2000/svg","svg").createSVGRect);var T=!1;try{T="WebSocket"in e&&2===e.WebSocket.CLOSING}catch(w){}Modernizr.addTest("websockets",T),Modernizr.addTest("es5array",function(){return!!(Array.prototype&&Array.prototype.every&&Array.prototype.filter&&Array.prototype.forEach&&Array.prototype.indexOf&&Array.prototype.lastIndexOf&&Array.prototype.map&&Array.prototype.some&&Array.prototype.reduce&&Array.prototype.reduceRight&&Array.isArray)}),Modernizr.addTest("es5date",function(){var e="2013-04-12T06:06:37.307Z",t=!1;try{t=!!Date.parse(e)}catch(n){}return!!(Date.now&&Date.prototype&&Date.prototype.toISOString&&Date.prototype.toJSON&&t)}),Modernizr.addTest("es5function",function(){return!(!Function.prototype||!Function.prototype.bind)}),Modernizr.addTest("es5object",function(){return!!(Object.keys&&Object.create&&Object.getPrototypeOf&&Object.getOwnPropertyNames&&Object.isSealed&&Object.isFrozen&&Object.isExtensible&&Object.getOwnPropertyDescriptor&&Object.defineProperty&&Object.defineProperties&&Object.seal&&Object.freeze&&Object.preventExtensions)}),Modernizr.addTest("strictmode",function(){"use strict";return!this}()),Modernizr.addTest("es6array",!!(Array.prototype&&Array.prototype.copyWithin&&Array.prototype.fill&&Array.prototype.find&&Array.prototype.findIndex&&Array.prototype.keys&&Array.prototype.entries&&Array.prototype.values&&Array.from&&Array.of)),Modernizr.addTest("eventsource","EventSource"in e);var S=t.documentElement;Modernizr.addTest("cssall","all"in S.style);var k="svg"===S.nodeName.toLowerCase();Modernizr.addTest("canvas",function(){var e=a("canvas");return!(!e.getContext||!e.getContext("2d"))}),Modernizr.addTest("canvastext",function(){return Modernizr.canvas===!1?!1:"function"==typeof a("canvas").getContext("2d").fillText}),Modernizr.addTest("video",function(){var e=a("video"),t=!1;try{(t=!!e.canPlayType)&&(t=new Boolean(t),t.ogg=e.canPlayType('video/ogg; codecs="theora"').replace(/^no$/,""),t.h264=e.canPlayType('video/mp4; codecs="avc1.42E01E"').replace(/^no$/,""),t.webm=e.canPlayType('video/webm; codecs="vp8, vorbis"').replace(/^no$/,""),t.vp9=e.canPlayType('video/webm; codecs="vp9"').replace(/^no$/,""),t.hls=e.canPlayType('application/x-mpegURL; codecs="avc1.42E01E"').replace(/^no$/,""))}catch(n){}return t}),Modernizr.addTest("bgpositionshorthand",function(){var e=a("a"),t=e.style,n="right 10px bottom 10px";return t.cssText="background-position: "+n+";",t.backgroundPosition===n}),Modernizr.addTest("multiplebgs",function(){var e=a("a").style;return e.cssText="background:url(https://),url(https://),red url(https://)",/(url\s*\(.*?){3}/.test(e.background)}),Modernizr.addTest("createelementattrs",function(){try{return"test"==a('<input name="test" />').getAttribute("name")}catch(e){return!1}},{aliases:["createelement-attrs"]}),Modernizr.addTest("fileinput",function(){if(navigator.userAgent.match(/(Android (1.0|1.1|1.5|1.6|2.0|2.1))|(Windows Phone (OS 7|8.0))|(XBLWP)|(ZuneWP)|(w(eb)?OSBrowser)|(webOS)|(Kindle\/(1.0|2.0|2.5|3.0))/))return!1;var e=a("input");return e.type="file",!e.disabled});var C=a("input"),A="autocomplete autofocus list placeholder max min multiple pattern required step".split(" "),O={};Modernizr.input=function(t){for(var n=0,r=t.length;r>n;n++)O[t[n]]=!!(t[n]in C);return O.list&&(O.list=!(!a("datalist")||!e.HTMLDataListElement)),O}(A);var P="search tel url email datetime date month week time datetime-local number range color".split(" "),E={};Modernizr.inputtypes=function(e){for(var r,o,s,a=e.length,i="1)",d=0;a>d;d++)C.setAttribute("type",r=e[d]),s="text"!==C.type&&"style"in C,s&&(C.value=i,C.style.cssText="position:absolute;visibility:hidden;",/^range$/.test(r)&&C.style.WebkitAppearance!==n?(S.appendChild(C),o=t.defaultView,s=o.getComputedStyle&&"textfield"!==o.getComputedStyle(C,null).WebkitAppearance&&0!==C.offsetHeight,S.removeChild(C)):/^(search|tel)$/.test(r)||(s=/^(url|email)$/.test(r)?C.checkValidity&&C.checkValidity()===!1:C.value!=i)),E[e[d]]=!!s;return E}(P);var j=x._config.usePrefixes?" -webkit- -moz- -o- -ms- ".split(" "):["",""];x._prefixes=j,Modernizr.addTest("csscalc",function(){var e="width:",t="calc(10px);",n=a("a");return n.style.cssText=e+j.join(t+e),!!n.style.length}),Modernizr.addTest("opacity",function(){var e=a("a").style;return e.cssText=j.join("opacity:.55;"),/^0.55$/.test(e.opacity)}),Modernizr.addTest("csspositionsticky",function(){var e="position:",t="sticky",n=a("a"),r=n.style;return r.cssText=e+j.join(t+";"+e).slice(0,-e.length),-1!==r.position.indexOf(t)});var z={elem:a("modernizr")};Modernizr._q.push(function(){delete z.elem}),Modernizr.addTest("csschunit",function(){var e,t=z.elem.style;try{t.fontSize="3ch",e=-1!==t.fontSize.indexOf("ch")}catch(n){e=!1}return e});var _="CSS"in e&&"supports"in e.CSS,R="supportsCSS"in e;Modernizr.addTest("supports",_||R);var N=x.testStyles=c;Modernizr.addTest("checked",function(){return N("#modernizr {position:absolute} #modernizr input {margin-left:10px} #modernizr :checked {margin-left:20px;display:block}",function(e){var t=a("input");return t.setAttribute("type","checkbox"),t.setAttribute("checked","checked"),e.appendChild(t),20===t.offsetLeft})});var L=function(){var e=navigator.userAgent,t=e.match(/applewebkit\/([0-9]+)/gi)&&parseFloat(RegExp.$1),n=e.match(/w(eb)?osbrowser/gi),r=e.match(/windows phone/gi)&&e.match(/iemobile\/([0-9])+/gi)&&parseFloat(RegExp.$1)>=9,o=533>t&&e.match(/android/gi);return n||o||r}();L?Modernizr.addTest("fontface",!1):N('@font-face {font-family:"font";src:url("https://")}',function(e,n){var r=t.getElementById("smodernizr"),o=r.sheet||r.styleSheet,s=o?o.cssRules&&o.cssRules[0]?o.cssRules[0].cssText:o.cssText||"":"",a=/src/i.test(s)&&0===s.indexOf(n.split(" ")[0]);Modernizr.addTest("fontface",a)}),N("#modernizr div {width:1px} #modernizr div:nth-child(2n) {width:2px;}",function(e){for(var t=e.getElementsByTagName("div"),n=!0,r=0;5>r;r++)n=n&&t[r].offsetWidth===r%2+1;Modernizr.addTest("nthchild",n)},5);var B="Moz O ms Webkit",$=x._config.usePrefixes?B.split(" "):[];x._cssomPrefixes=$;var W=function(t){var r,o=j.length,s=e.CSSRule;if("undefined"==typeof s)return n;if(!t)return!1;if(t=t.replace(/^@/,""),r=t.replace(/-/g,"_").toUpperCase()+"_RULE",r in s)return"@"+t;for(var a=0;o>a;a++){var i=j[a],d=i.toUpperCase()+"_"+r;if(d in s)return"@-"+i.toLowerCase()+"-"+t}return!1};x.atRule=W;var I=x._config.usePrefixes?B.toLowerCase().split(" "):[];x._domPrefixes=I;var M={style:z.elem.style};Modernizr._q.unshift(function(){delete M.style}),x.testAllProps=g,x.testAllProps=v,Modernizr.addTest("cssanimations",v("animationName","a",!0)),Modernizr.addTest("backgroundcliptext",function(){return v("backgroundClip","text")}),Modernizr.addTest("bgpositionxy",function(){return v("backgroundPositionX","3px",!0)&&v("backgroundPositionY","5px",!0)}),Modernizr.addTest("bgrepeatround",v("backgroundRepeat","round")),Modernizr.addTest("bgrepeatspace",v("backgroundRepeat","space")),Modernizr.addTest("backgroundsize",v("backgroundSize","100%",!0)),Modernizr.addTest("bgsizecover",v("backgroundSize","cover")),Modernizr.addTest("borderimage",v("borderImage","url() 1",!0)),Modernizr.addTest("borderradius",v("borderRadius","0px",!0)),Modernizr.addTest("boxshadow",v("boxShadow","1px 1px",!0)),Modernizr.addTest("boxsizing",v("boxSizing","border-box",!0)&&(t.documentMode===n||t.documentMode>7)),function(){Modernizr.addTest("csscolumns",function(){var e=!1,t=v("columnCount");try{(e=!!t)&&(e=new Boolean(e))}catch(n){}return e});for(var e,t,n=["Width","Span","Fill","Gap","Rule","RuleColor","RuleStyle","RuleWidth","BreakBefore","BreakAfter","BreakInside"],r=0;r<n.length;r++)e=n[r].toLowerCase(),t=v("column"+n[r]),("breakbefore"===e||"breakafter"===e||"breakinside"==e)&&(t=t||v(n[r])),Modernizr.addTest("csscolumns."+e,t)}(),Modernizr.addTest("ellipsis",v("textOverflow","ellipsis")),Modernizr.addTest("cssfilters",function(){if(Modernizr.supports)return v("filter","blur(2px)");var e=a("a");return e.style.cssText=j.join("filter:blur(2px); "),!!e.style.length&&(t.documentMode===n||t.documentMode>9)}),Modernizr.addTest("flexbox",v("flexBasis","1px",!0)),Modernizr.addTest("overflowscrolling",v("overflowScrolling","touch",!0)),Modernizr.addTest("textalignlast",v("textAlignLast")),Modernizr.addTest("csstransforms",function(){return-1===navigator.userAgent.indexOf("Android 2.")&&v("transform","scale(1)",!0)}),Modernizr.addTest("csstransitions",v("transition","all",!0));var D=x.prefixed=function(e,t,n){return 0===e.indexOf("@")?W(e):(-1!=e.indexOf("-")&&(e=i(e)),t?g(e,t,n):g(e,"pfx"))};Modernizr.addTest("backgroundblendmode",D("backgroundBlendMode","text")),o(),s(h),delete x.addTest,delete x.addAsyncTest;for(var V=0;V<Modernizr._q.length;V++)Modernizr._q[V]();e.Modernizr=Modernizr}(window,document);