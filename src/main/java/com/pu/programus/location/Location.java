package com.pu.programus.location;

import com.pu.programus.project.Project;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "location", cascade = CascadeType.PERSIST)
    private final List<Project> projects = new ArrayList<>();

    public Location(String name) {
        this.name = name;
    }

    public void add(Project project) {
        project.setLocation(this);
        this.projects.add(project);
    }
}
