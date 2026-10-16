<template>
  <div>
    <!-- C 端用户列表 -->
    <el-card shadow="never">
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userNo" label="用户编号" width="160" />
        <el-table-column prop="nickname" label="昵称" min-width="140" show-overflow-tooltip>
          <template #default="{ row }">{{ row.nickname || '-' }}</template>
        </el-table-column>
        <el-table-column label="性别" width="90">
          <template #default="{ row }">{{ genderMap[row.gender] || '-' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusMap[row.status] || '未知' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="注册来源" width="140">
          <template #default="{ row }">{{ registerSourceMap[row.registerSource] || row.registerSource || '-' }}</template>
        </el-table-column>
        <el-table-column prop="lastLoginTime" label="最后登录" width="180">
          <template #default="{ row }">{{ row.lastLoginTime || '-' }}</template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180">
          <template #default="{ row }">{{ row.createTime || '-' }}</template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pager"
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="loadUsers"
        @size-change="handleSizeChange"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { pageUsers, type User } from '../api/user'

const loading = ref(false)
const tableData = ref<User[]>([])
const total = ref(0)

const query = reactive({
  pageNum: 1,
  pageSize: 10
})

// 状态映射
const statusMap: Record<number, string> = {
  0: '禁用',
  1: '正常',
  2: '注销'
}

function statusTagType(status: number): 'success' | 'warning' | 'info' {
  if (status === 1) return 'success'
  if (status === 0) return 'warning'
  return 'info'
}

// 性别映射
const genderMap: Record<number, string> = {
  0: '未知',
  1: '男',
  2: '女'
}

// 注册来源映射
const registerSourceMap: Record<string, string> = {
  PC_WEB: 'PC 网页',
  MINI_PROGRAM: '小程序',
  APP_IOS: 'iOS App',
  APP_ANDROID: 'Android App'
}

async function loadUsers() {
  loading.value = true
  try {
    const res = await pageUsers({ pageNum: query.pageNum, pageSize: query.pageSize })
    tableData.value = res?.data?.list || []
    total.value = Number(res?.data?.total || 0)
  } catch {
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function handleSizeChange() {
  query.pageNum = 1
  loadUsers()
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.pager {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>
