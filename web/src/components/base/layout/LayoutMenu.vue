<template>
	<el-menu
		:default-openeds="['1', '3']"
		backgroundColor="#11193c"
		textColor="#fff"
		activeTextColor="#5E4AE0"
	>
		<LayoutMenuItem :menuData="menuData"></LayoutMenuItem>
	</el-menu>
</template>

<script>
export default {
	name: "LayoutMenu",
	data() {
		return {
			menuData: []
		};
	},
	created() {
		var that = this;
		this.$api
			.getMenuTreeList()
			.then(data => {
				console.log(data);
				that.menuData = data;
			})
			.catch(function(err) {
				console.log(err);
			});
	}
};
</script>

<style scoped lang="scss">
.el-aside {
	margin-top: 60px;
}
.el-menu {
	width: 100%;
}
</style>
