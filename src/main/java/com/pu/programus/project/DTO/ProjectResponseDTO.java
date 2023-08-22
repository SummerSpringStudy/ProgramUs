package com.pu.programus.project.DTO;

import com.pu.programus.member.DTO.ProjectMemberDTO;
import com.pu.programus.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ProjectResponseDTO {

    private String title;
    private String location;
    private String description;
    private Date startTime;
    private Date endTime;
    private List<String> keywords;
    private List<HeadCountResponseDTO> projectHeadCounts;
    private List<ProjectMemberDTO> projectMembers;

    public static ProjectResponseDTO make(Project project){
        return ProjectResponseDTO.builder()
                .title(project.getTitle())
                .location(project.getLocation().getName())
                .description(project.getDescription())
                .startTime(project.getStartTime())
                .endTime(project.getEndTime())
                .keywords(project.getKeywordValues())
                .projectHeadCounts(project.getHeadCounts())
                .projectMembers(project.getProjectMembers())
                .build();
    }
}
