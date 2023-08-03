package com.pu.programus.position;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
}
