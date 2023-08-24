package com.pu.programus.location.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class LocationList {
    @Builder.Default
    private List<String> locations = new ArrayList<>();
}
