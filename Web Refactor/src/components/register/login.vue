<template>
  <div>
    <div class="title">F4N BlOG</div>
    <div class="slogan">Wellcome login!!!</div>
    <div class="login">Log In</div>
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
        <el-form-item
          prop="email"
          label="Email"
        >
          <el-input v-model="formData.email"></el-input>
        </el-form-item>
        <el-form-item label="Password" prop="pass">
          <el-input
            type="password"
            v-model="formData.pass"
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
            登录
            <el-icon class="el-icon--right"><ArrowRight /></el-icon>
          </el-button>
          <!-- <div class="login-button" @click="submit">
            <span>登录</span>
            <img
              style="width: 30px; height: 30px; margin: auto 0"
              src="@/assets/img/left-outlined.png"
            />
          </div> -->
        </el-form-item>
      </el-form>
    </div>
    <div style="font-family: Poppins-SemiBold">
      I don't have an account?
      <el-link @click="SignUp" style="color: #f47458">Sign Up</el-link>
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

import { strpscript, validateVEmail, validateVPassword } from "@/utils/check";
export default {
  components: {
    ArrowRight,
  },
  setup(props,ctx) {
    
    //邮箱验证
    let validateEmail = (rule, value, callback) => {
      if (value == "") callback(new Error("邮箱不能为空！"));
      else if (validateVEmail(value)) {
        callback(new Error("邮箱格式有误！"));
      } else {
        callback();
      }
    };

    // 密码验证
    let validatePassword = (rule, value, callback) => {
      console.log(strpscript(value));
      if (value == "") callback(new Error("密码不能为空！"));
      else if (validateVPassword(value)) {
        callback(new Error("密码格式6-15位，只能包含大小写字母和数字！"));
      } else callback();
    };

    const formData = reactive({
      pass: "",
      email: "",
    });

    const rules = reactive({
      email: [{ validator: validateEmail, trigger: "blur" }],
      pass: [{ validator: validatePassword, trigger: "blur" }],
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
      ctx.emit("isSignIn",false)
      console.log("zhuce");
    }
    // 登录
    // const login = () => {
    //   console.log("inter ", formData);
    //   //root.$router.push("/console");
    //   return false;
    //   // let loginData = {
    //   //   username: formData.email,
    //   //   password: formData.pass,
    //   //   // code: loginForm.checkCode,
    //   //   // module: "login",
    //   // };
    //   // 使用vuex进行登录
    //   // root.$store
    //   //   .dispatch("login", loginData)
    //   //   .then(response => {
    //   //     root.$massage.success("登录成功！");
    //   //     root.$router.push("/console");
    //   //   })
    //   //   .catch(error => {
    //   //     console.log(error);
    //   //     root.$message.error("登录失败！");
    //   //   });
    //   // 传统方式
    //   // Login(loginData)
    //   //   .then(response => {
    //   //     root.$massage.success("登录成功！");
    //   //     root.$router.push("/console");
    //   //   })
    //   //   .catch(error => {
    //   //     root.$message.error("登录失败！");
    //   //   });
    // };
    return {
      formData,
      rules,
      ruleRef,
      submit,
      SignUp,
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
.slogan {
  color: #00000077;
  font-weight: bold;
  text-align: left;
  margin-top: 30px;
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
  left: 30%;
  // background-color: #f47458;
  width: 150px;
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
</style>