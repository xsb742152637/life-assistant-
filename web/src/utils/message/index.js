import { Message, Loading } from "element-ui";
var msgDuration = 2500;
var loadEntity;
export default {
	success(text) {
		Message({
			showClose: true,
			type: "success",
			duration: msgDuration,
			dangerouslyUseHTMLString: true,
			message: text
		});
	},
	warning(text) {
		Message({
			showClose: true,
			type: "warning",
			duration: msgDuration,
			dangerouslyUseHTMLString: true,
			message: text
		});
	},
	error(text) {
		Message({
			showClose: true,
			type: "error",
			duration: msgDuration,
			dangerouslyUseHTMLString: true,
			message: text
		});
	},
	queryLoadOpen() {
		this.loadOpen("正在加载");
	},
	deleteLoadOpen() {
		this.loadOpen("正在删除");
	},
	loadOpen(text) {
		loadEntity = Loading.service({ text: text + "……", fullscreen: false });
	},
	loadClose(rs) {
		if (loadEntity) {
			loadEntity.close();
		}
		if (rs) {
			if (rs.error == 0) {
				this.success(rs.msg);
			} else if (rs.error == 1) {
				this.error(rs.msg);
			} else if (rs.message) {
				this.warning(rs.message);
			} else {
				this.warning(JSON.stringify(rs));
			}
		}
	}
};
