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
        validateDuplicate(positionName);

        Position position = Position.builder()
                .name(positionName)
                .build();
        positionRepository.save(position);
    }

    private void validateDuplicate(String positionName) {
        if (positionRepository.findByName(positionName).isPresent())
            throw new IllegalArgumentException("이미 존재하는 모집 분야 입니다.");
    }

    //포지션 기준으로 프로젝트 조회
}
