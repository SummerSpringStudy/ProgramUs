package com.pu.programus.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

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
