package com.example.newEntity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: jiangxu
 * @date: 2018/9/3
 * Time: 13:49
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Data
@Entity
@Table(name="gauge_script")
public class GaugeScriptNew implements Serializable{
    private static final long serialVersionUID = 2171661971687207491L;
    @Id
    private Long id;

    private String scriptName;

    private String description;

    private String content;

    private Date createdDate;

    private Date updatedDate;

    private int delFlag;

    private Long createUserId;

    private String fileUrl;

    private Long updateUserId;

}