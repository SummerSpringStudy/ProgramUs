package com.pu.programus.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> getAllLocation() {
        return locationRepository.findAll();
    }

    public List<String> getAllLocationNames() {
        List<String> result = new ArrayList<>();
        for (Location l : locationRepository.findAll()) {
            result.add(l.getName());
        }
        return result;
    }
}
