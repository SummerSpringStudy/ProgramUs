package com.pu.programus.project.DTO;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequestDTO {

    private String title;
    @Nullable
    private String location;
    private String description;
    private Date startTime;
    private Date endTime;
    private List<String> keywords;
    private List<HeadCountResponseDTO> projectHeadCounts;
}
