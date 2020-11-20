<template>
	<div>
		<template v-for="menu in menuData">
			<template v-if="menu.children">
				<el-submenu :index="menu.code" :key="menu.code">
					<template slot="title">
						<i :class="menu.icon"></i>
						{{ menu.title }}
					</template>
					<LayoutMenuItem :menuData="menu.children"></LayoutMenuItem>
				</el-submenu>
			</template>
			<template v-else>
				<el-menu-item :index="menu.code" :key="menu.code">
					<i :class="menu.icon"></i>
					<span>{{ menu.title }}</span>
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
	}
};
</script>
