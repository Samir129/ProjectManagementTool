package com.mypersonalproject.projectmanagementtool.services;

import com.mypersonalproject.projectmanagementtool.domain.Backlog;
import com.mypersonalproject.projectmanagementtool.domain.Project;
import com.mypersonalproject.projectmanagementtool.domain.ProjectTask;
import com.mypersonalproject.projectmanagementtool.exceptions.ProjectIdExceptionResponse;
import com.mypersonalproject.projectmanagementtool.exceptions.ProjectNotFoundException;
import com.mypersonalproject.projectmanagementtool.repositories.BacklogRepository;
import com.mypersonalproject.projectmanagementtool.repositories.ProjectRepository;
import com.mypersonalproject.projectmanagementtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){


        try{
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
            if(projectTask.getPriority() == 0 || projectTask.getPriority() == null){
                projectTask.setPriority(3);
            }
            // INITIAL status when status null
            if(projectTask.getStatus()=="" || projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);
        }
        catch(Exception e){
            throw new ProjectNotFoundException("Project Not Found");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String id){
        Project project = projectRepository.findByProjectIdentifier(id);

        if(project == null){
            throw new ProjectNotFoundException("Project with ID: '" + id + "' does not exist.");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){
        // make sure we are searching on the right backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog == null) throw new ProjectNotFoundException("Project with ID: '" + backlog_id + "' does not exist.");

        // make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask == null) throw new ProjectNotFoundException("Project Task: '" + pt_id + "' not found." );

        // make sure that the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id))
            throw new ProjectNotFoundException("Project Task '" + pt_id  + "' does not exist in project: '" + backlog_id + "'");

        return projectTask;

    }

    public ProjectTask updateProjectTaskbySequence(ProjectTask newTask, String backlog_id, String pt_id){

        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
        projectTask = newTask;
        return projectTaskRepository.save(projectTask);
    }

    public void deleteProjectTaskbySequence(String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
//        Backlog backlog = projectTask.getBacklog();
//        List<ProjectTask> projectTasks = backlog.getProjectTasks();
//        projectTasks.remove(projectTask);
//        backlogRepository.save(backlog);
        projectTaskRepository.delete(projectTask);
        return;
    }
}
