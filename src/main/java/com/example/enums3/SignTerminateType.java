package com.example.enums3;

import com.hitales.commons.enums.tag.EnumTag;
import com.hitales.commons.enums.typeable.Describable;
import com.hitales.commons.enums.typeable.IntEnum;

/**
 * 签约解除类型
 * 
 * @author harryhe
 *
 */
@EnumTag(key = "sign_terminate_type", name = " 签约解除类型")
public enum SignTerminateType implements IntEnum, Describable {
	/**
	 * 到期：已过签约时间自然到期
	 */
	EXPIRED(1, "到期"),
	/**
	 * 解约：由医生、居民或系统提前解约
	 */
	TERMINATED(2, "解约"),

	;
	private Integer key;
	private String desc;

	private SignTerminateType(Integer key, String desc) {
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