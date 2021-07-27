package au.com.auspost.integration.controller;

import au.com.auspost.EarthquakeApplication;
import au.com.auspost.domain.Region;
import au.com.auspost.repository.RegionRepository;
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
public class RegionControllerIT {
    @Inject
    private RegionRepository regionRepository;
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
        regionRepository.deleteAll();
    }

    private void createRegions(){

        Region region1 = saveRegion("test region 1");
        Region region2 = saveRegion("test region 2");
    }

    private Region saveRegion(String name)
    {
        Region region = new Region();
        region.setName(name);
        region = regionRepository.save(region);
        return region;
    }

    @Test
    public void testGetRegions() throws Exception
    {
        /*
        Test to see that the restful GET will return an earthquake for the supplied if
         */
        createRegions();

        ResultActions result = mockMvc.perform(get("/regions"));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));

        //[{"id":1,"region":"test region 1"},{"id":2,"region":"test region 2"}]
        String json=result.andReturn().getResponse().getContentAsString();

        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$", Matchers.hasSize(2)));
        result.andExpect(jsonPath("$[0].id", Matchers.is(1)));
        result.andExpect(jsonPath("$[0].region", Matchers.is("test region 1")));
        result.andExpect(jsonPath("$[1].id", Matchers.is(2)));
        result.andExpect(jsonPath("$[1].region", Matchers.is("test region 2")));

    }


}
