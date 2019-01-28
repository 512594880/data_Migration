package com.example.newEntity;

import com.example.entity.GaugeTemplate;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 *
 * @author: jinweiwei
 * @date: 2018/6/15
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Entity
@Data
public class Task implements Serializable{
    private static final long serialVersionUID = 8779573625150835919L;
    @Id
    private Long id;

    private String name;

    private Long templateId;

    private String areaCode;

    private Date beginDate;

    private Date endDate;

    private Integer delFlag;

    private Date createdDate;

    private Date updatedDate;

    private Long createUserId;

    private Integer taskType;

    private Integer source;

    private Integer cycleFlag;

    private Integer cycleUnit;

    private Integer frequency;

    private Long parentTaskId;

    private Integer gaugeCalcType;

    private Integer complete;

    private Integer unComplete;

    private Long taskLabelId;

    private Long updateUserId;

    private Integer aiInquisitionFlag;

    private Integer price;

    private Long serviceId;

    private String taskCardId;

    private Integer verifyType;


}