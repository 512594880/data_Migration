package com.example.enums3;

import com.hitales.commons.enums.tag.EnumTag;
import com.hitales.commons.enums.typeable.Describable;
import com.hitales.commons.enums.typeable.IntEnum;

/**
 * 居民证件类型
 * 
 * @author harryhe
 *
 */
@EnumTag(key="citizen_id_type",name="居民证件类型")
public enum CitizenIdType implements IntEnum, Describable {
	/**
	 * 身份证
	 */
	ID(1, "身份证"),
	/**
	 * 出生证明
	 */
	BIRTH(2, "出生证明"),
//	/**
//	 * 贫困人口识别卡号
//	 */
//	POOR(3, "贫困人口识别卡号"),

	;
	private Integer key;
	private String desc;

	private CitizenIdType(Integer key, String desc) {
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