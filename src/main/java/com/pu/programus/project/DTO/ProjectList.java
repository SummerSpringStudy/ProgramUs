package com.pu.programus.project.DTO;

import com.pu.programus.project.Project;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ProjectList {
    private final List<Project> projects;
}
