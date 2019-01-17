package com.example.entity;/**
 * Created by jackmcgrady on 2018/9/11.
 */

import lombok.Data;

import javax.persistence.Entity;
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
public class PageLayoutBigQuestion {
    private static final long serialVersionUID = 2171661971687207491L;

    private Long id;

    private Long pageLayoutId;

    private Long bigQuestionId;

    private Date createdDate;

    private Date updatedDate;

    private Integer delFlag;

    private Integer sequence;

}
