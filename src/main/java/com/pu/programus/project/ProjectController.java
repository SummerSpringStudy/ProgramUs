package com.pu.programus.project;

import com.pu.programus.project.DTO.ProjectMiniResponseDTO;
import com.pu.programus.project.DTO.ProjectRequestDTO;
import com.pu.programus.project.DTO.ProjectResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/project-mini")
    public List<ProjectMiniResponseDTO> getProjects(@RequestParam(value="location", defaultValue = "전체") String location,
                                                    @RequestParam(value="position", defaultValue = "전체") String position,
                                                    Pageable pageable){
        return projectService.getMiniProjects(location, position, pageable);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long projectId){
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }
}
