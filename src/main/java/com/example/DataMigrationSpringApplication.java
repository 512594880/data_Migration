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
import com.example.service.CitzenDataSerivce;
import com.example.service.DoctorDataService;
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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@EnableAutoConfiguration
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
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
	protected PatientDimensionRepository patientDimensionRepository;


	@Autowired
	protected HospitalAreaRepository hospitalAreaRepository;

	@Autowired
	protected HospitalRepository hospitalRepository;







	@Autowired
	protected TaskLabelRepository taskLabelRepository;

	@Autowired
	protected ErrorTaskService errorTaskService;

	@Autowired
	protected CitzenDataSerivce citzenDataSerivce;

	@Autowired
	protected DoctorDataService doctorDataService;

	@Autowired
	protected PatientLabelDetailRepository patientLabelDetailRepository;



	@Override
	public void run(String ... strings) throws IOException, ParseException {
		//迁移居民域2.0到3.0
//		citzenDataSerivce.CitzenDataMigaration();
		//2.0到3.0医生域数据迁移
		doctorDataService.dataMigrationToNationalServer();

		//根据任务获取标签信息
//		errorTaskService.getErrorTask();
		//根据标签表修改居民的标签
//		errorTaskService.updatePatientLabel();



		//获取2.0错误的医患关系
//		doctorDataService.getErrorDoctorAndPatientShip();



//		;

		log.info("执行完成");
		System.exit(1);
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

