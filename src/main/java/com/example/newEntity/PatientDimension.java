package com.example.newEntity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class PatientDimension {
    @Id
    private Long id;

    private Integer older;

    private Integer psychosis;

    private Integer diabetes;

    private Integer hypertension;

    private Integer poor;

    private String viliageCode;

    private Long tId;

    private Long patientId;

    private Integer delFlag;

    private String clinic;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOlder() {
        return older;
    }

    public void setOlder(Integer older) {
        this.older = older;
    }

    public Integer getPsychosis() {
        return psychosis;
    }

    public void setPsychosis(Integer psychosis) {
        this.psychosis = psychosis;
    }

    public Integer getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(Integer diabetes) {
        this.diabetes = diabetes;
    }

    public Integer getHypertension() {
        return hypertension;
    }

    public void setHypertension(Integer hypertension) {
        this.hypertension = hypertension;
    }

    public String getViliageCode() {
        return viliageCode;
    }

    public void setViliageCode(String viliageCode) {
        this.viliageCode = viliageCode == null ? null : viliageCode.trim();
    }

    public Long gettId() {
        return tId;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getPoor() {
        return poor;
    }

    public void setPoor(Integer poor) {
        this.poor = poor;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }
}