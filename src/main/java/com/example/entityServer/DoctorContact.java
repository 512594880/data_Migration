package com.example.entityServer;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 医生-反馈信息
 * @Author: jingang
 * @Date: 2019/1/7 16:02
 * @Description:
 */

@Entity
@org.hibernate.annotations.Table(appliesTo = "doctor_contact", comment = "医生APP-联系我们")
@Table(name = "doctor_contact")
@Data
@EqualsAndHashCode(callSuper=false)
public class DoctorContact extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20) unsigned  comment '系统ID'")
    private Long id;

    @Column(name = "doctor_id", columnDefinition = "bigint(20)  DEFAULT 0 COMMENT '医生ID'")
    private Long doctorId;

    @NotBlank(message="手机号不能为空")
    @Column(name = "phone", columnDefinition = "varchar(32)  default '' comment '联系电话'")
    private String phone;

    @NotBlank(message="留言内容不能为空")
    @Size(min=1,max=500,message ="留言内容长度应在1-500个字符之间")
    @Column(name = "content", columnDefinition = "varchar(500)  default '' comment '留言内容'")
    private String content;

}
