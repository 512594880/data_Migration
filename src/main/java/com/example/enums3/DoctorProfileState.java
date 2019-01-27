package com.example.enums3;

import com.hitales.commons.enums.tag.EnumTag;
import com.hitales.commons.enums.typeable.Describable;
import com.hitales.commons.enums.typeable.IntEnum;

/**
 * 医生资料完善状态
 * 
 * @author harryhe
 *
 */
@EnumTag(key="doctor_profile_state",name="医生账户(资料）完善状态")
public enum DoctorProfileState implements IntEnum, Describable {
	notUse(0, ""),
	/**
	 * 未完善
	 */
	INCOMPLETE(1, "未完善"),
	/**
	 * 已完善
	 */
	COMPLETED(2, "已完善"),
	;
	private Integer key;
	private String desc;

	private DoctorProfileState(Integer key, String desc) {
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