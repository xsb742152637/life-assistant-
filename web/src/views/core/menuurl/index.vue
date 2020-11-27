<template>
	<div>
		<Table
			:tableData="tableData"
			@loadSuccess="loadSuccess"
			@loadError="loadError"
			@deleteSuccess="deleteSuccess"
		></Table>
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
			tableData: {
				getListApi: this.$api.getMenuUrlList,
				deleteApi: this.$api.deleteMenuUrl,
				tools: {
					isSearch: true,
					isAdd: true,
					isEdit: true,
					isDelete: true
				},
				head: [
					{
						label: "编码",
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
						label: "地址",
						prop: "address",
						align: "left"
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
		}
	},
	mounted() {},
	created() {}
};
</script>

<style scoped lang="scss"></style>
