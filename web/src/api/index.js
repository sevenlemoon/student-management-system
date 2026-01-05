import axios from 'axios'
import { ElMessage } from 'element-plus'

/**
 * Axios 实例封装
 * 配置基础路径、超时时间以及统一的请求/响应拦截器
 */
const service = axios.create({
  baseURL: '/api', // 接口请求的前缀，会被 Vite 代理转发到后端
  timeout: 5000    // 请求超时时间 5 秒
})

// 请求拦截器：在发送请求之前执行
service.interceptors.request.use(
  config => {
    // 这里可以统一添加 Auth Token 等请求头
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器：在接收到响应后执行
service.interceptors.response.use(
  response => {
    const res = response.data
    // 约定：code 为 200 表示业务逻辑成功
    if (res.code !== 200) {
      ElMessage.error(res.message || '系统错误')
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      // 成功则返回 data 部分，简化调用方代码
      return res.data || res
    }
  },
  error => {
    // 处理网络错误或 HTTP 状态码错误
    console.error('Response error:', error)
    ElMessage.error(error.message || '网络连接失败')
    return Promise.reject(error)
  }
)

export default service
