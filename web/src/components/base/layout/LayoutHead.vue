<template>
	<div class="layout layout-head">
		<el-dropdown>
			<i class="el-icon-setting" style="margin-right: 15px"></i>
			<el-dropdown-menu slot="dropdown">
				<el-dropdown-item>查看</el-dropdown-item>
				<el-dropdown-item>新增</el-dropdown-item>
				<el-dropdown-item>删除</el-dropdown-item>
			</el-dropdown-menu>
		</el-dropdown>
		<span>王小虎</span>
	</div>
</template>

<script>
export default {
	name: "LayoutHead",
	data() {
		return {};
	}
};
</script>

<style scoped lang="scss">
.layout-head {
	position: fixed;
	top: 0px;
	background-color: $MenuBackgroundColor;
	color: #fff;
	flex: 1;
	left: 0px;
	right: 0px;
	z-index: 5;
	padding: 0px 15px;
}
</style>
