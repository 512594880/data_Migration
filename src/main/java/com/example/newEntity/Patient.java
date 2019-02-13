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
 * @date: 2018/6/13
 * Time: 20:47
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Entity
@Data
public class Patient implements Serializable{
    private static final long serialVersionUID = 6025023052605699056L;
    @Id
    private Long id;

    private String name;

    private Integer sex;

    private String telephone;

    private Date birthday;

    private String cardNumber;

    private String homeAddress;

    private Date createdDate;

    private Date updatedDate;

    private Integer age;

    private String area;

    private Long tId;

    private String poorCardNumber;

    private String msAreaCode;

    private String ehrNumber;

    private String causeOfPovert;


}