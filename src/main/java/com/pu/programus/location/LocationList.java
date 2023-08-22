package com.pu.programus.location;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class LocationList {
    private List<String> locations = new ArrayList<>();
}
