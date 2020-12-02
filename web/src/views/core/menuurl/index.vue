<template>
	<div>
		<Table
			:tableData="tableData"
			:searchForm="searchForm"
			:saveForm="saveForm"
			:saveRules="saveRules"
			@loadSuccess="loadSuccess"
			@loadError="loadError"
			@deleteSuccess="deleteSuccess"
		>
			<template #searchSlot>
				<el-form-item label="关键字：" :label-width="formLabelWidth">
					<el-input v-model="searchForm.searchKey" autocomplete="off" placeholder="请输入关键字搜索"></el-input>
				</el-form-item>
			</template>
			<template #saveSlot>
				<el-input type="hidden" v-model="saveForm.urlId" ></el-input>
				<el-form-item label="菜单编码：" prop="code" :label-width="formLabelWidth">
					<el-input v-model="saveForm.code" placeholder="请输入菜单编码"></el-input>
				</el-form-item>
				<el-form-item label="菜单名称：" prop="title" :label-width="formLabelWidth">
					<el-input v-model="saveForm.title" placeholder="请输入菜单名称"></el-input>
				</el-form-item>
				<el-form-item label="菜单路径：" prop="url" :label-width="formLabelWidth">
					<el-input v-model="saveForm.url" placeholder="请输入菜单路径"></el-input>
				</el-form-item>
				<el-form-item label="参数：" prop="parameter" :label-width="formLabelWidth">
					<el-input v-model="saveForm.parameter" placeholder="请输入菜单路径"></el-input>
				</el-form-item>
			</template>
		</Table>

	</div>

</template>

<script scoped>
export default {
	name: "MenuUrl",
	props: {},
	components: {},
	computed: {},
	data() {
		return {
			formLabelWidth: "100px",
			searchForm: { // 搜索字段
				searchKey: ""
			},
			saveForm: { // 表单字段
				urlId: "",
				title: "",
				code: "",
				url: "",
				parameter: ""
			},
			saveRules: { // 验证规则
				title: [
					{required: true, message: '请输入菜单名称', trigger: 'blur'}
				],
				code: [
					{required: true, message: '请输入菜单编码', trigger: 'blur'}
				],
				url: [
					{required: true, message: '请输入菜单路径', trigger: 'blur'}
				]
			},
			tableData: {
				onlyIcon: false,
				getListApi: this.$api.getMenuUrlList,
				saveApi: this.$api.saveMenuUrl,
				deleteApi: this.$api.deleteMenuUrl,
				tools: {
					isSearch: true,
					isAdd: true,
					isEdit: true,
					isDelete: true
				},
				head: [
					{
						label: "菜单编码",
						prop: "code",
						width: "180",
						fixed: "left",
						align: "left"
					},
					{
						label: "菜单名称",
						prop: "title",
						width: "180",
						fixed: false,
						align: "left"
					},
					{
						label: "菜单路径",
						prop: "url",
						align: "left"
					},
					{
						label: "参数",
						prop: "parameter",
						width: "180",
						align: "left"
					},
					{
						label: "创建时间",
						prop: "sysTime",
						width: "180",
						align: "center",
						formatter: this.sysTimeFormatter
					}
				],
				pageSize: 10
			}
		};
	},
	methods: {
		loadSuccess(rs) {
			console.log("加载成功");
			console.log(rs);
		},
		loadError(rs) {
			console.log("加载失败");
			console.log(rs);
		},
		deleteSuccess(rs) {
			console.log("删除");
			console.log(rs);
		},
		sysTimeFormatter(row, colunm, cellValue, index) {
			if(cellValue){
				return this.$tool.formatDate(new Date(cellValue.time), "yyyy-MM-dd HH:mm");
			}else{
				return "";
			}
		}
	},
	mounted() {},
	created() {}
};
</script>

<style scoped lang="scss"></style>
