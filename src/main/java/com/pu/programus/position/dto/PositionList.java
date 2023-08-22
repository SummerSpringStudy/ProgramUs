package com.pu.programus.position.dto;

import com.pu.programus.position.Position;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PositionList {
    List<String> positions = new ArrayList<>();

    public static PositionList make(List<Position> positionList) {
        PositionList positions = new PositionList();
        List<String> stringPositions = positions.getPositions();

        for (Position position : positionList) {
            stringPositions.add(position.getName());
        }
        return positions;
    }
}
