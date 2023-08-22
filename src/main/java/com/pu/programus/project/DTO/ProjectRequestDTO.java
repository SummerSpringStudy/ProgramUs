package com.pu.programus.project.DTO;


import com.pu.programus.member.DTO.ProjectMemberDTO;
import com.pu.programus.member.Member;
import com.pu.programus.project.ProjectHeadCount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequestDTO {

    private Member owner;
    private String title;
    private String location;
    private String description;
    private Date startTime;
    private Date endTime;
    private List<String> keywords;
    private List<HeadCountResponseDTO> projectHeadCounts;
}
