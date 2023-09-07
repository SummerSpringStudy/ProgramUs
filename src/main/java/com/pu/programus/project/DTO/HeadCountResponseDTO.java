package com.pu.programus.project.DTO;

import com.pu.programus.project.ProjectHeadCount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class HeadCountResponseDTO {

    private String positionName;
    private int nowHeadCount;
    private int maxHeadCount;

    public static HeadCountResponseDTO make(ProjectHeadCount projectHeadCount) {
        return HeadCountResponseDTO.builder()
                .positionName(projectHeadCount.getPosition().getName())
                .nowHeadCount(projectHeadCount.getNowHeadCount())
                .maxHeadCount(projectHeadCount.getMaxHeadCount())
                .build();
    }
}