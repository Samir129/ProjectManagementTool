package com.mypersonalproject.projectmanagementtool.services;

import com.mypersonalproject.projectmanagementtool.domain.Backlog;
import com.mypersonalproject.projectmanagementtool.domain.ProjectTask;
import com.mypersonalproject.projectmanagementtool.repositories.BacklogRepository;
import com.mypersonalproject.projectmanagementtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
        // PTs to be added to a specific project, project != null, Backlog exists
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        // Set the BL to PT
        projectTask.setBacklog(backlog);
        // We want our project sequence to be like this: IDPRO-1, IDPRO-2 ... 100 101
        Integer backlogSequence =  backlog.getPTSequence();
        // Update the BL Sequence
        backlogSequence++;

        backlog.setPTSequence(backlogSequence);

        // Add sequence to project task
        projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        // INITIAL Priority when priority null
        if(projectTask.getPriority() == null){
            projectTask.setPriority(3);
        }
        // INITIAL status when status null
        if(projectTask.getStatus()=="" || projectTask.getStatus()==null){
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String id){
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
}
