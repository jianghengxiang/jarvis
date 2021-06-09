package com.jhxstudio.jarvis.common.exception;

import com.jhxstudio.jarvis.common.enums.RCodeEnum;

/**
 * @author jhx
 * @date 2019/10/18
 **/
public class BizException extends RuntimeException {

	private String code;

	private String msg;

	private Object data;

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public Object getData() {
		return data;
	}

	public BizException(String code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}

	public BizException(String msg) {
		super(msg);
		this.msg = msg;
		this.code = "500";
	}

	public BizException(String msg, Exception e) {
		super(msg);
		this.msg = msg;
		this.code = "500";
		e.printStackTrace();
	}

	public BizException(RCodeEnum rCodeEnum) {
		super(rCodeEnum.getMsg());
		this.code = rCodeEnum.getCode();
		this.msg = rCodeEnum.getMsg();
	}

	public BizException(RCodeEnum rCodeEnum, Object data) {
		this(rCodeEnum);
		this.data = data;
	}
}
