package com.example;

import com.example.entity.*;
import com.example.newRepository.*;
import com.example.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Set;
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

	@Override
	public void run(String... strings) throws Exception {

		//获取11月7日（含）到11月25日（含）并且未删除的量表
		List<GaugeTemplate> gaugeTemplates = gaugeTemplateRepository.findByCreatedDateBetweenAndDelFlag("2018-11-07","2018-11-25",1);
		//删除2.0中已有的量表
		gaugeTemplates.removeIf(this::Duplicate);

		//插入到2.0中
		gaugeTemplates.forEach(gaugeTemplate -> {
			//如果Id不重复
			if (gaugeTemplateRepositoryNew.findOne(gaugeTemplate.getId()) !=null){
				//关联gauge相关表
				handleGauge(gaugeTemplate);
				//关联page表
				handlePage(gaugeTemplate);

			}else {
				//量表ID在新的表中被占用，暂没有该情况
			}
		});
		
		
		
		
		
		log.info("============启动后执行的代码！！！============");
	}


	//处理PAGE相关表
	private  void handlePage(GaugeTemplate gaugeTemplate) {
		//pageLayOut、pageLayoutproperty 无ID占用情况，直接导过去
		//page_property，有使用以前属性情况，ID相同。

		List<PageLayout> pageLayouts = pageLayoutRepository.findByGaugeTemplateId(gaugeTemplate.getId());
		pageLayoutRepositoryNew.save(pageLayouts);
		pageLayouts.forEach(pageLayout -> {
			List<PageLayoutProperty> pageLayoutProperties = pageLayoutPropertyRepository.findByPageLayoutId(pageLayout.getId());
			pageLayoutPropertyRepositoryNew.save(pageLayoutProperties);
			Set<Long> pagePropertiesIds = pageLayoutProperties.stream().map(PageLayoutProperty::getPagePropertyId).collect(Collectors.toSet());
			List<PageProperty> pageProperties = pagePropertyRepository.findByIdIn(pagePropertiesIds);
			pageProperties.forEach(pageProperty -> {
				if (pagePropertyRepositoryNew.findOne(pageProperty.getId()) != null){
					pagePropertyRepositoryNew.save(pageProperty);
				}
			});


		});

		//pageLayoutBigQuestion 直接导入，bigQuestion 不重复（name和createDate）的导入。
		Set<Long> pageLayoutIds = pageLayouts.stream().map(PageLayout::getId).collect(Collectors.toSet());
		List<PageLayoutBigQuestion> pageLayoutBigQuestions = pageLayoutBigQuestionRepository.findByPageLayoutIdIn(pageLayoutIds);
		pageLayoutBigQuestions.forEach(pageLayoutBigQuestion -> {
			BigQuestion bigQuestion = bigQuestionRepository.findOne(pageLayoutBigQuestion.getBigQuestionId());
			if (bigQuestionRepositoryNew.findByNameAndCreatedDate(bigQuestion.getName()).size() == 0){
				List<SmallQuestion> smallQuestions = smallQuestionRepository.findByBigQuestionId(bigQuestion.getId());

				com.example.newEntity.BigQuestion bigQuestionNew = new com.example.newEntity.BigQuestion();
				ModelMapper modelMapper = new ModelMapper();
				bigQuestionNew = modelMapper.map(bigQuestion,com.example.newEntity.BigQuestion.class);
				//插入bigQuestion
				if (bigQuestionRepositoryNew.findOne(bigQuestion.getId()) != null){
					bigQuestion.setId(null);
					bigQuestionRepositoryNew.save(bigQuestionNew);
					bigQuestionNew = bigQuestionRepositoryNew.findOne(pageLayoutBigQuestion.getBigQuestionId());
				}else {
					bigQuestionRepositoryNew.save(bigQuestionNew);
				}



				//导入smallQuesiton
				for (SmallQuestion smallQuestion:smallQuestions){
					smallQuestion.setId(null);
					smallQuestion.setBigQuestionId(bigQuestionNew.getId());
				}
				smallQuestionRepositoryNew.save(smallQuestions);
				pageLayoutBigQuestion.setBigQuestionId(bigQuestionNew.getId());
				pageLayoutBigQuestionRepositoryNew.save(pageLayoutBigQuestion);
//                if (bigQuestionRepositoryNew.findOne(bigQuestion.getId()).isPresent()){
//                    bigQuestion.setId(null);
//                    bigQuestionRepositoryNew.save(bigQuestion);
//                    pageLayoutBigQuestion.set
//                };
			}

		});






	}

	private  void handleGauge(GaugeTemplate gaugeTemplate) {
		//关联gaugeResult
		List<GaugeResult> gaugeResults = gaugeResultRepository.findByTemplateId(gaugeTemplate.getId());
		gaugeResults.forEach(gaugeResult -> {
			if (gaugeResultRepositoryNew.findByTemplateIdAndResultName(gaugeTemplate.getId()) == null){
				if (gaugeResultRepositoryNew.findOne(gaugeResult.getId()) != null){
					gaugeResult.setId(null);
				}
				gaugeResultRepositoryNew.save(gaugeResult);
			}
		});

		//插入
		gaugeTemplateRepositoryNew.save(gaugeTemplate);
		//关联gauge_template_script
		List<GaugeTemplateScript> gaugeTemplateScripts = gaugeTemplateScriptRepository.findByTemplateId(gaugeTemplate.getId());
		gaugeTemplateScripts.forEach(gaugeTemplateScript -> {
			if (gaugeTemplateScriptRepositoryNew.findByTemplateIdAndScriptId(gaugeTemplateScript.getTemplateId(),gaugeTemplateScript.getScriptId()).size() == 0){
				//当gauge_template_script不存在时
				GaugeScript gaugeScript = gaugeScriptRepository.findOne(gaugeTemplateScript.getScriptId());
				if (gaugeScriptRepositoryNew.findByScriptName(gaugeScript.getScriptName()).size() == 0){
					//量表结果按名称查询不存在时候
					//验证ID是否存在
					if (gaugeScriptRepositoryNew.findOne(gaugeScript.getId())!=null){
						gaugeScript.setId(null);
						gaugeScriptRepositoryNew.save(gaugeScript);
						gaugeTemplateScript.setScriptId(gaugeScript.getId());
					}else {
						gaugeScriptRepositoryNew.save(gaugeScript);
					}
				}else {
					List<GaugeScript> gaugeScriptNews = gaugeScriptRepositoryNew.findByScriptName(gaugeScript.getScriptName());
					if (gaugeScriptNews.stream().noneMatch(gaugeScriptNew -> gaugeScriptNew.getId().equals(gaugeScript.getId()))){
						gaugeTemplateScript.setScriptId(gaugeScriptNews.get(0).getId());
					}
				}

				if (gaugeTemplateScriptRepositoryNew.findOne(gaugeTemplateScript.getId())!=null){
					gaugeTemplateScriptRepositoryNew.save(gaugeTemplateScript);
				}else {
					gaugeTemplateScript.setId(null);
					gaugeTemplateScriptRepositoryNew.save(gaugeTemplateScript);
				}
			}
		});
	}


	private  boolean Duplicate(GaugeTemplate gaugeTemplate) {
		return gaugeTemplateRepository.findByTemplateNameAndCreatedDate(gaugeTemplate.getTemplateName(),gaugeTemplate.getCreatedDate());
	}
}

