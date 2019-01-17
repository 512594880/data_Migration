package com.example.entity;

import lombok.Data;

import javax.persistence.Entity;
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
public class GaugeTemplate implements Serializable{
    private static final long serialVersionUID = 2171661971687207491L;

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