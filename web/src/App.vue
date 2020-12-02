<template>
	<div id="app">
		<el-container>
			<el-aside
				width="200px"
				style="background-color: rgb(238, 241, 246)"
			>
				<el-menu
					router
					:default-active="$route.path"
					backgroundColor="#11193c"
					textColor="#fff"
					activeTextColor="#5E4AE0"
				>
					<LayoutMenuItem :menuData="menuData"></LayoutMenuItem>
				</el-menu>
			</el-aside>
			<el-container>
				<el-header style="text-align: right; font-size: 12px">
					<div class="layout layout-head">
						<el-dropdown>
							<i class="el-icon-setting" style="margin-right: 15px"></i>
							<el-dropdown-menu slot="dropdown">
								<el-dropdown-item>查看</el-dropdown-item>
								<el-dropdown-item>新增</el-dropdown-item>
								<el-dropdown-item>删除</el-dropdown-item>
							</el-dropdown-menu>
						</el-dropdown>
						<el-avatar :size="20">
							<img :src="memberPhoto"/>
							<!--<img :src="memberPhoto" src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>-->
						</el-avatar>
						<span>小谢</span>
					</div>
				</el-header>
				<el-main>
					<div class="layout layout-content">
						<router-view />
					</div>
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
			memberPhoto: "",
			menuData: []
		};
	},
	created() {
		let self = this;
//		self.$router.push({ name: "MenuUrl", params: { id: "1" } });
		self.getMenuTreeList();
		self.getMemberPhoto();

	},
	mounted() {
		// js代码中使用环境变量
//		 console.log("VUE_APP_BUTTON_SIZE: ", process.env.VUE_APP_BUTTON_SIZE);
//		 console.log("BASE_URL: ", process.env.BASE_URL);
//		 console.log("VUE_APP_API: ", process.env.VUE_APP_API);
	},
	computed: {
	},
	methods: {
		getMenuTreeList() {
			let self = this;
			self.$api.getMenuTreeList()
				.then(data => {
					self.menuData = data;
				})
				.catch(function(err) {
					console.log(err);
				});
		},
		getMemberPhoto() {
			let self = this;
			let param = { memberId: "", text: ""};
			self.$api.getMemberPhoto(param)
				.then(data => {
					self.memberPhoto = data;
				})
				.catch(function(err) {
					console.log(err);
				});
		},
		errorHandler() {
			return true
		},
		aaa() {


		}
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
.el-main {
	padding: 10px !important;
}

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
	@include xie-box-shadow;
}
.layout-content {
	height: 800px;
}
.el-aside {
	margin-top: 60px;
}
.el-menu {
	width: 100%;
	@include xie-box-shadow;
}
.el-form>.el-form-item:last-child{
	margin-bottom: 0px !important;
}
.el-dialog>.el-dialog__header{
	border-bottom: $Border;
}
.el-dialog>.el-dialog__footer{
	border-top: $Border;
}
</style>
