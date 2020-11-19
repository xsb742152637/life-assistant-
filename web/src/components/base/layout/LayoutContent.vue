<template>
	<div class="layout layout-content">
		<router-view />
	</div>
</template>

<script>
export default {
	name: "LayoutMenu",
	data() {
		return {
			value1: true,
			value2: true
		};
	}
};
</script>

<style>
.layout-content {
	height: 800px;
}
</style>
