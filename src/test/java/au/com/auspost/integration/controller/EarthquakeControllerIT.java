package au.com.auspost.integration.controller;

import au.com.auspost.EarthquakeApplication;
import au.com.auspost.domain.Earthquake;
import au.com.auspost.domain.Region;
import au.com.auspost.domain.Station;
import au.com.auspost.domain.User;
import au.com.auspost.dto.EarthquakeDTO;
import au.com.auspost.repository.EarthquakeRepository;
import au.com.auspost.repository.RegionRepository;
import au.com.auspost.repository.StationRepository;
import au.com.auspost.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import javax.servlet.Filter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EarthquakeApplication.class)
@WebAppConfiguration
@TestPropertySource("/test.properties")
@ActiveProfiles("test")
public class EarthquakeControllerIT {
    private static final String TIME_DATE_EARTHQUAKE_1 = "2004-05-22 22:12:58";
    private static final String TIME_DATE_EARTHQUAKE_2 = "2005-06-23 23:13:59";
    @Inject
    private EarthquakeRepository earthquakeRepository;
    @Inject
    private StationRepository stationRepository;
    @Inject
    private RegionRepository regionRepository;
    @Inject
    private UserRepository userRepository;
    private MockMvc mockMvc;
    @Autowired
    private Environment env;
    @Inject
    private WebApplicationContext webApplicationContext;

    private Map<Long, Station> stations = new HashMap<Long, Station>();
    private Map<Long, Region> regions = new HashMap<Long, Region>();
    private Map<Long, Earthquake> earthquakes = new HashMap<Long, Earthquake>();

    @Autowired
    private Filter springSecurityFilterChain; //Added to ensure Security is available in tests
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();

    }

    @After
    public void tearDown() throws Exception {

        earthquakeRepository.deleteAll();
        stationRepository.deleteAll();
        regionRepository.deleteAll();
        userRepository.deleteAll();

        stations.clear();
        regions.clear();
        earthquakes.clear();
    }

    private void create2Earthquakes(){

        Station station1 = saveStation("UK");
        Station station2 = saveStation("AU");
        Region region1 = saveRegion("test region 1");
        Region region2 = saveRegion("test region 2");


        Date timeDate1 = null;
        Date timeDate2 = null;
        try {
            timeDate1 = sdf.parse(TIME_DATE_EARTHQUAKE_1);
            timeDate2 = sdf.parse(TIME_DATE_EARTHQUAKE_2);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        long index = 1;
        Earthquake earthquake1 = new Earthquake();
        earthquake1.setStation(station1);
        earthquake1.setRegion(region1);
        earthquake1.setEqId("eq-1-test");
        earthquake1.setTimeDate(timeDate1);
        earthquake1.setDepth(new BigDecimal("22.66"));
        earthquake1.setMagnitude(new BigDecimal("7.45"));
        earthquake1.setLatitude(80L);
        earthquake1.setLongitude(50L);
        earthquake1 = earthquakeRepository.save(earthquake1);
        earthquakes.put(index++, earthquake1);

        Earthquake earthquake2 = new Earthquake();
        earthquake2.setStation(station2);
        earthquake2.setRegion(region2);
        earthquake2.setEqId("eq-2-test");
        earthquake2.setTimeDate(timeDate2);
        earthquake2.setDepth(new BigDecimal("32.66"));
        earthquake2.setMagnitude(new BigDecimal("8.45"));
        earthquake2.setLatitude(90L);
        earthquake2.setLongitude(60L);
        earthquake2 = earthquakeRepository.save(earthquake2);
        earthquakes.put(index++, earthquake2);
    }

    private Region saveRegion(String name)
    {
        Region region = new Region();
        region.setName(name);
        region = regionRepository.save(region);
        regions.put(region.getId(), region);
        return region;
    }
    private Station saveStation(String src)
    {
        Station station = new Station();
        station.setSrc(src);
        station = stationRepository.save(station);
        stations.put(station.getId(), station);

        return station;
    }
    @Test
    public void testGetEarthQuake() throws Exception
    {
        /*
        Test to see that the restful GET will return an earthquake for the supplied id
         */
        create2Earthquakes();

        /*
        Get the 2nd earthquake
         */
        Earthquake earthquake = earthquakes.get(2L);

        //Ge6 the earthquake via it's id
        ResultActions result = mockMvc.perform(get("/earthquakes/" + earthquake.getIid()));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));

        String json=result.andReturn().getResponse().getContentAsString();

        result.andExpect(jsonPath("$.url", containsString("/earthquakes/"  + earthquake.getIid())));
        result.andExpect(jsonPath("$.data.src", Matchers.is("AU")));
        result.andExpect(jsonPath("$.data.eqid", Matchers.is("eq-2-test")));
        result.andExpect(jsonPath("$.data.lat", Matchers.is(90)));
        result.andExpect(jsonPath("$.data.lon", Matchers.is(60)));
        result.andExpect(jsonPath("$.data.timedate", Matchers.is(TIME_DATE_EARTHQUAKE_2)));
        result.andExpect(jsonPath("$.data.magnitude", Matchers.is(8.45)));
        result.andExpect(jsonPath("$.data.depth", Matchers.is(32.66)));
        result.andExpect(jsonPath("$.data.region", Matchers.is("test region 2")));

    }
    @Test
    public void testGetEarthquakes() throws Exception
    {
        /*
        Test to see that the restful GET will return all the earthquakes
         */
        create2Earthquakes();

        ResultActions result = mockMvc.perform(get("/earthquakes"));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));

        String json=result.andReturn().getResponse().getContentAsString();

        result.andExpect(jsonPath("$.url", containsString("/earthquakes")));
        result.andExpect(jsonPath("$.data.earthquakes").isArray());
        result.andExpect(jsonPath("$.data.earthquakes", Matchers.hasSize(2)));
        result.andExpect(jsonPath("$.data.earthquakes[0].data.src", Matchers.is("UK")));
        result.andExpect(jsonPath("$.data.earthquakes[0].data.eqid", Matchers.is("eq-1-test")));
        result.andExpect(jsonPath("$.data.earthquakes[0].data.lat", Matchers.is(80)));
        result.andExpect(jsonPath("$.data.earthquakes[0].data.lon", Matchers.is(50)));
        result.andExpect(jsonPath("$.data.earthquakes[0].data.timedate", Matchers.is(TIME_DATE_EARTHQUAKE_1)));
        result.andExpect(jsonPath("$.data.earthquakes[0].data.magnitude", Matchers.is(7.45)));
        result.andExpect(jsonPath("$.data.earthquakes[0].data.depth", Matchers.is(22.66)));
        result.andExpect(jsonPath("$.data.earthquakes[0].data.region", Matchers.is("test region 1")));

        result.andExpect(jsonPath("$.data.earthquakes[1].data.src", Matchers.is("AU")));
        result.andExpect(jsonPath("$.data.earthquakes[1].data.eqid", Matchers.is("eq-2-test")));
        result.andExpect(jsonPath("$.data.earthquakes[1].data.lat", Matchers.is(90)));
        result.andExpect(jsonPath("$.data.earthquakes[1].data.lon", Matchers.is(60)));
        result.andExpect(jsonPath("$.data.earthquakes[1].data.timedate", Matchers.is(TIME_DATE_EARTHQUAKE_2)));
        result.andExpect(jsonPath("$.data.earthquakes[1].data.magnitude", Matchers.is(8.45)));
        result.andExpect(jsonPath("$.data.earthquakes[1].data.depth", Matchers.is(32.66)));
        result.andExpect(jsonPath("$.data.earthquakes[1].data.region", Matchers.is("test region 2")));

    }

    @Test
    public void testPostEarthquakeAuthenticatedUser() throws Exception
    {
        create2Earthquakes();

        //create the user which will be authenticated

        User userEntity = new User();
        userEntity.setUsername("admin");
        userEntity.setFirstName("Joe");
        userEntity.setLastName("Bloggs");
        userEntity.setPassword("password");

        userRepository.save(userEntity);

        //Create the Earthquake DTO
        EarthquakeDTO earthquakeDTO = new EarthquakeDTO();
        earthquakeDTO.setDepth(new BigDecimal("77.75"));
        earthquakeDTO.setMagnitude(new BigDecimal("4.65"));
        earthquakeDTO.setLatitude(45L);
        earthquakeDTO.setLongitude(65L);
        earthquakeDTO.setRegionName("test region 2");
        earthquakeDTO.setStationSrc("AU");
        Calendar timeDateSuppliedCalender = Calendar.getInstance();
        //int year, int month, int date, int hourOfDay, int minute, int second
        timeDateSuppliedCalender.set(2006, 4, 25, 23, 11, 22);
        timeDateSuppliedCalender.set(Calendar.MILLISECOND, 0);
        earthquakeDTO.setTimeDate(timeDateSuppliedCalender.getTime());
        earthquakeDTO.setEqId("testPostEarthquakeAuthenticatedUser");

        /*
        Convert this DTO into the json string, and submit -Note we ARE inclusing the user name passwowrd of the usee
         */
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonIn = objectMapper.writeValueAsString(earthquakeDTO);
        ResultActions result = mockMvc.
                perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/earthquakes")
                        .with(user("admin").password("password"))
                        .content(jsonIn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isCreated());

        Object  locationValue = result.andReturn().getResponse().getHeaderValue("Location");
        assertTrue(locationValue instanceof String);

        // The location value should be the url of the newly created earthquake, get that id
        // and check we can find it in our repository
        String id = locationValue.toString().substring((locationValue.toString()).length() -1);

        assertNotNull(id);

        Long idAsLong = Long.valueOf(id);

        // Now read the earthquake table using the id retrieved
        Earthquake earthquake = earthquakeRepository.findOne(idAsLong);

        //The earthquake should exists and it should match exactly what we have just submitted
        assertNotNull(earthquake);

        assertEquals("testPostEarthquakeAuthenticatedUser", earthquake.getEqId());
        assertEquals(new BigDecimal("77.75"), earthquake.getDepth());
        assertEquals(new BigDecimal("4.65"), earthquake.getMagnitude());
        assertEquals(45L, earthquake.getLatitude().longValue());
        assertEquals(65L, earthquake.getLongitude().longValue());
        assertEquals("test region 2", earthquake.getRegion().getName());
        assertEquals(timeDateSuppliedCalender.getTime(), earthquake.getTimeDate());
        assertEquals("AU", earthquake.getStation().getSrc());


    }

    @Test
    public void testPostEarthquakeWithNoAuthenticatedUser() throws Exception
    {

        //Create the Earthquake DTO
        EarthquakeDTO earthquakeDTO = new EarthquakeDTO();
        earthquakeDTO.setDepth(new BigDecimal("77.75"));
        earthquakeDTO.setMagnitude(new BigDecimal("4.65"));
        earthquakeDTO.setLatitude(45L);
        earthquakeDTO.setLongitude(65L);
        earthquakeDTO.setRegionName("test region 2");
        earthquakeDTO.setStationSrc("AU");
        Calendar cal = Calendar.getInstance();
        //int year, int month, int date, int hourOfDay, int minute, int second
        cal.set(2006, 4, 25, 23, 11, 22);
        cal.set(Calendar.MILLISECOND, 0);
        earthquakeDTO.setTimeDate(cal.getTime());
        earthquakeDTO.setEqId("testPostEarthquakeAuthenticatedUser");

        /*
        Convert this DTO into the json string, and submit -Note we ARE NOT including the user
         */
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonIn = objectMapper.writeValueAsString(earthquakeDTO);
        ResultActions result = mockMvc.
                perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/earthquakes")
                        .content(jsonIn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        /*
        Expect unauthorised as no user was supplied
         */
        result.andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

}
