import Vue from "vue";
import App from "./App.vue";
import Router from "./router";
import Store from "./store";
import "@babel/polyfill"; // IE 兼容
import upperFirst from "lodash/upperFirst";
import camelCase from "lodash/camelCase";
import Element from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import "@styles/theme/index.css";
import * as api from "@utils/request/api";

Vue.use(Element);

// 挂载环境变量中的配置信息，然后在js中使用$src 访问。
Vue.prototype.$src = process.env.VUE_APP_PUBLIC_PATH;
Vue.prototype.$api = api;

Vue.config.productionTip = false;

// 自动读取全部基础组件
const requireComponent = require.context(
	// 其组件目录的相对路径
	"./components/base",
	// 是否查询其子目录
	true,
	// 匹配基础组件文件名的正则表达式
	/[A-Z]\w+\.(vue|js)$/
);

// 将自动读取到的组件全局注册
requireComponent.keys().forEach(fileName => {
	// 获取组件配置
	const componentConfig = requireComponent(fileName);

	console.log(requireComponent);
	// 获取组件的 PascalCase 命名
	const componentName = upperFirst(
		camelCase(
			// 获取和目录深度无关的文件名
			fileName
				.split("/")
				.pop()
				.replace(/\.\w+$/, "")
		)
	);

	// 全局注册组件
	Vue.component(
		componentName,
		// 如果这个组件选项是通过 `export default` 导出的，
		// 那么就会优先使用 `.default`，
		// 否则回退到使用模块的根。
		componentConfig.default || componentConfig
	);
});

new Vue({
	Router,
	Store,
	render: h => h(App)
}).$mount("#app");
