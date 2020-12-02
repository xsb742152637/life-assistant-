// 导入axios
import Axios from "axios";
import Qs from "qs";

// 进行一些全局配置
// 公共路由(网络请求地址)
Axios.defaults.baseURL = "/api";
// 请求响应超时时间 20秒
Axios.defaults.timeout = 20 * 1000;

// 封装自己的get/post方法
export default {
	get: function(path = "", data = {}, config = {}) {
		return new Promise(function(resolve, reject) {
			Axios.get(path, {params: data}, config)
				.then(function(response) {
					resolve(response.data);
				})
				.catch(function(error) {
					reject(error);
				});
		});
	},
	post: function(path = "", data = {}, config = {}) {
		return new Promise(function(resolve, reject) {
			Axios.post(path, Qs.stringify(data), config)
				.then(function(response) {
					resolve(response.data);
				})
				.catch(function(error) {
					reject(error);
				});
		});
	}
};
