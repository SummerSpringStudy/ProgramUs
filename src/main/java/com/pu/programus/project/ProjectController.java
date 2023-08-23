package com.pu.programus.project;

import com.pu.programus.annotation.PUTokenApiImplicitParams;
import com.pu.programus.config.security.SecurityConfiguration;
import com.pu.programus.jwt.JwtTokenProvider;
import com.pu.programus.project.DTO.ProjectMiniResponseDTO;
import com.pu.programus.project.DTO.ProjectRequestDTO;
import com.pu.programus.project.DTO.ProjectResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/mini")
    public List<ProjectMiniResponseDTO> getProjects(@RequestParam(value="location", defaultValue = "전체") String location,
                                                    @RequestParam(value="position", defaultValue = "전체") String position,
                                                    Pageable pageable){
        return projectService.getMiniProjects(location, position, pageable);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long projectId){
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @PUTokenApiImplicitParams
    @PostMapping("/create")
    public void createProject(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @RequestBody ProjectRequestDTO projectRequestDTO){
        log.info("[createProject] 프로젝트 생성 시작");
        String uid = jwtTokenProvider.getUid(token);
        log.info("[createProject] uid: {}", uid);
        projectService.create(uid, projectRequestDTO);
        log.info("[createProject] 프로젝트 생성 완료");
    }
}
