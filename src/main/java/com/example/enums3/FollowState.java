package com.example.enums3;

import com.hitales.commons.enums.tag.EnumTag;
import com.hitales.commons.enums.typeable.Describable;
import com.hitales.commons.enums.typeable.IntEnum;

/**
 * 居民访问跟踪状态(失访状态）
 *
 * @author harryhe
 */
@EnumTag(key = "follow_state", name = "居民访问跟踪状态(失访状态）")
public enum FollowState implements IntEnum, Describable {


    /**
     * 正常
     */
    FOLLOW(1, "正常"),
    /**
     * 失访
     */
    MISS(2, "失访"),

    ;
    private Integer key;
    private String desc;

    private FollowState(Integer key, String desc) {
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
