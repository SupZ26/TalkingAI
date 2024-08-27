<template>
  <div class='container'>
    <section>
      <div class="login-box">
        <router-view v-slot="{ Component }">
          <transition name="el-fade-in-linear" mode="out-in">
            <component :is="Component" style="height: 100%" />
          </transition>
        </router-view>
        </div>
    </section>
  </div>
</template>

<script setup></script>

<style scoped>
.container{
  position: relative;
  width: 100vw;
  height: 100vh;
  background-image :url("https://th.bing.com/th/id/R.e095e6cf39baa7d349d45c4c1c6f5f78?rik=%2fJXDWPsR3VaxVw&riu=http%3a%2f%2fimg.sj33.cn%2fuploads%2fallimg%2f200912%2f20091223150330493.jpg&ehk=TJoSjF68pWq6vl0NHUl8RTUroI2xHptF83Im%2b0RVWnc%3d&risl=&pid=ImgRaw&r=0");
  background-size:100%;
}
.login-box{
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%,-50%);
  width: 500px;
  height: 500px;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  box-shadow: 0px 0px 5px #777;
  background-color: cornsilk;
}
</style>
