package com.pu.programus.comment;

import com.pu.programus.member.Member;
import com.pu.programus.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByMember(Member member);
    List<Comment> findAllByProject(Project project);
}
