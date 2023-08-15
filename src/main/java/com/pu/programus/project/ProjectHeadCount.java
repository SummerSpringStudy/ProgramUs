package com.pu.programus.project;

import com.pu.programus.position.Position;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
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

    @Override
    public String toString() {
        return "ProjectHeadCount{" +
                "id=" + id +
                ", nowHeadCount=" + nowHeadCount +
                ", maxHeadCount=" + maxHeadCount +
                '}';
    }
}
