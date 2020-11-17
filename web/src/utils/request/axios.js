// 导入axios
import Axios from "axios";
import Qs from "qs";

// 进行一些全局配置
// 公共路由(网络请求地址)
Axios.defaults.baseURL = "/api";
// 请求响应超时时间
Axios.defaults.timeout = 5000;

// 封装自己的get/post方法
export default {
	get: function(path = "", data = {}) {
		return new Promise(function(resolve, reject) {
			Axios.get(path, {
				params: data
			})
				.then(function(response) {
					// 按需求来，这里我需要的是response.data，所以返回response.data，一般直接返回response
					resolve(response.data);
				})
				.catch(function(error) {
					reject(error);
				});
		});
	},
	post: function(path = "", data = {}) {
		return new Promise(function(resolve, reject) {
			Axios.post(path, Qs.stringify(data))
				.then(function(response) {
					resolve(response.data);
				})
				.catch(function(error) {
					reject(error);
				});
		});
	}
};
