package com.mypersonalproject.projectmanagementtool.repositories;

import com.mypersonalproject.projectmanagementtool.domain.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {
}
