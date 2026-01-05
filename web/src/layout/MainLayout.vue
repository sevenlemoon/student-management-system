<template>
  <div class="min-h-screen bg-slate-50 flex">
    <!-- 侧边栏 -->
    <aside class="w-64 bg-white border-r border-gray-100 flex flex-col">
      <div class="p-8 flex items-center gap-3">
        <div class="w-10 h-10 bg-blue-600 rounded-xl flex items-center justify-center shadow-blue-100 shadow-lg text-2xl">
          😋
        </div>
        <span class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-gray-800 to-gray-500">高兴展实训</span>
      </div>

      <nav class="flex-1 px-4 space-y-2">
        <router-link 
          v-for="item in menuItems" 
          :key="item.path"
          :to="item.path"
          class="flex items-center gap-3 px-4 py-3 rounded-2xl transition-all duration-300 group"
          :class="[route.path === item.path ? 'bg-blue-50 text-blue-600' : 'text-gray-500 hover:bg-gray-50']"
        >
          <el-icon class="text-xl"><component :is="item.icon" /></el-icon>
          <span class="font-medium">{{ item.title }}</span>
        </router-link>
      </nav>

      <div class="p-4 border-t border-gray-50">
        <div class="flex items-center gap-3 px-4 py-3 rounded-2xl bg-gray-50">
          <el-avatar :size="32" :src="user.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=Felix'" />
          <div class="flex-1 min-w-0">
            <p class="text-sm font-semibold text-gray-800 truncate">{{ user.username }}</p>
            <p class="text-xs text-gray-400 truncate">管理员</p>
          </div>
          <button @click="handleLogout" class="text-gray-400 hover:text-red-500 transition-colors">
            <el-icon><SwitchButton /></el-icon>
          </button>
        </div>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="flex-1 flex flex-col min-w-0 overflow-hidden">
      <header class="h-16 bg-white/80 backdrop-blur-md border-b border-gray-100 px-8 flex items-center justify-between sticky top-0 z-20">
        <h2 class="text-lg font-bold text-gray-800">{{ route.meta.title }}</h2>
        <div class="flex items-center gap-4">
          <el-popover
            placement="bottom-end"
            :width="320"
            trigger="click"
            popper-class="notification-popper"
          >
            <template #reference>
              <button class="w-10 h-10 rounded-full hover:bg-gray-50 flex items-center justify-center text-gray-500 relative">
                <el-icon><Bell /></el-icon>
                <span v-if="notifications.length > 0" class="absolute top-2 right-2 w-2 h-2 bg-red-500 rounded-full border-2 border-white"></span>
              </button>
            </template>
            
            <div class="flex flex-col">
              <div class="flex items-center justify-between mb-4 px-1">
                <span class="font-bold text-gray-800">系统通知</span>
                <el-button v-if="notifications.length > 0" type="primary" link @click="clearNotifications">清空</el-button>
              </div>
              
              <div v-if="notifications.length === 0" class="py-10 flex flex-col items-center justify-center text-gray-400">
                <el-icon class="text-4xl mb-2"><ChatDotRound /></el-icon>
                <p>暂无新通知</p>
              </div>
              
              <div class="max-h-[400px] overflow-y-auto space-y-3 pr-1" v-else>
                <div 
                  v-for="item in notifications" 
                  :key="item.id"
                  class="p-3 rounded-xl bg-gray-50/50 border border-gray-100 hover:bg-gray-100/50 transition-colors"
                >
                  <div class="flex items-start gap-3">
                    <div 
                      class="w-8 h-8 rounded-lg flex items-center justify-center text-lg"
                      :class="[
                        item.type === 'success' ? 'bg-green-100 text-green-600' : 
                        item.type === 'warning' ? 'bg-yellow-100 text-yellow-600' : 'bg-blue-100 text-blue-600'
                      ]"
                    >
                      <el-icon v-if="item.type === 'success'"><CircleCheck /></el-icon>
                      <el-icon v-else-if="item.type === 'warning'"><Warning /></el-icon>
                      <el-icon v-else><InfoFilled /></el-icon>
                    </div>
                    <div class="flex-1 min-w-0">
                      <p class="text-sm font-bold text-gray-800 truncate">{{ item.title }}</p>
                      <p class="text-xs text-gray-500 mt-0.5 line-clamp-2">{{ item.message }}</p>
                      <p class="text-[10px] text-gray-400 mt-2">{{ item.time }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-popover>
        </div>
      </header>

      <div class="flex-1 overflow-y-auto p-8">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>
  </div>
</template>

<script setup>
/**
 * 核心布局组件
 * 包含侧边导航栏、顶部通知栏以及子路由的视图容器
 */
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { 
  User, 
  SwitchButton, 
  Bell, 
  ChatDotRound, 
  CircleCheck, 
  Warning, 
  InfoFilled,
  HomeFilled,
  Reading
} from '@element-plus/icons-vue'
import { useNotification } from '../composables/useNotification'

const route = useRoute()
const router = useRouter()
// 引入全局通知数据和清空函数
const { notifications, clearNotifications } = useNotification()

// 当前登录用户信息
const user = ref({
  username: '',
  avatar: ''
})

// 侧边栏菜单项配置
const menuItems = [
  { title: '学生管理', path: '/', icon: Reading }
]

/**
 * 处理退出登录
 */
const handleLogout = () => {
  localStorage.removeItem('user') // 清除本地存储
  router.push('/login')           // 返回登录页
}

// 页面挂载时从本地存储获取用户信息
onMounted(() => {
  const userData = localStorage.getItem('user')
  if (userData) {
    user.value = JSON.parse(userData)
  }
})
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.fade-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
