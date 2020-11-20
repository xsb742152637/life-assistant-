<template>
	<div>
		<Table :tableData="tableData"></Table>
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
				tools: {
					isSearch: true,
					isAdd: true,
					isEdit: true,
					isDelete: true
				},
				head: [
					{
						label: "日期",
						prop: "data",
						width: "180",
						fixed: "left",
						align: "left"
					},
					{
						label: "姓名",
						prop: "name",
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
				currentPage: 1,
				total: 100
			}
		};
	},
	methods: {
		del(data) {
			console.log("回调");
			console.log(data);
		}
	},
	mounted() {},
	created() {}
};
</script>

<style scoped lang="scss"></style>
