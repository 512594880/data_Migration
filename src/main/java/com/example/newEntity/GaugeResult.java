package com.example.newEntity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: jinweiwei
 * @date: 2018/6/18
 * Time: 01:52
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Data
@Entity
public class GaugeResult implements Serializable{
    private static final long serialVersionUID = -4535125661480495416L;
    @Id
    private Long id;

    private String resultName;

    private Long templateId;

    private Date createdDate;

    private Date updatedDate;

    private int version;

    private Long createUserId;

    private String expression;

    private Long gtsId;

}