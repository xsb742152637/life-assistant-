<template>
	<div>
		<Table :tableData="tableData" @loadSuccess="loadSuccess"></Table>
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
		loadSuccess(data) {
			console.log("加载成功");
			console.log(data);
		},
		delete() {
			console.log("删除");
		}
	},
	mounted() {},
	created() {
//		this.$mes.deleteLoadOpen();
	}
};
</script>

<style scoped lang="scss"></style>
