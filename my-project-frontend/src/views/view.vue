<template>
    test
</template>

<script setup>
import { get, post } from '../net';

onMounted(() => {
    // 获得列表
    // getUserInf();
    get("/api/token/getTokenDetails");
    // let { data } = await getSubject();
    // data.forEach(item => subjectList.push(item));
})
</script>