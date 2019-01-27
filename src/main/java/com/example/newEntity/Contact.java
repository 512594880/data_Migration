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
 * @date: 2018/7/17
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Entity
@Data
public class Contact implements Serializable{
    private static final long serialVersionUID = -2140272185035905237L;
    @Id
    private Long id;

    private String content;

    private String phone;

    private String userId;

    private String name;

    private Date createdDate;


}