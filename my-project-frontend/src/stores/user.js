import {defineStore} from 'pinia'
//在选项你可以认为state 是 store 的数据(data)，
//getters 是 store 的计算属性(computed)，而 actions 则是方法(methods)。
//appSessionId是令牌
export const useUserStore=defineStore('user',{
    state(){
        return {
            id:0,
            username:'',
            password:'',
            email:'',
            role:'',
            registerTime:'',
            deposit:0,
            token:0,
        }
    },
    getters:{

    },
    actions:{
        login(id,username,email,role,registerTime,deposit,token){
            this.id=id;
            this.username=username;
            this.password=password;
            this.email=email;
            this.role=role;
            this.registerTime=registerTime;
            this.token=token;
        }
    },
    persist:true
});