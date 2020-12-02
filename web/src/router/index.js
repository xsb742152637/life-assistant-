import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";

Vue.use(VueRouter);

const routes = [
	{
		path: "/",
		name: "Home",
		// component: Home
		meta: { title: "应用管理" },
		component: () => import("@core/menuurl/index.vue")
	},
	{
		path: "/about",
		name: "About",
		// route level code-splitting
		// this generates a separate chunk (about.[hash].js) for this route
		// which is lazy-loaded when the route is visited.
		component: () => import("@views/About.vue")
	},
	{
		path: "/memberinfo",
		name: "MemberInfo",
		meta: { title: "成员管理" },
		component: () => import("@core/memberinfo/index.vue")
	},
	{
		path: "/menuurl",
		name: "MenuUrl",
		meta: { title: "应用管理" },
		component: () => import("@core/menuurl/index.vue")
	},
	{
		path: "/iconfont",
		name: "IconFont",
		meta: { title: "字体图标" },
		component: () => import("@core/iconfont/index.vue")
	}
];

const router = new VueRouter({
	mode: "history",
	base: process.env.BASE_URL,
	routes
});

export default router;
