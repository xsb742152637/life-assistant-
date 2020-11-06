<template>
<div id="app">
    <LayoutMenu/>
    <div class="layout layout-body">
        <LayoutHead/>
        <LayoutContent/>
    </div>
</div>
</template>

<script>

// import LayoutHead from './components/layout/LayoutHead.vue'

// 如果要跨域的话, 对axios进行一些设置,当前使用的是跨域的
// const axiosInstance = axios.create({
//    headers: {'Content-Type': 'application/json;charset=utf-8'}, // 设置传输内容的类型和编码
//    withCredentials: true // 指定某个请求应该发送凭据。允许客户端携带跨域cookie，也需要此配置
// })

export default {
    name: 'App'
//    ,
//    created () {
//        var url = '/core/menuurl/getList.do'
//        // 跨域访问
//        this.$axios.post(url).then(response => {
//            console.log(response.data) // response.data为后端返回的具体数据
//            alert('请求成功')
//        }).catch(error => {
//            console.log(error)
//            alert('请求失败')
//        })
//    }
}
</script>

<style>

html,body,#app{
    margin: 0px;
    padding: 0px;
    background-color: #f2f2f2;
    height: 100%;
}

#app{
    display: flex;
}
.layout-body{
    display: flex;
    flex: 1;
    position: relative;
    overflow: hidden;
}
</style>
