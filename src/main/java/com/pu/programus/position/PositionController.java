package com.pu.programus.position;

import com.pu.programus.location.DTO.LocationList;
import com.pu.programus.position.dto.PositionList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/position")
public class PositionController {

    private final PositionService positionService;

    @GetMapping
    public ResponseEntity<PositionList> getAllPosition() {
        List<Position> positions = positionService.getPositionList();
        PositionList positionList = PositionList.make(positions);

        return ResponseEntity.status(HttpStatus.OK).body(positionList);
    }

    // Todo: 나중에 관리자 권한으로 바꾸기
    @PostMapping
    public void addPosition(String position) {
        log.info("[addPosition] add: {}", position);
        positionService.addPosition(position);
    }
}
