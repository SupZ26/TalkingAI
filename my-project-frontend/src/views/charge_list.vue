<template>
    <div id="charge">
        <div class="charge-content">
            <h1 style="margin-top: 15vh">充值列表</h1>
            <div style="margin-left: 20%; margin-right: 20%">
                <div class="amount-selector">
                    <h3>充值金额：</h3>

                    <div class="options">
                        <label v-for="option in amountOptions" :key="option.value" class="option"
                            :class="{ selected: selectedAmount === option.value }">
                            <input type="radio" :value="option.value" v-model="selectedAmount" />
                            <span>{{ option.label }}</span>
                        </label>
                        <!-- <label class="option custom-amount"
                            :class="{ active: customAmount !== null, selected: selectedAmount === 'custom' }">
                            <input type="radio" value="custom" v-model="selectedAmount" />
                            <span>自定义金额</span>
                            <input type="text" v-if="selectedAmount === 'custom'" v-model.number="customAmount"
                                @input="validateCustomAmount" min="10" placeholder="请输入金额"
                                :disabled="selectedAmount !== 'custom'" />
                        </label> -->
                    </div>
                </div>

                <div class="payment-method">
                    <h3>支付方式：</h3>
                    <div class="options">
                        <label v-for="method in paymentMethods" :key="method.value" class="option"
                            :class="{ selected: selectedPaymentMethod === method.value }">
                            <input type="radio" :value="method.value" v-model="selectedPaymentMethod" />
                            <span>{{ method.label }}</span>
                        </label>
                    </div>
                </div>

                <div class="summary">
                    <h3 style="display: inline">
                        您将支付:
                        <span class="total-amount">{{ 2 * totalAmount }} 元</span>
                    </h3>
                </div>

                <div class="actions">
                    <button @click="pay">确认支付</button>
                </div>
                <p style="text-align: center">目前仅支持支付宝付款</p>
                <!--拿数据-->
                <p style="text-align: center">当前余额：{{ this.userStore.deposit }}</p>
            </div>
        </div>
        <div class="payment-form" v-if="paymentFormHtml">
            <div v-html="paymentFormHtml"></div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import { ElMessage } from "element-plus";
import { useUserStore } from '@/stores/user';

export default {
    setup() {
        const userStore = useUserStore();

        return {
            userStore
        };
    },
    data() {
        return {
            amountOptions: [
                { value: 5, label: "10元" },
                { value: 15, label: "30元" },
                { value: 25, label: "50元" },
                { value: 50, label: "100元" },
                { value: 75, label: "150元" },
                { value: 100, label: "200元" },
            ],
            customAmount: null,
            selectedAmount: 5,
            paymentMethods: [{ value: "alipay", label: "支付宝" }],
            selectedPaymentMethod: "alipay",
            paymentFormHtml: "",
        };
    },
    computed: {
        // 计算总金额
        totalAmount() {
            return this.selectedAmount === "custom" ? this.customAmount : this.selectedAmount;
        },
    },
    watch: {
        // 监听selectedAmount的变化，当选择预设金额时清空自定义金额
        selectedAmount(newVal) {
            if (newVal !== "custom") {
                this.customAmount = null;
            } else {
                this.customAmount = 10;
            }
            console.log(this.selectedAmount);
            console.log(this.customAmount);
            console.log(this.totalAmount);
            console.log(this.selectedPaymentMethod);
        },

        // 监听customAmount的变化，当输入自定义金额时清空预设金额

        customAmount(newVal) {
            if (newVal !== null) {
                this.selectedAmount = "custom";
            }
            if (this.customAmount !== null && this.customAmount < 10) {
                this.customAmount = 10;
            }
        },
    },
    methods: {
        addDeposit() {
            const vo = {
                username: this.userStore.username,
                deposit: (2 * this.totalAmount).toString(),
            };
            axios
                .post("api/accounts/addDeposit", vo)
                .then(() => {
                })
                .catch(() => {
                }, 5000);
        },
        pay() {
            const order = {
                price: 2 * this.totalAmount,
                subject: `充值${2 * this.totalAmount}元`,
                body: `通过${this.selectedPaymentMethod}充值${2 * this.totalAmount}元`,
            };

            this.addDeposit();
            this.userStore.deposit =


                axios
                    .post("/alipay/pay", order)
                    .then((res) => {
                        console.log("支付成功", res.data);
                        this.paymentFormHtml = res.data.data;

                        setTimeout(() => {
                            document.forms["punchout_form"].submit();
                        }, 3000);
                    })
                    .catch((error) => {
                        console.error("支付失败", error);
                    });
        },

        validateCustomAmount(event) {
            // 验证输入是否为整数
            const value = event.target.value;
            if (!/^\d*$/.test(value)) {
                // 如果不是整数，则清除非法字符
                event.target.value = value.replace(/[^0-9]/g, "");
            }
        },
    },
};
</script>

<style scoped>
#charge {
    font-family: Arial, sans-serif;
    height: 100vh;
    align-items: center;
    background-color: #f5f5f5;
}

.charge-content {
    padding: 20px;
    border-radius: 5px;
    margin: 0 auto;
    display: flex;
    flex-direction: column;
    align-items: center;
}

h1 {
    text-align: center;
    color: #333;
}

h3 {
    color: #555;
    margin-top: 3%;
    margin-bottom: 3%;
}

.amount-selector,
.payment-method {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-bottom: 20px;
}

.options {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
}

.options .option {
    display: flex;
    align-items: center;
    margin-top: 0;
    margin-right: 20px;
    background-color: #fff;
    border: 1px solid #88bbff;
    color: #88bbff;
    border-radius: 5px;
    padding: 10px 20px;
    height: 25px;
    width: max-content;
    text-align: center;
    cursor: pointer;
    position: relative;
}

.options .option.selected {
    background-color: #88bbff;
    color: #fff;
}

.options .option input[type="radio"] {
    position: absolute;
    opacity: 0;
    width: 0;
    height: 0;
}

.summary {
    text-align: left;
    margin-bottom: 20px;
}

.summary .total-amount {
    font-weight: bold;
    color: #ff314d;
}

.actions button {
    background-color: #3c76d7;
    color: white;
    border: none;
    padding: 15px 40px;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
    display: block;
    margin: 0 auto;
    width: 100%;
}

.actions button:hover {
    background-color: #88bbff;
}

.payment-form {
    margin-top: 20px;
}
</style>
