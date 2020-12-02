/**
 * 工具类
 */
export default {
	formatDate(date, fmt) { // 日期格式化
		if(!date){
			return "";
		}
		date = new Date(date);
		if (/(y+)/.test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
		}
		let o = {
			'M+': date.getMonth() + 1,
			'd+': date.getDate(),
			'H+': date.getHours(),
			'm+': date.getMinutes(),
			's+': date.getSeconds()
		};
		for (let k in o) {
			if (new RegExp(`(${k})`).test(fmt)) {
				let str = o[k] + '';
				fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? str : this.padLeftZero(str));
			}
		}
		return fmt;
	},
	padLeftZero(str) {
		return ('00' + str).substr(str.length);
	}
}