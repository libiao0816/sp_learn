<template>
  <!-- 登录等独立页面：整页渲染，不套后台布局 -->
  <router-view v-if="isStandalone" />

  <!-- 后台管理系统布局：左侧导航 + 右侧内容区 -->
  <el-container v-else class="layout">
    <!-- 左侧导航条 -->
    <el-aside :width="isCollapse ? '64px' : '210px'" class="aside">
      <div class="logo">
        <span v-if="!isCollapse">lb-learn1 后台管理</span>
        <span v-else>LB</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        class="side-menu"
        background-color="#001529"
        text-color="rgba(255, 255, 255, 0.68)"
        active-text-color="#ffffff"
      >
        <el-menu-item v-for="item in menus" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.label }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶部栏：折叠按钮 + 面包屑 + 用户下拉 -->
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>后台管理</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <el-dropdown @command="handleCommand">
          <span class="user">
            <el-icon><UserFilled /></el-icon>
            <span class="user-name">{{ username }}</span>
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowDown,
  Avatar,
  Expand,
  Fold,
  Goods,
  Tickets,
  UserFilled
} from '@element-plus/icons-vue'
import { logout } from './api/auth'

const route = useRoute()
const router = useRouter()

// 侧边栏折叠状态
const isCollapse = ref(false)

// 左侧菜单配置：路由路径 + 文案 + 图标
const menus = [
  { path: '/product', label: '商品管理', icon: Goods },
  { path: '/order', label: '订单管理', icon: Tickets },
  { path: '/user', label: '用户管理', icon: Avatar },
  { path: '/sysUser', label: '个人中心', icon: UserFilled }
]

// 菜单高亮跟随当前路由
const activeMenu = computed(() => route.path)

// 面包屑取当前路由的标题
const currentTitle = computed(() => (route.meta.title as string) || '')

// 登录等独立页面不显示后台布局
const isStandalone = computed(() => route.meta.standalone === true)

// 登录时写入的用户名，用于右上角展示
const username = ref(localStorage.getItem('username') || '管理员')

async function handleCommand(command: string) {
  if (command === 'profile') {
    router.push('/sysUser')
    return
  }
  if (command === 'logout') {
    await handleLogout()
  }
}

// 退出登录：调用后端注销接口，并清理本地登录态
async function handleLogout() {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' })
  } catch {
    return // 用户取消
  }

  try {
    await logout()
    ElMessage.success('已退出登录')
  } catch {
    // 注销接口失败也继续清理本地登录态，避免卡在无效会话
  } finally {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('username')
    router.push('/login')
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body,
#app {
  height: 100%;
}

.layout {
  height: 100%;
}

/* 左侧导航：深色背景 */
.aside {
  background-color: #001529;
  transition: width 0.2s;
  overflow-x: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  white-space: nowrap;
  background-color: #002140;
}

.side-menu {
  border-right: none;
}

/* 选中菜单项高亮 */
.side-menu .el-menu-item.is-active {
  background-color: #409eff;
}

.side-menu .el-menu-item:hover {
  background-color: #1f3b52;
}

/* 顶部栏 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  border-bottom: 1px solid #e4e7ed;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #303133;
}

.user {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  color: #303133;
  outline: none;
}

.user-name {
  font-size: 14px;
}

/* 内容区 */
.main {
  background-color: #f5f7fa;
  padding: 20px;
  overflow-y: auto;
}
</style>
