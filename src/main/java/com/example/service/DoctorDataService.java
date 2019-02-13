package com.example.service;

import com.example.Util.CardNumberUtil;
import com.example.Util.ExcelUtil;
import com.example.entityServer.DoctorClinic;
import com.example.entityServer.DoctorContact;
import com.example.entityServer.DoctorServer;
import com.example.enums3.*;
import com.example.nationalServerRepository.DoctorClinicRepository;
import com.example.nationalServerRepository.DoctorContactRepository;
import com.example.nationalServerRepository.DoctorServerRepository;
import com.example.newEntity.*;
import com.example.newRepository.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorDataService {
    @Autowired
    protected HospitalRepository hospitalRepository;

    @Autowired
    protected DoctorContactRepository doctorContactRepository;

    @Autowired
    protected DoctorClinicRepository doctorClinicRepository;

    @Autowired
    protected DoctorRepository doctorRepository;

    @Autowired
    protected DoctorServerRepository doctorServerRepository;

    @Autowired
    protected ContactRepository contactRepository;

    @Autowired
    protected HospitalAreaRepository hospitalAreaRepository;

    @Autowired
    protected DoctorPatientRepository doctorPatientRepository;

    @Autowired
    protected PatientRepository patientRepository;

    /**
     * 手机号正则
     */
    private static String pthoneRegex = "^((\\+?86)|(\\(\\+86\\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$";

    /**
     * 2.0To3.0Id对应map
     */
    private static Map<Long,Long> doctorIdMap = new HashMap<>();

    private static Map<Long,List<String>> doctorsInfo = new HashMap<>();

    private static Map<Long,String> patientInfo = new HashMap<>();

    /**
     * 2.0数据库医生域迁移到3.0
     */
    public void dataMigrationToNationalServer() {
        //保存doctor错误信息,供演练使用
        XSSFWorkbook errorInfo = new XSSFWorkbook();
        XSSFSheet errorDoctorInfo = errorInfo.createSheet("医生错误信息");
        String heading [] = new String[]{"医生ID","医生身份证","医生所在卫生室","医生电话"};
        ExcelUtil.setHeading(errorDoctorInfo,heading);

        //保存contact错误信息 供调试使用 发现contact中有手机号问题
        XSSFSheet contactError = errorInfo.createSheet("contact错误信息");
        String [] contactErrorHeading  = new String[]{"医生Id","电话"};
        ExcelUtil.setHeading(contactError,contactErrorHeading);

        //迁移到doctor_clinic
        toDoctorClinic();

        //迁移到doctor 并保存错误信息
        toDoctor(errorDoctorInfo);

        //Contact迁移
        toContact(contactError);

        ExcelUtil.writeExcel(errorInfo,"医生域迁移错误信息");

    }

    /**
     * 迁移doctor_contact表 并保存错误信息
     * @param contactError
     */
    private void toContact(XSSFSheet contactError) {
        List<Contact> contacts = contactRepository.findByUserId();
        contacts.forEach(contact -> {
            DoctorContact doctorContact = new DoctorContact();
            if (!contact.getPhone().matches(pthoneRegex)){
                Row row = contactError.createRow(contactError.getLastRowNum()+1);
                row.createCell(0).setCellValue(contact.getUserId());
                row.createCell(1).setCellValue(contact.getPhone());
                return;
            }
            doctorContact.setPhone(contact.getPhone());
            doctorContact.setDoctorId(doctorIdMap.get(Long.valueOf(contact.getUserId())));
            doctorContact.setContent(contact.getContent());
            doctorContact.setCreateTime(contact.getCreatedDate());
            doctorContact.setUpdateTime(contact.getCreatedDate());
            doctorContactRepository.save(doctorContact);
        });
    }


    /**
     * 迁移数据到doctor_clinic
     */
    private void toDoctorClinic() {
        //TODO DoctorClinic关联 辖区待处理
        List<Hospital> hospitals = hospitalRepository.findByParentId(null);


        hospitals.forEach(hospital -> {
            DoctorClinic doctorClinic = new DoctorClinic();
            doctorClinic.setCreateTime(hospital.getCreatedDate());
            doctorClinic.setUpdateTime(hospital.getUpdatedDate());
            List<Hospital> hospitalsChildren = hospitalRepository.findByParentId(hospital.getId());
            doctorClinic.setChildSize(hospitalsChildren.size());
            doctorClinic.setDepth(0);
            doctorClinic.setName(hospital.getHospitalName());
            //todo 是否需要Scope
            doctorClinicRepository.save(doctorClinic);
            Long id = doctorClinicRepository.findMaxId();
            //获取卫生室
            for (Hospital hospitalChildren : hospitalsChildren) {
                saveClinic(hospitalChildren, id);
            }

        });
    }

    /**
     * 保存卫生院
     */
    private void saveClinic(Hospital hospital, Long parentId){
        DoctorClinic doctorClinic = new DoctorClinic();
        doctorClinic.setCreateTime(hospital.getCreatedDate());
        doctorClinic.setUpdateTime(hospital.getUpdatedDate());
        List<HospitalArea> hospitalAreas = hospitalAreaRepository.findByHospitalId(hospital.getId());
        doctorClinic.setChildSize(hospitalAreas.size());
        doctorClinic.setDepth(1);
        doctorClinic.setParentId(Math.toIntExact(parentId));
        doctorClinic.setName(hospital.getHospitalName());
        //todo 增加辖区scope

        doctorClinicRepository.save(doctorClinic);

//        Long Id = doctorClinicRepository.findMaxId();
//        for (int i = 0; i < hospitalAreas.size(); i++) {
//            HospitalArea hospitalArea = hospitalAreas.get(i);
//            saveArar(hospitalArea, Id);
//        }

    }

    /**
     * 保存辖区
     * @param hospitalArea
     */
    private void saveArar(HospitalArea hospitalArea,Long parentId) {
        DoctorClinic doctorClinic = new DoctorClinic();
        doctorClinic.setCreateTime(hospitalArea.getCreatedDate());
        doctorClinic.setUpdateTime(hospitalArea.getCreatedDate());
        doctorClinic.setName(hospitalArea.getAreaId());
        doctorClinic.setDepth(2);
        doctorClinic.setParentId(Math.toIntExact(parentId));
        doctorClinicRepository.save(doctorClinic);
    }

    /**
     * //迁移到doctor 并保存错误信息
     * @param errorDoctorInfo
     */
    private void toDoctor(XSSFSheet errorDoctorInfo) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        List<Doctor> doctors = doctorRepository.findByTId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        doctors.forEach(doctor -> {
            try {
                DoctorServer doctorServer = new DoctorServer();
                //ID采用自增 记录ID

//                doctorServer.setId(doctor.getId());
                doctorServer.setCreateTime(doctor.getCreatedDate());
                doctorServer.setUpdateTime(doctor.getUpdatedDate());
                doctorServer.setIdName(doctor.getName());
                doctorServer.setIdNo(doctor.getCardNumber());
                doctorServer.setAvatar(doctor.getPhoto());
                doctorServer.setAccountState(DoctorAccountState.AVAILABLE);

                Map<String, String> cardInfoMap = CardNumberUtil.getBirAgeSex(doctor.getCardNumber());
                doctorServer.setBirthday(sdf.parse(cardInfoMap.get("birthday")));
                String gender = cardInfoMap.get("sexCode");
                doctorServer.setGender(gender.equals("M")? DoctorGender.MALE : DoctorGender.FEMALE);
                doctorServer.setClinicId(Math.toIntExact(doctorClinicRepository.findByName(doctor.getHospitalName()).getId()));
                doctorServer.setPhone(doctor.getUserName());
                doctorServer.setProfileState(DoctorProfileState.INCOMPLETE);
                doctorServer.setTitle(DoctorTitle.其他);
                doctorServer.setPassword(bCryptPasswordEncoder.encode(doctor.getPassword()));
                doctorServer.setAuditState(DoctorAuditState.APPROVED);
                doctorServer.setDepartment(doctor.getHospitalName());
                doctorServer.setStar(doctor.getStar().doubleValue());
                doctorServerRepository.save(doctorServer);
                doctorIdMap.put(doctor.getId(),doctorServerRepository.findMaxId());
            }catch (Exception e){
                e.printStackTrace();
                Row row = errorDoctorInfo.createRow(errorDoctorInfo.getLastRowNum()+1);
                row.createCell(0).setCellValue(doctor.getId().toString());
                row.createCell(1).setCellValue(doctor.getCardNumber());
                row.createCell(2).setCellValue(doctor.getHospitalName());
                row.createCell(3).setCellValue(doctor.getUserName());
            }
        });
    }

    /**
     * 获取2.0错误的医患关系表
     */
    public void getErrorDoctorAndPatientShip(){


        List<DoctorPatient> list = doctorPatientRepository.findAll();
        Map<Long,List<Long>> relationShipMapByDoctorPatient = new HashMap<>();
        list.forEach(doctorPatient -> {
            relationShipMapByDoctorPatient.computeIfAbsent(doctorPatient.getDoctorId(),
                    v-> new ArrayList<>()).add(doctorPatient.getPatientId());
        });
        System.out.println(relationShipMapByDoctorPatient.entrySet().stream().map(longListEntry -> longListEntry.getValue().size()).reduce(Integer::sum));
        List<Doctor> doctors = doctorRepository.findAll();
        Map<Long,List<Long>> relationShipMap =doctors.stream().collect(Collectors.toMap(Doctor::getId,this::findPatientIdByArea));

        List<Patient> patients = patientRepository.findAll();
        patients.forEach(patient -> patientInfo.put(patient.getId(),patient.getArea()));
        String heading [] = new String[]{"医生Id","医生所属卫生室","卫生室所在地区","居民Id","居民所在地区"};
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet xssfSheet = xssfWorkbook.createSheet("以医患关系表为基准");
        Row headingRow = xssfSheet.createRow(0);
        Arrays.stream(heading).forEach(s -> headingRow.createCell(headingRow.getLastCellNum() == -1?0:headingRow.getLastCellNum()).setCellValue(s));

        //居民地区和卫生室所在地区对应
        Map<String,List<Long>> areaAndHospitalArea = new HashMap<>();

//		Map<String,List<String>> areaAndHospitalAreaNotRalation = new IdentityHashMap<>();
        mapDifferentToRow(relationShipMapByDoctorPatient, relationShipMap, xssfSheet,true,areaAndHospitalArea);

        XSSFSheet xssfSheetByArea = xssfWorkbook.createSheet("以地区关联关系为基准");
        String xssfSheetByAreaHeading [] = new String[]{"医生Id","医生所属卫生室","卫生室所在地区","居民Id","居民所在地区"};
        ExcelUtil.setHeading(xssfSheetByArea,xssfSheetByAreaHeading);
        mapDifferentToRow(relationShipMap, relationShipMapByDoctorPatient, xssfSheetByArea,false,areaAndHospitalArea);


        //以地区关系为基准的错误信息 疑似卫生管辖区有误
        //存放卫生和管辖的地区的map
        //卫生室管辖地区可能有误工作薄
        maybeError(xssfSheetByArea,xssfWorkbook);

        //卫生室和地区对应关系
        hospitalAreaRalation(xssfWorkbook,relationShipMapByDoctorPatient);

        //居民地区和对应的卫生室地区
        patientAreaAndHospitalArea(xssfWorkbook,areaAndHospitalArea);

        ExcelUtil.writeExcel(xssfWorkbook,"居民医生关系差异统计");

    }

    private void patientAreaAndHospitalArea(XSSFWorkbook xssfWorkbook, Map<String, List<Long>> areaAndHospitalArea) {
        XSSFSheet xssfSheet = xssfWorkbook.createSheet("居民地区对应卫生室地区");
        String heading [] = new String[]{"居民所在地区","对应卫生室所在地区","居民id列表"};
        ExcelUtil.setHeading(xssfSheet,heading);
        areaAndHospitalArea.forEach((k,v)->{
            Row row = xssfSheet.createRow(xssfSheet.getLastRowNum()+1);
            row.createCell(0).setCellValue(k.split("，")[0]);
            row.createCell(1).setCellValue(k.split("，")[1]);
            row.createCell(2).setCellValue(v.stream().map(Object::toString).collect(Collectors.joining("，")));
        });
    }

    private void hospitalAreaRalation(XSSFWorkbook xssfWorkbook, Map<Long, List<Long>> relationShipMapByDoctorPatient) {
        XSSFSheet hospitalAreaRalationSheet = xssfWorkbook.createSheet("卫生室和地区对应");
        String heading []= new String[]{"卫生室","卫生室管辖地区"};
        ExcelUtil.setHeading(hospitalAreaRalationSheet,heading);
        Map<String,String> hospitalAreaRalation = new HashMap<>();
        relationShipMapByDoctorPatient.forEach((k,v)->{
            if (doctorsInfo.get(k) != null){
                hospitalAreaRalation.put(doctorsInfo.get(k).get(0),doctorsInfo.get(k).size()>1?doctorsInfo.get(k).get(1):"");
            }
        });

        hospitalAreaRalation.forEach((k,v)->{
            Row row = hospitalAreaRalationSheet.createRow(hospitalAreaRalationSheet.getLastRowNum()+1);
            row.createCell(0).setCellValue(k);
            row.createCell(1).setCellValue(v);
        });
    }

    /**
     * //以地区关系为基准的错误信息 疑似卫生管辖区有误
     * @param xssfSheetByArea
     * @param xssfWorkbook
     */
    private void maybeError(XSSFSheet xssfSheetByArea, XSSFWorkbook xssfWorkbook) {
        XSSFSheet xssfSheet = xssfWorkbook.createSheet("卫生室管辖地区可能有误");
        String heading [] = new String[]{"医生所属卫生室","卫生室所在地区"};
        ExcelUtil.setHeading(xssfSheet,heading);
        Map<String,String> clinicAndArea = new HashMap<>();
        for (int i = 1; i <= xssfSheetByArea.getLastRowNum(); i++) {
            Row row = xssfSheetByArea.getRow(i);
            String hosipital = row.getCell(1).getStringCellValue();
            String area = row.getCell(2).getStringCellValue();
            clinicAndArea.computeIfAbsent(hosipital,y->area);
        }
        clinicAndArea.forEach((k,v)->{
            Row row = xssfSheet.createRow(xssfSheet.getLastRowNum()+1);
            row.createCell(0).setCellValue(k);
            row.createCell(1).setCellValue(v);
        });
    }



    private void mapDifferentToRow(Map<Long, List<Long>> relationShipMapByDoctorPatient, Map<Long, List<Long>> relationShipMap, XSSFSheet xssfSheet, boolean put, Map<String, List<Long>> areaAndHospitalArea) {
        relationShipMapByDoctorPatient.forEach((k,v)->{
//			relationShipMap.compute(k,(s1, strings)->{
//
//				return strings;
//			})

            if(relationShipMap.get(k) == null){
                v.forEach(aLong -> {
                    addRow(k,aLong,xssfSheet,put,areaAndHospitalArea);
                });
            }else {
                List<Long> patientIds = relationShipMap.get(k);
                v.forEach(aLong -> {

                    if (!patientIds.contains(aLong)){
                        addRow(k,aLong,xssfSheet,put,areaAndHospitalArea);
                    }else if (put){
                        areaAndHospitalArea.computeIfAbsent(patientInfo.get(aLong) + "，" + patientInfo.get(aLong),value->new ArrayList<>()).add(aLong);
                    }
                });
            }
        });
    }

    private void addRow(Long doctorId, Long patientId, XSSFSheet xssfSheet, boolean put, Map<String,List<Long>> areaAndHospitalArea){
        Row row = xssfSheet.createRow(xssfSheet.getLastRowNum()+1);
        String doctorHospital = "";
        String doctorHospitalArea = "";
        String patientArea = "";
        if (doctorsInfo.get(doctorId) != null){
            doctorHospital = doctorsInfo.get(doctorId).get(0);
            if (doctorsInfo.get(doctorId).size() > 1){
                doctorHospitalArea = doctorsInfo.get(doctorId).get(1);
            }
        }

        patientArea = patientInfo.get(patientId) == null?"": patientInfo.get(patientId);

        if (put && !"".equals(doctorHospitalArea.trim())){
            areaAndHospitalArea.computeIfAbsent(patientArea + "，" + doctorHospitalArea,v->new ArrayList<>()).add(patientId);
        }


        row.createCell(0).setCellValue(doctorId);
        row.createCell(1).setCellValue(doctorHospital);
        row.createCell(2).setCellValue(doctorHospitalArea);
        row.createCell(3).setCellValue(patientId);
        row.createCell(4).setCellValue(patientArea);
    }

    /**
     * 通过地区查找居民
     * @param doctor
     * @return
     */
    private List<Long> findPatientIdByArea(Doctor doctor) {
        try {
            List<String> doctorInfo = new ArrayList<>();
            doctorInfo.add(doctor.getHospitalName());
            doctorsInfo.put(doctor.getId(),doctorInfo);
            Hospital hospital = hospitalRepository.findByHospitalName(doctor.getHospitalName());
            List<HospitalArea> hospitalAreas = hospitalAreaRepository.findByHospitalId(hospital.getId());
            doctorsInfo.get(doctor.getId()).add(hospitalAreas.stream().map(HospitalArea::getAreaId).collect(Collectors.joining("，")));
            List<Patient> patients = new ArrayList<>();
            hospitalAreas.forEach(hospitalArea -> patients.addAll(patientRepository.findByArea(hospitalArea.getAreaId())));
            return patients.stream().map(Patient::getId).collect(Collectors.toList());
        }catch (NullPointerException e){
            return new ArrayList<>();
        }

    }

}
