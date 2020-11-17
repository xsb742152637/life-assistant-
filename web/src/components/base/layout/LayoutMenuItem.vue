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
					{{ menu.title }}
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
