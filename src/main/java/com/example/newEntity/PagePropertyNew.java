package com.example.newEntity;/**
 * Created by jackmcgrady on 2018/9/11.
 */

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
@Table(name="page_property")
public class PagePropertyNew {
    private static final long serialVersionUID = 2171661971687207491L;
    @Id
    private Long id;

    private String name;

    private Date createdDate;

    private Date updatedDate;

    private Integer delFlag;

    private Long tId;

    private Long pId;

    private Long createUserId;

    private Long updateUserId;

}
