package com.mypersonalproject.projectmanagementtool.services;

import com.mypersonalproject.projectmanagementtool.domain.Backlog;
import com.mypersonalproject.projectmanagementtool.domain.Project;
import com.mypersonalproject.projectmanagementtool.domain.ProjectTask;
import com.mypersonalproject.projectmanagementtool.exceptions.ProjectNotFoundException;
import com.mypersonalproject.projectmanagementtool.repositories.BacklogRepository;
import com.mypersonalproject.projectmanagementtool.repositories.ProjectRepository;
import com.mypersonalproject.projectmanagementtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

            // PTs to be added to a specific project, project != null, Backlog exists
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog(); // backlogRepository.findByProjectIdentifier(projectIdentifier);
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
            if(projectTask.getPriority() == null || projectTask.getPriority() == 0){
                projectTask.setPriority(3);
            }
            // INITIAL status when status null
            if(projectTask.getStatus().equals("") || projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);

    }

    public Iterable<ProjectTask> findBacklogById(String id, String username){
        Project projectByIdentifier = projectService.findProjectByIdentifier(id, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String userName){

        // make sure we are searching on the right backlog
        Project projectByIdentifier = projectService.findProjectByIdentifier(backlog_id, userName);

        // make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask == null) throw new ProjectNotFoundException("Project Task: '" + pt_id + "' not found." );

        // make sure that the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id))
            throw new ProjectNotFoundException("Project Task '" + pt_id  + "' does not exist in project: '" + backlog_id + "'");

        return projectTask;

    }

    public ProjectTask updateProjectTaskbySequence(ProjectTask newTask, String backlog_id, String pt_id, String username){

        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTask = newTask;
        return projectTaskRepository.save(projectTask);
    }

    public void deleteProjectTaskbySequence(String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTaskRepository.delete(projectTask);
    }
}
