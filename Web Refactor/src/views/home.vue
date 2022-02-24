<template>
  <div>
    <el-container>
      <el-header height="55px">
        <v-header></v-header>
      </el-header>
      <el-main>
        <v-head-section></v-head-section>
        <v-top-article></v-top-article>
      </el-main>
      <el-footer>Footer</el-footer>
    </el-container>
  </div>
</template>
<script>
import VHeader from "@/components/home/header.vue";
import VTopArticle from "@/components/home/topArticle.vue";
import VHeadSection from "@/components/home/headSection.vue";
export default {
  components: {
    VHeader,
    VHeadSection,
    VTopArticle
  },
  setup() {},
};
</script>

<style lang="less" scoped>
.common-layout .el-header,
.common-layout .el-footer {
  
  background-color: #b3c0d1;
  color: var(--el-text-color-primary);
  text-align: center;
  //   line-height: 60px;
}
.common-layout .el-footer {
  line-height: 60px;
}
.el-main {
  --el-main-padding: 0;
}
.common-layout .el-main {
  background-color: #e9eef3;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 160px;

  //   :deep(--el-main-padding) {
  //     padding: 0;
  //   }
}
</style>