package com.example.newEntity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: jiangxu
 * @date: 2018/9/3
 * Time: 13:49
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Data
@Entity
@Table(name="gauge_template_script")
public class GaugeTemplateScriptNew implements Serializable{
    private static final long serialVersionUID = 2171661971687207491L;
    @Id
    private Long id;

    private Long templateId;

    private Long scriptId;

    private String description;

    private Date createdDate;

    private Date updatedDate;

    private int delFlag;

    private Long createUserId;

    private Long updateUserId;


}