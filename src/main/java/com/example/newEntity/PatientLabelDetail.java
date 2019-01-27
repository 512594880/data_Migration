package com.example.newEntity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: jinweiwei
 * @date: 2018/6/21
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Data
@Entity
public class PatientLabelDetail implements Serializable{
    private static final long serialVersionUID = 7265144512172589756L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;

    private Long taskLabelId;

    private Date createdDate;


}