package com.pu.programus.project;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/project-mini")
    public void testMethod(@RequestParam(value="location", defaultValue = "전체") String location,
                             @RequestParam(value="position", defaultValue = "전체") String position,
                             Pageable pageable){

        projectService.getMiniProjects(location, position, pageable);
    }
}
