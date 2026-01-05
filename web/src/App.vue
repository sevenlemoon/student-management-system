<template>
  <router-view />
</template>

<script setup>
</script>

<style>
/* 全局样式覆盖 */
.el-button {
  @apply rounded-xl transition-all duration-300;
}
.el-card {
  @apply rounded-3xl border-none shadow-sm;
}
</style>
