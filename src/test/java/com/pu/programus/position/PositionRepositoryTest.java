package com.pu.programus.position;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PositionRepositoryTest {

    @Autowired
    PositionRepository positionRepository;

    @Test
    void findByName() {
        Position position = new Position("디자이너");

        positionRepository.save(position);

        Optional<Position> result = positionRepository.findByName("디자이너");
        Assertions.assertEquals(result.get().toString(), position.toString());
    }

    @Test
    void findAll(){
        Position position1 = new Position("포지션1");
        Position position2 = new Position("포지션2");

        positionRepository.save(position1);
        positionRepository.save(position2);

        List<Position> positions = positionRepository.findAll();

        assertThat(positions.size()).isEqualTo(2);
    }

}