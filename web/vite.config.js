import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

/**
 * Vite 项目配置文件
 * 包含插件配置、路径别名设置以及开发服务器代理配置
 */
export default defineConfig({
  // 启用 Vue 插件支持 SFC (.vue 文件)
  plugins: [vue()],
  resolve: {
    alias: {
      // 设置 '@' 为 'src' 目录的别名，方便导入文件
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 5173, // 前端开发服务器端口
    proxy: {
      // 配置 API 代理，解决开发环境跨域问题
      '/api': {
        target: 'http://localhost:8081', // 后端 Spring Boot 服务地址
        changeOrigin: true,              // 允许跨域请求携带原主机头
      }
    }
  }
})
