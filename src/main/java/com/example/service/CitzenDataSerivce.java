package com.example.service;

import antlr.StringUtils;
import com.example.Util.ExcelUtil;
import com.example.entityServer.Citizen;
import com.example.enums3.*;
import com.example.nationalServerRepository.CitizenRepository;
import com.example.newEntity.Patient;
import com.example.newEntity.PatientDimension;
import com.example.newEntity.PatientLabelDetail;
import com.example.newEntity.TaskLabel;
import com.example.newRepository.PatientDimensionRepository;
import com.example.newRepository.PatientLabelDetailRepository;
import com.example.newRepository.PatientRepository;
import com.example.newRepository.TaskLabelRepository;
import com.hitales.commons.enums.YesNo;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CitzenDataSerivce {

    @Autowired
    protected CitizenRepository citizenRepository;

    @Autowired
    protected PatientRepository patientRepository;

    @Autowired
    protected PatientLabelDetailRepository patientLabelDetailRepository;

    @Autowired
    protected PatientDimensionRepository patientDimensionRepository;

    @Autowired
    protected TaskLabelRepository taskLabelRepository;

    /**
     * 2.0To3.0Id对应map
     */
    private static Map<Long,Long> citizenIdMap = new HashMap<>();

    public void CitzenDataMigaration(){
        XSSFWorkbook errorPatientInfoXssfWorkbook = new XSSFWorkbook();
        XSSFSheet errorPatientInfoXssfSheet = errorPatientInfoXssfWorkbook.getSheet("居民必填信息错误");
        String [] heading = new String[]{"证件号码","证件类型","姓名","location"};
        ExcelUtil.setHeading(errorPatientInfoXssfSheet,heading);

        LocalDate today = LocalDate.now();
        List<Patient> patients = patientRepository.findAll();
        patients.forEach(patient -> {
            Citizen citizen = new Citizen();
            //ID自增
//            citizen.setId(patient.getId());
            citizen.setCreateTime(patient.getCreatedDate());
            citizen.setUpdateTime(patient.getUpdatedDate());
            citizen.setAddress(patient.getHomeAddress());
            citizen.setAuditState(CitizenAuditState.APPROVED);
            citizen.setBirthday(patient.getBirthday());
            //ABO、RH血型，联系人电话、联系人姓名默认 不作处理
            //获取年龄
            LocalDate citizenDate = LocalDateTime.ofInstant(patient.getBirthday().toInstant(), ZoneId.systemDefault()).toLocalDate();
            long age = ChronoUnit.YEARS.between(citizenDate, today);
            citizen.setCrowdChild(age <=6?YesNo.YES:YesNo.NO);
            List<PatientLabelDetail> patientLabelDetails = patientLabelDetailRepository.findByPatientId(patient.getId());
            List<Long> taskLabelIds = patientLabelDetails.stream().map(PatientLabelDetail::getTaskLabelId).collect(Collectors.toList());
            List<TaskLabel> taskLabels = taskLabelRepository.findByIdIn(taskLabelIds);
            List<String> taskLabelNames = taskLabels.stream().map(TaskLabel::getName).collect(Collectors.toList());
            boolean 糖尿病 = taskLabelNames.stream().anyMatch(s -> s.contains("糖尿病") || s.contains("糖并高"));
            citizen.setCrowdDiabetes2(糖尿病?YesNo.YES:YesNo.NO);
            //2.0patient疾病字段为空 是否残疾人、是否传染病、是否孕产妇、是否肺结核、残疾情况、文化程度暂不处理
            citizen.setCrowdElder(age >=65?YesNo.YES:YesNo.NO);
            boolean 高血压 = taskLabelNames.stream().anyMatch(s -> s.contains("高血压") || s.contains("糖并高"));
            citizen.setCrowdHypertension(高血压?YesNo.YES:YesNo.NO);
            boolean 精神病 = taskLabelNames.stream().anyMatch(s -> s.contains("精神病"));
            citizen.setCrowdMentalDisorder(精神病?YesNo.YES:YesNo.NO);
            List<PatientDimension> patientDimensions = patientDimensionRepository.findByPatientId(patient.getId());
            if (patientDimensions.size() == 1){
                if (patientDimensions.get(0).getPoor() == 0){
                    citizen.setCrowdPoor(YesNo.NO);
                }else if (patientDimensions.get(0).getPoor() == 1){
                    citizen.setCrowdPoor(YesNo.YES);
                }
                //TODO location 对应sys_gb2260编码
//                citizen.setLocation(patientDimensions.get(0).getViliageCode());
            }
            citizen.setFollowState(FollowState.FOLLOW);
            if (patient.getSex() == 1){
                citizen.setGender(CitizenGender.FEMALE);
            }else if (patient.getSex() == 2){
                citizen.setGender(CitizenGender.MALE);
            }else {
                citizen.setGender(CitizenGender.UNKNOWN);
            }
            citizen.setIdName(patient.getName());
            citizen.setIdNo(patient.getCardNumber());
            citizen.setIdState(IdState.AVAILABLE);
            if (patient.getCardNumber() == null || "".equals(patient.getCardNumber())){
                citizen.setIdType(IdType.ID);
            }
            //婚姻状况待定
            //医疗费用支付方式为默认值
            //民族为默认
            //职业分类为默认
            citizen.setPhone(patient.getTelephone());
            citizen.setResidentType(ResidentType.REGISTERED);

            if (ObjectUtils.isEmpty(citizen.getIdNo()) || ObjectUtils.isEmpty(citizen.getIdType()) || ObjectUtils.isEmpty(citizen.getIdName()) ||
                    ObjectUtils.isEmpty(citizen.getLocation())){
                addErrorExcel(citizen,errorPatientInfoXssfSheet);
                return;
            }


            citizenRepository.save(citizen);
            citizenIdMap.put(patient.getId(),citizenRepository.findMaxId());
        });
        ExcelUtil.writeExcel(errorPatientInfoXssfWorkbook,"居民领域居民错误信息表");
    }

    private void addErrorExcel(Citizen citizen, XSSFSheet errorPatientInfoXssfSheet) {
        Row row = errorPatientInfoXssfSheet.createRow(errorPatientInfoXssfSheet.getLastRowNum()+1);
        row.createCell(0).setCellValue(citizen.getIdNo());
        row.createCell(1).setCellValue(citizen.getIdType() == null?"":citizen.getIdType().getDesc());
        row.createCell(2).setCellValue(citizen.getIdName());
        row.createCell(3).setCellValue(citizen.getLocation());
    }



}
