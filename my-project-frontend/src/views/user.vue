<template>
    <div class="container">
        <el-form :model="form" label-width="auto" style="max-width: 600px">
            <el-form-item label="用户名">
                <el-input v-model="form.username" />
            </el-form-item>
            <el-form-item label="新密码">
                <el-input v-model="password" />
            </el-form-item>
            <el-form-item label="旧密码">
                <el-input v-model="password1" />
            </el-form-item>

            <el-form-item label="邮箱">
                <el-input v-model="form.email" type="textarea" disabled />
            </el-form-item>

            <el-form-item label="token">
                <el-input v-model="form.token" type="textarea" disabled />
            </el-form-item>

            <el-form-item label="余额">
                <el-input v-model="form.deposit" type="textarea" disabled />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit">确定</el-button>
                <el-button>取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import axios from "axios";
export default {
    components: {},
    props: {},
    data() {
        return {
            form: {
                username: "",
                email: "",
                token: "",
                deposit: "",
                password: "",
                // delivery: false,
                // type: [],
                // resource: '',
                // desc: '',
            },

            username: "",
            password: "",
            userId: "",

            password1: "",
        };
    },
    watch: {},
    computed: {},
    methods: {
        init() {
            axios
                .get("/api/accounts/findAllDetails", {
                    headers: {
                        Authorization: "Bearer" + " " + JSON.parse(sessionStorage.getItem("authorize")).token,
                    },
                })
                .then((res) => {
                    console.log(res.data.data);

                    this.form = res.data.data;
                    this.form1 = res.data.data;
                    this.username = res.data.data.username;

                    // this.password=res.data.data.password;
                    this.userId = res.data.data.id;
                });
        },
        onSubmit() {
            //  this.init();
            if (this.username == this.form.username) {
                alert("用户名不能和之前一样");

                return false;
            } else {
                this.restName();
            }
            if (this.password == "") {
                return false;
            } else {
                this.restPassword();
            }
        },

        restName() {
            console.log(this.username, this.form.username);
            var obj = {
                newUsername: this.form.username,
                oldUsername: this.form1.username,
            };
            axios
                .put(
                    "/api/accounts/updateUsername?oldUsername=" + this.username + "&newUsername=" + this.form.username,
                    null,
                    {
                        headers: {
                            Authorization: "Bearer " + JSON.parse(sessionStorage.getItem("authorize")).token,
                        },
                    }
                )
                .then((res) => {
                    console.log(res.data.data);
                    // 如果需要，可以在这里更新表单或其他状态
                    // this.form = res.data.data; // 注意：这里假设 res.data.data 是你期望更新的表单数据

                    alert(res.data.data);
                })
                .catch((error) => {
                    console.error("Error updating username:", error);
                });
        },

        restPassword() {
            var obj = {
                oldPassword: this.password1,
                newPassword: this.password,
            };
            axios
                .put(
                    "/api/accounts/updatePassword?userId=" + this.userId,
                    {
                        oldPassword: this.password1,
                        newPassword: this.password,
                    },
                    {
                        headers: {
                            Authorization: "Bearer " + JSON.parse(sessionStorage.getItem("authorize")).token,
                        },
                    }
                )
                .then((res) => {
                    console.log(res.data.data);
                    // 如果需要，可以在这里更新表单或其他状态
                    // 注意：通常更新密码后不需要将响应数据赋值给表单，除非响应中包含了新的表单状态
                    // this.form = res.data.data; // 假设这不是必要的
                    alert(res.data.data);
                })
                .catch((error) => {
                    console.error("Error updating password:", error);
                });
        },
    },
    created() {},
    mounted() {
        this.init();
    },
};
</script>
<style scoped>
.container {
    position: absolute;
    top: 50%;
    left: 50%;

    transform: translate(-50%, -50%);
}
</style>
