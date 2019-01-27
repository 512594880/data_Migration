package com.example.enums3;

import com.hitales.commons.enums.typeable.Describable;
import com.hitales.commons.enums.typeable.IntEnum;

/**
 * 医生手机号绑定状态
 * 
 * @author harryhe
 *
 */
public enum DoctorPhoneState implements IntEnum, Describable {
	notUse(0, ""),
	/**
	 * 未绑定
	 */
	NOT_BIND(1, "未绑定"),
	/**
	 * 已绑定
	 */
	BINDED(2, "已绑定"),

	;
	private Integer key;
	private String desc;

	private DoctorPhoneState(Integer key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	@Override
	public Integer getKey() {
		return key;
	}

	@Override
	public String getDesc() {
		return desc;
	}

}