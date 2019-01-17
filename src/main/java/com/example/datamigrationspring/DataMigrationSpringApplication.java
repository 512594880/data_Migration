package com.example.datamigrationspring;

import com.example.entity.GaugeScript;
import com.example.entity.GaugeTemplate;
import com.example.entity.GaugeTemplateScript;
import com.example.repository.GaugeResultRepository;
import com.example.repository.GaugeScriptRepository;
import com.example.repository.GaugeTemplateRepository;
import com.example.repository.GaugeTemplateScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

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


	public static void main(String[] args) {
		//获取11月7日（含）到11月25日（含）并且未删除的量表
		List<GaugeTemplate> gaugeTemplates = gaugeTemplateRepository.findByCreatedDateBetweenAndDelFlag("2018-11-07","2018-11-25",1);
		//删除2.0中已有的量表
        gaugeTemplates.removeIf(DataMigrationSpringApplication::Duplicate);

		//插入到2.0中
        gaugeTemplates.forEach(gaugeTemplate -> {
			//如果Id不重复
			if (!gaugeTemplateRepositoryNew.findById(gaugeTemplate.getId()).isPresent()){
				//关联gauge相关表
				//插入
				gaugeTemplateRepositoryNew.save(gaugeTemplate);
				//关联gauge_template_script
				List<GaugeTemplateScript> gaugeTemplateScripts = gaugeTemplateScriptRepository.findByTemplateId(gaugeTemplate.getId());
                gaugeTemplateScripts.forEach(gaugeTemplateScript -> {
                    if (gaugeTemplateScriptRepositoryNew.findByTemplateIdAndScriptId(gaugeTemplateScript.getTemplateId(),gaugeTemplateScript.getScriptId()).size() == 0){
                        //当gauge_template_script不存在时
                    }else {
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
                    };
				});
			}
		});





		SpringApplication.run(DataMigrationSpringApplication.class, args);
	}


	private static boolean Duplicate(GaugeTemplate gaugeTemplate) {
		return gaugeTemplateRepository.findByTemplateName(gaugeTemplate.getTemplateName());
	}

}

