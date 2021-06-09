package com.jhxstudio.jarvis.common.utils;


import com.jhxstudio.jarvis.common.enums.RCodeEnum;
import lombok.Data;

/**
 * 返回数据
 *
 * @author jhx
 */
@Data
public class R {

	private String status;

	private String msg;

	private Object data;

	private R() {

	}

	private R(String status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	private R(String status, String msg, Object data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public static R ok() {
		return new R(RCodeEnum.OK.getCode(), RCodeEnum.OK.getMsg());
	}

	public static R ok(Object data) {
		return new R(RCodeEnum.OK.getCode(), RCodeEnum.OK.getMsg(), data);
	}

	public static R error() {
		return new R(RCodeEnum.ERROR.getCode(), RCodeEnum.ERROR.getMsg());
	}


	public static R error(String msg) {
		return new R(RCodeEnum.ERROR.getCode(), msg);
	}

	public static R error(String status, String msg) {
		return new R(status, msg);
	}

	public static R error(RCodeEnum rCodeEnum) {
		return R.error(rCodeEnum.getCode(), rCodeEnum.getMsg());
	}

}
