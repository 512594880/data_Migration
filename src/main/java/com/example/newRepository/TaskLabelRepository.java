package com.example.newRepository;

import com.example.newEntity.TaskLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskLabelRepository extends JpaRepository<TaskLabel,Long> {
    TaskLabel findByName(String labelName);
    @Query(value = "SELECT t.name from task t INNER JOIN task_patient tp on tp.task_id = t.id where tp.patient_id = ? and t.name REGEXP '^.*(高血压|糖尿病|糖并高|精神病).*$'",nativeQuery = true)
    List<String> findNameByTask(Long id);


    List<TaskLabel> findByNameAndIdIn(String taskLabelName, List<Long> labelIds);

    List<TaskLabel> findByIdIn(List<Long> taskLabelIds);
}
