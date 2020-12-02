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
				<el-tooltip content="搜索" placement="top" :disabled="!onlyIcon">
					<el-button
						type="primary"
						icon="el-icon-search"
						:size="buttonSize"
						:loading="loading"
						:disabled="disabled"
						v-show="defaultTableData.tools.isSearch"
						@click="searchDialog = true"
					>
						{{searchText}}
					</el-button>
				</el-tooltip>

				<el-tooltip content="新增" placement="top" :disabled="!onlyIcon">
					<el-button
						type="success"
						icon="el-icon-plus"
						:size="buttonSize"
						:loading="loading"
						:disabled="disabled"
						v-show="defaultTableData.tools.isAdd"
						@click="saveShow(true)"
					>
						{{addText}}
					</el-button>
				</el-tooltip>
				<el-tooltip content="修改" placement="top" :disabled="!onlyIcon">
					<el-button
						type="warning"
						icon="el-icon-edit"
						:size="buttonSize"
						:loading="loading"
						:disabled="disabled"
						v-show="defaultTableData.tools.isEdit"
						@click="saveShow(false)"
					>
						{{editText}}
					</el-button>
				</el-tooltip>

				<el-tooltip content="删除" placement="top" :disabled="!onlyIcon">
					<el-button
						type="danger"
						icon="el-icon-delete"
						:size="buttonSize"
						:loading="loading"
						:disabled="disabled"
						v-show="defaultTableData.tools.isDelete"
						@click="deleteData"
					>
						{{deleteText}}
					</el-button>
				</el-tooltip>

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
					:formatter="row.formatter"
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

		<!-- 搜索 -->
		<el-dialog title="搜索" width="40%" :visible.sync="searchDialog">
			<el-form :model="searchForm">
				<slot name="searchSlot"></slot>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button
					icon="el-icon-close"
					:size="buttonSize"
					:loading="loading"
					:disabled="disabled"
					@click="searchDialog = false"
				>
					取消
				</el-button>
				<el-button
					type="info"
					icon="el-icon-refresh"
					:size="buttonSize"
					:loading="loading"
					:disabled="disabled"
					@click="resetSearchData('searchForm')"
				>
					重置
				</el-button>
				<el-button
					type="primary"
					icon="el-icon-check"
					:size="buttonSize"
					:loading="loading"
					:disabled="disabled"
					@click="searchData"
				>
					确 定
				</el-button>
			</div>
		</el-dialog>

		<!-- 新增/修改 -->
		<el-dialog title="新增/修改" width="40%" :visible.sync="saveDialog">
			<el-form :model="saveForm" :rules="saveRules" ref="saveForm">
				<slot name="saveSlot"></slot>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button
					icon="el-icon-close"
					:size="buttonSize"
					:loading="loading"
					:disabled="disabled"
					@click="saveDialog = false"
				>
					取消
				</el-button>
				<el-button
					type="primary"
					icon="el-icon-check"
					:size="buttonSize"
					:loading="loading"
					:disabled="disabled"
					@click="saveData('saveForm')"
				>
					确 定
				</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<!--
完成后事件:
	loadSuccess：加载成功
	loadError：加载失败
	deleteSuccess：删除成功
	deleteError：删除失败
-->

<script scoped>
export default {
	name: "Table",
	props: ["tableData", "searchForm", "saveForm", "saveRules"],
	components: {},
	data() {
		return {
			loading: false,
			disabled: false,
			onlyIcon: false, // 是否只显示按钮图标
			buttonSize: process.env.VUE_APP_BUTTON_SIZE,
			searchDialog: false, // 显示/隐藏 搜索弹出框
			saveDialog: false, // 显示/隐藏 编辑弹出框
			selectedData: null, // 选中的数据
			defHead: {
				headerAlign: "center",
				showOverflowTooltip: true //是否隐藏多余内容
			}, // 表头默认属性
			defaultTableData: {
				primaryField: "", // 数据的主键字段名称
				getListApi: null,
				saveApi: null,
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
				data: [], // 数据
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
	computed: {
		searchText() {
			return this.onlyIcon ? "" : "搜索";
		},
		addText() {
			return this.onlyIcon ? "" : "新增";
		},
		editText() {
			return this.onlyIcon ? "" : "修改";
		},
		deleteText() {
			return this.onlyIcon ? "" : "删除";
		}
	},
	methods: {
		handleSizeChange(val) {
			let self = this;
			self.reLoad({ currentPage: val });
			self.loadData();
		},
		handleCurrentChange(val) {
			let self = this;
			self.$set(self.defaultTableData, "currentPage", val);
			self.reLoad({ currentPage: val });
			self.loadData();
		},
		handleRowClick(val) {
			// 选中一行
			this.selectedData = val;
		},
		deleteData() {
			let self = this;
			if (!this.selectedData) {
				self.$mes.warning("请选中一行数据");
				return;
			}

			this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				type: "danger"
			})
				.then(() => {
					self.loading = true;
					self.$mes.deleteLoadOpen();
					self.defaultTableData
						.deleteApi(self.selectedData)
						.then(rs => {
							self.loading = false;
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
							self.loading = false;
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
				params = Object.assign(
					[],
					params,
					self.searchForm
				);
				params.page = self.defaultTableData.currentPage;
				params.rows = self.defaultTableData.pageSize;

				console.log(params);
				self.$mes.queryLoadOpen();
				self.loading = true;
				self.defaultTableData
					.getListApi(params)
					.then(rs => {
						self.loading = false;
						self.$mes.loadClose();
						if (rs.error == 0) {
							self.reLoad({ data: rs.data, total: rs.total });
							self.$emit("loadSuccess", rs);
						} else {
							self.$emit("loadError", rs);
						}
					})
					.catch(function(err) {
						self.loading = false;
						self.$mes.loadClose();
						self.$emit("loadError", err);
					});
			}
		},
		resetSearchData(formName) {
			this.searchDialog = false;
			this.$refs[formName].resetFields();
			this.loadData();
//			this.searchForm.searchKey = "";
		},
		searchData() {
			this.searchDialog = false;
			this.loadData();
//			this.searchForm.searchKey = "";
		},
		saveShow(isAdd){
			let self = this;
			self.saveDialog = true;
			// 重置表单
			if(!isAdd){
				if (!this.selectedData) {
					self.$mes.warning("请选中一行数据");
					return;
				}
				for(let name in self.saveForm){
					self.saveForm[name] = self.selectedData[name];
				}
//				self.saveForm = self.selectedData;
			}else{
				for(let name in self.saveForm){
					self.saveForm[name] = "";
				}
			}
		},
		saveData(formName) {
			let self = this;
			let state = true;
			self.$refs[formName].validate((valid) => {
				if (!valid) {
					state = false;
					return state;
				}
			});
			if (state) {
				console.log(self.saveForm);
				self.loading = true;
				self.$mes.deleteLoadOpen();
				self.defaultTableData
					.saveApi(self.saveForm)
					.then(rs => {
						self.loading = false;
						self.$mes.loadClose(rs);
						if (rs.error == 0) {
							self.saveDialog = false;
							// 加载数据
							self.loadData(self);
							self.$emit("saveSuccess", rs);
						} else {
							self.$emit("saveError", rs);
						}
					})
					.catch(function(err) {
						self.loading = false;
						self.$mes.loadClose(err);
						self.$emit("saveError", err);
					});
			}else{
				self.$mes.warning("请检查表单内容！");
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
			if(this.tableData.onlyIcon){
				this.onlyIcon = this.tableData.onlyIcon;
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
