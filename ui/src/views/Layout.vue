<template>
  <el-container class="layout">
    <el-header class="header">
      <div class="brand">积分权益运营系统</div>
      <el-menu
        mode="horizontal"
        :default-active="$route.path"
        router
        class="menu"
        background-color="#182848"
        text-color="#c8d6ef"
        active-text-color="#ffffff"
      >
        <el-menu-item index="/home">首页</el-menu-item>
        <el-menu-item index="/task">任务</el-menu-item>
        <el-menu-item index="/product">积分兑换</el-menu-item>
        <el-menu-item index="/seckill">限时抢购</el-menu-item>
        <el-menu-item index="/bill">账单</el-menu-item>
        <el-menu-item index="/mine">我的</el-menu-item>
      </el-menu>
    </el-header>
    <el-main class="main">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
</script>

<style scoped>
.layout {
  min-height: 100vh;
}
.header {
  display: flex;
  align-items: center;
  background: #182848;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}
.brand {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  margin-right: 40px;
  white-space: nowrap;
}
.menu {
  border-bottom: none;
  flex: 1;
}
.main {
  background: #f0f2f5;
}
</style>
