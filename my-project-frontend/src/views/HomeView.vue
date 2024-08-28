<template>
    <div class="home">
        <div class="menu">
            <div class="subject">
                <!-- 添加话题接口 -->
                <div class="add-talk" @click="addSubject">
                    <div>添加新的话题</div>
                </div>
                <!-- 话题列表 -->
                <div class="subject-list">
                    <div class="list" :class="{ active: activeSubjectIndex == index }" @click="changeSubject(index,subject)"
                        v-for="(subject, index) in subjectList">
                        <div class="left">
                            <lay-icon type="layui-icon-file-b" color="white"></lay-icon>
                        </div>
                        <div class="center">{{ subject }}</div>
                        <div class="right">
                            <lay-icon type="layui-icon-edit" color="white" @click="alterSubject(index)"></lay-icon>
                            <lay-icon type="layui-icon-delete" color="white" @click="removeSubject(index)"></lay-icon>
                        </div>
                    </div>
                </div>
            </div>
            <div class="user" @click="router.push({ name: 'login' })">
                <div class="usercenter">用户中心</div>
            </div>
        </div>
        <div class="main">
            <div class="answers">
                <template v-if="activeSubjectIndex!=-1">
                    <div class="answer-item" v-for="question in questionList" :key="question.dialog_id">
                        <div class="question-name">
                            <div class="left">
                                <lay-avatar class='avatar'> A </lay-avatar>
                            </div>
                            <div class="right">{{ question.question }}</div>
                        </div>
                        <div class="answer">
                            <div class="left">
                                <img src="@/assets/logo.png" alt="">
                            </div>
                            <div class="right" @click="answerClickHandler" v-html="formatMd(question.response)">

                            </div>
                        </div>
                    </div>
                </template>
                <!-- 默认首页 -->
                <template v-else>
                    <div
                        class="headercenter text-gray-800 w-full md:max-w-2xl lg:max-w-3xl md:h-full md:flex md:flex-col px-6 dark:text-gray-100">
                        <h1
                            class="chatailogo text-4xl font-semibold text-center mt-6 sm:mt-[20vh] ml-auto mr-auto mb-10 sm:mb-16 flex gap-2 items-center justify-center">
                            ChatAi</h1>
                        <div class=" md:flex items-start text-center gap-3.5">
                            <div class="flex flex-col mb-8 md:mb-auto gap-3.5 flex-1">
                                <h2
                                    class="chatailogo flex gap-3 items-center m-auto text-lg font-normal md:flex-col md:gap-2">
                                    <svg stroke="currentColor" fill="none" stroke-width="1.5" viewBox="0 0 24 24"
                                        stroke-linecap="round" stroke-linejoin="round" class="h-6 w-6" height="1em"
                                        width="1em" xmlns="http://www.w3.org/2000/svg">
                                        <circle cx="12" cy="12" r="5"></circle>
                                        <line x1="12" y1="1" x2="12" y2="3"></line>
                                        <line x1="12" y1="21" x2="12" y2="23"></line>
                                        <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
                                        <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
                                        <line x1="1" y1="12" x2="3" y2="12"></line>
                                        <line x1="21" y1="12" x2="23" y2="12"></line>
                                        <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
                                        <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
                                    </svg>
                                    例子
                                </h2>
                                <ul class="flex flex-col gap-3.5 w-full sm:max-w-md m-auto">
                                    <button @click="inputChat('java编程语言是什么？')"
                                        class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md hover:bg-gray-200 dark:hover:bg-gray-900">
                                        "java编程语言是什么？" →
                                    </button>
                                    <button @click="inputChat('如何编写一个二分查找？')"
                                        class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md hover:bg-gray-200 dark:hover:bg-gray-900">
                                        "如何编写一个二分查找？"
                                        →
                                    </button>
                                    <button @click="inputChat('把大象放进冰箱需要几步？')"
                                        class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md hover:bg-gray-200 dark:hover:bg-gray-900">
                                        "把大象放进冰箱需要几步？" →
                                    </button>
                                </ul>
                            </div>
                            <div class="flex flex-col mb-8 md:mb-auto gap-3.5 flex-1">
                                <h2
                                    class="chatailogo flex gap-3 items-center m-auto text-lg font-normal md:flex-col md:gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                        stroke-width="1.5" stroke="currentColor" aria-hidden="true" class="h-6 w-6">
                                        <path stroke-linecap="round" stroke-linejoin="round"
                                            d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z">
                                        </path>
                                    </svg>
                                    能力
                                </h2>
                                <ul class="flex flex-col gap-3.5 w-full sm:max-w-md m-auto">
                                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                                        极大提升你的工作效率
                                    </li>
                                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                                        你的日常聊天伙伴
                                    </li>
                                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                                        给你代码快乐
                                    </li>
                                </ul>
                            </div>
                            <div class="flex flex-col mb-8 md:mb-auto gap-3.5 flex-1">
                                <h2
                                    class="chatailogo flex gap-3 items-center m-auto text-lg font-normal md:flex-col md:gap-2">
                                    <svg stroke="currentColor" fill="none" stroke-width="1.5" viewBox="0 0 24 24"
                                        stroke-linecap="round" stroke-linejoin="round" class="h-6 w-6" height="1em"
                                        width="1em" xmlns="http://www.w3.org/2000/svg">
                                        <path
                                            d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z">
                                        </path>
                                        <line x1="12" y1="9" x2="12" y2="13"></line>
                                        <line x1="12" y1="17" x2="12.01" y2="17"></line>
                                    </svg>
                                    注意
                                </h2>
                                <ul class="flex flex-col gap-3.5 w-full sm:max-w-md m-auto">
                                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                                        ai回复不代表本站观点
                                    </li>
                                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                                        回复可能偏离实际
                                    </li>
                                    <li class="w-full bg-gray-50 dark:bg-white/5 p-3 rounded-md">
                                        不要诱导ai
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </template>
            </div>
            <div class="add-question">
                <div class="question-box">
                    <lay-input @keydown.enter="sendQuestion" v-model="question" placeholder="请输入问题"
                        class="add-question-input" size="lg">
                        <template #append>
                            <lay-icon @click="sendQuestion" type="layui-icon-release" class="add-question-icon"></lay-icon>
                        </template>
                    </lay-input>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup>
import { useUserStore } from '@/stores/user';
import { layer } from '@layui/layui-vue';
import { ref, reactive, onMounted } from 'vue';
import '@/assets/index.css';
import { createSubject, getSubject, deleteSubject, changeSubjectTitle } from '@/net/index.js';
import { getQuestions } from '@/net/index';
import MarkdownIt from 'markdown-it';
import hljs from 'highlight.js';
import 'highlight.js/styles/default.css';
import { useRouter, useRoute } from 'vue-router';
const store = useUserStore();
const router = useRouter();

let md = new MarkdownIt({
    highlight: function (str, lang) {
        if (lang && hljs.getLanguage(lang)) {
            return highlightBlock(hljs.highlight(lang, str).value, lang);
        }
        return ''; // 使用额外的默认转义
    }
});
//markdown格式化及高亮显示
const formatMd = str => {
    return md.render(str);
}
const highlightBlock = (str, lang) => {
    return
    `<pre class="pre-code-box"><div class="pre-code-header">${lang && `<span class="code-block-header__lang">${lang}</span>`}<span class="code-block-header__copy">复制代码</span></div><div class="pre-code"><code class="hljs code-block-body language-${lang}">${str}</code></div></pre>`
}

//复制代码
const answerClickHandler = async ($event) => {
    if ($event.srcElement.className = "code-block-header__copy") {
        await navigator.clipboard.writeText($event.srcElement.parentElement.parentElement.querySelector('code').innerHTML);
    };
}

//记录当前活跃的主题编号
const activeSubjectIndex = ref(-1);
//记录所有的话题数据
const subjectList = reactive([{ title: 'nihao', id: 1 }, { title: '世界', id: 2 }]);
const questionList = reactive([]);
const question = ref('');
// 点击添加话题处理事件
const addSubject = () => {
    layer.prompt({
        title: "输入话题名称",
        value: "新话题",
        maxLength: 32,
        async yes(layero, title) {
            let { data } = await createSubject(title);
            console.log(data);
            //更新列表
            subjectList.unshift(data);
            layer.close(layero);
        }
    })
}

//删除话题
const removeSubject = async (index) => {
    await deleteSubject(subjectList[index]);
    subjectList.splice(index, 1);
}

//修改话题名称
const alterSubject = async (index) => {
    const old=subjectList[index];
    layer.prompt({
        title: "输入话题名称",
        value: "",
        maxLength: 32,
        async yes(layero, title) {
            await changeSubjectTitle(store.id,old,title);
            layer.close(layero);
        }
    })
}

//选中话题
const changeSubject = async (index,subject) => {
    //记录当前话题编号
    activeSubjectIndex.value=index;
    let { data } = await getQuestions(store.username,subject);
    //清除QuestionList中的数据
    questionList.length = 0;
    data.array.forEach(element => {
        questionList.push(element);
    });
    console.log(data)
}

//问问题
const sendQuestion = () => {
    const url = 'http://127.0.0.1:8080/api/chatAI/toChat';
    let es = new EventSource(url,{
        username:store.username,
        topic:subjectList[activeSubjectIndex.value],
        model:'gpt-3.5-turbo',
        message:{
            role:store.role,
            content:question.value
        }
    });
    //组装回复的问题
    questionList.push({
        username: store.username,
        topic: subjectList[activeSubjectIndex.value],
        model: 'gpt-3.5-turbo',
        response: ''
    });
    es.onopen = function () {
        //打卡链接

    }
    es.onmessage = function (event) {
        //接受消息
        //status是状态码，content是内容
        console.log(event);
        const message = JSON.parse(event.data);
        switch (message.status) {
            case 1:
                //正常接收数据
                questionList[questionList.length - 1].response += message.content;
                break;
            case 2:
            //更新token


            default:
                break;
        }

    }
    es.onclose = function () {
        //关闭连接

    }
}

const inputChat = str => {
    question.value = str;
}

onMounted(async () => {
    // 获得列表
    let { data } = await getSubject();
    data.forEach(item => subjectList.push(item));
})
</script>

<style lang="scss" scoped>
.home {
    width: 100%;
    height: 100%;
    display: flex;
    position: relative;
    overflow: hidden;
}

.menu {
    background-color: #202123;
    width: 300px;
    height: 100vh;
    color: white;
    display: flex;
    flex-direction: column; //设置成上下排列方式
    justify-content: space-between;
}

.subject {
    width: 100%;
    padding: 5px;
    display: flex;
    flex-direction: column;

    .add-talk {
        width: 100%;
        border: 1px solid #555659;
        padding: 10px;
        padding-top: 7px;
        border-radius: 5px;
        height: 50px;


        cursor: pointer;

        &:hover {
            background-color: #2b2c2f;
        }

        div {
            margin-left: 20px;
            font-size: 15px;
            align-items: center;
            height: 100%;
        }
    }

    .subject-list {
        width: 100%;
        margin-top: 10px;
        height: 90%;

        .list {
            color: white;
            height: 40px;
            line-height: 40px;
            margin: 10px 5px;

            .left {
                width: 10%;
                float: left;
            }

            .center {
                width: 65%;
                float: left;
                text-align: left;
            }

            .right {
                float: right;
                width: 25%;
            }

            &:hover {
                cursor: pointer;
                background-color: #2b2c2f;
            }
        }

        .active {
            background-color: #2a2b32;
        }

    }
}




.user {
    width: 96%;
    height: 50px;
    line-height: 50px;
    display: flex;
    justify-content: space-between;
    margin: 10px;

    &:hover {
        cursor: pointer;
        background-color: #2b2c2f;
    }

    .usercenter {
        width: 100%;
        height: 40px;

    }
}

.main {
    display: flex;
    flex: 1 1 0%;
    max-width: 100%;
    height: 100vh;
    position: relative;
    overflow: hidden;
    flex-direction: column;
    background-color: #343541;
}

.add-question {
    position: absolute;
    width: 100%;
    bottom: 0;

    .add-question-input .layui-input {
        background-color: #40414f;
        border-width: 0px;
        bottom: 20px;
        color: white;
    }

    // .add-question-icon{
    //     background-color: #40414f;
    // }

    .question-box {
        max-width: 900px;
        margin: 30px auto;
    }
}

.answers {
    position: relative;
    width: 100%;
    bottom: 140px;
    height: 100%;
    top: 0;
    overflow: scroll;

    .answer-item {
        max-width: 900px;
        overflow: hidden;
    }
}

.headercenter {
    position: absolute;
    left: 25%;
    top: 0%;
}

.chatailogo {
    color: white;
}
</style>
