package au.com.auspost.unit.repository;

import au.com.auspost.domain.Earthquake;
import au.com.auspost.domain.Region;
import au.com.auspost.domain.Station;
import au.com.auspost.dto.EarthquakeDTO;
import au.com.auspost.repository.EarthquakeDTOToEntityConverter;
import au.com.auspost.repository.RegionRepository;
import au.com.auspost.repository.StationRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class EarthquakeDTOToEntityConverterTest {

    @Mock
    private StationRepository stationRepository;
    @Mock
    private RegionRepository regionRepository;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testMappingIsSuccessful()
    {
        EarthquakeDTOToEntityConverter target = new EarthquakeDTOToEntityConverter();

        ReflectionTestUtils.setField(target, "stationRepository",
                stationRepository);
        ReflectionTestUtils.setField(target, "regionRepository",
                regionRepository);

        //Mock calls to the region and station repositories
        Station station= new Station();
        station.setSrc("src");
        station.setId(1L);
        Region region= new Region();
        region.setName("region");
        region.setId(2L);

        when(stationRepository.findBySrc(station.getSrc())).thenReturn(station);
        when(regionRepository.findByName(region.getName())).thenReturn(region);

        //Create the Earthquake DTO
        EarthquakeDTO earthquakeDTO = new EarthquakeDTO();
        earthquakeDTO.setDepth(new BigDecimal("77.75"));
        earthquakeDTO.setMagnitude(new BigDecimal("4.65"));
        earthquakeDTO.setLatitude(45L);
        earthquakeDTO.setLongitude(65L);
        earthquakeDTO.setRegionName(region.getName());
        earthquakeDTO.setStationSrc(station.getSrc());
        Calendar timeDateSuppliedCalender = Calendar.getInstance();
        //int year, int month, int date, int hourOfDay, int minute, int second
        timeDateSuppliedCalender.set(2006, 4, 25, 23, 11, 22);
        timeDateSuppliedCalender.set(Calendar.MILLISECOND, 0);
        earthquakeDTO.setTimeDate(timeDateSuppliedCalender.getTime());
        earthquakeDTO.setEqId("testMappingIsSuccessful");

        Earthquake earthquake = target.map(earthquakeDTO);

        assertEquals("testMappingIsSuccessful", earthquake.getEqId());
        assertEquals(new BigDecimal("77.75"), earthquake.getDepth());
        assertEquals(new BigDecimal("4.65"), earthquake.getMagnitude());
        assertEquals(45L, earthquake.getLatitude().longValue());
        assertEquals(65L, earthquake.getLongitude().longValue());
        assertEquals(region.getName(), earthquake.getRegion().getName());
        assertEquals(timeDateSuppliedCalender.getTime(), earthquake.getTimeDate());
        assertEquals(station.getSrc(), earthquake.getStation().getSrc());

    }
}
