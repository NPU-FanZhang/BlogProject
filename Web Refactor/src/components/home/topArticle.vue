<template>
  <div>
    <el-card class="top-article" shadow="hover">
      <div class="content">left</div>
      <div class="top-img">right</div>
    </el-card>
  </div>
</template>
<script>
export default {};
</script>
<style lang="less" scoped>
.top-article {
  width: 90%;
  margin: 20px auto;
  height: 400px;
  :deep(.el-card__body) {
    display: flex !important;
  }
}
.content {
  width: 60%;
  height: inherit;
}
.top-img {
  height: 80%;
}
</style>