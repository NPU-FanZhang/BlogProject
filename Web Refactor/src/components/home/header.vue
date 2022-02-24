<template>
  <div class="header">
    <el-row>
      <el-col :span="8">
        <el-link class="title" :underline="false" @click="toHome"
          >F4N Blog</el-link
        >
      </el-col>
      <el-col :span="8">
        <div class="search">
          <el-input
            v-model="searchContext"
            class="w-50 m-2"
            size="large"
            clearable
            placeholder="Search.."
            :prefix-icon="Search"
          ></el-input>
          <!-- <img
            class="search-picture"
            src="../../assets/icon/Search.png"
            style="margin: 0 auto"
          /> -->
        </div>
      </el-col>
      <el-col :span="8" style="display: flex">
        <el-link class="community" :underline="false" @click="toCommunity"
          >Community</el-link
        >
        <el-link class="article-list" :underline="false" @click="toArticle"
          >Article</el-link
        >
        <el-link class="personCenter" :underline="false" @click="login"
          >Login</el-link
        >

        <!-- <div class="community">Community</div>
        <div class="article-list">Article</div> -->
        <!-- <div class="personCenter" >Login</div> -->
      </el-col>
    </el-row>
    <!-- <div class="title">F4N Blog</div>
    <div class="search">Search</div>
    <div class="community">Community</div>
    <div class="articleList">Article</div>
    <div class="personCenter">Home</div> -->
  </div>
</template>
<script>
import { useRouter } from "vue-router";
// import router from "../../router/index";

import { ref } from "vue";
import { Search } from "@element-plus/icons-vue";
export default {
  setup() {
    const searchContext = ref("");
    const router = useRouter();
    function login() {
      router.push("/login");
    }
    function toHome() {
      router.push("/");
    }

    return { searchContext, Search, login, toHome };
  },
};
</script>
<style lang="less" scoped>
.header {
  // height: 45px;
  line-height: 55px;
  //   display: flex;
  //   justify-content: center;
}
.title {
  margin: 0 50px;
  font-size: 2rem;
  font-weight: bolder;
}
.search {
  //line-height: 5vh;
  margin: 0 50px;
  // font-size: 1.5rem;
  // color: #718096;
  display: table-cell;
  // vertical-align: middle;
  :deep(.el-input__inner) {
    border: transparent;
    font-size: 1.2rem;
    padding-left: 50px !important;
    // border-radius: 23px;
    // height: 45px;
  }
  :deep(.el-input__prefix) {
    font-size: 1.2rem;
    color: black;
  }
}
// ::v-deep .el-input__inner {
//   border-radius: 23px;
//   height: 45px;
// }

.community {
  margin: 0 10px;
  font-size: 1.3rem;
  // font-weight: bolder;
}
.article-list {
  margin: 0 10px;
  font-size: 1.3rem;
  // font-weight: bolder;
}

.personCenter {
  margin: 0 100px;
  font-size: 1.3rem;
  // font-weight: bolder;
}
.bg-purple {
  background: #d3dce6;
}
</style>