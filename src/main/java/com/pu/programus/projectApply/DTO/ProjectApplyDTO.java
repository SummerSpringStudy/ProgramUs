package com.pu.programus.projectApply.DTO;


import com.pu.programus.position.DTO.PositionDTO;
import com.pu.programus.position.Position;
import com.pu.programus.project.Project;
import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectApplyDTO {
    private Long projectId; //프로젝트
    private String position; //포지션
    private String intro;  //한줄소개

    /*
    public static ProjectApplyDTO make(ProjectApplyRequestDTO projectApplyRequestDTO) {
        return ProjectApplyDTO.builder()
                .projectId(projectApplyRequestDTO.getProjectId())
                .position(projectApplyRequestDTO.getPosition())
                .intro(projectApplyRequestDTO.getIntroduce())
                .build();
    }
    */


}
