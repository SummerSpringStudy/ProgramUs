package com.pu.programus.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectMiniList {
    private List<ProjectMiniResponseDTO> projectMiniList;
}
