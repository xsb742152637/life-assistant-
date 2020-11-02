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
export default {
    name: 'App'
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
