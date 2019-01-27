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
 * @date: 2018/6/21
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Data
@Entity
public class TaskLabel implements Serializable{
    private static final long serialVersionUID = -9110984016131365465L;

    @Id
    private Long id;

    private String name;

    private Date createdDate;

    private Long createUserId;

    private String description;

    private Integer type;

    private Long pId;


}