<template>
  <div class="headSection">
    <div class="left-info">
      <div class="slogan1">Make Better Note
          <img style="height:60px;color:#ff7810;" src="../../assets/icon/Edit.png" />
      </div>
      <div class="slogan2">More important to share information</div>
    </div>

    <img class="head-picture" src="../../assets/img/croods.png" />
  </div>
</template>
<script>
export default {
  setup() {},
};
</script>
<style lang="less" scoped>
.headSection {
  height: 500px;
  display: flex;
  background-color: #fbfbfb;
}
.left-info {
  width: 40%;
}
.head-picture {
  margin: auto auto;
  //width: 50%;
  height: 380px;
}
.slogan1 {
  font-size: 3rem;
  margin-top: 200px;
  margin-bottom: 30px;
  //   line-height: 380px;
  font-weight: bold;
}
.slogan2 {
  font-size: 2rem;
  //   margin: auto auto;
  //   line-height: 380px;
//   text-align: left;
  color: #685e4a;
}
</style>
