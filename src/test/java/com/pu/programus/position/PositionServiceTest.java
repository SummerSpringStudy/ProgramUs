package com.pu.programus.position;

import com.pu.programus.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PositionServiceTest {

    @Autowired
    PositionService positionService;
    @Autowired
    PositionRepository positionRepository;

    @Test
    public void 태그_단일_조회(){
        Position position = new Position("포지션");

        positionRepository.save(position);

        Position result = positionService.getPosition("포지션")
                .orElseThrow(() -> new IllegalArgumentException("Wrong position"));
        assertThat(result.getName()).isEqualTo(position.getName());
    }

    @Test
    public void 태그_전체_조회(){
        Position position1 = new Position("포지션1");
        Position position2 = new Position("포지션2");
        Position position3 = new Position("포지션3");

        positionRepository.save(position1);
        positionRepository.save(position2);
        positionRepository.save(position3);

        List<Position> positionList = positionService.getPositionList();
        assertThat(positionList.size()).isEqualTo(3);
    }

}
