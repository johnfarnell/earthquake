package au.com.auspost.unit.controller;

import au.com.auspost.EarthquakeApplication;
import au.com.auspost.controller.StationController;
import au.com.auspost.domain.Station;
import au.com.auspost.repository.StationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EarthquakeApplication.class)
@ContextConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class StationControllerTest {

    @InjectMocks
    private StationController stationController;
    @Mock
    private StationRepository stationRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(stationController).build();
    }

    @Test
    public void testGetStationsSuccessful() throws Exception
    {
        ArrayList<Station> stations = new ArrayList<Station>();

        when(stationRepository.findAll()).thenReturn(stations);

        mockMvc.perform(get("/stations"))
                .andExpect(status().isOk());


        verify(stationRepository, atLeastOnce()).findAll();
    }
}
