<template>
  <div class="min-h-screen bg-slate-50 flex items-center justify-center relative overflow-hidden">
    <!-- 背景装饰 Blob -->
    <div class="absolute top-0 -left-4 w-72 h-72 bg-purple-300 rounded-full mix-blend-multiply filter blur-xl opacity-70 animate-blob"></div>
    <div class="absolute top-0 -right-4 w-72 h-72 bg-yellow-300 rounded-full mix-blend-multiply filter blur-xl opacity-70 animate-blob animation-delay-2000"></div>
    <div class="absolute -bottom-8 left-20 w-72 h-72 bg-pink-300 rounded-full mix-blend-multiply filter blur-xl opacity-70 animate-blob animation-delay-4000"></div>

    <div class="w-full max-w-md px-6 py-12 bg-white/80 backdrop-blur-xl rounded-3xl shadow-xl border border-white/20 relative z-10">
      <div class="text-center mb-10">
        <div class="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-tr from-blue-600 to-indigo-600 rounded-2xl shadow-lg mb-4 text-4xl">
          😋
        </div>
        <h2 class="text-3xl font-bold text-gray-800 tracking-tight">欢迎回来</h2>
        <p class="text-gray-500 mt-2 font-sans">学生信息管理系统</p>
      </div>

      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-position="top">
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入用户名" 
            class="custom-input"
            :prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码" 
            show-password
            class="custom-input"
            :prefix-icon="Lock"
          />
        </el-form-item>

        <div class="mt-8">
          <el-button 
            type="primary" 
            class="w-full !h-12 !text-lg !font-semibold !rounded-2xl shadow-blue-200 shadow-lg hover:scale-[1.02] active:scale-95 transition-all"
            :loading="loading"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </div>
      </el-form>

      <div class="mt-8 text-center">
        <span class="text-gray-400 text-sm">还没有账号？</span>
        <button @click="router.push('/register')" class="text-blue-600 font-semibold text-sm hover:underline ml-1">立即注册</button>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 登录页面组件
 * 提供用户名密码登录功能，并将用户信息存储在 localStorage 中
 */
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { UserFilled, Lock } from '@element-plus/icons-vue'
import { login } from '../api/app'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: ''
})

// 登录校验规则
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

/**
 * 处理登录逻辑
 * 验证表单通过后调用登录 API，成功后跳转至首页
 */
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await login(loginForm)
        // 登录成功，保存用户信息到本地存储
        localStorage.setItem('user', JSON.stringify(res))
        ElMessage.success('登录成功')
        router.push('/') // 跳转至管理后台首页
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
:deep(.custom-input .el-input__wrapper) {
  @apply !bg-gray-50/50 !shadow-none !border-none !rounded-2xl !h-12 !px-4;
}
:deep(.custom-input .el-input__inner) {
  @apply !text-gray-700 font-sans;
}
</style>
