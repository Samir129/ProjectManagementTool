package com.mypersonalproject.projectmanagementtool.exceptions;

public class ProjectNotFoundExceptionResponse {

    private String projectNotFound;

    public ProjectNotFoundExceptionResponse (String projectNotFound){
        this.projectNotFound = projectNotFound;
    }

    public String getprojectNotFound() {
        return projectNotFound;
    }

    public void setprojectNotFound(String projectNotFound) {
        this.projectNotFound = projectNotFound;
    }
}
