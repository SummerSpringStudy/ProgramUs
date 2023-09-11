package com.pu.programus.projectApply;


import com.pu.programus.annotation.PUTokenApiImplicitParams;
import com.pu.programus.config.security.SecurityConfiguration;
import com.pu.programus.jwt.JwtTokenProvider;
import com.pu.programus.project.ProjectService;
import com.pu.programus.projectApply.DTO.ProjectApplyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/projectApply")
public class ProjectApplyController {

    private final ProjectApplyService projectApplyService;
    private final ProjectService projectService;
    private final JwtTokenProvider jwtTokenProvider;

    @PUTokenApiImplicitParams
    @PostMapping("/apply")
    public void applyProject(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token,
                             @RequestBody ProjectApplyDTO projectApplyDTO) {
        String uid = jwtTokenProvider.getUid(token);
        projectApplyService.apply(projectApplyDTO,uid);
    }

    @PUTokenApiImplicitParams
    @PutMapping("/confirm")
    public void confirmApply() {

    }


    @PUTokenApiImplicitParams
    @GetMapping("/getAppliedProject")
    public void getMyApplyProjects() {

    }

    @PUTokenApiImplicitParams
    @GetMapping("/getProjectApplicant")
    public void getProjectApplicant() {

    }


    @PUTokenApiImplicitParams
    @PostMapping("/cancel")
    public void cancelApply(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token,
                            @RequestParam Long projectId,
                            @RequestParam String positionName) {
        String uid = jwtTokenProvider.getUid(token);
        projectService.cancelApply(projectId, positionName, uid);
    }


}
