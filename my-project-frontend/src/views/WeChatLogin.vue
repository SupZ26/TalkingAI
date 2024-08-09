<template>
  <div id="app">
    <div id="ss">
      <div id="wxCode"></div> <!-- 用于显示二维码的容器 -->
    </div>
  </div>
</template>

<script>
export default {
  name: 'WxLoginComponent',
  mounted() {
    // 动态加载外部wxLogin.js文件
    const script = document.createElement('script');
    script.src = 'https://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js';
    script.onload = this.initializeWxLogin; // 确保脚本加载后初始化WxLogin
    document.body.appendChild(script);
  },
  methods: {
    initializeWxLogin() {
      // WxLogin.js脚本加载完毕后，初始化WxLogin
      if (typeof WxLogin !== 'undefined') {
        const obj = new WxLogin({
          id: "wxCode",  // div的id
          appid: "wx5963483aaf4a6422",  // 替换为实际的appid
          scope: "snsapi_userinfo",  // 写死
          redirect_uri: encodeURI("https://supz.mynatapp.cc/api/VX/callback"),  // 扫描二维码后跳转的页面
          state: "state",
          style: "black",  // 二维码黑白风格
          href: ""
        });
      } else {
        console.error('WxLogin is not defined');
      }
    }
  }
}
</script>

<style scoped>
/* 在这里添加你的样式，如果需要 */
</style>
