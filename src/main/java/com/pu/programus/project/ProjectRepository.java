package com.pu.programus.project;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByTitle(String title);

    @Query("select p from Project p, ProjectHeadCount h " +
            "where p.id = h.project.id and " +
            "(:title is null or p.title like %:title%) and " +
            "(:location is null or p.location.name = :location or p.location.name = '" + Project.ALL_LOCATION + "') and " +
            "(:position is null or h.position.name = :position) " +
            "group by p " +
            "order by p.id DESC")
    List<Project> findAllByLocationAndPosition(String title, String location, String position, Pageable pageable);
}
