package com.example.newRepository;

import com.example.entity.GaugeTemplate;
import com.example.newEntity.GaugeTemplateNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GaugeTemplateRepositoryNew extends JpaRepository<GaugeTemplateNew,Long>{
    List<GaugeTemplateNew> findByDelFlagAndCreatedDateBetween(int i,Date beginDate, Date EndDate);


//    boolean findByTemplateName(String templateName);

    GaugeTemplateNew findByTemplateNameAndCreatedDate(String templateName, Date createdDate);


}
