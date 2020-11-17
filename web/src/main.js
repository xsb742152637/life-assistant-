import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "@babel/polyfill"; // IE 兼容

// 挂载环境变量中的配置信息，然后在js中使用$src 访问。
Vue.prototype.$src = process.env.VUE_APP_PUBLIC_PATH;

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
