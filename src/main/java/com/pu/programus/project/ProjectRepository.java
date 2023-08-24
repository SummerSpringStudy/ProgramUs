package com.pu.programus.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByTitle(String title);

    @Query("select p from Project p, ProjectHeadCount h " +
            "where p.id = h.project.id and " +
            "p.location.name = :location and " +
            "h.position.name = :position " +
            "group by p " +
            "order by p.id DESC")
    List<Project> findAllByLocationAndPosition(String location, String position, Pageable pageable);

    @Query("select p from Project p " +
            "where p.location.name = :location " +
            "order by p.id DESC")
    List<Project> findAllByLocation(String location, Pageable pageable);

    @Query("select p from Project p, ProjectHeadCount h " +
            "where p.id = h.project.id and " +
            "h.position.name = :position " +
            "group by p " +
            "order by p.id DESC ")
    List<Project> findAllByPosition(String position, Pageable pageable);
}
