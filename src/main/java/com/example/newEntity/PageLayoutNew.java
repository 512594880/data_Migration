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
@Data
@Entity
@Table(name="page_layout")
public class PageLayoutNew {
    private static final long serialVersionUID = 2171661971687207491L;

    @Id
    private Long id;

    private String title;

    @Column(name = "[desc]")
    private String desc;

    private Date createdDate;

    private Date updatedDate;

    private Integer delFlag;

    private Long gaugeTemplateId;

    private Long tId;

    private Integer sequence;

}
