package com.example.entity;/**
 * Created by jackmcgrady on 2018/9/11.
 */


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: jiangxu
 * @date: 2018/9/11
 * Time: 上午10:47
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Entity
@Data
public class SmallQuestion{
    private static final long serialVersionUID = 2171661971687207491L;
    @Id
    private Long id;

    private String name;

    private Long bigQuestionId;

    private Date createdDate;

    private Date updatedDate;

    private Integer delFlag;

    private Integer type;

    private String prefix;

    private String postfix;

    private String placeholder;

    private String option;

    private Long tId;

    private Integer sequence;

    /**
     * '是否可修改',true- 可以修改 false-不可以修改
     */
    private Boolean isEdit;

    /**
     * '是否填写默认值',true- 自动填入默认值 false-
     */
    private Boolean isDefault;

    /**
     * 下拉列表的组ID
     */
    private String groupId;

    /**
     * 默认值 绑定字段
     */
    private String defaultValue;

    private String text;

    /**
     * 题目唯一标识, 1身高，2体重，3BMI，4舒张压，5收缩压
     */
    private Integer unique;

    /**
     * 验证方式
     */
    private String validate;


}


