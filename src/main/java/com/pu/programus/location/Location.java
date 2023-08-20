package com.pu.programus.location;

import com.pu.programus.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LOCATION_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private final List<Project> projects = new ArrayList<>();

    public void add(Project project) {
        project.setLocation(this);
        this.projects.add(project);
    }
}
