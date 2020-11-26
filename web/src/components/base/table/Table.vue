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
				<el-button type="info" v-show="defaultTableData.tools.isSearch">
					搜索
				</el-button>
				<el-button type="success" v-show="defaultTableData.tools.isAdd">
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
					@click="deleteData"
				>
					删除
				</el-button>
			</div>
		</div>
		<el-table
			:data="defaultTableData.data"
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
					:key="row.prop"
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
				getListApi: null,
				deleteApi: null,
				params: {},
				//需要显示的工具按钮
				tools: {
					isSearch: false, //是否显示搜索按钮
					isAdd: false, // 是否显示新增按钮
					isEdit: false, // 是否显示修改按钮
					isDelete: false // 是否显示删除按钮
				},
				head: [], // 表头
				data: null, // 数据
				maxHeight: 500, // 表格最大高度
				height: null, //
				highlightCurrentRow: true, // 是否高亮当前行
				showSummary: false, // 是否在表尾显示合计行

				currentPage: 1, // 当前页数
				pageSizes: [10, 25, 50, 100], // 每页显示个数选择器的选项设置
				pageSize: 25, // 每页显示条目个数
				total: 0 // 总条目数
			}
		};
	},
	computed: {},
	methods: {
		handleSizeChange(val) {
			let self = this;
			console.log(`每页 ${val} 条`);
			self.reLoad({ currentPage: val });
			self.loadData();
		},
		handleCurrentChange(val) {
			let self = this;
			console.log(`当前页: ${val}`);
			self.$set(self.defaultTableData, "currentPage", val);
			self.reLoad({ currentPage: val });
			self.loadData();
		},
		handleRowClick(val) {
			this.multipleTable = val;
			console.log(val);
		},
		deleteData() {
			let self = this;
			if (!this.multipleTable) {
				self.$message({
					type: "warning",
					message: "请选中一行数据"
				});
				return;
			}

			this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				type: "danger"
			})
				.then(() => {
					self.$mes.deleteLoadOpen();
					self.defaultTableData
						.deleteApi(self.multipleTable)
						.then(rs => {
							self.$mes.loadClose(rs);
							if (rs.error == 0) {
								//如果当前页面只有一条数据且删除成功，页数减一
								if (
									self.defaultTableData.data &&
									self.defaultTableData.data.length == 1
								) {
									self.defaultTableData.currentPage -= 1;
								}
								// 加载数据
								self.loadData(self);
								self.$emit("deleteSuccess", rs);
							} else {
								self.$emit("deleteError", rs);
							}
						})
						.catch(function(err) {
							self.$mes.loadClose(err);
							self.$emit("deleteError", err);
						});
				})
				.catch(() => {
					console.log("已取消删除");
				});
		},
		loadData() {
			let self = this;
			if (self.defaultTableData.getListApi) {
				let params = self.defaultTableData.params;
				if (!params) {
					params = {};
				}
				params.page = self.defaultTableData.currentPage;
				params.rows = self.defaultTableData.pageSize;

				self.$mes.queryLoadOpen();
				self.defaultTableData
					.getListApi(params)
					.then(rs => {
						self.$mes.loadClose();
						if (rs.error == 0) {
							self.reLoad({ data: rs.data, total: rs.total });
							self.$emit("loadSuccess", rs);
						} else {
							self.$emit("loadError", rs);
						}
					})
					.catch(function(err) {
						self.$mes.loadClose(err);
						self.$emit("loadError", err);
					});
			}
		},
		reLoad(obj) {
			this.defaultTableData = Object.assign(
				[],
				this.defaultTableData,
				obj
			);
		}
	},
	mounted() {},
	created() {
		let self = this;
		if (this.tableData) {
			this.reLoad(this.tableData);
			if (this.defaultTableData.head) {
				for (let i = 0; i < this.defaultTableData.head.length; i++) {
					this.defaultTableData.head[i] = Object.assign(
						{},
						this.defHead,
						this.defaultTableData.head[i]
					);
				}
			}
			self.loadData();
		}
	}
};
</script>

<style scoped lang="scss">
.el-table {
	margin-top: $Padding;
	margin-bottom: $Padding;
}
.el-pagination {
	padding: 0px;
}
.table-tools {
	display: flex;
}
.table-tools-right {
	flex: 1;
	text-align: right;
}
</style>
