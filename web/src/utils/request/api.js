// api.js
import request from "@utils/request/axios";

// 封装各种接口请求
// export const 接口名 = () => request.get("/路由",参数对象);
export const getMenuUrlList = params =>
	request.post("/core/menuurl/getList.do", params);

export const deleteMenuUrl = params =>
	request.post("/core/menuurl/delete.do", { primaryId: params.urlId });

export const getMenuTreeList = () =>
	request.post("/core/menutree/getList.do", {
		needGuide: false,
		isTop: false
	});
