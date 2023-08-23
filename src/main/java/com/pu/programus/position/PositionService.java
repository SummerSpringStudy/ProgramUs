package com.pu.programus.position;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class PositionService {

    private final PositionRepository positionRepository;

    public Optional<Position> getPosition(String name) {
        return positionRepository.findByName(name);
    }

    public List<Position> getPositionList() {
        List<Position> positions = positionRepository.findAll();
        return positions;
    }

    public void addPosition(String positionName) {
        Position position = Position.builder()
                .name(positionName)
                .build();

        // Todo: 중복검사
        Position savedPosition = positionRepository.save(position);
        // Todo: 새로 익셉션 만들기?
        if (savedPosition == null)
            throw new IOException("저장에 실패했습니다.");
    }
    
    //포지션 기준으로 프로젝트 조회
}
