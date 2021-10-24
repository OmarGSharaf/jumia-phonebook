import Vue from 'vue'
import axios from '@/plugins/axios.js';
import vuetify from '@/plugins/vuetify';
import App from './App.vue'
import '@mdi/font/css/materialdesignicons.css'

Vue.config.productionTip = false;

Vue.prototype.axios = axios;

new Vue({
    vuetify,
    axios,
    render: h => h(App),
}).$mount('#app')
