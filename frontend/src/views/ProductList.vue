<template>
  <div>
    <!-- 搜索条件 -->
    <el-card shadow="never">
      <el-form :inline="true" :model="query">
        <el-form-item label="分类">
          <el-select
            v-model="query.categoryId"
            placeholder="全部分类"
            clearable
            style="width: 180px"
          >
            <el-option
              v-for="c in categories"
              :key="c.id"
              :label="c.name"
              :value="String(c.id)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="query.productName" placeholder="名称模糊搜索" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 商品列表 -->
    <el-card shadow="never" style="margin-top: 16px">
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" min-width="180" show-overflow-tooltip />
        <el-table-column label="分类" width="140">
          <template #default="{ row }">
            {{ categoryName(row.categoryId) }}
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格(元)" width="120" />
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">{{ row.status === 1 ? '上架' : '下架' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row.id)">详情</el-button>
            <el-button link type="success" @click="openAdd(row)">加购</el-button>
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
        @current-change="loadProducts"
        @size-change="handleSearch"
      />
    </el-card>

    <!-- 商品详情弹窗 -->
    <el-dialog v-model="detailVisible" title="商品详情" width="560px">
      <el-descriptions v-if="detail" :column="1" border>
        <el-descriptions-item label="ID">{{ detail.id }}</el-descriptions-item>
        <el-descriptions-item label="名称">{{ detail.name }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ categoryName(detail.categoryId) }}</el-descriptions-item>
        <el-descriptions-item label="价格">{{ detail.price }} 元</el-descriptions-item>
        <el-descriptions-item label="库存">{{ detail.stock }}</el-descriptions-item>
        <el-descriptions-item label="描述">{{ detail.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createdTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 加入购物车弹窗 -->
    <el-dialog v-model="addVisible" title="加入购物车" width="420px">
      <el-form label-width="80px">
        <el-form-item label="商品">
          <span>{{ currentProduct.name }}</span>
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="addForm.quantity" :min="1" :max="999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addVisible = false">取消</el-button>
        <el-button type="primary" :loading="adding" @click="handleAddCart">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { pageProducts, getProductDetail } from '../api/product'
import { listCategories } from '../api/category'
import { addCart } from '../api/cart'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const categories = ref([])

const query = reactive({
  pageNum: 1,
  pageSize: 5,
  categoryId: '',
  productName: ''
})

// 详情弹窗
const detailVisible = ref(false)
const detail = ref(null)

// 加购弹窗
const addVisible = ref(false)
const adding = ref(false)
const currentProduct = reactive({ id: null, name: '' })
const addForm = reactive({ quantity: 1 })

// 分类 id -> 名称
function categoryName(id) {
  const hit = categories.value.find((c) => String(c.id) === String(id))
  return hit ? hit.name : '-'
}

async function loadCategories() {
  try {
    categories.value = (await listCategories())?.data || []
  } catch (e) {
    // 错误提示已在响应拦截器统一处理
  }
}

async function loadProducts() {
  loading.value = true
  try {
    const res = await pageProducts({
      pageNum: query.pageNum,
      pageSize: query.pageSize,
      categoryId: query.categoryId || null,
      productName: query.productName || null
    })
    console.log(res.data)
    tableData.value = res?.data?.list || []
    total.value = Number(res?.data?.total || 0)
  } catch (e) {
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.pageNum = 1
  loadProducts()
}

function handleReset() {
  query.categoryId = ''
  query.productName = ''
  handleSearch()
}

async function openDetail(id) {
  try {
    detail.value = await getProductDetail(id)
    detailVisible.value = true
  } catch (e) {
    // 错误提示已在响应拦截器统一处理
  }
}

function openAdd(row) {
  currentProduct.id = row.id
  currentProduct.name = row.name
  addForm.quantity = 1
  addVisible.value = true
}

async function handleAddCart() {
  adding.value = true
  try {
    await addCart({
      productId: currentProduct.id,
      quantity: addForm.quantity
    })
    ElMessage.success('已加入购物车')
    addVisible.value = false
  } catch (e) {
    // 错误提示已在响应拦截器统一处理
  } finally {
    adding.value = false
  }
}

onMounted(() => {
  loadCategories()
  loadProducts()
})
</script>

<style scoped>
.pager {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>