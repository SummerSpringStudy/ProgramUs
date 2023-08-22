package com.pu.programus.location;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    /**
     * 모든 지역 정보를 가져오는 API
     *
     * @return 200 OK, 지역 정보 목록
     */
    @GetMapping("/location")
    public ResponseEntity<List<String>> getAllLocation() {
        List<String> result = locationService.getAllLocationNames();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
