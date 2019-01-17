package com.example.datamigrationspring;

import com.example.entity.*;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class DataMigrationSpringApplication {
	@Autowired
	protected static GaugeResultRepository gaugeResultRepository;

	@Autowired
	protected static GaugeScriptRepository gaugeScriptRepository;

    @Autowired
    protected static GaugeScriptRepository gaugeScriptRepositoryNew;

	@Autowired
	protected static GaugeTemplateRepository gaugeTemplateRepository;

    @Autowired
    protected static GaugeTemplateRepository gaugeTemplateRepositoryNew;

	@Autowired
	protected static GaugeTemplateScriptRepository gaugeTemplateScriptRepository;

    @Autowired
    protected static GaugeTemplateScriptRepository gaugeTemplateScriptRepositoryNew;

    @Autowired
    protected static PageLayoutPropertyRepository pageLayoutPropertyRepository;

    @Autowired
    protected static PageLayoutPropertyRepository pageLayoutPropertyRepositoryNew;

    @Autowired
    protected static PageLayoutRepository pageLayoutRepository;

    @Autowired
    protected static PageLayoutRepository pageLayoutRepositoryNew;

    @Autowired
    protected static PagePropertyRepository pagePropertyRepository;

    @Autowired
    protected static PagePropertyRepository pagePropertyRepositoryNew;

    @Autowired
    protected static PageLayoutBigQuestionRepository pageLayoutBigQuestionRepository;

    @Autowired
    protected static PageLayoutBigQuestionRepository pageLayoutBigQuestionRepositoryNew;

    @Autowired
    protected static BigQuestionRepository bigQuestionRepository;

    @Autowired
    protected static BigQuestionRepository bigQuestionRepositoryNew;

    @Autowired
    protected static SmallQuestionRepository smallQuestionRepository;

    @Autowired
    protected static SmallQuestionRepository smallQuestionRepositoryNew;

	public static void main(String[] args) {
        SpringApplication.run(DataMigrationSpringApplication.class, args);

        //获取11月7日（含）到11月25日（含）并且未删除的量表
            List<GaugeTemplate> gaugeTemplates = gaugeTemplateRepository.findByCreatedDateBetweenAndDelFlag("2018-11-07","2018-11-25",1);
		//删除2.0中已有的量表
        gaugeTemplates.removeIf(DataMigrationSpringApplication::Duplicate);

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
    private static void handlePage(GaugeTemplate gaugeTemplate) {
        //pageLayOut、pageLayoutproperty 无ID占用情况，直接导过去
        //page_property，有使用以前属性情况，ID相同。

        List<PageLayout> pageLayouts = pageLayoutRepository.findByGaugeTemplateId(gaugeTemplate.getId());
        pageLayoutRepositoryNew.saveAll(pageLayouts);
        pageLayouts.forEach(pageLayout -> {
            List<PageLayoutProperty> pageLayoutProperties = pageLayoutPropertyRepository.findByPageLayoutId(pageLayout.getId());
            pageLayoutPropertyRepositoryNew.saveAll(pageLayoutProperties);
            Set<Long> pagePropertiesIds = pageLayoutProperties.stream().map(PageLayoutProperty::getPagePropertyId).collect(Collectors.toSet());
            List<PageProperty> pageProperties = pagePropertyRepository.findByIdIn(pagePropertiesIds);
            pageProperties.forEach(pageProperty -> {
                if (!pagePropertyRepositoryNew.findById(pageProperty.getId()).isPresent()){
                    pagePropertyRepositoryNew.save(pageProperty);
                }
            });


        });

        //pageLayoutBigQuestion 直接导入，bigQuestion 不重复（name和createDate）的导入。
        Set<Long> pageLayoutIds = pageLayouts.stream().map(PageLayout::getId).collect(Collectors.toSet());
        List<PageLayoutBigQuestion> pageLayoutBigQuestions = pageLayoutBigQuestionRepository.findByPageLayoutIdIn(pageLayoutIds);
        pageLayoutBigQuestions.forEach(pageLayoutBigQuestion -> {
            BigQuestion bigQuestion = bigQuestionRepository.findById(pageLayoutBigQuestion.getBigQuestionId()).get();
            if (bigQuestionRepositoryNew.findByNameAndCreatedDate(bigQuestion.getName()).size() == 0){
                List<SmallQuestion> smallQuestions = smallQuestionRepository.findByBigQuestionId(bigQuestion.getId());

                //插入bigQuestion
                if (bigQuestionRepositoryNew.findById(bigQuestion.getId()).isPresent()){
                    bigQuestion.setId(null);
                    bigQuestionRepositoryNew.save(bigQuestion);
                    bigQuestion = bigQuestionRepositoryNew.findById(pageLayoutBigQuestion.getBigQuestionId()).get();
                }else {
                    bigQuestionRepositoryNew.save(bigQuestion);
                }



                //导入smallQuesiton
                for (SmallQuestion smallQuestion:smallQuestions){
                    smallQuestion.setId(null);
                    smallQuestion.setBigQuestionId(bigQuestion.getId());
                }
                smallQuestionRepositoryNew.saveAll(smallQuestions);
                pageLayoutBigQuestion.setBigQuestionId(bigQuestion.getId());
                pageLayoutBigQuestionRepositoryNew.save(pageLayoutBigQuestion);
//                if (bigQuestionRepositoryNew.findById(bigQuestion.getId()).isPresent()){
//                    bigQuestion.setId(null);
//                    bigQuestionRepositoryNew.save(bigQuestion);
//                    pageLayoutBigQuestion.set
//                };
            }

        });






    }

    private static void handleGauge(GaugeTemplate gaugeTemplate) {
        //插入
        gaugeTemplateRepositoryNew.save(gaugeTemplate);
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

                if (!gaugeTemplateScriptRepositoryNew.findById(gaugeTemplateScript.getId()).isPresent()){
                    gaugeTemplateScriptRepositoryNew.save(gaugeTemplateScript);
                }else {
                    gaugeTemplateScript.setId(null);
                    gaugeTemplateScriptRepositoryNew.save(gaugeTemplateScript);
                }
            }
        });
    }


    private static boolean Duplicate(GaugeTemplate gaugeTemplate) {
		return gaugeTemplateRepository.findByTemplateNameAndCreatedDate(gaugeTemplate.getTemplateName(),gaugeTemplate.getCreatedDate());
	}

}

