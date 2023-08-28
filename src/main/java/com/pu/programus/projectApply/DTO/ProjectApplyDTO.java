package com.pu.programus.projectApply.DTO;


import com.pu.programus.position.DTO.PositionDTO;
import com.pu.programus.position.Position;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectApplyDTO {
    private String uid;    //지원자
    private String intro;  //한줄소개
    private Long projectId; //프로젝트
    private PositionDTO positionDTO; //포지션

}
