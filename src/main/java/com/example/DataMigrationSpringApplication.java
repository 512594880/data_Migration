package com.example;

import com.example.Util.CardNumberUtil;
import com.example.Util.ExcelUtil;
import com.example.entity.*;
import com.example.entityServer.DoctorClinic;
import com.example.entityServer.DoctorContact;
import com.example.entityServer.DoctorServer;
import com.example.enums3.*;
import com.example.nationalServerRepository.DoctorClinicRepository;
import com.example.nationalServerRepository.DoctorContactRepository;
import com.example.nationalServerRepository.DoctorServerRepository;
import com.example.newEntity.*;
import com.example.newRepository.*;
import com.example.repository.*;
import com.example.service.ErrorTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.units.qual.A;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class DataMigrationSpringApplication implements CommandLineRunner {

	public  static void main(String[] args) {
		SpringApplication.run(DataMigrationSpringApplication.class, args);
	}

	@Autowired
	protected  GaugeResultRepository gaugeResultRepository;

	@Autowired
	protected  GaugeResultRepositoryNew gaugeResultRepositoryNew;

	@Autowired
	protected  GaugeScriptRepository gaugeScriptRepository;

	@Autowired
	protected GaugeScriptRepositoryNew gaugeScriptRepositoryNew;

	@Autowired
	protected  GaugeTemplateRepository gaugeTemplateRepository;

	@Autowired
	protected GaugeTemplateRepositoryNew gaugeTemplateRepositoryNew;

	@Autowired
	protected  GaugeTemplateScriptRepository gaugeTemplateScriptRepository;

	@Autowired
	protected GaugeTemplateScriptRepositoryNew gaugeTemplateScriptRepositoryNew;

	@Autowired
	protected  PageLayoutPropertyRepository pageLayoutPropertyRepository;

	@Autowired
	protected PageLayoutPropertyRepositoryNew pageLayoutPropertyRepositoryNew;

	@Autowired
	protected  PageLayoutRepository pageLayoutRepository;

	@Autowired
	protected PageLayoutRepositoryNew pageLayoutRepositoryNew;

	@Autowired
	protected  PagePropertyRepository pagePropertyRepository;

	@Autowired
	protected  PagePropertyRepositoryNew pagePropertyRepositoryNew;

	@Autowired
	protected  PageLayoutBigQuestionRepository pageLayoutBigQuestionRepository;

	@Autowired
	protected  PageLayoutBigQuestionRepositoryNew pageLayoutBigQuestionRepositoryNew;

	@Autowired
	protected  BigQuestionRepository bigQuestionRepository;

	@Autowired
	protected  BigQuestionRepositoryNew bigQuestionRepositoryNew;

	@Autowired
	protected  SmallQuestionRepository smallQuestionRepository;

	@Autowired
	protected  SmallQuestionRepositoryNew smallQuestionRepositoryNew;

	@Autowired
	protected DoctorRepository doctorRepository;

	@Autowired
	protected DoctorPatientRepository doctorPatientRepository;

	@Autowired
	protected PatientRepository patientRepository;

	@Autowired
	protected PatientDimensionRepository patientDimensionRepository;

	@Autowired
	protected HospitalRepository hospitalRepository;

	@Autowired
	protected HospitalAreaRepository hospitalAreaRepository;

	@Autowired
	protected DoctorServerRepository doctorServerRepository;

	@Autowired
	protected ContactRepository contactRepository;

	@Autowired
	protected DoctorContactRepository doctorContactRepository;

	@Autowired
	protected DoctorClinicRepository doctorClinicRepository;

	@Autowired
	protected TaskLabelRepository taskLabelRepository;

	@Autowired
	protected ErrorTaskService errorTaskService;

	@Autowired
	protected PatientLabelDetailRepository patientLabelDetailRepository;

	private static Map<Long,List<String>> doctorsInfo = new HashMap<>();

	private static Map<Long,String> patientInfo = new HashMap<>();

	//手机号正则
	private static String pthoneRegex = "^((\\+?86)|(\\(\\+86\\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$";
	@Override
	public void run(String ... strings) throws IOException, ParseException {
		//根据任务获取标签信息
//		errorTaskService.getErrorTask();
		errorTaskService.updatePatientLabel();
//		updatePatientLabel();
//		if (strings.length == 0 || "Server".equals(strings[0])){
//			System.out.println("开始2.0到3.0医生域数据迁移");
//			dataMigrationToNationalServer();
//		}else if ("error".equals(strings[0])){
//			System.out.println("开始获取2.0医生域错误数据");
//			getErrorDoctorAndPatientShip();
//		}else if ("patient_lable".equals(strings[0])){
//			System.out.println("开始清洗2.0居民标签数据");
//			updatePatientLabel();
//		}



//		;

		log.info("执行完成");
		System.exit(1);
	}

	private void updatePatientLabel() throws IOException {
			InputStream inputStreamA = new FileInputStream("./居民标签统计加卫生室.xlsx");
			XSSFWorkbook xssfWorkbookA = new XSSFWorkbook(inputStreamA);
			XSSFSheet BminusA = xssfWorkbookA.getSheet("B-A");
			BminusA.forEach(cells -> {
				String labelStr = cells.getCell(2).toString();
				if (!labelStr.contains("糖并高")){
					String [] labelList = labelStr.split("，");
					if (cells.getCell(0).toString().equals("患者ID")){
						return;
					}
					Long id = Long.valueOf(cells.getCell(0).toString());
					String hospital = getHospital(cells);
					if (hospital.contains("，")){
						List<String> labelNameList = taskLabelRepository.findNameByTask(id);
						labelNameList.forEach(labelName->{
							labelName = labelName.replace("随访","");
							ifNotHaveSave(id,labelName);
						});
					}else {
						for (String label : labelList){
							String labelName = hospital + label + "人群";
							ifNotHaveSave(id, labelName);
						}
					}

				}
			});


		InputStream inputStreamBAddA = new FileInputStream("./居民标签统计正确性判断-结果返回-2019.1.26.xlsx");
		XSSFWorkbook xssfWorkbookBaddA = new XSSFWorkbook(inputStreamBAddA);

		XSSFSheet BaddAtangbinggao = xssfWorkbookBaddA.getSheet("比对结果");
		for (int i = 1; i <= BaddAtangbinggao.getLastRowNum(); i++) {
			Row cells = BaddAtangbinggao.getRow(i);
			if (cells.getCell(0) == null){
				break;
			}
			Long id = Long.valueOf(cells.getCell(0).toString().replace(".0",""));
			System.out.println(id);
			try {
				String[] oldLabelList = cells.getCell(2).toString().split("，");
				String[] newLabelList = cells.getCell(15).toString().split("，");
				String hospital = getHospital(cells);
				String [] hospitalList = hospital.split("，");

				Set<String> newLabelSet = Arrays.stream(newLabelList).collect(Collectors.toSet());
					for (String oldLabel : oldLabelList) {
						if (newLabelSet.contains(oldLabel)) {
							Arrays.stream(hospitalList).forEach(hos->{
								String labelName = hos + oldLabel + "人群";
								//查询有没有
								ifNotHaveSave(id, labelName);
							});
							newLabelSet.remove(oldLabel);
						} else {
							Arrays.stream(hospitalList).forEach(hos->{
								String labelName = hos + oldLabel + "人群";
								TaskLabel taskLabel = taskLabelRepository.findByName(labelName);
								PatientLabelDetail patientLabelDetail = patientLabelDetailRepository.findByPatientIdAndTaskLabelId(id, taskLabel.getId());
								if (patientLabelDetail != null) {
									patientLabelDetailRepository.delete(patientLabelDetail);

								}
							});
						}
					}
					newLabelSet.forEach(newLabel -> {
						Arrays.stream(hospitalList).forEach(hos->{
							String labelName = hospital + newLabel + "人群";
							//查询有没有
							ifNotHaveSave(id, labelName);
						});
					});
//				}
			}catch (Exception e){
				System.out.println("id：" + id);
				e.printStackTrace();
			}
		};
	}

	private void ifNotHaveSave(Long id, String labelName) {


		if (labelName.contains("回龙") && !labelName.contains("卫生室")){
			labelName = labelName.replace("回龙","回龙社区服务中心");
		}
		TaskLabel taskLabel = taskLabelRepository.findByName(labelName);
		if (taskLabel == null ){
			System.out.println(id);
		}
		if (patientLabelDetailRepository.findByPatientIdAndTaskLabelId(id,taskLabel.getId()) == null){
            PatientLabelDetail patientLabelDetail = new PatientLabelDetail();
            patientLabelDetail.setPatientId(id);
            patientLabelDetail.setTaskLabelId(taskLabel.getId());
            patientLabelDetail.setCreatedDate(new Date());
            patientLabelDetailRepository.save(patientLabelDetail);
        }
	}

	private String getHospital(Row cells) {
		String hospital = cells.getCell(8) == null?"":cells.getCell(8).toString();
		if ("".equals(hospital)){
            hospital = cells.getCell(7) == null?"":cells.getCell(7).toString();
        }
		return hospital;
	}


	/**
	 * 2.0数据库医生域迁移到3.0
	 */
	private void dataMigrationToNationalServer() {
		//保存doctor错误信息
		XSSFWorkbook errorInfo = new XSSFWorkbook();
		XSSFSheet errorDoctorInfo = errorInfo.createSheet("医生错误信息");
		String heading [] = new String[]{"医生ID","医生身份证","医生所在卫生室","医生电话"};
		ExcelUtil.setHeading(errorDoctorInfo,heading);
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
			doctorClinicRepository.save(doctorClinic);
			Long id = doctorClinicRepository.findMaxId();
			//获取卫生院
			for (Hospital hospitalChildren : hospitalsChildren) {
				saveClinic(hospitalChildren, id);
			}

		});

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		List<Doctor> doctors = doctorRepository.findByTId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		doctors.forEach(doctor -> {
			try {
				DoctorServer doctorServer = new DoctorServer();
				doctorServer.setId(doctor.getId());
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
			}catch (Exception e){
				Row row = errorDoctorInfo.createRow(errorDoctorInfo.getLastRowNum()+1);
				row.createCell(0).setCellValue(doctor.getId().toString());
				row.createCell(1).setCellValue(doctor.getCardNumber());
				row.createCell(2).setCellValue(doctor.getHospitalName());
				row.createCell(3).setCellValue(doctor.getUserName());
			}
		});
		XSSFSheet contactError = errorInfo.createSheet("contact错误信息");
		String [] contactErrorHeading  = new String[]{"医生Id","电话"};
		ExcelUtil.setHeading(contactError,contactErrorHeading);
		//Contact迁移
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
			doctorContact.setDoctorId(Long.valueOf(contact.getUserId()));
			doctorContact.setContent(contact.getContent());
			doctorContact.setCreateTime(contact.getCreatedDate());
			doctorContact.setUpdateTime(contact.getCreatedDate());
			doctorContactRepository.save(doctorContact);
		});


		try {
			FileOutputStream fos = new FileOutputStream("./医生域迁移错误信息.xlsx");
			errorInfo.write(fos);
			fos.close();
			errorInfo.close();
			System.out.println("成功");
		} catch (IOException e) {
			e.printStackTrace();
		}



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
		doctorClinicRepository.save(doctorClinic);
		Long Id = doctorClinicRepository.findMaxId();
		for (int i = 0; i < hospitalAreas.size(); i++) {
			HospitalArea hospitalArea = hospitalAreas.get(i);
			saveArar(hospitalArea, Id);
		}

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
	 * 获取2.0错误的医患关系表
	 */
	private void getErrorDoctorAndPatientShip(){


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



		try {
			FileOutputStream fos = new FileOutputStream("./居民医生关系差异统计.xlsx");
			xssfWorkbook.write(fos);
			fos.close();
			xssfWorkbook.close();
			System.out.println("成功");
		} catch (IOException e) {
			e.printStackTrace();
		}






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
//		if ("".equals(patientArea) && !"".equals(doctorHospitalArea.trim()) ){
//			System.out.println();
//		}

		if (put && !"".equals(doctorHospitalArea.trim())){
			areaAndHospitalArea.computeIfAbsent(patientArea + "，" + doctorHospitalArea,v->new ArrayList<>()).add(patientId);
		}
//		Hospital hospital = hospitalRepository.findByHospitalName(doctorHospital);
//
//		if (hospital != null){
//			List<HospitalArea> hospitalArea = hospitalAreaRepository.findByHospitalId(hospital.getId());
//			doctorHospitalArea = hospitalArea.stream().map(HospitalArea::getAreaId).collect(Collectors.joining("，"));
//			patientArea = patientRepository.findById(patientId).get().getArea();
//		}

		row.createCell(0).setCellValue(doctorId);
		row.createCell(1).setCellValue(doctorHospital);
		row.createCell(2).setCellValue(doctorHospitalArea);
		row.createCell(3).setCellValue(patientId);
		row.createCell(4).setCellValue(patientArea);
	}

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

	/**
	 * 1.0量表数据迁移到2。0
	 */
	private void oneVersionToSecond() throws ParseException {
		ModelMapper modelMapper = new ModelMapper();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		//获取11月7日（含）到11月25日（含）并且未删除的量表
		List<GaugeTemplate> gaugeTemplates = gaugeTemplateRepository.findByDelFlagAndCreatedDateBetween(1,formatter.parse("2018-11-07"),formatter.parse("2018-11-26"));
		//删除2.0中已有的量表
		gaugeTemplates.removeIf(this::Duplicate);
		//插入到2.0中
		gaugeTemplates.forEach(gaugeTemplate -> {
			//如果Id不重复
			if (!gaugeTemplateRepositoryNew.findById(gaugeTemplate.getId()).isPresent()){
				//关联gauge相关表
				handleGauge(gaugeTemplate);
				//关联page表
				handlePage(gaugeTemplate);

			}else {
				//量表ID在新的表中被占用，暂没有该情况
			}
		});

	}


	//处理PAGE相关表
	private  void handlePage(GaugeTemplate gaugeTemplate) {
		ModelMapper modelMapper = new ModelMapper();
		//pageLayOut、pageLayoutproperty 无ID占用情况，直接导过去
		//page_property，有使用以前属性情况，ID相同。

		List<PageLayout> pageLayouts = pageLayoutRepository.findByGaugeTemplateId(gaugeTemplate.getId());
		List<PageLayoutNew> pageLayoutNews = pageLayouts.stream().map(pageLayout -> modelMapper.map(pageLayout, PageLayoutNew.class))
				.collect(Collectors.toList());
		pageLayoutRepositoryNew.saveAll(pageLayoutNews);
		pageLayouts.forEach(pageLayout -> {
			List<PageLayoutProperty> pageLayoutProperties = pageLayoutPropertyRepository.findByPageLayoutId(pageLayout.getId());
			List<PageLayoutPropertyNew> pageLayoutPropertieNews = pageLayoutProperties.stream().map(pageLayoutProperty -> modelMapper.map(pageLayoutProperty, PageLayoutPropertyNew.class))
					.collect(Collectors.toList());
			pageLayoutPropertyRepositoryNew.saveAll(pageLayoutPropertieNews);
			Set<Long> pagePropertiesIds = pageLayoutProperties.stream().map(PageLayoutProperty::getPagePropertyId).collect(Collectors.toSet());
			List<PageProperty> pageProperties = pagePropertyRepository.findByIdIn(pagePropertiesIds);
			pageProperties.forEach(pageProperty -> {
				if (!pagePropertyRepositoryNew.findById(pageProperty.getId()).isPresent()){
					pagePropertyRepositoryNew.save(modelMapper.map(pageProperty, PagePropertyNew.class));
				}
			});


		});

		//pageLayoutBigQuestion 直接导入，bigQuestion 不重复（name和createDate）的导入。
		Set<Long> pageLayoutIds = pageLayouts.stream().map(PageLayout::getId).collect(Collectors.toSet());

		List<PageLayoutBigQuestion> pageLayoutBigQuestions = pageLayoutBigQuestionRepository.findByPageLayoutIdIn(pageLayoutIds);
		pageLayoutBigQuestions.forEach(pageLayoutBigQuestion -> {
			BigQuestion bigQuestion = bigQuestionRepository.findById(pageLayoutBigQuestion.getBigQuestionId()).get();
			if (bigQuestionRepositoryNew.findByNameAndCreatedDate(bigQuestion.getName(),bigQuestion.getCreatedDate()).size() == 0){
				List<SmallQuestion> smallQuestions = smallQuestionRepository.findByBigQuestionId(bigQuestion.getId());

				BigQuestionNew bigQuestionNew = new BigQuestionNew();
				bigQuestionNew = modelMapper.map(bigQuestion,BigQuestionNew.class);
				//插入bigQuestion
				if (bigQuestionRepositoryNew.findById(bigQuestion.getId()).isPresent()){
					bigQuestion.setId(bigQuestionRepositoryNew.findMaxId()+1);
					bigQuestionRepositoryNew.save(bigQuestionNew);
					bigQuestionNew = bigQuestionRepositoryNew.findById(pageLayoutBigQuestion.getBigQuestionId()).get();
				}else {
					bigQuestionRepositoryNew.save(bigQuestionNew);
				}



				//导入smallQuesiton
				Long maxId = smallQuestionRepositoryNew.findMaxId()+1;
				for (SmallQuestion smallQuestion:smallQuestions){
					smallQuestion.setId(maxId);
					maxId+=1;
					smallQuestion.setBigQuestionId(bigQuestionNew.getId());
				}
				List<SmallQuestionNew> smallQuestionNews = smallQuestions.stream().map(smallQuestion -> modelMapper.map(smallQuestion,SmallQuestionNew.class)
				).collect(Collectors.toList());
				smallQuestionRepositoryNew.saveAll(smallQuestionNews);
				pageLayoutBigQuestion.setBigQuestionId(bigQuestionNew.getId());
			}else {
				pageLayoutBigQuestion.setBigQuestionId(bigQuestionRepositoryNew.findByNameAndCreatedDate(bigQuestion.getName(),bigQuestion.getCreatedDate()).get(0).getId());
			}
			pageLayoutBigQuestionRepositoryNew.save(modelMapper.map(pageLayoutBigQuestion,PageLayoutBigQuestionNew.class));

		});






	}

	private  void handleGauge(GaugeTemplate gaugeTemplate) {
		ModelMapper modelMapper = new ModelMapper();
		//关联gaugeResult
		List<GaugeResult> gaugeResults = gaugeResultRepository.findByTemplateId(gaugeTemplate.getId());
		gaugeResults.forEach(gaugeResult -> {
			if (gaugeResultRepositoryNew.findByTemplateIdAndResultName(gaugeTemplate.getId(),gaugeResult.getResultName()) == null){
				if (gaugeResultRepositoryNew.findById(gaugeResult.getId()).isPresent()){
					gaugeResult.setId(gaugeResultRepositoryNew.findMaxId()+1);
				}
				gaugeResultRepositoryNew.save(modelMapper.map(gaugeResult, GaugeResultNew.class));
			}
		});

		//插入
		gaugeTemplateRepositoryNew.save(modelMapper.map(gaugeTemplate,GaugeTemplateNew.class));
		//关联gauge_template_script
		List<GaugeTemplateScript> gaugeTemplateScripts = gaugeTemplateScriptRepository.findByTemplateId(gaugeTemplate.getId());
		gaugeTemplateScripts.forEach(gaugeTemplateScript -> {
			if (gaugeTemplateScriptRepositoryNew.findByTemplateIdAndScriptId(gaugeTemplateScript.getTemplateId(),gaugeTemplateScript.getScriptId()).size() == 0){
				//当gauge_template_script不存在时
				GaugeScript gaugeScript = gaugeScriptRepository.findById(gaugeTemplateScript.getScriptId()).get();
				if (gaugeScriptRepositoryNew.findByScriptName(gaugeScript.getScriptName()).size() == 0){
					//量表结果按名称查询不存在时候
					//验证ID是否存在
					if (gaugeScriptRepositoryNew.findById(gaugeScript.getId()).isPresent()){
						gaugeScript.setId(gaugeScriptRepositoryNew.findMaxId()+1);
						gaugeScriptRepositoryNew.save(modelMapper.map(gaugeScript,GaugeScriptNew.class));
						gaugeTemplateScript.setScriptId(gaugeScript.getId());
					}else {
						gaugeScriptRepositoryNew.save(modelMapper.map(gaugeScript,GaugeScriptNew.class));
					}
				}else {
					List<GaugeScriptNew> gaugeScriptNews = gaugeScriptRepositoryNew.findByScriptName(gaugeScript.getScriptName());
					if (gaugeScriptNews.stream().noneMatch(gaugeScriptNew -> gaugeScriptNew.getId().equals(gaugeScript.getId()))){
						gaugeTemplateScript.setScriptId(gaugeScriptNews.get(0).getId());
					}
				}

				if (!gaugeTemplateScriptRepositoryNew.findById(gaugeTemplateScript.getId()).isPresent()){
					gaugeTemplateScriptRepositoryNew.save(modelMapper.map(gaugeTemplateScript,GaugeTemplateScriptNew.class));
				}else {
					gaugeTemplateScript.setId(gaugeTemplateScriptRepositoryNew.findMaxId()+1);
					gaugeTemplateScriptRepositoryNew.save(modelMapper.map(gaugeTemplateScript,GaugeTemplateScriptNew.class));
				}
			}
		});
	}


	private  boolean Duplicate(GaugeTemplate gaugeTemplate) {
		return gaugeTemplateRepositoryNew.findByTemplateNameAndCreatedDate(gaugeTemplate.getTemplateName(),gaugeTemplate.getCreatedDate()) != null;
	}

}

