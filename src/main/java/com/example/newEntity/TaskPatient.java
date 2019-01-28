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
 * @date: 2018/6/15
 * Time: 11:46
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Entity
@Data
public class TaskPatient implements Serializable{
    private static final long serialVersionUID = -8833106001252638664L;
    @Id
    private Long id;

    private Long taskId;

    private Long patientId;

    private Integer status;

    private Date createdDate;

    private Long templateId;

    private Date updatedDate;



}