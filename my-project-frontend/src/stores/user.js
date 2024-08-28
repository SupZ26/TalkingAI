import { defineStore } from "pinia";
//在选项你可以认为state 是 store 的数据(data)，
//getters 是 store 的计算属性(computed)，而 actions 则是方法(methods)。
//appSessionId是令牌
export const useUserStore = defineStore("user", {
    state() {
        return {
            id: 0,
            username: "",
            password: "",
            email: "",
            role: "",
            registerTime: "",
            deposit: 0,
            token: 0,
        };
    },
    getters: {},
    actions: {
        login(user) {
            this.id = user.id;
            this.username = user.username;
            this.password = user.password;
            this.email = user.email;
            this.role = user.role;
            this.registerTime = user.registerTime;
            this.deposit = user.deposit;
            this.token = user.token;
        },

        logout() {
            this.id = 0;
            this.username = "";
            this.password = "";
            this.email = "";
            this.role = "";
            this.registerTime = "";
            this.deposit = 0;
            this.token = "";
        },

        setDeposit(value) {
            this.deposit = value;
        },

        getToken() {
            return this.token;
        },

        setToken(token) {
            this.token = token;
        },
    },
    persist: {
        key: "user",
        paths: ["id", "username", "email", "role", "registerTime", "deposit", "token"],
    },
});
