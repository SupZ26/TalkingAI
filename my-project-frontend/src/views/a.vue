<!--支付宝测试支付页面-->
<template>
    <div id="box">
        <div>
            <h1>订单列表</h1>
            <ul>
                <li v-for="order in orders" :key="order.orderId">
                    <h3>{{ order.subject }}</h3>
                    <p>价格: ¥{{ order.price }}</p>
                    <p>{{ order.body }}</p>
                    <button @click="pay(order)">购买</button>
                    <hr />
                </li>
            </ul>
        </div>
        <div v-html="paymentFormHtml"></div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            orders: [
                {
                    price: "12148.00",
                    subject: "iPhone 15 Pro Max 512GB 原色钛金属",
                    body: "iPhone 15 Pro Max 512GB 原色钛金属",
                },
                {
                    price: "0.1",
                    subject: "xiaomi 14 钛金属",
                    body: "xiaomi 14 钛金属 256gb",
                },
            ],
            paymentFormHtml: "",
        };
    },
    methods: {
        pay(order) {
            axios
                .post("/alipay/pay", order)
                .then((res) => {
                    console.log("支付成功", res.data);
                    this.paymentFormHtml = res.data.data;

                    setTimeout(() => {
                        document.forms["punchout_form"].submit();
                    }, 1000);
                })
                .catch((error) => {
                    console.error("支付失败", error);
                });
        },
    },
};
</script>

<style scoped>
/* 在这里添加你的样式 */
</style>
