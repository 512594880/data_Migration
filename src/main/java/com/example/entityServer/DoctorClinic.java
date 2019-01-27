package com.example.entityServer;


import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * 医疗机构<br/>
 * <br/>
 * parentId(上级机构ID) = 0 为顶层节点
 *
 * @author harryhe
 */
@Entity
@org.hibernate.annotations.Table(appliesTo = "doctor_clinic", comment = "医疗机构表")
@Table(name = "doctor_clinic", indexes = {//
        @Index(columnList = "clinic_id")//
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class DoctorClinic extends AuditableEntity {

    /**
     * 顶层节点 ClinicId
     */
    public static final Integer ROOT_PARENT = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20) unsigned comment '系统ID'")
    private Long id;

    @Column(name = "clinic_id", columnDefinition = "int(10) unsigned default 0 comment '机构ID'")
    private Integer clinicId;

    @Column(name = "[name]", columnDefinition = "varchar(20) default '' comment '机构名称'")
    private String name;

    @Column(name = "parent_id", columnDefinition = "int(10) unsigned default 0 comment '上级机构ID'")
    private Integer parentId;

//    @Column(name = "[scope]")
//    private List<String> scope;

    @Column(name = "depth", columnDefinition = "tinyint unsigned default 0 comment '节点深度，根为0'")
    private Integer depth;

    @Column(name = "child_size", columnDefinition = "tinyint unsigned default 0 comment '直接下集节点数量'")
    private Integer childSize;
}
