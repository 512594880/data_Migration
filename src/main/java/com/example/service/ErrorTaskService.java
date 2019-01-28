package com.example.service;

import com.example.Util.ExcelUtil;
import com.example.newEntity.*;
import com.example.newRepository.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ErrorTaskService {
    @Autowired
    protected TaskRepository taskRepository;

    @Autowired
    protected TaskPatientRepository taskPatientRepository;

    @Autowired
    protected PatientLabelDetailRepository patientLabelDetailRepository;

    @Autowired
    protected TaskLabelRepository taskLabelRepository;

    @Autowired
    protected PatientRepository patientRepository;

    //台账身份证map
    private static Map<String,List<String>> taizhangCardNo = new HashMap<>();
    //台账档案号map
    private static Map<String,List<String>> taizhangEhrNo = new HashMap<>();

    //健康档案号身份证map
    private static Map<String,List<String>> healthCardNo = new HashMap<>();
    //健康档案号档案号map
    private static Map<String,List<String>> healthEhrNo = new HashMap<>();

    /**
     * 修改数据库中的标签
     * @throws IOException
     */
    public void updatePatientLabel() throws IOException {
        //操作:1.手动删除patient_label_detail表
        //2.读取居民标签统计加卫生室加标签表，插入patient_label_detail表
        patientLabelDetailRepository.deleteAll();
        InputStream inputStreamPatientLabel = new FileInputStream("./居民标签统计加卫生室加标签.xlsx");
        XSSFWorkbook xssfWorkbookPatientLabel = new XSSFWorkbook(inputStreamPatientLabel);
        XSSFSheet xssfSheetPatientLabel = xssfWorkbookPatientLabel.getSheetAt(0);
        List<PatientLabelDetail> patientLabelDetails = new ArrayList<>();
        xssfSheetPatientLabel.forEach(cells->{
            if (cells.getCell(15) != null && !"任务标签".equals(cells.getCell(15).toString())){
                String [] labelStr = cells.getCell(15).toString().split("，");
                Long patientId = Long.valueOf(cells.getCell(0).toString());
                Arrays.stream(labelStr).forEach(label->{
                    TaskLabel taskLabel = taskLabelRepository.findByName(label);
                    PatientLabelDetail patientLabelDetail = new PatientLabelDetail();
                    patientLabelDetail.setPatientId(patientId);

                    if (taskLabel == null){
                        taskLabel = new TaskLabel();
                        taskLabel.setName(label);
                        taskLabel.setDescription(label);
                        taskLabel.setType(1);
                        String pidName = "砚山县" + label.replaceAll(".*(高血压|糖尿病|糖并高|精神病).*","$1") + "人群";
                        taskLabel.setPId(taskLabelRepository.findByName(pidName).getId());
                        taskLabel.setCreatedDate(new Date());
                        taskLabel.setCreateUserId(6L);

                        taskLabel = taskLabelRepository.save(taskLabel);
                        System.out.println("新增标签：" +  label);
                    }

                    patientLabelDetail.setTaskLabelId(taskLabel.getId());
                    patientLabelDetail.setCreatedDate(new Date());
                    patientLabelDetails.add(patientLabelDetail);
                });

            }
        });
        patientLabelDetailRepository.saveAll(patientLabelDetails);
    }



    public void getErrorTask() throws IOException, ParseException {
        saveMapData();

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet result = xssfWorkbook.createSheet("标签决策表");
        String [] headig = new String[]{"居民ID","疾病标签","任务卫生室","居民姓名","居民身份证号","居民档案号","编号","下发方式","是否糖并高","和台账一致性","居民健康档案","置信度","处理方式","任务开始时间","任务结束时间","任务完成时间"};
        ExcelUtil.setHeading(result,headig);
        List<Task> tasks = taskRepository.findAll();
        //特殊处理编号为6的数据
        Map<Long,List<String>> mapSpecaial6 = new HashMap<>();
        //特殊处理编号为6的数据的行号
        Map<Long,List<Integer>> mapSpecaial6Index = new HashMap<>();
        //特殊处理编号为7的数据行号
        Map<Long,List<String>> mapSpecaial7 = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2019-01-01");
        tasks.forEach(task -> {
            if (!task.getName().matches(".*(高血压|糖尿病|糖并高|精神病).*")){
                return;
            }

            List<TaskPatient> taskPatients = taskPatientRepository.findByTaskIdAndCreatedDateBefore(task.getId(),date);
            taskPatients.forEach(taskPatient -> {
                Long patientId = taskPatient.getPatientId();
                try {

                    String 下发方式 = "";
                    Integer No = null;
                    String 是否糖并高 = "";
                    String 和台账一致性 = "";
                    String 居民健康档案 = "";
                    String 置信度 = "";
                    String 处理方式 = "";
                    Set<String> labelStrSetResult = new HashSet<>();

                    Patient patient = patientRepository.findByTIdAndId(3L,patientId).orElseThrow(()->new Exception("居民ID不存在:"+patientId));
//

                    String taskLabelName = task.getName().replace("随访", "");
                    List<PatientLabelDetail> patientLabelDetails = patientLabelDetailRepository.findByPatientId(patientId);
                    String hospital = taskLabelName.replaceAll("(高血压|糖尿病|糖并高|精神病).*", "");
                    if (patientLabelDetails.size() == 0) {
                        下发方式 = "医生创建";
                    } else {
                        List<Long> labelIds = patientLabelDetails.stream().map(PatientLabelDetail::getTaskLabelId).collect(Collectors.toList());
                        List<TaskLabel> taskLabels = taskLabelRepository.findByNameAndIdIn(taskLabelName, labelIds);
                        下发方式 = taskLabels.size() == 0 ? "医生创建" : "系统下发";
                    }
                    String labelStr = taskLabelName.replaceAll(".*(高血压|糖尿病|糖并高|精神病).*", "$1");
                    if (下发方式.equals("系统下发")) {
                        List<String> taizhangLabel = getTaiZhang(patient);
                        if (taizhangLabel.contains(labelStr) && taizhangLabel.size() == 1) {
                            No = 1;
                            和台账一致性 = "是";
                            置信度 = "高";
                        } else if (taizhangLabel.size() > 0) {
                            No = 6;
                            和台账一致性 = "台账有,但是不一致";
                            置信度 = "中";
                            处理方式 = "以台账为准";
                            labelStrSetResult.addAll(taizhangLabel);
                            if (taizhangLabel.contains(labelStr)){
                                labelStrSetResult.remove(labelStr);
                                if (mapSpecaial6.get(patientId)!= null && mapSpecaial6.get(patientId).contains(labelStr)){
                                    int i = mapSpecaial6.get(patientId).indexOf(labelStr);
                                    int index = mapSpecaial6Index.get(patientId).get(i);

                                    Row row = result.getRow(index);
                                    row.getCell(6).setCellValue(1);
                                    row.getCell(9).setCellValue("是");
                                    row.getCell(11).setCellValue("高");
                                    row.getCell(12).setCellValue("");
                                    mapSpecaial6.get(patientId).remove(i);
                                    mapSpecaial6Index.get(patientId).remove(i);
                                    if (mapSpecaial6.get(patientId).size() == 0){
                                        return;
                                    }
                                }else {
                                    saveResult(result, patient, labelStr , hospital, 1, 下发方式, 是否糖并高, "是", 居民健康档案, "高", "", task.getBeginDate(), task.getEndDate(), taskPatient.getUpdatedDate(), taskPatient.getStatus());
                                    List<Integer> index = new ArrayList<>();
                                    for (int i = result.getLastRowNum()+1; i < result.getLastRowNum()+1 + labelStrSetResult.size(); i++) {
                                        index.add(i);
                                    }
                                    mapSpecaial6Index.put(patientId,index);
                                    mapSpecaial6.put(patientId, new ArrayList<>(labelStrSetResult));
                                }
                            }

                        } else {
                            List<String> healthLabel = getHealthLabel(patient);
                            if (healthLabel.size() > 0) {
                                if (mapSpecaial7.get(patientId) == null){
                                    mapSpecaial7.put(patientId,healthLabel);
                                }else {
                                    return;
                                }
                                No = 7;
                                和台账一致性 = "台账没有";
                                居民健康档案 = "有";
                                置信度 = "中";
                                处理方式 = "以居民健康档案为准";
                                labelStrSetResult.addAll(healthLabel);
                            } else {
                                No = 8;
                                和台账一致性 = "台账没有";
                                居民健康档案 = "没有";
                                置信度 = "低";
                                处理方式 = "应该不存在";
                            }
                        }
                    } else {
                        if (!"糖并高".equals(labelStr)) {
                            No = 2;
                            是否糖并高 = "否";
                            置信度 = "高";
                        } else {
                            是否糖并高 = "是";
                            List<String> taizhangLabel = getTaiZhang(patient);
                            if (taizhangLabel.contains(labelStr) && taizhangLabel.size() == 1) {
                                No = 3;
                                和台账一致性 = "是";
                                置信度 = "高";
                            }
                            //部分一致
                            else if (taizhangLabel.contains("糖尿病") || taizhangLabel.contains("高血压") || taizhangLabel.contains(labelStr)) {
                                No = 4;
                                和台账一致性 = "部分一致";
                                置信度 = "中";
                                处理方式 = "以台账为准";
                                labelStrSetResult.addAll(taizhangLabel);
                            } else if (taizhangLabel.size() == 0) {
                                No = 5;
                                和台账一致性 = "台账无信息";
                                置信度 = "低";
                                处理方式 = "暂时以\"糖并高\"";
                            } else {
                                //No为9
                                No = 9;
                                和台账一致性 = "不一致";
                                置信度 = "低";
                                处理方式 = "不下发";
//                                labelStrSetResult.addAll(taizhangLabel);
                            }

                        }
                    }
                    if (labelStrSetResult.size()>0){
                        for (String s : labelStrSetResult){
                            saveResult(result, patient, s , hospital, No, 下发方式, 是否糖并高, 和台账一致性, 居民健康档案, 置信度, 处理方式,task.getBeginDate(),task.getEndDate(),taskPatient.getUpdatedDate(),taskPatient.getStatus());
                        }
                    }else {
                        saveResult(result, patient, labelStr , hospital, No, 下发方式, 是否糖并高, 和台账一致性, 居民健康档案, 置信度, 处理方式,task.getBeginDate(),task.getEndDate(),taskPatient.getUpdatedDate(),taskPatient.getStatus());
                    }
                }catch (Exception e){

                    System.err.println("报错 id："+patientId + "报错信息：" + e.getMessage());
                }


            });






        });
        ExcelUtil.writeExcel(xssfWorkbook,"标签决策表");




    }

    private void saveResult(XSSFSheet result, Patient patient, String labelStr, String hospital, Integer no, String 下发方式, String 是否糖并高, String 和台账一致性, String 居民健康档案, String 置信度, String 处理方式, Date beginDate, Date endDate, Date updatedDate, Integer status) {
        Row row = result.createRow(result.getLastRowNum()+1);
        row.createCell(0).setCellValue(patient.getId());
        row.createCell(1).setCellValue(labelStr);
        row.createCell(2).setCellValue(hospital);
        row.createCell(3).setCellValue(patient.getName());
        row.createCell(4).setCellValue(patient.getCardNumber());
        row.createCell(5).setCellValue(patient.getEhrNumber());
        row.createCell(6).setCellValue(no);
        row.createCell(7).setCellValue(下发方式);
        row.createCell(8).setCellValue(是否糖并高);
        row.createCell(9).setCellValue(和台账一致性);
        row.createCell(10).setCellValue(居民健康档案);
        row.createCell(11).setCellValue(置信度);
        row.createCell(12).setCellValue(处理方式);
        row.createCell(13).setCellValue(beginDate.toString());
        row.createCell(14).setCellValue(endDate.toString());
        if (status == 2){
            row.createCell(15).setCellValue(updatedDate.toString());
        }
    }

    private void saveMapData() throws IOException {
        InputStream taizhang = new FileInputStream("./附件二：台账表格850 - 整理数据 - 84669.xlsx");
        XSSFWorkbook xssfWorkbookA = new XSSFWorkbook(taizhang);
        saveMap(7,5,xssfWorkbookA, taizhangCardNo, taizhangEhrNo);
        System.out.println("获取台账信息");
        xssfWorkbookA.close();;
        taizhang.close();

        InputStream health = new FileInputStream("./居民标签-健康档案系统.xlsx");
        XSSFWorkbook healthWorkBook = new XSSFWorkbook(health);
        saveMap(2,3,healthWorkBook,healthCardNo,healthEhrNo);
        System.out.println("获取健康档案信息");
        healthWorkBook.close();
        health.close();



        HandleMapData(healthCardNo);
        HandleMapData(healthEhrNo);
        HandleMapData(taizhangCardNo);
        HandleMapData(taizhangEhrNo);

    }

    private void HandleMapData(Map<String,List<String>> mapdata) {
        mapdata.forEach((k,v)->mapdata.put(k,v.stream().distinct().collect(Collectors.toList())));

        mapdata.forEach((k,v)->{
            if (v.contains("高血压") && v.contains("糖尿病")){
                v.remove("高血压");
                v.remove("糖尿病");
                v.add("糖并高");
            }
        });
    }

    private void saveMap(int healthNoIndex, int cardNoIndex,XSSFWorkbook healthWorkBook, Map<String, List<String>> healthCardNoMap, Map<String, List<String>> healthEhrNoMap) {
        String [] labels = new String[]{"高血压","糖尿病","精神病"};
        Arrays.stream(labels).forEach(label -> {
            try {
                XSSFSheet gaoxueya = healthWorkBook.getSheet(label);
                for (int i = 1; i <= gaoxueya.getLastRowNum(); i++) {
                    Row row = gaoxueya.getRow(i);
                    if (row == null){
                        break;
                    }
                    if (row.getCell(cardNoIndex) != null){
                        healthCardNoMap.computeIfAbsent(row.getCell(cardNoIndex).toString(), v->new ArrayList<>()).add(label);
                    }
                    if (row.getCell(healthNoIndex) != null){
                        healthEhrNoMap.computeIfAbsent(row.getCell(healthNoIndex).toString(), v->new ArrayList<>()).add(label);
                    }
                }
            }catch (Exception e){
                System.err.println("异常");
                e.printStackTrace();
            }

        });


    }

    private List<String> getHealthLabel(Patient patient) {
        //规则：
        if (healthEhrNo.get(patient.getEhrNumber()) != null){
            return healthEhrNo.get(patient.getEhrNumber());
        }else if (healthCardNo.get(patient.getCardNumber()) != null){
            return healthCardNo.get(patient.getCardNumber());
        }
        return new ArrayList<>();

    }

    private List<String> getTaiZhang(Patient patient) {
        //规则：
        if (taizhangEhrNo.get(patient.getEhrNumber()) != null){
            return taizhangEhrNo.get(patient.getEhrNumber());
        }else if (taizhangCardNo.get(patient.getCardNumber()) != null){
            return taizhangCardNo.get(patient.getCardNumber());
        }
        return new ArrayList<>();
    }



}
