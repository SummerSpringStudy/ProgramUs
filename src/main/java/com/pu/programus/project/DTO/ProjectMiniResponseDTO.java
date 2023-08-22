package com.pu.programus.project.DTO;

import com.pu.programus.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ProjectMiniResponseDTO {

    private String title;
    private List<String>  keywords;
    private List<HeadCountResponseDTO> projectHeadCounts;

    public static ProjectMiniResponseDTO make(Project project){
        return ProjectMiniResponseDTO.builder()
                .title(project.getTitle())
                .keywords(project.getKeywordValues())
                .projectHeadCounts(project.getHeadCounts()) // 인원 정보 리스트는 흠.. [포지션,현재,맥스] 이 리스트를 가져오면 댈 듯
                .build();
    }

}
