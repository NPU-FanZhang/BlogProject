<template>
  <div>
    <div class="title">F4N BlOG</div>
    <div class="login">Sign Up</div>
    <div class="form-login">
      <el-form
        :model="formData"
        label-position="top"
        status-icon
        :rules="rules"
        ref="ruleRef"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item prop="email" label="Email">
          <el-input v-model="formData.email"></el-input>
        </el-form-item>
        <el-form-item label="Password" prop="pass">
          <el-input
            type="password"
            v-model="formData.pass"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="Re-enter Password" prop="checkPass">
          <el-input
            type="password"
            v-model="formData.checkPass"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            class="login-button"
            type="danger"
            round
            size="large"
            @click="submit"
          >
            注册
            <el-icon class="el-icon--right"><ArrowRight /></el-icon>
          </el-button>
          <el-button
            class="back-button"
            type="warning"
            round
            plain
            size="large"
            @click="turnback"
          >
            返回
            <!-- <el-icon class="el-icon--right"><ArrowRight /></el-icon> -->
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
import { ref, reactive } from "vue";
import { login } from "@/api/register.js";
import {
  // ArrowLeft,
  // Edit,
  // Share,
  // Delete,
  ArrowRight,
} from "@element-plus/icons-vue";
// import  { ElForm } from "element-plus";
import { validateVEmail, validateVPassword } from "@/utils/check";
import { useRouter } from "vue-router";
export default {
  components: {
    ArrowRight,
  },
  setup(props, ctx) {
    const router = useRouter();
    //邮箱验证
    let validateEmail = (rule, value, callback) => {
      if (value == "") {
        callback(new Error("邮箱不能为空！"));
      } else if (validateVEmail(value)) {
        callback(new Error("邮箱格式有误！"));
      } else {
        callback();
      }
    };

    // 密码验证
    let validatePassword = (rule, value, callback) => {
      //console.log(strpscript(value));
      if (value == "") callback(new Error("密码不能为空！"));
      else if (validateVPassword(value)) {
        callback(new Error("密码格式6-15位，只能包含大小写字母和数字！"));
      } else callback();
    };

    // 注册再次密码验证
    let revalidatePassword = (rule, value, callback) => {
      console.log(value, formData.pass);
      if (value == "") callback(new Error("密码不能为空！"));
      else if (value != formData.pass) {
        callback(new Error("两次密码输入不一致！"));
      } else callback();
    };

    const formData = reactive({
      pass: "",
      checkPass: "",
      email: "",
    });

    const rules = reactive({
      email: [{ validator: validateEmail, trigger: "blur" }],
      pass: [{ validator: validatePassword, trigger: "blur" }],
      checkPass: [{ validator: revalidatePassword, trigger: "blur" }],
    });

    var ruleRef = ref(null);
    // 提交按钮
    function submit() {
      console.log("submit", ruleRef);
      ruleRef.value.validate((valid) => {
        if (valid) {
          login(formData).then((res) => {
            if (res.data.success) {
              console.log(res);
            }
          });
        }
      });
    }
    function SignUp() {
      ctx.emit("isSignIn", false);
      console.log("zhuce");
    }
    function turnback() {
      router.go("-1");
    }
    return {
      formData,
      rules,
      ruleRef,
      submit,
      SignUp,
      turnback,
    };
  },
};
</script>
<style lang="less" scoped>
.title {
  color: #f47458;
  font-weight: bold;
  text-align: left;
  font-size: 2.5rem;
}

.login {
  font-family: Poppins;
  font-size: 2.5rem;
  font-weight: bold;
  text-align: left;
}
.form-login {
  margin-top: 15px;
}
.login-button {
  --el-button-bg-color: #f47458 !important;
  --el-button-border-color: #f47458 !important;
  --el-button-hover-bg-color: #d66c55 !important;
  --el-button-hover-border-color: #d66c55 !important;
  --el-button-active-bg-color: #f47458 !important;
  --el-button-active-border-color: #f47458 !important;

  position: relative;
  left: 10%;
  // background-color: #f47458;
  width: 120px;
  // border-radius: 30px;
  font-size: 1.2rem;
  // color: #fff;
  // display: flex;
  // text-align: center;
  padding-left: 35px;
  // justify-content: center;
  // line-height: 50px;
  // height: 50px;
}
.back-button {
  position: relative;
  left: 20%;
  // background-color: #f47458;
  width: 120px;
  // border-radius: 30px;
  font-size: 1.2rem;
  text-align: center;
  // padding-left: 35px;
}
</style>