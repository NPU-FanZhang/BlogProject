# F4NBlog Project Web Construction Instructions

>前端使用框架使用Vue3，使用脚手架（Vue-cli）搭建模板项目。

[TOC]

## 1. 项目搭建

### a. 安装步骤

1. 安装 ==Node.js==

2. 修改 Node 国内源

   ```shell
   npm config set registry http://registry.npm.taobao.org/
   ```

3. 安装 Vue 脚手架

   ```shell
   # 安装脚手架
   npm install -g @vue/cli
   # 更新依赖包
   vue upgrade --next
   ```

4. 初始化项目

   ```shell
   vue create [项目名称] #不能有大写字母 其实会生成一个文件夹，是文件夹名，后期可以挪动，所以无所谓
   ```

   

### b. 项目目录结构

下面这个是 ==Vue2== 生成的项目结构，Vue3 的目录比较精简，所以先看一下Vue2 的目录结构比较好。

```bash
├── build/                      # webpack 编译任务配置文件: 开发环境与生产环境
│   └── ...
├── config/                     
│   ├── index.js                # 项目核心配置
│   └── ...
├ ── node_module/               #项目中安装的依赖模块
   ── src/
│   ├── main.js                 # 程序入口文件
│   ├── App.vue                 # 程序入口vue组件
│   ├── components/             # 组件
│   │   └── ...
│   └── assets/                 # 资源文件夹，一般放一些静态资源文件
│       └── ...
├── static/                     # 纯静态资源 (直接拷贝到dist/static/里面)
├── test/
│   └── unit/                   # 单元测试
│   │   ├── specs/              # 测试规范
│   │   ├── index.js            # 测试入口文件
│   │   └── karma.conf.js       # 测试运行配置文件
│   └── e2e/                    # 端到端测试
│   │   ├── specs/              # 测试规范
│   │   ├── custom-assertions/  # 端到端测试自定义断言
│   │   ├── runner.js           # 运行测试的脚本
│   │   └── nightwatch.conf.js  # 运行测试的配置文件
├── .babelrc                    # babel 配置文件
├── .editorconfig               # 编辑配置文件
├── .gitignore                  # 用来过滤一些版本控制的文件，比如node_modules文件夹 
├── index.html                  # index.html 入口模板文件
└── package.json                # 项目文件，记载着一些命令和依赖还有简要的项目描述信息 
└── README.md                   #介绍自己这个项目的，可参照github上star多的项目。
```

## 2. 项目初始化一般设置

### a. 路由配置

1. 安装路由

   ```sh
   npm install vue-router@4
   ```

2. `src目录`下创建 `router` 文件夹并新建 `index.js`，创建路由到组件的映射，生成路由实例。

   ```js
   // 这里是Vue3的用法
   import {createRouter,createWebHistory} from 'vue-router'
   const routers = [
       {
           path:"/",
           component:()=>import("../components/HelloWorld.vue")
       }
   ]
   const router =createRouter({
       history:createWebHistory(),
       routes:routers
   })
   export default router
   ```

3. 在 `main.js` 中进行挂载

   ```js
   import { createApp } from 'vue'
   import App from './App.vue'
   import router from './router'
   
   const app = createApp(App);
   
   app.use(router)
   app.mount('#app')
   ```
   
4. 特别注意，要在 `App.vue` 中要添加 `<router-view></router-view>`，才能实现跳转

### b. 统一请求处理

1. 安装 axios

   ```sh
    npm install axios 
   ```

2. src目录下新建 request 目录，在此目录下新建 index.js，对axios请求进行封装

   ```js
   import axios from "axios";
   const service = axios.create({
       baseURL:"http:localhost:8888",
       timeout:5000
   })
   export default service
   ```

3. 对API接口进行解耦，src 下新建 api 文件夹，对应的一个模块的接口写在一个文件中，比如新建  ==src/api/article.js==，在其中编写与article 有关的接口。

   ```js
   import request from "@/request"
   export function getArticles(params){
       return request({
           method:'post',
           url:'/articles',
           data:params // 这里的params是需要向后端传递的参数
       })
   }
   // 可以同时抛出其他接口，同一类的接口写在一个文件下
   // export function getArticlesById(params){
   //     return request({
   //         method:'post',
   //         url:'/articles',
   //         data:params // 这里的params是需要向后端传递的参数
   //     })
   // }
   ```

4. 在需要调用的组件中，先声明，后传参调用。

   ```js
   import { getArticles } from "@/api/article.js";
   getArticles( innerParams).then((res) => {
       console.log(res);
   });
   ```

   



### c. webpack打包配置文件

`vue.config.js` 是一个可选的配置文件，如果项目的 (和 `package.json` 同级的) 根目录中存在这个文件，那么它会被==自动加载==。你也可以使用 `package.json` 中的 `vue` 字段，但是注意这种写法需要你严格遵照 JSON 的格式来写。

1. 项目根目录中新建 vue.config.js
2. 



### d. 全局注册组件

1. ==src/components==下新建组件文件夹，以页面为单位，同一页面的最好放在一起，编写组件。

   ```vue
   <template>
   <div>123</div>
   </template>
   <script>
   export default ({
       setup() {
       },
   })
   </script>
   <style lang="less" scoped>
   </style>
   ```

2. ==src/components==下新建 index.js 将组件都进行声明。

   ```js
   // 这里声明全局直接调用的组件，也可以在页面中按需引入
   import home from './home/home.vue'
   const components = {
       install: function(Vue) {
           // 注册并获取组件，然后在main.js中引用，在Vue.use()就可以了
           Vue.component('v-home', home)
       }
   }
   export default components
   ```

3. 在 main.js 中进行声明挂载。

   ```js
   import components from './components/index'
   app.use(components)
   ```

   

### e.  Vue 状态管理

1. 安装Vuex，注意Vue3 需要和 VueX4一起使用。

   ```js
   npm install vuex@next --save
   // --legacy-peer-deps 在npm 版本过高时使用
   ```

2. 来创建一个 store。创建过程直截了当——仅需要提供一个初始 state 对象和一些 mutation，新建 `src/store/index.js`

   ```js
   import { createStore } from 'vuex'
   import { register } from '@/api/register'
   
   // Create a new store instance.
   const store = createStore({
       state() {
           return {
               id: '',
               account: '',
               name: '',
               avatar: '',
               token: localStorage.token
           }
       },
       mutations: {
           SET_TOKEN: (state, token) => {
               state.token = token;
           },
       }, actions: {
           register({ commit }, user) {
               return new Promise((resolve, reject) => {
                   // 异步处理
                   // 成功调用 resolve 失败调用 reject
                   register(user.account, user.nickname, user.password).then(res => {
                       if (res.data.success) {
                           commit('SET_TOKEN', res.data.data);
                           localStorage.token = res.data.data
                           resolve()
                       } else {
                           reject(res.data.msg)
                       }
                   }).catch(error => {
                       reject(error)
                   })
               })
           }
       }
   })
   export default store;
   ```

3. 在 `main.js`中引入注册。

   ```js
   import store from './store/index'
   app.use(store)
   ```

   







## Tips

### 1. Vue3项目配置@路径别名

Vue项目，发现没有build目录了，里面的webpack相关的也没了，都在==vue.config.js==里来配置了配置路径别名，方便引用，不用写那么长。

```js
let path = require('path')
function resolve(dir) {
    return path.join(__dirname, dir)
}
module.exports = {
    chainWebpack: config => {
        config.resolve.alias
            .set('@', resolve('src'))
        //    .set('components',resolve('./src/components'))
        //set第一个参数：设置的别名，第二个参数：设置的路径
    }
}
```



### 2. Vue3引入less

如果安装 `npm install less-loader` 出错，这是因为 `less-loader` 的版本太高的原因，需要安装7.x的版本

```sh
npm install -D less-loader@7.x
```

### 3. Less 修改 ElementUI 固有样式

使用 `:deep([样式名])` 的方法来修改

```les
:deep(.el-input__inner) {
  border: transparent;
  // border-radius: 23px;
  // height: 45px;
}
```

### 4. CSS div水平垂直居中

```css
{
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
}
```

### 5. Vue3 引入自定义字体

1. `src/assets/fonts/`下放入下载的 `.tff` 字体文件，并创建 `font.css` 写入

   ```css
   @font-face {
       font-family: 'Mono-Bold';/*自定义的字体名*/
       src: url('JetBrainsMono-Bold.ttf'); /*文件名*/
       font-weight: normal;
       font-style: normal;
   }
   ```

2. 在 `main.js` 中引入。

   ```js
   import '@/assets/fonts/font.css';
   ```



### 6. Vue3父子组件传值

#### a. 子组件向父组件传值

子组件：

```js
setup(props,ctx) {
    function SignUp() {
        ctx.emit("isSignIn",false);// 触发父组件isSignIn函数
        console.log("zhuce");
    }
}
```

父组件：

```vue
<v-login v-if="loginOrLogUp" @isSignIn="isSign"></v-login>
```

```js
setup(){
    function isSign(params) {
        console.log("asd");
        loginOrLogUp.value = params;
    }
}
```



