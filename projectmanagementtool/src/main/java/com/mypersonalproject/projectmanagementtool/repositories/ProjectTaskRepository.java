package com.mypersonalproject.projectmanagementtool.repositories;

import com.mypersonalproject.projectmanagementtool.domain.Backlog;
import com.mypersonalproject.projectmanagementtool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {
    List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);

    ProjectTask findByProjectSequence(String sequence);
}
