(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-76e4"],{"+Lc1":function(t,e,r){"use strict";r.d(e,"b",function(){return o}),r.d(e,"d",function(){return i}),r.d(e,"g",function(){return a}),r.d(e,"h",function(){return s}),r.d(e,"c",function(){return c}),r.d(e,"a",function(){return l}),r.d(e,"f",function(){return u}),r.d(e,"e",function(){return f});var n=r("t3Un");function o(t){return Object(n.a)({url:"/order/modelNumber",method:"post",data:t})}function i(t){return Object(n.a)({url:"/order/getMandatoryResult",method:"post",data:t})}function a(t){return Object(n.a)({url:"/order/add",method:"post",data:t})}function s(t){return Object(n.a)({url:"/order/update",method:"post",data:t})}function c(t){return Object(n.a)({url:"/order/list",method:"post",data:t})}function l(t){return Object(n.a)({url:"/order/"+t,method:"delete"})}function u(t){return Object(n.a)({url:"/order/orderDetail/"+t,method:"get"})}function f(t){return Object(n.a)({url:"/order/"+t,method:"get"})}},"14Xm":function(t,e,r){t.exports=r("u938")},"6EGO":function(t,e,r){"use strict";var n=r("TvSj");r.n(n).a},D3Ub:function(t,e,r){"use strict";e.__esModule=!0;var n=function(t){return t&&t.__esModule?t:{default:t}}(r("4d7F"));e.default=function(t){return function(){var e=t.apply(this,arguments);return new n.default(function(t,r){return function o(i,a){try{var s=e[i](a),c=s.value}catch(t){return void r(t)}if(!s.done)return n.default.resolve(c).then(function(t){o("next",t)},function(t){o("throw",t)});t(c)}("next")})}}},DkuF:function(t,e,r){},PLwA:function(t,e,r){"use strict";var n={name:"PanThumb",props:{image:{type:String,required:!0},zIndex:{type:Number,default:1},width:{type:String,default:"150px"},height:{type:String,default:"150px"}}},o=(r("qU75"),r("KHd+")),i=Object(o.a)(n,function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"pan-item",style:{zIndex:this.zIndex,height:this.height,width:this.width}},[e("div",{staticClass:"pan-info"},[e("div",{staticClass:"pan-info-roles-container"},[this._t("default")],2)]),this._v(" "),e("img",{staticClass:"pan-thumb",attrs:{src:this.image}})])},[],!1,null,"670009ea",null);i.options.__file="index.vue";e.a=i.exports},TvSj:function(t,e,r){},foiM:function(t,e,r){"use strict";r.r(e);var n=r("14Xm"),o=r.n(n),i=r("D3Ub"),a=r.n(i),s=r("PLwA"),c=r("+Lc1"),l={name:"orderDetail",components:{PanThumb:s.a},data:function(){return{centerDialogVisible:!1,orderDetail:{order:{},components:[]},currentComponentDetail:{}}},mounted:function(){var t=this;return a()(o.a.mark(function e(){var r,n;return o.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return r=t.$route.params&&t.$route.params.orderId,e.next=3,Object(c.f)(r);case 3:n=e.sent,t.orderDetail.components=n.components.sort(function(t,e){return t.componentId-e.componentId}),t.orderDetail.order=n.order,document.title=t.orderDetail.order.productModel;case 7:case"end":return e.stop()}},e,t)}))()},methods:{showDetailDialog:function(t){this.currentComponentDetail=t,this.centerDialogVisible=!0}}},u=(r("6EGO"),r("KHd+")),f=Object(u.a)(l,function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("el-row",{staticClass:"customInlineFrom",staticStyle:{padding:"20px"},attrs:{id:"printJS-form"}},[r("el-col",{staticStyle:{"font-size":"20px"},attrs:{span:22}},[t._v("KYOKUTOH产品选型: "+t._s(t.orderDetail.order.productModel))]),t._v(" "),r("el-form",{staticClass:"demo-form-inline",attrs:{"label-position":"left",inline:!0}},[r("el-col",{staticStyle:{height:"63px"},attrs:{span:8}},[r("el-form-item",{staticStyle:{"font-size":"18px"},attrs:{label:"订单号:"}},[r("span",{staticStyle:{"font-size":"18px"}},[t._v(t._s(t.orderDetail.order.orderNumber))])])],1),t._v(" "),r("el-col",{staticStyle:{height:"63px"},attrs:{span:4}},[r("el-form-item",{staticStyle:{"font-size":"18px"},attrs:{label:"制单人:"}},[r("span",{staticStyle:{"font-size":"18px"}},[t._v(t._s(t.orderDetail.order.creator))])])],1),t._v(" "),r("el-col",{staticStyle:{height:"63px"},attrs:{span:6}},[r("el-form-item",{staticStyle:{"font-size":"18px"},attrs:{label:"时间:"}},[r("span",{staticStyle:{"font-size":"18px"}},[t._v(t._s(t._f("parseTime")(t.orderDetail.order.updateTime,"{y}-{m}-{d} {h}:{i}:{s}")))])])],1),t._v(" "),r("el-col",{staticStyle:{height:"63px"},attrs:{span:6}},[r("el-form-item",{staticStyle:{"font-size":"18px"},attrs:{label:"关联单号:"}},[r("span",{staticStyle:{"font-size":"18px"}},[t._v(t._s(t.orderDetail.order.relateSellId))])])],1),t._v(" "),r("el-col",{staticStyle:{height:"63px"},attrs:{span:8}},[r("el-form-item",{staticStyle:{"font-size":"18px"},attrs:{label:"客户:"}},[r("span",{staticStyle:{"font-size":"18px"}},[t._v(t._s(t.orderDetail.order.customer))])])],1),t._v(" "),r("el-col",{staticStyle:{height:"63px"},attrs:{span:4}},[r("el-form-item",{staticStyle:{"font-size":"18px"},attrs:{label:"最终用户:"}},[r("span",{staticStyle:{"font-size":"18px"}},[t._v(t._s(t.orderDetail.order.endUser))])])],1),t._v(" "),r("el-col",{staticStyle:{height:"63px"},attrs:{span:6}},[r("el-form-item",{staticStyle:{"font-size":"18px"},attrs:{label:"安装高度:"}},[r("span",{staticStyle:{"font-size":"18px",height:"18px"}},[t._v(t._s(t.orderDetail.order.mountHeight))])])],1),t._v(" "),r("el-col",{staticStyle:{height:"63px"},attrs:{span:6}},[r("el-form-item",{staticStyle:{"font-size":"18px"},attrs:{label:"架子高度:"}},[r("span",{staticStyle:{"font-size":"18px",height:"18px"}},[t._v(t._s(t.orderDetail.order.shelfHeight))])])],1)],1),t._v(" "),r("div",{staticClass:"custom-card",staticStyle:{margin:"0"}},t._l(t.orderDetail.components,function(e){return r("el-col",{key:e.componentId,staticClass:"text-center",attrs:{span:4}},[r("el-card",{staticClass:"box-card"},[r("div",{staticClass:"clearfix",staticStyle:{"text-align":"center","font-size":"14px",padding:"2px"},attrs:{slot:"header"},slot:"header"},[r("el-col",{staticStyle:{"font-size":"20px"},attrs:{span:24}},[t._v(t._s(e.componentName))]),t._v(" "),r("el-col",{staticStyle:{"font-size":"16px","margin-top":"10px"},attrs:{span:24}},[t._v(t._s(e.componentModelNumber))])],1),t._v(" "),r("div",{staticClass:"component-item"},[r("pan-thumb",{attrs:{width:"265px",height:"280px",image:e.componentImage}})],1)])],1)}))],1)},[],!1,null,null,null);f.options.__file="orderDetailPrint.vue";e.default=f.exports},ls82:function(t,e){!function(e){"use strict";var r,n=Object.prototype,o=n.hasOwnProperty,i="function"==typeof Symbol?Symbol:{},a=i.iterator||"@@iterator",s=i.asyncIterator||"@@asyncIterator",c=i.toStringTag||"@@toStringTag",l="object"==typeof t,u=e.regeneratorRuntime;if(u)l&&(t.exports=u);else{(u=e.regeneratorRuntime=l?t.exports:{}).wrap=_;var f="suspendedStart",h="suspendedYield",p="executing",d="completed",m={},y={};y[a]=function(){return this};var v=Object.getPrototypeOf,g=v&&v(v(N([])));g&&g!==n&&o.call(g,a)&&(y=g);var x=L.prototype=b.prototype=Object.create(y);S.prototype=x.constructor=L,L.constructor=S,L[c]=S.displayName="GeneratorFunction",u.isGeneratorFunction=function(t){var e="function"==typeof t&&t.constructor;return!!e&&(e===S||"GeneratorFunction"===(e.displayName||e.name))},u.mark=function(t){return Object.setPrototypeOf?Object.setPrototypeOf(t,L):(t.__proto__=L,c in t||(t[c]="GeneratorFunction")),t.prototype=Object.create(x),t},u.awrap=function(t){return{__await:t}},O(D.prototype),D.prototype[s]=function(){return this},u.AsyncIterator=D,u.async=function(t,e,r,n){var o=new D(_(t,e,r,n));return u.isGeneratorFunction(e)?o:o.next().then(function(t){return t.done?t.value:o.next()})},O(x),x[c]="Generator",x[a]=function(){return this},x.toString=function(){return"[object Generator]"},u.keys=function(t){var e=[];for(var r in t)e.push(r);return e.reverse(),function r(){for(;e.length;){var n=e.pop();if(n in t)return r.value=n,r.done=!1,r}return r.done=!0,r}},u.values=N,k.prototype={constructor:k,reset:function(t){if(this.prev=0,this.next=0,this.sent=this._sent=r,this.done=!1,this.delegate=null,this.method="next",this.arg=r,this.tryEntries.forEach(z),!t)for(var e in this)"t"===e.charAt(0)&&o.call(this,e)&&!isNaN(+e.slice(1))&&(this[e]=r)},stop:function(){this.done=!0;var t=this.tryEntries[0].completion;if("throw"===t.type)throw t.arg;return this.rval},dispatchException:function(t){if(this.done)throw t;var e=this;function n(n,o){return s.type="throw",s.arg=t,e.next=n,o&&(e.method="next",e.arg=r),!!o}for(var i=this.tryEntries.length-1;i>=0;--i){var a=this.tryEntries[i],s=a.completion;if("root"===a.tryLoc)return n("end");if(a.tryLoc<=this.prev){var c=o.call(a,"catchLoc"),l=o.call(a,"finallyLoc");if(c&&l){if(this.prev<a.catchLoc)return n(a.catchLoc,!0);if(this.prev<a.finallyLoc)return n(a.finallyLoc)}else if(c){if(this.prev<a.catchLoc)return n(a.catchLoc,!0)}else{if(!l)throw new Error("try statement without catch or finally");if(this.prev<a.finallyLoc)return n(a.finallyLoc)}}}},abrupt:function(t,e){for(var r=this.tryEntries.length-1;r>=0;--r){var n=this.tryEntries[r];if(n.tryLoc<=this.prev&&o.call(n,"finallyLoc")&&this.prev<n.finallyLoc){var i=n;break}}i&&("break"===t||"continue"===t)&&i.tryLoc<=e&&e<=i.finallyLoc&&(i=null);var a=i?i.completion:{};return a.type=t,a.arg=e,i?(this.method="next",this.next=i.finallyLoc,m):this.complete(a)},complete:function(t,e){if("throw"===t.type)throw t.arg;return"break"===t.type||"continue"===t.type?this.next=t.arg:"return"===t.type?(this.rval=this.arg=t.arg,this.method="return",this.next="end"):"normal"===t.type&&e&&(this.next=e),m},finish:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var r=this.tryEntries[e];if(r.finallyLoc===t)return this.complete(r.completion,r.afterLoc),z(r),m}},catch:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var r=this.tryEntries[e];if(r.tryLoc===t){var n=r.completion;if("throw"===n.type){var o=n.arg;z(r)}return o}}throw new Error("illegal catch attempt")},delegateYield:function(t,e,n){return this.delegate={iterator:N(t),resultName:e,nextLoc:n},"next"===this.method&&(this.arg=r),m}}}function _(t,e,r,n){var o=e&&e.prototype instanceof b?e:b,i=Object.create(o.prototype),a=new k(n||[]);return i._invoke=function(t,e,r){var n=f;return function(o,i){if(n===p)throw new Error("Generator is already running");if(n===d){if("throw"===o)throw i;return C()}for(r.method=o,r.arg=i;;){var a=r.delegate;if(a){var s=E(a,r);if(s){if(s===m)continue;return s}}if("next"===r.method)r.sent=r._sent=r.arg;else if("throw"===r.method){if(n===f)throw n=d,r.arg;r.dispatchException(r.arg)}else"return"===r.method&&r.abrupt("return",r.arg);n=p;var c=w(t,e,r);if("normal"===c.type){if(n=r.done?d:h,c.arg===m)continue;return{value:c.arg,done:r.done}}"throw"===c.type&&(n=d,r.method="throw",r.arg=c.arg)}}}(t,r,a),i}function w(t,e,r){try{return{type:"normal",arg:t.call(e,r)}}catch(t){return{type:"throw",arg:t}}}function b(){}function S(){}function L(){}function O(t){["next","throw","return"].forEach(function(e){t[e]=function(t){return this._invoke(e,t)}})}function D(t){var e;this._invoke=function(r,n){function i(){return new Promise(function(e,i){!function e(r,n,i,a){var s=w(t[r],t,n);if("throw"!==s.type){var c=s.arg,l=c.value;return l&&"object"==typeof l&&o.call(l,"__await")?Promise.resolve(l.__await).then(function(t){e("next",t,i,a)},function(t){e("throw",t,i,a)}):Promise.resolve(l).then(function(t){c.value=t,i(c)},a)}a(s.arg)}(r,n,e,i)})}return e=e?e.then(i,i):i()}}function E(t,e){var n=t.iterator[e.method];if(n===r){if(e.delegate=null,"throw"===e.method){if(t.iterator.return&&(e.method="return",e.arg=r,E(t,e),"throw"===e.method))return m;e.method="throw",e.arg=new TypeError("The iterator does not provide a 'throw' method")}return m}var o=w(n,t.iterator,e.arg);if("throw"===o.type)return e.method="throw",e.arg=o.arg,e.delegate=null,m;var i=o.arg;return i?i.done?(e[t.resultName]=i.value,e.next=t.nextLoc,"return"!==e.method&&(e.method="next",e.arg=r),e.delegate=null,m):i:(e.method="throw",e.arg=new TypeError("iterator result is not an object"),e.delegate=null,m)}function j(t){var e={tryLoc:t[0]};1 in t&&(e.catchLoc=t[1]),2 in t&&(e.finallyLoc=t[2],e.afterLoc=t[3]),this.tryEntries.push(e)}function z(t){var e=t.completion||{};e.type="normal",delete e.arg,t.completion=e}function k(t){this.tryEntries=[{tryLoc:"root"}],t.forEach(j,this),this.reset(!0)}function N(t){if(t){var e=t[a];if(e)return e.call(t);if("function"==typeof t.next)return t;if(!isNaN(t.length)){var n=-1,i=function e(){for(;++n<t.length;)if(o.call(t,n))return e.value=t[n],e.done=!1,e;return e.value=r,e.done=!0,e};return i.next=i}}return{next:C}}function C(){return{value:r,done:!0}}}(function(){return this}()||Function("return this")())},qU75:function(t,e,r){"use strict";var n=r("DkuF");r.n(n).a},u938:function(t,e,r){var n=function(){return this}()||Function("return this")(),o=n.regeneratorRuntime&&Object.getOwnPropertyNames(n).indexOf("regeneratorRuntime")>=0,i=o&&n.regeneratorRuntime;if(n.regeneratorRuntime=void 0,t.exports=r("ls82"),o)n.regeneratorRuntime=i;else try{delete n.regeneratorRuntime}catch(t){n.regeneratorRuntime=void 0}}}]);