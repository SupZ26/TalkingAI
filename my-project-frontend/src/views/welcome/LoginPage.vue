<link rel="stylesheet" href="https://at.alicdn.com/t/c/font_4665451_zkpbelp7bfe.css?spm=a313x.manage_type_myprojects.i1.9.3f1b3a81QC6xsA&file=font_4665451_zkpbelp7bfe.css">
<template>
    <div style="text-align: center; margin: 0 80px">
        <div style="margin-top: 40px">
            <div style="font-size: 25px; font-weight: bold">账号登录</div>
        </div>
        <div class="kuang" style="margin-top: 30px" >
            <el-form :model="form" :rules="rules" ref="formRef">
                <el-form-item prop="username">
                    <el-input style="margin-top: 20px" v-model="form.username" maxlength="10" type="text" placeholder="用户名/邮箱">
                        <template #prefix>
                            <el-icon>
                                <User />
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                        v-model="form.password"
                        type="password"
                        maxlength="20"
                        style="margin-top: 10px"
                        placeholder="密码"
                    >
                        <template #prefix>
                            <el-icon>
                                <Lock />
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-row style="margin-top: 5px">
                    <el-col :span="12" style="text-align: left">
                        <el-form-item prop="remember">
                            <el-checkbox v-model="form.remember" label="记住我" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" style="text-align: right">
                        <el-link @click="router.push('/forget')">忘记密码？</el-link>
                    </el-col>
                </el-row>
            </el-form>
        </div>
        <div style="margin-top: 20px">
            <el-button @click="userLogin()" style="width: 270px" type="success" plain>立即登录</el-button>
        </div>
        <el-divider>
            <span  style="color: red; font-size: 13px; " >没有账号</span>
        </el-divider>
        <div>
            <el-button style="width: 270px" @click="router.push('/register')" type="warning" plain>注册账号</el-button>
        </div>
        <!--TODO 这里的按钮进作为临时测试微信登录的按钮，修改为正式网页时需进行替换-->
      <!--
        <div>
            <el-button style="width: 270px" @click="router.push('/WeChatLogin')" type="warning" plain
                >微信登录</el-button
            >
        </div>-->
      <div class="iconfont">
        <!--
        <el-button @click=githubLogin() ,type="warning" >github登录
        </el-button>-->
        <span class="icon-weixin"></span>
        <span class="icon-github" @click=githubLogin()></span>
      </div>
    </div>

</template>

<script setup>

import { User, Lock } from "@element-plus/icons-vue";
import router from "@/router";
import { reactive, ref } from "vue";
import { login } from "@/net";

const formRef = ref();
const form = reactive({
    username: "",
    password: "",
    remember: false,
});

const rules = {
    username: [{ required: true, message: "请输入用户名" }],
    password: [{ required: true, message: "请输入密码" }],
};

function userLogin() {
    formRef.value.validate((isValid) => {
        if (isValid) {
            //TODO test
            login(form.username, form.password, form.remember, () => router.push("/a"));
        }
    });
}
function githubLogin() {
  window.location.href = "http://localhost:8080/oauth2/authorization/github";
}
</script>

<style scoped>
.kuang{
  border-style:solid ;
  height: 180px;
  width: 350px;
  border-color:transparent;
}
@font-face {
  font-family: "iconfont"; /* Project id 4665451 */
  src: url('//at.alicdn.com/t/c/font_4665451_zkpbelp7bfe.woff2?t=1724724754367') format('woff2'),
  url('//at.alicdn.com/t/c/font_4665451_zkpbelp7bfe.woff?t=1724724754367') format('woff'),
  url('//at.alicdn.com/t/c/font_4665451_zkpbelp7bfe.ttf?t=1724724754367') format('truetype');
}
.iconfont {
  font-family: "iconfont" !important;
  font-size: 35px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
.icon-weixin{
  position: absolute;
  left: 120px;
  color: darkgreen;
}
.icon-github{
  position: absolute;
  right: 120px;
}
.icon-weixin:before {
  content: "\e6ea";
}

.icon-github:before {
  content: "\e689";
}

</style>
