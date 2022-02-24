<template>
  <div>
    <v-header></v-header>
    <div class="backgroud">
      <div class="center-card">
        <div class="left">
          <el-card class="box-card">
            <v-login v-if="loginOrLogUp" @isSignIn="isSign"></v-login>
            <v-logup v-else></v-logup>
          </el-card>
        </div>
        <!-- <div class="right"></div> -->
        <img class="right" src="../assets/img/login.png" />
      </div>
    </div>
  </div>
</template>
<script>
import VHeader from "@/components/home/header.vue";
import VLogin from "@/components/register/login.vue";
import VLogup from "@/components/register/logup.vue";
import { onMounted } from "@vue/runtime-core";
import { ref } from "vue";
export default {
  setup() {
    onMounted(() => {
      console.log("register");
    });
    const loginOrLogUp = ref(true);
    function isSign(params) {
      console.log("asd");
      loginOrLogUp.value = params;
    }
    return {
      isSign,
      loginOrLogUp,
    };
  },
  components: {
    VHeader,
    VLogin,
    VLogup,
  },
};
</script>
<style lang="less" scoped>
.backgroud {
  background-color: #f47458;
  height: 98vh;
}
.center-card {
  width: 80vw;
  height: 80vh;
  background-color: #fff;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  border-radius: 30px;
  display: flex;
  .left {
    width: 60%;
    height: 100%;
    .box-card {
      position: relative;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
      height: 70%;
      width: 60%;
    }
  }
  .right {
    // background-color: #FFCEAE;
    width: 60%;
    height: 100%;
    border-top-right-radius: 30px;
    border-bottom-right-radius: 30px;
    height: 100%;
  }
}
</style>