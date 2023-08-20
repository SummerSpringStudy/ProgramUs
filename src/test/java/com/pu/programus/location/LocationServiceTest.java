package com.pu.programus.location;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class LocationServiceTest {
    @Autowired
    LocationService locationService;
    @Autowired
    LocationRepository locationRepository;

    @Test
    void get_locations_test() {
        // given
        Location seoul = Location.builder().name("서울").build();
        Location pusan = Location.builder().name("부산").build();
        Location daegu = Location.builder().name("대구").build();
        locationRepository.save(seoul);
        locationRepository.save(pusan);
        locationRepository.save(daegu);

        // when
        List<Location> locations = locationService.getAllLocation();

        // then
        Assertions.assertThat(locations).contains(seoul, pusan, daegu);
    }

}