package com.pu.programus.position;

import com.pu.programus.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    public List<Position> getPositionList() {
        List<Position> positions = positionRepository.findAll();
        return positions;
    }

}
