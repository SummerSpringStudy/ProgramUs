package com.pu.programus.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocationController {
    @Autowired
    private LocationService locationService;

    /**
     * 모든 지역 정보를 가져오는 API
     *
     * @return ResponseEntity< List < Location > > 200 OK, 지역 정보 목록
     */
    @GetMapping("/location")
    public ResponseEntity<List<Location>> getAllLocation() {
        List<Location> locationList = locationService.getAllLocation();
        return ResponseEntity.status(HttpStatus.OK).body(locationList);
    }
}