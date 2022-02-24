import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import store from './store/index'
import components from './components/index'

import '@/assets/fonts/font.css';
import 'element-plus/dist/index.css'

const app = createApp(App);
app.use(ElementPlus)
app.use(store)
app.use(router)
app.use(components)
app.mount('#app')
