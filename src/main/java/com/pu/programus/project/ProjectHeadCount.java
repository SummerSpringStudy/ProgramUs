package com.pu.programus.project;

import com.pu.programus.position.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ProjectHeadCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Position position;

    private int nowHeadCount;
    private int maxHeadCount;
}
