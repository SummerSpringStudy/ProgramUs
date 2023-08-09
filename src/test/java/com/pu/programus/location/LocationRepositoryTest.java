package com.pu.programus.location;

import com.pu.programus.project.Project;
import com.pu.programus.project.ProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class LocationRepositoryTest {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Test
    void findByName() {
        Location location = new Location();
        location.setName("서울");

        Project project1 = new Project();
        project1.setTitle("project1");
        project1.setLocation(location);

        Project project2 = new Project();
        project2.setTitle("project2");
        project2.setLocation(location);

        location.add(project1);
        location.add(project2);

        locationRepository.save(location);
        projectRepository.save(project1);
        projectRepository.save(project2);

        Location getLoc = locationRepository.findByName("서울")
                .orElseThrow(() -> new IllegalArgumentException("Wrong Location Name"));
        Assertions.assertEquals(2, getLoc.getProjects().size());
    }
}
