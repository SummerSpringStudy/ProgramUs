package com.pu.programus.project;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String title;
    private String keyword; // 리스트로 만들필요 ex) 스프링 장고 -> project처럼 만들기

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Location location;

    private Date startTime;
    private Date endTime;

    private ProjectStatus status;
    private String description;

    // 포지션별 필요 인원 데이터 추가

    @OneToMany(mappedBy = "member")
    private List<MemberProject> memberProject;
}
