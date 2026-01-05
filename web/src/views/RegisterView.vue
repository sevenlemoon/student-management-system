<template>
  <div class="min-h-screen bg-slate-50 flex items-center justify-center relative overflow-hidden">
    <!-- 背景装饰 Blob -->
    <div class="absolute top-0 -left-4 w-72 h-72 bg-purple-300 rounded-full mix-blend-multiply filter blur-xl opacity-70 animate-blob"></div>
    <div class="absolute top-0 -right-4 w-72 h-72 bg-yellow-300 rounded-full mix-blend-multiply filter blur-xl opacity-70 animate-blob animation-delay-2000"></div>
    <div class="absolute -bottom-8 left-20 w-72 h-72 bg-pink-300 rounded-full mix-blend-multiply filter blur-xl opacity-70 animate-blob animation-delay-4000"></div>

    <div class="w-full max-w-md px-6 py-12 bg-white/80 backdrop-blur-xl rounded-3xl shadow-xl border border-white/20 relative z-10">
      <div class="text-center mb-10">
        <div class="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-tr from-green-600 to-teal-600 rounded-2xl shadow-lg mb-4">
          <el-icon class="text-3xl text-white"><UserFilled /></el-icon>
        </div>
        <h2 class="text-3xl font-bold text-gray-800 tracking-tight">创建账号</h2>
        <p class="text-gray-500 mt-2 font-sans">加入学生管理系统</p>
      </div>

      <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-position="top">
        <el-form-item prop="username">
          <el-input 
            v-model="registerForm.username" 
            placeholder="请输入用户名" 
            class="custom-input"
            :prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="registerForm.password" 
            type="password" 
            placeholder="请输入密码" 
            show-password
            class="custom-input"
            :prefix-icon="Lock"
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input 
            v-model="registerForm.confirmPassword" 
            type="password" 
            placeholder="请确认密码" 
            show-password
            class="custom-input"
            :prefix-icon="CircleCheck"
          />
        </el-form-item>

        <div class="mt-8">
          <el-button 
            type="success" 
            class="w-full !h-12 !text-lg !font-semibold !rounded-2xl shadow-green-200 shadow-lg hover:scale-[1.02] active:scale-95 transition-all"
            :loading="loading"
            @click="handleRegister"
          >
            注 册
          </el-button>
        </div>
      </el-form>

      <div class="mt-8 text-center">
        <span class="text-gray-400 text-sm">已有账号？</span>
        <button @click="router.push('/login')" class="text-green-600 font-semibold text-sm hover:underline ml-1">立即登录</button>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 注册页面组件
 * 提供新管理员账号注册功能，包含密码二次确认校验
 */
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { UserFilled, Lock, Back } from '@element-plus/icons-vue'
import { register } from '../api/app'
import { ElMessage } from 'element-plus'

const router = useRouter()
const registerFormRef = ref(null)
const loading = ref(false)

// 注册表单数据
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

/**
 * 密码二次确认自定义校验规则
 */
const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

// 注册校验规则
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [{ validator: validatePass2, trigger: 'blur' }]
}

/**
 * 处理注册逻辑
 * 验证通过后调用注册 API，成功后引导用户跳转至登录页
 */
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await register({
          username: registerForm.username,
          password: registerForm.password
        })
        ElMessage.success('注册成功，请登录')
        router.push('/login') // 注册成功，跳转至登录页面
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
  @apply !bg-white/50 !shadow-none !border-none !rounded-2xl !h-12 px-4;
}
:deep(.custom-input .el-input__inner) {
  @apply !text-gray-700;
}

@keyframes blob {
  0% { transform: translate(0px, 0px) scale(1); }
  33% { transform: translate(30px, -50px) scale(1.1); }
  66% { transform: translate(-20px, 20px) scale(0.9); }
  100% { transform: translate(0px, 0px) scale(1); }
}
.animate-blob {
  animation: blob 7s infinite;
}
.animation-delay-2000 {
  animation-delay: 2s;
}
.animation-delay-4000 {
  animation-delay: 4s;
}
</style>
