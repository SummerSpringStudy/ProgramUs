package com.pu.programus.keyword;

import com.pu.programus.bridge.ProjectKeyword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;

    @OneToMany(mappedBy = "keyword")
    private List<ProjectKeyword> projectKeywords = new ArrayList<>();
}
