package com.example.enums3;

import com.hitales.commons.enums.typeable.Describable;
import com.hitales.commons.enums.typeable.IntEnum;

/**
 * 医生修改密码类型
 * @Author: jingang
 * @Date: 2019/1/9 15:23
 * @Description:
 */
public enum  DoctorEditPasswordType implements IntEnum, Describable {
    notUse(0, ""),
    /**
     * 忘记密码
     */
    FORGET_PWD(1, "忘记密码"),
    /**
     * 修改密码
     */
    EDIT_PWD(2, "修改密码"),


    ;
    private Integer key;
    private String desc;

    private DoctorEditPasswordType(Integer key, String desc) {
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
