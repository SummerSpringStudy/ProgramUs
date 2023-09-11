package com.pu.programus.projectApply.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectApplyList {
    private List<ProjectApplyDTO> ApplyProjects;
}
