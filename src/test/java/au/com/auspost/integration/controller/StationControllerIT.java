package au.com.auspost.integration.controller;

import au.com.auspost.EarthquakeApplication;
import au.com.auspost.domain.Station;
import au.com.auspost.repository.StationRepository;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EarthquakeApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class StationControllerIT {
    @Inject
    private StationRepository stationRepository;
    private MockMvc mockMvc;
    @Inject
    private WebApplicationContext webApplicationContext;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = webAppContextSetup(webApplicationContext).build();

    }
    @After
    public void tearDown() throws Exception {
        stationRepository.deleteAll();
    }

    private void createStations(){

        Station station1 = saveStation("test station 1");
        Station station2 = saveStation("test station 2");
    }

    private Station saveStation(String name)
    {
        Station station = new Station();
        station.setSrc(name);
        station = stationRepository.save(station);
        return station;
    }

    @Test
    public void testGetStations() throws Exception
    {
        /*
        Test to see that the restful GET will return an earthquake for the supplied if
         */
        createStations();

        ResultActions result = mockMvc.perform(get("/stations"));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));

        //[[{"id":1,"src":"test station 1"},{"id":2,"src":"test station 2"}]
        String json=result.andReturn().getResponse().getContentAsString();

        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$", Matchers.hasSize(2)));
        result.andExpect(jsonPath("$[0].id", Matchers.is(1)));
        result.andExpect(jsonPath("$[0].src", Matchers.is("test station 1")));
        result.andExpect(jsonPath("$[1].id", Matchers.is(2)));
        result.andExpect(jsonPath("$[1].src", Matchers.is("test station 2")));

    }


}
