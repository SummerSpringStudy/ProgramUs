package com.pu.programus.projectApply.DTO;


import com.pu.programus.position.DTO.PositionDTO;
import com.pu.programus.position.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectApplyDTO {
    private String uid;    //지원자
    private String intro;  //한줄소개
    private Long projectId; //프로젝트
    private PositionDTO positionDTO; //포지션

}
