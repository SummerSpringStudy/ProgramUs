package com.pu.programus.project.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectDTO {
    private String title;
    private String description;
}
