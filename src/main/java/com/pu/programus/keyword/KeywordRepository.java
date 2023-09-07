package com.pu.programus.keyword;

import com.pu.programus.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByValue(String value);
//    List<Keyword> findAllByValues(List<String> values);
}
