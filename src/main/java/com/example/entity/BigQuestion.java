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
public class BigQuestion {
    private static final long serialVersionUID = 2171661971687207491L;
    @Id
    private Long id;

    private String name;

    private Date createdDate;

    private Date updatedDate;

    private Integer delFlag;

    private Integer rule;

    private Long tId;

    private Long createUserId;

    private Long updateUserId;


}
