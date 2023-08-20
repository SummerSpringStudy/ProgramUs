package com.pu.programus.location;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService service;

    @MockBean
    private LocationRepository repository;

    @Test
    void getAllLocationShouldReturnLocationsFromService() throws Exception {
        // given
        Location seoul = Location.builder().name("서울").build();
        Location pusan = Location.builder().name("부산").build();
        Location daegu = Location.builder().name("대구").build();
        repository.save(seoul);
        repository.save(pusan);
        repository.save(daegu);

        List<Location> locations = new ArrayList<>(List.of(seoul, pusan, daegu));
        // when
        when(service.getAllLocation()).thenReturn(locations);

        // then
        this.mockMvc.perform(get("/location")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("['서울', '부산', '대구']"));
    }

}