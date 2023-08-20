package com.pu.programus.location;

import com.pu.programus.location.DTO.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LocationController {
    @Autowired
    private LocationService locationService;

    /**
     * 모든 지역 정보를 가져오는 API
     *
     * @return ResponseEntity<List < Location> > 200 OK, 지역 정보 목록
     */
    @GetMapping("/location")
    public ResponseEntity<List<String>> getAllLocation() {
        List<Location> locationList = locationService.getAllLocation();
        List<String> result = new ArrayList<>();
        for (Location l : locationList) {
            result.add(l.getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
