package com.pu.programus.project;

import com.pu.programus.project.DTO.ProjectMiniResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/project-mini")
    public List<ProjectMiniResponseDTO> getProjects(@RequestParam(value="location", defaultValue = "전체") String location,
                                                    @RequestParam(value="position", defaultValue = "전체") String position,
                                                    Pageable pageable){

        return projectService.getMiniProjects(location, position, pageable);
    }
}
