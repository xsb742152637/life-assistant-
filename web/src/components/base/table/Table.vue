<template>
	<div class="xie-panel">
		<div class="table-tools">
			<div class="table-tools-left">
				<!-- 使用： <template #left>aaa</template> -->
				<slot name="left"></slot>
			</div>
			<div class="table-tools-right">
				<!-- 使用： <template #right>bbb</template> -->
				<slot name="right"></slot>
				<el-button
					type="info"
					v-show="defaultTableData.tools.isSearch"
				>
					搜索
				</el-button>
				<el-button
					type="success"
					v-show="defaultTableData.tools.isAdd"
				>
					新增
				</el-button>
				<el-button
					type="warning"
					v-show="defaultTableData.tools.isEdit"
				>
					修改
				</el-button>
				<el-button
					type="danger"
					v-show="defaultTableData.tools.isDelete"
					@click="clickTableDelete"
				>
					删除
				</el-button>
			</div>
		</div>
		<el-table
			:data="tableData1"
			:max-height="defaultTableData.maxHeight"
			:height="defaultTableData.height"
			:highlight-current-row="defaultTableData.highlightCurrentRow"
			:show-summary="defaultTableData.showSummary"
			size="small"
			border
			stripe
			style="width: 100%"
			@row-click="handleRowClick"
		>

			<template v-for="row in defaultTableData.head">
				<el-table-column
					:label="row.label"
					:prop="row.prop"
					:width="row.width"
					:fixed="row.fixed"
					:header-align="row.headerAlign"
					:align="row.align"
					:show-overflow-tooltip="row.showOverflowTooltip"
				>
				</el-table-column>
			</template>
		</el-table>
		<div class="block">
			<el-pagination
				background
				:hide-on-single-page="true"
				@size-change="handleSizeChange"
				@current-change="handleCurrentChange"
				:current-page="defaultTableData.currentPage"
				:page-sizes="defaultTableData.pageSizes"
				:page-size="defaultTableData.pageSize"
				:pager-count="5"
				layout="prev, pager, next, jumper, sizes, total"
				:total="defaultTableData.total"
			>
			</el-pagination>
		</div>
	</div>
</template>

<script scoped>
export default {
	name: "Table",
	props: ["tableData"],
	components: {},
	data() {
		return {
			multipleTable: null,
			defHead: {
				headerAlign: "center",
				showOverflowTooltip: true //是否隐藏多余内容
			}, // 表头默认属性
			defaultTableData: {
				primaryField: "", // 数据的主键字段名称
				//
				api: {
					delete: null
				},
				//回调方法
				callback: {
					delete: null
				},
				//需要显示的工具按钮
				tools: {
					isSearch: false, //是否显示搜索按钮
					isAdd: false, // 是否显示新增按钮
					isEdit: false, // 是否显示修改按钮
					isDelete: false // 是否显示删除按钮
				},
				head: [], // 表头
				maxHeight: 500, // 表格最大高度
				height: 'auto', //
				highlightCurrentRow: true, // 是否高亮当前行
				showSummary: false, // 是否在表尾显示合计行

				currentPage: 1, // 当前页数
				pageSizes: [10, 25, 50, 100 , 1000], // 每页显示个数选择器的选项设置
				pageSize: 25, // 每页显示条目个数
				total: 100 // 总条目数
			},
			tableData1: [
				{
				date: '2016-05-03',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
			}, {
				date: '2016-05-02',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
			}, {
				date: '2016-05-04',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
			}, {
				date: '2016-05-01',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
			}, {
				date: '2016-05-08',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
			}, {
				date: '2016-05-06',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
			}, {
				date: '2016-05-07',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
			}]
		};
	},
	computed: {},
	methods: {
		handleSizeChange(val) {
			console.log(`每页 ${val} 条`);
		},
		handleCurrentChange(val) {
			console.log(`当前页: ${val}`);
		},
		handleRowClick(val) {
			this.multipleTable = val;
			console.log(val);
		},
		clickTableDelete() {
			let that = this;
			if(!this.multipleTable){
				this.$message({
					type: 'warning',
					message: '请选中一行数据'
				});
				return;
			}

			this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'danger'
			}).then(() => {
				if(that.defaultTableData.api.delete){ // 有删除api
					that.defaultTableData.api.delete(that.multipleTable)
						.then(rs => {
							if (rs.error) {
								this.$message.error(rs.msg);
							}else{
								this.$message({
									type: 'success',
									message: rs.msg
								});
								if (that.defaultTableData.callback.delete) {
									that.defaultTableData.callback.delete(that.multipleTable);
								}
							}
						})
						.catch(function(err) {
							this.$message.error("操作失败！");
						});
				} else if (that.defaultTableData.callback.delete) { // 有回调
					that.defaultTableData.callback.delete(that.multipleTable);
				} else {
					that.$message.error("未获取到删除方法的API或回调，无法删除！");
				}
			}).catch(() => {
				this.$message({
					type: 'info',
					message: '已取消删除'
				});
			});
		}
	},
	mounted() {},
	created() {
		if (this.tableData) {
			console.log(this.defaultTableData);
			this.defaultTableData = Object.assign([], this.defaultTableData, this.tableData);
			if(this.defaultTableData.head){
				for( let i = 0 ; i < this.defaultTableData.head.length ; i++ ){
					this.defaultTableData.head[i] = Object.assign({}, this.defHead, this.defaultTableData.head[i]);
				}
			}
			console.log(this.defaultTableData);
		}
	}
};
</script>

<style scoped lang="scss">
.el-table{
	margin-top: $Padding;
	margin-bottom: $Padding;
}
.el-pagination{
	padding: 0px;
}
.table-tools{
	display: flex;
}
.table-tools-left{

}
.table-tools-right{
	flex: 1;
	text-align: right;
}
</style>
