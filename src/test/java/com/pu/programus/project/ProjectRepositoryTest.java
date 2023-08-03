package com.pu.programus.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectRepositoryTest {
    @Autowired
    ProjectRepository projectRepository;

    @Test
    void findByTitle() {
    }

    @Test
    void findProjectsByUserId() {

    }
}