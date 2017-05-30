// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import App from "./App";
import router from "./router";
import Vuetify from "vuetify";
import VueCookie from "vue-cookie";

Vue.config.productionTip = false;
Vue.use(Vuetify);
Vue.use(VueCookie);

const routesForUsersOnly = ['/courses', '/lessonCard', '/user', '/course', '/user/change-personal-info'];

// redirect to login page (if path not available for guests)
router.beforeEach((to, from, next) => {
  const accessKey = Vue.cookie.get('accessKey');
  routesForUsersOnly.includes(to.path) ? accessKey ? next() : next('/login') : next();
});

new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: {App}
})
