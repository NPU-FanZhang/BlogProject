<template>
  <router-view></router-view>
  <!-- <v-home></v-home> -->
  <!-- <img alt="Vue logo" src="./assets/logo.png" />
  <v-home></v-home>
  <HelloWorld msg="Welcome to Your Vue.js App" /> -->
</template>

<script>
// import HelloWorld from "./components/HelloWorld.vue";
// import VHome from "@/views/home.vue";
import { getArticles } from "@/api/article.js";
import { onMounted } from "@vue/runtime-core";
import { getCurrentInstance } from "vue";

export default {
  name: "App",
  components: {
    // HelloWorld,
    // VHome
  },
  setup(props, context) {
    console.log("setUp", props, context);
    const instance = getCurrentInstance();
    const _this = instance.appContext.config.globalProperties;
    onMounted(() => {
      console.log("onMounted", _this);
      const innerParams = {
        page: 1,
        pageSize: 5,
      };
      getArticles(innerParams).then((res) => {
        console.log(res);
      });
    });
  },
  data() {
    return {
      wang: "asd",
    };
  },
};
</script>

<style>
#app {
  font-family:Poppins, Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 10px;
}
.el-link.el-link--default:hover{
  color: #ff7810 !important;;
}
</style>
