package com.pu.programus.bridge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberProjectRepository extends JpaRepository<MemberProject, Long> {
    void deleteByMemberIdAndProjectId(Long memberId,Long projectId);
}
