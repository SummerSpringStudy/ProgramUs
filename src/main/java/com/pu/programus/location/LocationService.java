package com.pu.programus.location;

import com.pu.programus.location.DTO.LocationList;
import com.pu.programus.position.Position;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
        validateDuplicate(locationName);
        locationRepository.save(location);
    }
    private void validateDuplicate(String locationName) {
        if (locationRepository.findByName(locationName).isPresent())
            throw new IllegalArgumentException("이미 존재하는 지역입니다.");
    }
}
