import { createApp } from 'vue'
import vue3GoogleLogin from 'vue3-google-login';
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

import 'normalize.css'

import VueSweetalert2 from 'vue-sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(VueSweetalert2);
app.use(vue3GoogleLogin, {
    clientId: import.meta.env.CLIENT_ID,
    scope: 'profile email',
    prompt: 'select_account',
});

app.mount('#app')
