package com.pu.programus.project.DTO;

import com.pu.programus.project.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectList {
    private List<ProjectDTO> projects;
}
