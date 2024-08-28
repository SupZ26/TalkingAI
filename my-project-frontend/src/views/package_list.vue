<template>
    <div id="packages">
        <div class="package-content">
            <h1 style="margin-top: 15vh">套餐列表</h1>
            <div style="margin-left: 20%; margin-right: 20%">
                <div class="package-selector">
                    <h3>套餐：</h3>

                    <div class="options">
                        <label v-for="option in package_options" :key="option.value" class="option"
                            :class="{ selected: selected_package === option.value }">
                            <input type="radio" :value="option.value" v-model="selected_package" />
                            <span>{{ option.label }}</span>
                        </label>
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
                    <h4 style="display: inline; text-align: center; color: #555">
                        您将使用{{ selected_paymethod }}购买<span class="total-amount">{{ selected_package
                            }}元</span><span>套餐，获得</span><span class="total-amount">{{ selected_tokens }} tokens</span>
                    </h4>
                </div>

                <div class="actions">
                    <button @click="pay">确认购买</button>
                </div>
            </div>
        </div>
        <div class="payment-form" v-if="paymentFormHtml">
            <div v-html="paymentFormHtml"></div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import { useUserStore } from "../stores/user";

export default {
    setup() {
        const userStore = useUserStore();

        return {
            userStore
        };
    },
    data() {
        return {
            package_options: [{ value: 20, label: "20元", tokens: 500000 }],
            selected_package: 20,
            paymentMethods: [
                { value: "remain", label: "余额" },
                { value: "alipay", label: "支付宝" },
            ],
            selectedPaymentMethod: "remain",
            paymentFormHtml: "",
        };
    },
    computed: {
        selected_tokens() {
            return this.package_options.find((option) => option.value === this.selected_package).tokens;
        },
        selected_paymethod() {
            return this.paymentMethods.find((option) => option.value === this.selectedPaymentMethod).label;
        },
    },
    methods: {
        ali_pay(order) {
            axios.post("/api/token/buyKeyByDeposit");

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

        pay() {
            const order = {
                price: this.selected_package,
                subject: `购买${this.selected_package}元套餐`,
                body: `通过${this.selectedPaymentMethod}购买${this.selected_package}元套餐`,
            };

            // check remain
            if (this.selectedPaymentMethod === "remain") {
                if (this.userStore.deposit >= 20)
                    axios.post("/api/token/buyKeyByDeposit");
            } else {
                this.ali_pay(order);
            }
        },
    },
};
</script>

<style scoped>
#packages {
    font-family: Arial, sans-serif;
    height: 100vh;
    align-items: center;
    background-color: #f5f5f5;
}

.package-content {
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

.package-selector,
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
    text-align: center;
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
