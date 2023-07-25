package com.pu.programus.position;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

@SpringBootTest
class PositionRepositoryTest {

    @Autowired
    PositionRepository positionRepository;

    @Test
    void save() {
        Position position = new Position("디자이너");

        positionRepository.save(position);

        Optional<Position> result = positionRepository.findByName("디자이너");
        Assertions.assertEquals(result.get(), position);
    }

}