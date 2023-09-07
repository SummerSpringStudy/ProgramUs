package com.pu.programus.position.DTO;

import com.pu.programus.position.Position;
import lombok.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Builder
@ToString
public class PositionList {
    private final List<PositionDTO> positions = new ArrayList<>();

    public static PositionList make(List<Position> positionList) {
        PositionList positions = new PositionList();
        List<PositionDTO> positionDTOs = positions.getPositions();

        for (Position position : positionList) {
            PositionDTO positionDTO = new PositionDTO(position.getName());
            positionDTOs.add(positionDTO);
        }
        return positions;
    }
}
