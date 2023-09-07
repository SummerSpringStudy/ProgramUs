package com.pu.programus.location;

import com.pu.programus.location.DTO.LocationList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;

    /**
     * 모든 지역 정보를 가져오는 API
     *
     * @return 200 OK, 지역 정보 목록
     */
    @GetMapping
    public ResponseEntity<LocationList> getAllLocation() {
        LocationList result = locationService.getAllLocationNames();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // Todo: 나중에 관리자 권한으로 바꾸기
    @PostMapping
    public void addLocation(String location) {
        log.info("[addLocation] add: {}", location);
        locationService.addLocation(location);
    }
}
