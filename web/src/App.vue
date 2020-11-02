<template>
  <div id="app">
    <LayoutHead/>
    <div class="layout layout-body">
      <div class="layout layout-menu">菜单</div>
      <div class="layout layout-content">正文</div>
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
body{
  margin: 0px;
  padding: 0px;
}
.layout-head{
  position: fixed;
  top: 0;
  width: 100%;
  padding: 10px;
  height: 40px;
  border-bottom: 1px solid #efefef;
  background-color: #fff;
}

.layout-body{
  margin-top: 60px;
  height: 800px;
  display: flex;
}
.layout-menu{
  width: 200px;
  border-right: 1px solid #efefef;
  height: 100%;

}
</style>
