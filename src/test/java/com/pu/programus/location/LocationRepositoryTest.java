package com.pu.programus.location;

import com.pu.programus.project.Project;
import com.pu.programus.project.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

        Project pr1 = new Project();
        pr1.setTitle("pr1");
        pr1.setLocation(location);
        projectRepository.save(pr1);

        List<Project> prs = location.getProjects();
        prs.add(pr1);

        locationRepository.save(location);

        Location getLoc = locationRepository.findByName("서울").get();
        System.out.println(getLoc.getProjects());

    }

}
