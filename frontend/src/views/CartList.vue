<template>
  <div>
    <el-card shadow="never">
      <div class="toolbar">
        <span class="title">我的购物车</span>
        <div>
          <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleRemove">
            删除选中（{{ selectedIds.length }}）
          </el-button>
          <el-button @click="loadCart">刷新</el-button>
        </div>
      </div>
    </el-card>

    <el-card shadow="never" style="margin-top: 16px">
      <el-table
        :data="tableData"
        v-loading="loading"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="购物车ID" width="100" />
        <el-table-column prop="productId" label="商品ID" width="90" />
        <el-table-column label="商品名称" min-width="180" show-overflow-tooltip>
          <template #default="{ row }">{{ row.productName || '-' }}</template>
        </el-table-column>
        <el-table-column label="单价(元)" width="120">
          <template #default="{ row }">{{ row.price ?? '-' }}</template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="90" />
        <el-table-column label="小计(元)" width="120">
          <template #default="{ row }">{{ subtotal(row) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleRemoveOne(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pager"
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        :page-sizes="[5, 10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="loadCart"
        @size-change="handleSearch"
      />
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pageCart, removeCart } from '../api/cart'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const selectedIds = ref([])

const query = reactive({
  pageNum: 1,
  pageSize: 5
})

const subtotal = (row) => {
  if (row.price === undefined || row.price === null) return '-'
  return (Number(row.price) * Number(row.quantity)).toFixed(2)
}

async function loadCart() {
  loading.value = true
  try {
    const data = await pageCart({
      pageNum: query.pageNum,
      pageSize: query.pageSize
    })
    tableData.value = data?.data?.list || []
    total.value = Number(data?.data?.total || 0)
  } catch (e) {
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.pageNum = 1
  loadCart()
}

function handleSelectionChange(rows) {
  selectedIds.value = rows.map((r) => r.id)
}

async function handleRemoveOne(id) {
  await confirmRemove([id])
}

async function handleRemove() {
  await confirmRemove(selectedIds.value)
}

async function confirmRemove(ids) {
  if (!ids.length) return
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${ids.length} 条购物车记录吗？`, '提示', {
      type: 'warning'
    })
  } catch (e) {
    return // 用户取消
  }

  try {
    await removeCart(ids)
    ElMessage.success('删除成功')
    // 删除后若当前页已空，回退一页
    if (tableData.value.length === ids.length && query.pageNum > 1) {
      query.pageNum -= 1
    }
    loadCart()
  } catch (e) {
    // 错误提示已在响应拦截器统一处理
  }
}

onMounted(loadCart)
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  font-size: 16px;
  font-weight: bold;
}

.pager {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>