(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-2c61"],{c11S:function(t,n,e){"use strict";var o=e("gTgX");e.n(o).a},gTgX:function(t,n,e){},ntYl:function(t,n,e){"use strict";e.r(n);var o={name:"Login",data:function(){return{loginForm:{username:"admin",password:"1234"},loginRules:{username:[{required:!0,trigger:"blur",validator:function(t,n,e){!function(t){return["admin","editor"].indexOf(t.trim())>=0}(n)?e(new Error("请输入正确的用户名")):e()}}],password:[{required:!0,trigger:"blur",validator:function(t,n,e){n.length<4?e(new Error("密码不能小于4位")):e()}}]},loading:!1,pwdType:"password",redirect:void 0}},watch:{$route:{handler:function(t){this.redirect=t.query&&t.query.redirect},immediate:!0}},methods:{showPwd:function(){"password"===this.pwdType?this.pwdType="":this.pwdType="password"},handleLogin:function(){var t=this;this.loading=!0,this.$store.dispatch("Login",this.loginForm).then(function(){t.loading=!1,t.$router.push({path:t.redirect||"/"})}).catch(function(){t.loading=!1})}}},r=(e("c11S"),e("uX0t"),e("KHd+")),s=Object(r.a)(o,function(){var t=this,n=t.$createElement,e=t._self._c||n;return e("div",{staticClass:"login-container"},[e("el-form",{ref:"loginForm",staticClass:"login-form",attrs:{model:t.loginForm,rules:t.loginRules,"auto-complete":"on","label-position":"left"}},[e("h3",{staticClass:"title"},[t._v("系统登录")]),t._v(" "),e("el-form-item",{attrs:{prop:"username"}},[e("span",{staticClass:"svg-container"},[e("svg-icon",{attrs:{"icon-class":"user"}})],1),t._v(" "),e("el-input",{attrs:{name:"username",type:"text","auto-complete":"on",placeholder:"username"},model:{value:t.loginForm.username,callback:function(n){t.$set(t.loginForm,"username",n)},expression:"loginForm.username"}})],1),t._v(" "),e("el-form-item",{attrs:{prop:"password"}},[e("span",{staticClass:"svg-container"},[e("svg-icon",{attrs:{"icon-class":"password"}})],1),t._v(" "),e("el-input",{attrs:{type:t.pwdType,name:"password","auto-complete":"on",placeholder:"password"},nativeOn:{keyup:function(n){return"button"in n||!t._k(n.keyCode,"enter",13,n.key,"Enter")?t.handleLogin(n):null}},model:{value:t.loginForm.password,callback:function(n){t.$set(t.loginForm,"password",n)},expression:"loginForm.password"}}),t._v(" "),e("span",{staticClass:"show-pwd",on:{click:t.showPwd}},[e("svg-icon",{attrs:{"icon-class":"eye"}})],1)],1),t._v(" "),e("el-form-item",[e("el-button",{staticStyle:{width:"100%"},attrs:{loading:t.loading,type:"primary"},nativeOn:{click:function(n){return n.preventDefault(),t.handleLogin(n)}}},[t._v("\n        登录\n      ")])],1)],1)],1)},[],!1,null,"e1c3da7a",null);s.options.__file="index.vue";n.default=s.exports},sft9:function(t,n,e){},uX0t:function(t,n,e){"use strict";var o=e("sft9");e.n(o).a}}]);