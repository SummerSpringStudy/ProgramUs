package com.pu.programus.location;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class LocationControllerTest {
    @Autowired
    private LocationController locationController;
    @Test
    void get_all_location_test() {
        // given

        // when

        // then
        Assertions.assertThat(locationController).isNotNull();
    }

}