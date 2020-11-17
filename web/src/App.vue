<template>
	<div id="app">
		<!--<div id="nav">-->
		<!--<router-link to="/">Home</router-link> |-->
		<!--<router-link to="/about">About</router-link>-->
		<!--</div>-->
		<!--<router-view />-->

		<el-container>
			<el-aside
				width="200px"
				style="background-color: rgb(238, 241, 246)"
			>
				<LayoutMenu />
			</el-aside>
			<el-container>
				<el-header style="text-align: right; font-size: 12px">
					<LayoutHead />
				</el-header>
				<el-main>
					<LayoutContent />
				</el-main>
			</el-container>
		</el-container>
	</div>
</template>

<script>
export default {
	name: "App",
	data() {
		return {
			api: process.env.VUE_APP_API
		};
	},
	mounted() {
		// js代码中使用环境变量
		console.log("BASE_URL: ", process.env.BASE_URL);
		console.log("VUE_APP_API: ", process.env.VUE_APP_API);
	}
};
</script>

<style lang="scss">
html,
body,
#app {
	margin: 0px;
	padding: 0px;
	overflow: hidden;
	height: 100%;
}
#app {
	margin: 0px;
	padding: 0px;
	background-color: $BackgroundColor;
	overflow: auto;
	height: 100%;
	-ms-overflow-style: none;
	overflow: -moz-scrollbars-none;
}

::-webkit-scrollbar {
	width: 6px; /*对垂直流动条有效*/
	height: 6px; /*对水平流动条有效*/
}

/*定义滚动条的轨道颜色、内阴影及圆角*/
::-webkit-scrollbar-track {
	-webkit-box-shadow: inset 0 0 6px $BackgroundColor;
	background-color: $BackgroundColor;
	border-radius: 3px;
}

/*定义滑块颜色、内阴影及圆角*/
::-webkit-scrollbar-thumb {
	border-radius: 7px;
	-webkit-box-shadow: inset 0 0 6px #666;
	background-color: #e8e8e8;
}

/*定义两端按钮的样式*/
::-webkit-scrollbar-button {
	background-color: $BackgroundColor;
}

/*定义右下角汇合处的样式*/
::-webkit-scrollbar-corner {
	background: $BackgroundColor;
}

.el-header {
	line-height: 60px;
	padding: 0;
}
</style>
