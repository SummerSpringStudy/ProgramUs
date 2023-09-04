package com.pu.programus.project;

import com.pu.programus.annotation.PUTokenApiImplicitParams;
import com.pu.programus.config.security.SecurityConfiguration;
import com.pu.programus.exception.AuthorityException;
import com.pu.programus.jwt.JwtTokenProvider;
import com.pu.programus.project.DTO.ProjectMiniList;
import com.pu.programus.project.DTO.ProjectRequestDTO;
import com.pu.programus.project.DTO.ProjectResponseDTO;
import com.pu.programus.projectApply.DTO.ProjectApplyDTO;
import com.pu.programus.projectApply.ProjectApplyService;
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
    private final ProjectApplyService projectApplyService;

    @GetMapping("/mini")
    public ResponseEntity<ProjectMiniList> getProjectMiniList(@RequestParam(required = false) String title, @RequestParam(required = false) String location, @RequestParam(required = false) String position, Pageable pageable){

        ProjectMiniList projectMiniList = projectService.getProjectMiniList(title, location, position, pageable);
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
    public void updateProject(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @PathVariable Long projectId, @RequestBody ProjectRequestDTO projectRequestDTO) throws AuthorityException {
        String uid = jwtTokenProvider.getUid(token);
        projectService.update(uid, projectId, projectRequestDTO);
    }

    @PUTokenApiImplicitParams
    @DeleteMapping
    public void deleteProject(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @RequestParam Long projectId) throws AuthorityException{
        String uid = jwtTokenProvider.getUid(token);
        projectService.delete(uid, projectId);
    }

    //Todo: 네이밍
    @PUTokenApiImplicitParams
    @PostMapping("/apply")
    public void applyProject(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token,
                             @RequestParam Long projectId,
                             @RequestParam String positionName,
                             @RequestBody ProjectApplyDTO projectApplyDTO) {
        String uid = jwtTokenProvider.getUid(token);
        projectApplyService.apply(projectApplyDTO,uid);
    }

    //Todo: 네이밍
    @PUTokenApiImplicitParams
    @PostMapping("/cancel")
    public void cancelApply(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token,
                            @RequestParam Long projectId,
                            @RequestParam String positionName) {
        String uid = jwtTokenProvider.getUid(token);
        projectService.cancelApply(projectId, positionName, uid);
    }

}
