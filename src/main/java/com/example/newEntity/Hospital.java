package com.example.newEntity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class Hospital {
    @Id
    private Long id;

    private String hospitalName;

    private Long parentId;

    private String areaCode;

    private String desc;

    private String photo;

    private String address;

    private Long tId;

    private Date createdDate;

    private Date updatedDate;


}