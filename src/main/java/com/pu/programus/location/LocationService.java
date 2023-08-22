package com.pu.programus.location;

import com.pu.programus.location.DTO.LocationList;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> getAllLocation() {
        return locationRepository.findAll();
    }

    public LocationList getAllLocationNames() {
        List<String> result = new ArrayList<>();
        for (Location l : locationRepository.findAll()) {
            result.add(l.getName());
        }
        return LocationList.builder().locations(result).build();
    }

    public void addLocation(String locationName) {
        Location location = Location.builder()
                .name(locationName)
                .build();

        Location savedLocation = locationRepository.save(location);
        // Todo: 새로 익셉션 만들기?
        if (savedLocation == null)
            throw new IOException("저장에 실패했습니다.");
    }
}
