import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import axios from 'axios';
import store from './store';

axios.defaults.baseURL = "http://10.0.4.3:8080/"

axios.interceptors.request.use(function (config) {
    const token = store.state.user.token;

    if (!config) {
        config = {};
    }
    if (!config.headers) {
        config.headers = {};
    }

    if (token !== null && token !== "" && token !== undefined) {
        config.headers.token = token
    }

    return config
})

const app = createApp(App)

app.use(router)
app.use(Antd);

app.mount('#app')
