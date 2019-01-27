package com.example.enums3;

import com.hitales.commons.enums.tag.EnumTag;
import com.hitales.commons.enums.typeable.Describable;
import com.hitales.commons.enums.typeable.IntEnum;

/**
 * 医生账户可用性状态
 * 
 * @author harryhe
 *
 */
@EnumTag(key="doctor_account_state",name="医生账户可用性状态")
public enum DoctorAccountState implements IntEnum, Describable {
	notUse(0, ""),
	/**
	 * 活跃
	 */
	AVAILABLE(1, "活跃中"),
	/**
	 * 锁定（临时停用）
	 */
	LOCKED(2, "锁定中"),
	/**
	 * 账户（永久停用）
	 */
	CANCELLED(3, "已注销"),


	;
	private Integer key;
	private String desc;

	private DoctorAccountState(Integer key, String desc) {
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