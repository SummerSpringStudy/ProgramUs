package com.pu.programus.bridge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberProjectRepository extends JpaRepository<MemberProject, Long> {
    void deleteByMemberIdAndProjectId(@Param("memberId") Long memberId, @Param("projectId") Long projectId);
    Optional<MemberProject> findByMemberIdAndProjectId(@Param("memberId") Long memberId, @Param("projectId") Long projectId);
}
