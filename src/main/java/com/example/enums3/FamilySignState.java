package com.example.enums3;

import com.hitales.commons.enums.tag.EnumTag;
import com.hitales.commons.enums.typeable.Describable;
import com.hitales.commons.enums.typeable.IntEnum;

/**
 * 家庭签约状态
 *
 * @author harryhe
 */
@EnumTag(key = "family_sign_state", name = "家庭签约状态")
public enum FamilySignState implements IntEnum, Describable {
    /**
     * 全家人都被签约
     */
    WHOLE(1, "全家签约"),
    /**
     * 全家里部分人被签约
     */
    PARTIALLY(2, "部分签约"),
    /**
     * 未签约
     */
    UNSIGN(3, "未签约"),
    ;
    private Integer key;
    private String desc;

    private FamilySignState(Integer key, String desc) {
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


    /*
     * 是否全家签约
     */
    public boolean isWhole() {
        return WHOLE.equals(this);
    }
}