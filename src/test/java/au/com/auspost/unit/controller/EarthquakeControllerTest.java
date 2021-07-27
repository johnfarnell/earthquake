package au.com.auspost.unit.controller;

import au.com.auspost.EarthquakeApplication;
import au.com.auspost.controller.EarthquakeController;
import au.com.auspost.domain.Earthquake;
import au.com.auspost.domain.Region;
import au.com.auspost.domain.Station;
import au.com.auspost.dto.EarthquakeDTO;
import au.com.auspost.repository.EarthquakeDTOToEntityConverter;
import au.com.auspost.repository.EarthquakeEntityToDTOConverter;
import au.com.auspost.repository.EarthquakeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EarthquakeApplication.class)
@ContextConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class EarthquakeControllerTest {

    @InjectMocks
    private EarthquakeController earthquakeController;

    @Mock
    private EarthquakeRepository earthquakeRepository;
    @Mock
    private EarthquakeEntityToDTOConverter earthquakeEntityToDTOConverter;
    @Mock
    private EarthquakeDTOToEntityConverter earthquakeDTOToEntityConverter;

    private MockMvc mockMvc;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(earthquakeController).build();
    }

    @Test
    public void testGetEarthquakesSuccessful() throws Exception
    {
        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();
        ArrayList<EarthquakeDTO> earthquakesDTOs = new ArrayList<EarthquakeDTO>();

        when(earthquakeRepository.findAll()).thenReturn(earthquakes);
        when(earthquakeEntityToDTOConverter.map(earthquakes)).thenReturn(earthquakesDTOs);

        mockMvc.perform(get("/earthquakes"))
                .andExpect(status().isOk());


        verify(earthquakeRepository, atLeastOnce()).findAll();
        verify(earthquakeEntityToDTOConverter, atLeastOnce()).map(earthquakes);
    }
    @Test
    public void testGetEarthquakeSuccessful() throws Exception
    {
        Earthquake earthquake = new Earthquake();
        EarthquakeDTO earthquakesDTO = new EarthquakeDTO();

        when(earthquakeRepository.findOne(2L)).thenReturn(earthquake);
        when(earthquakeEntityToDTOConverter.map(earthquake)).thenReturn(earthquakesDTO);

        mockMvc.perform(get("/earthquakes/2"))
                .andExpect(status().isOk());


        verify(earthquakeRepository, atLeastOnce()).findOne(2L);
        verify(earthquakeEntityToDTOConverter, atLeastOnce()).map(earthquake);
    }
    @Test
    public void testGetEarthquakeNotFoundReturnedWhenNoEarthquakeExists() throws Exception
    {

        //Mock a NULL, which should force an exception
        when(earthquakeRepository.findOne(2L)).thenReturn(null);

        mockMvc.perform(get("/earthquakes/2"))
                .andExpect(status().isNotFound());


        verify(earthquakeRepository, atLeastOnce()).findOne(2L);
    }
    @Test
    public void testCreateEarthquakeSuccessful() throws Exception
    {

        //Create the Earthquake DTO to allow a jsonstring to be generated
        EarthquakeDTO earthquakeDTO = new EarthquakeDTO();
        earthquakeDTO.setDepth(new BigDecimal("77.75"));
        earthquakeDTO.setMagnitude(new BigDecimal("4.65"));
        earthquakeDTO.setLatitude(45L);
        earthquakeDTO.setLongitude(65L);
        earthquakeDTO.setRegionName("region");
        earthquakeDTO.setStationSrc("station");
        Calendar timeDateSuppliedCalender = Calendar.getInstance();
        //int year, int month, int date, int hourOfDay, int minute, int second
        timeDateSuppliedCalender.set(2006, 4, 25, 23, 11, 22);
        timeDateSuppliedCalender.set(Calendar.MILLISECOND, 0);
        earthquakeDTO.setTimeDate(timeDateSuppliedCalender.getTime());
        earthquakeDTO.setEqId("testCreateEarthquakeSuccessful");

        ObjectMapper objectMapper = new ObjectMapper();
        //Create the json string
        String jsonIn = objectMapper.writeValueAsString(earthquakeDTO);

        //create the earthquake - add station and region to ensure it works
        Earthquake earthquake = new Earthquake();
        earthquake.setStation(new Station());
        earthquake.setRegion(new Region());

        when(earthquakeDTOToEntityConverter.map(any())).thenReturn(earthquake);
        when(earthquakeRepository.save(earthquake)).thenReturn(earthquake);

        ResultActions result = mockMvc.
                perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/earthquakes")
                        .content(jsonIn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isCreated());


        //verify the save is called once only
        verify(earthquakeRepository, times(1)).save(earthquake);
        verify(earthquakeDTOToEntityConverter, atLeastOnce()).map(any());
    }
    @Test
    public void testCreateEarthquakeNotFoudnReturnedWhenNoRegionExists() throws Exception
    {

        //Create the Earthquake DTO to allow a jsonstring to be generated
        EarthquakeDTO earthquakeDTO = new EarthquakeDTO();
        earthquakeDTO.setDepth(new BigDecimal("77.75"));
        earthquakeDTO.setMagnitude(new BigDecimal("4.65"));
        earthquakeDTO.setLatitude(45L);
        earthquakeDTO.setLongitude(65L);
        earthquakeDTO.setRegionName("region");
        earthquakeDTO.setStationSrc("station");
        Calendar timeDateSuppliedCalender = Calendar.getInstance();
        //int year, int month, int date, int hourOfDay, int minute, int second
        timeDateSuppliedCalender.set(2006, 4, 25, 23, 11, 22);
        timeDateSuppliedCalender.set(Calendar.MILLISECOND, 0);
        earthquakeDTO.setTimeDate(timeDateSuppliedCalender.getTime());
        earthquakeDTO.setEqId("testCreateEarthquakeSuccessful");

        ObjectMapper objectMapper = new ObjectMapper();
        //Create the json string
        String jsonIn = objectMapper.writeValueAsString(earthquakeDTO);

        //create the earthquake - Add Station BUT NO REGION
        Earthquake earthquake = new Earthquake();
        earthquake.setStation(new Station());

        when(earthquakeDTOToEntityConverter.map(any())).thenReturn(earthquake);

        ResultActions result = mockMvc.
                perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/earthquakes")
                        .content(jsonIn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());


        //verify the save is NOT Called
        verify(earthquakeRepository, never()).save(earthquake);
        verify(earthquakeDTOToEntityConverter, atLeastOnce()).map(any());
    }
    @Test
    public void testCreateEarthquakeNotFoudnReturnedWhenNoStationExists() throws Exception
    {

        //Create the Earthquake DTO to allow a jsonstring to be generated
        EarthquakeDTO earthquakeDTO = new EarthquakeDTO();
        earthquakeDTO.setDepth(new BigDecimal("77.75"));
        earthquakeDTO.setMagnitude(new BigDecimal("4.65"));
        earthquakeDTO.setLatitude(45L);
        earthquakeDTO.setLongitude(65L);
        earthquakeDTO.setRegionName("region");
        earthquakeDTO.setStationSrc("station");
        Calendar timeDateSuppliedCalender = Calendar.getInstance();
        //int year, int month, int date, int hourOfDay, int minute, int second
        timeDateSuppliedCalender.set(2006, 4, 25, 23, 11, 22);
        timeDateSuppliedCalender.set(Calendar.MILLISECOND, 0);
        earthquakeDTO.setTimeDate(timeDateSuppliedCalender.getTime());
        earthquakeDTO.setEqId("testCreateEarthquakeSuccessful");

        ObjectMapper objectMapper = new ObjectMapper();
        //Create the json string
        String jsonIn = objectMapper.writeValueAsString(earthquakeDTO);

        //create the earthquake - Add region BUT NO STATION
        Earthquake earthquake = new Earthquake();
        earthquake.setRegion(new Region());

        when(earthquakeDTOToEntityConverter.map(any())).thenReturn(earthquake);

        ResultActions result = mockMvc.
                perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/earthquakes")
                        .content(jsonIn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());


        //verify the save is NOT Called
        verify(earthquakeRepository, never()).save(earthquake);
        verify(earthquakeDTOToEntityConverter, atLeastOnce()).map(any());
    }
}
