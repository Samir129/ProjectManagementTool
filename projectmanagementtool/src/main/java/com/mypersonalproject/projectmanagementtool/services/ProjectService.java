package com.mypersonalproject.projectmanagementtool.services;

import com.mypersonalproject.projectmanagementtool.domain.Backlog;
import com.mypersonalproject.projectmanagementtool.domain.Project;
import com.mypersonalproject.projectmanagementtool.domain.User;
import com.mypersonalproject.projectmanagementtool.exceptions.ProjectIdException;
import com.mypersonalproject.projectmanagementtool.repositories.BacklogRepository;
import com.mypersonalproject.projectmanagementtool.repositories.ProjectRepository;
import com.mypersonalproject.projectmanagementtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject (Project project, String username){

        try {
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if(project.getId() == null){
                Backlog backlog = new Backlog();
                backlog.setProject(project);
                project.setBacklog(backlog);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        }
        catch(Exception e){
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists.");
        }
    }

    public Project findProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null){
            throw new ProjectIdException("Project does not exist.");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null){
            throw new ProjectIdException("Cannot delete project with ID '" + projectId + "'. Does not exist.");
        }
        projectRepository.delete(project);
    }
}
