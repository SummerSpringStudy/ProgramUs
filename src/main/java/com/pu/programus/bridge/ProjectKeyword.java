package com.pu.programus.bridge;

import com.pu.programus.keyword.Keyword;
import com.pu.programus.project.Project;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Data
public class ProjectKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Keyword keyword;
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public String getKeywordValue(){
        return getKeyword().getValue();
    }
}
