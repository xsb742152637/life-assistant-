// api.js
import request from "@utils/request/axios";

// 封装各种接口请求
// export const 接口名 = () => request.get("/路由",参数对象);

// 获取菜单路径列表
export const getMenuUrlList = params =>
	request.post("/core/menuurl/getList.do", params);

// 保存菜单路径
export const saveMenuUrl = params =>
	request.post("/core/menuurl/save.do", params);

// 删除菜单路径
export const deleteMenuUrl = params =>
	request.post("/core/menuurl/delete.do", { primaryId: params.urlId });

// 获取头像
export const getMemberPhoto = params =>
	request.post(
		"/core/memberarchives/getPhotoByBase64.do",
		{ memberId: params.memberId, text: params.text }
	);

// 获取菜单树
export const getMenuTreeList = () =>
	request.post("/core/menutree/getList.do", {
		needGuide: false,
		isTop: false
	});
