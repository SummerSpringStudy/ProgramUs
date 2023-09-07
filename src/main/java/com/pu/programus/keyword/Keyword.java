package com.pu.programus.keyword;

import com.pu.programus.bridge.ProjectKeyword;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL)
    private List<ProjectKeyword> projectKeywords = new ArrayList<>();
}
