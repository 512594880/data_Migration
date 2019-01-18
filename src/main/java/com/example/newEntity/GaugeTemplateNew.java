package com.example.newEntity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: jinweiwei
 * @date: 2018/6/21
 * Time: 09:43
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Data
@Entity
@Table(name="gauge_template")
public class GaugeTemplateNew implements Serializable{
    private static final long serialVersionUID = 2171661971687207491L;
    @Id
    private Long id;

    private String templateName;

    private String description;

    private Date createdDate;

    private Date updatedDate;

    private int delFlag;

    private Long createUserId;

    private int gaugeCalcType;

    private String fileUrl;

    private Long updateUserId;

    private String docTemplate;

    private Long parent;

}