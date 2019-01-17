package com.example.datamigrationspring;

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
	@Qualifier("oldDataBase")
	protected JdbcTemplate oldDataBase;

	@Autowired
	@Qualifier("newDataBase")
	protected JdbcTemplate newDataBase;


	public static void main(String[] args) {
		//获取11月7日（含）到11月25日（含）并且未删除的量表
		List<Map<String,Object>> guageTemplates =oldDataBase.queryForList("SELECT * from guage_template where ");
		//删除2.0中已有的量表
		guageTemplates.removeIf(DataMigrationSpringApplication::Duplicate);
		//插入到2.0中
		guageTemplates.forEach(map -> {
			//如果Id不重复
			if (newDataBase.queryForList("SELECT * from guage_template WHERE id = " + map.get("id").toString()).size() == 0){
				//关联gauge相关表
				//插入
				insertOne(map,"guage_template");
				//关联gauge_template_script
				List<Map<String,Object>> gauge_template_scripts = oldDataBase.queryForList("SELECT * from gauge_template_script where template_id = " + map.get("id").toString());
				guageTemplates.forEach(gauge_template_scriptMap -> {
					if (newDataBase.queryForList("SELECT * from gauge_template_script WHERE template_id = " + map.get("id").toString() + "and scipt_id = " + gauge_template_scriptMap.get("script_id").toString()).size()==0){
						//当gauge_template_script不存在时
					}else {
						Map<String,Object> scriptName = oldDataBase.queryForMap("SELECT * from guage_script WHERE id = " + gauge_template_scriptMap.get("script_id").toString());
						int script_Id = -1;
						//量表结果按名称查询不存在时候
						if (newDataBase.queryForList("SELECT * from guage_script WHERE srcipt_name = `" + scriptName.get("srcipt_name").toString() + "`").size() == 0){
							if (newDataBase.queryForList("SELECT * from guage_script WHERE id = " + scriptName.get("id").toString() ).size() == 0){
								insertOne(scriptName,"guage_script");
							}else {
								insertOneNoNeedId(scriptName,"guage_script");

							}
						}//量表结果按名称查询存在的时候
						else {

						}
					}
				});
			}
		});





		SpringApplication.run(DataMigrationSpringApplication.class, args);
	}

	private static void insertOne(Map<String, Object> map, String tableName) {
		StringBuilder sql = new StringBuilder("insert into values(");
		StringBuilder column = new StringBuilder("(");
		map.forEach((k,v)-> {sql.append(k).append(",");column.append(v).append(",");});
		sql.deleteCharAt(sql.lastIndexOf(",")).append(")");
		column.deleteCharAt(column.lastIndexOf(",")).append(")");
		sql.append(column);
		newDataBase.update(sql.toString());
	}

	private static void insertOneNoNeedId(Map<String, Object> map, String tableName) {
		StringBuilder sql = new StringBuilder("insert into values(");
		StringBuilder column = new StringBuilder("(");
		map.forEach((k,v)-> {
			if (!"id".equals(k)){
				sql.append(k).append(",");column.append(v).append(",");
			}
		});
		sql.deleteCharAt(sql.lastIndexOf(",")).append(")");
		column.deleteCharAt(column.lastIndexOf(",")).append(")");
		sql.append(column);
		newDataBase.update(sql.toString());
	}

	private static boolean Duplicate(Map<String, Object> map) {
		return newDataBase.queryForList("").size() > 0;
	}

}

