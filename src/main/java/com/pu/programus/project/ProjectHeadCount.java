package com.pu.programus.project;

import com.pu.programus.position.Position;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@Builder
public class ProjectHeadCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="PROJECT_ID")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="POSITION_ID")
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
