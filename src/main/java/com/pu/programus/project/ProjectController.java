package com.pu.programus.project;

import com.pu.programus.annotation.PUTokenApiImplicitParams;
import com.pu.programus.config.security.SecurityConfiguration;
import com.pu.programus.jwt.JwtTokenProvider;
import com.pu.programus.project.DTO.ProjectMiniList;
import com.pu.programus.project.DTO.ProjectRequestDTO;
import com.pu.programus.project.DTO.ProjectResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/mini")
    public ResponseEntity<ProjectMiniList> getProjectMiniList(@RequestParam(required = false) String location, @RequestParam(required = false) String position, Pageable pageable){

        ProjectMiniList projectMiniList = projectService.getProjectMiniList(location, position, pageable);
        return ResponseEntity.ok(projectMiniList);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long projectId){

        ProjectResponseDTO projectResponseDTO = projectService.getProjectById(projectId);
        return ResponseEntity.ok(projectResponseDTO);
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

    @PUTokenApiImplicitParams
    @PutMapping("/{projectId}")
    public void updateProject(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @PathVariable Long projectId, @RequestBody ProjectRequestDTO projectRequestDTO){
        String uid = jwtTokenProvider.getUid(token);
        projectService.update(uid, projectId, projectRequestDTO);
    }

    @PUTokenApiImplicitParams
    @DeleteMapping()
    public void deleteProject(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @RequestParam Long projectId){
        String uid = jwtTokenProvider.getUid(token);
        projectService.delete(uid, projectId);
    }

    @PUTokenApiImplicitParams
    @PostMapping("/apply")
    public void applyProject(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token,
                             @RequestParam Long projectId,
                             @RequestParam String positionName) {
        String uid = jwtTokenProvider.getUid(token);
        projectService.apply(projectId, positionName, uid);
    }

    @PUTokenApiImplicitParams
    @PostMapping("/cancle")
    public void cancelApply(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token,
                            @RequestParam Long projectId,
                            @RequestParam String positionName) {
        String uid = jwtTokenProvider.getUid(token);
        projectService.cancelApply(projectId, positionName, uid);
    }

    @GetMapping("/contain/{title}")
    public ResponseEntity<ProjectMiniList> getProjectContainsTitle(@RequestParam String title) {
        ProjectMiniList projectMiniList = projectService.getProjectsContainsTitle(title);
        return ResponseEntity.ok(projectMiniList);
    }

}
