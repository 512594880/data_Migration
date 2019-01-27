package com.example.newEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: jinweiwei
 * @date: 2018/6/11 Time: 11:31 To change this template use File | Settings |
 *        File Templates. Description:
 */
@Entity
@Data
public class Doctor implements Serializable {

	private static final long serialVersionUID = 3160852872435692374L;
	@Id
	private Long id;

	private String photo;

	private String name;

	private String hospitalName;

	private String password;

	private String userName;

	private String doc_type;

	private Date createdDate;

	private Date updatedDate;

	private String cardNumber;

	private BigDecimal star;

	private Long tId;

	// 账号ID
	private Long aId;

	private Integer level;

	private String msOrgId;


}