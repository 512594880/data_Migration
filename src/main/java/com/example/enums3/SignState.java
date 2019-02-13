package com.example.enums3;

import com.hitales.commons.enums.tag.EnumTag;
import com.hitales.commons.enums.typeable.Describable;
import com.hitales.commons.enums.typeable.IntEnum;

/**
 * 签约状态
 *
 * @author harryhe
 */
@EnumTag(key = "sign_state", name = "签约状态")
public enum SignState implements IntEnum, Describable {

    /**
     * 未签约
     */
    UNSIGNED(0, "未签约"),
    /**
     * 已签约
     */
    SIGNED(1, "已签约"),
//    /**
//     * 已解约
//     */
//    TERMINATED(2, "已解约"),

    ;
    private Integer key;
    private String desc;

    SignState(Integer key, String desc) {
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