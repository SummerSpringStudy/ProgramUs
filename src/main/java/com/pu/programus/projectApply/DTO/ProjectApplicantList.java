package com.pu.programus.projectApply.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
@AllArgsConstructor
public class ProjectApplicantList {
    private List<ProjectApplicantDTO> ProjectApplicants;
}
