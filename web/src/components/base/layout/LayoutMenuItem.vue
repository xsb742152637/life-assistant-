<template>
	<div>
		<template v-for="menu in menuData">
			<template v-if="menu.children">
				<el-submenu :index="menu.id" :key="menu.id">
					<template slot="title">
						<i class="el-icon-message"></i>
						{{ menu.title }}
					</template>
					<LayoutMenuItem :menuData="menu.children"></LayoutMenuItem>
				</el-submenu>
			</template>
			<template v-else>
				<el-menu-item :index="menu.id" :key="menu.id">
					<!--<router-link :to="{ name: menu.code }">{{ menu.title }}</router-link>-->
					<span @click="clickMenuItem(menu)">{{ menu.title }}</span>
				</el-menu-item>
			</template>
		</template>
	</div>
</template>

<script>
export default {
	name: "LayoutMenuItem",
	props: ["menuData"],
	data() {
		return {};
	},
	methods: {
		clickMenuItem(menu) {
			console.log(menu);
			this.$router.push({ name: menu.code, params: { id: "1" } });
		}
	}
};
</script>
