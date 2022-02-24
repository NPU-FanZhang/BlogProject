// 这里声明全局直接调用的组件，也可以在页面中按需引入
import home from './home/home.vue'

const components = {
    install: function(Vue) {
        // 注册并获取组件，然后在main.js中引用，在Vue.use()就可以了
        Vue.component('v-home', home)
    }
}
export default components