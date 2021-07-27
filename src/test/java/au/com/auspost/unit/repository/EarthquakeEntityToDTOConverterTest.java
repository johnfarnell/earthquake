package au.com.auspost.unit.repository;

import au.com.auspost.domain.Earthquake;
import au.com.auspost.domain.Region;
import au.com.auspost.domain.Station;
import au.com.auspost.dto.EarthquakeDTO;
import au.com.auspost.repository.EarthquakeEntityToDTOConverter;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EarthquakeEntityToDTOConverterTest {

    private static final String TIME_DATE_EARTHQUAKE_1 = "2004-05-22 22:12:58";
    private static final String TIME_DATE_EARTHQUAKE_2 = "2005-06-23 23:13:59";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void testMappingIsSuccessful()
    {
        EarthquakeEntityToDTOConverter target =  new EarthquakeEntityToDTOConverter();

        List<Earthquake> earthquakes = new ArrayList<>();

        Station station1 = new Station();
        station1.setSrc("AU");
        Station station2 = new Station();
        station2.setSrc("US");

        Region region1 = new Region();
        region1.setName("region 1");
        Region region2 = new Region();
        region2.setName("region 2");


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
        
        Earthquake earthquake1 = new Earthquake();
        earthquake1.setStation(station1);
        earthquake1.setRegion(region1);
        earthquake1.setEqId("eq-1-test");
        earthquake1.setTimeDate(timeDate1);
        earthquake1.setDepth(new BigDecimal("22.66"));
        earthquake1.setMagnitude(new BigDecimal("7.45"));
        earthquake1.setLatitude(80L);
        earthquake1.setLongitude(50L);

        earthquakes.add(earthquake1);

        Earthquake earthquake2 = new Earthquake();
        earthquake2.setStation(station2);
        earthquake2.setRegion(region2);
        earthquake2.setEqId("eq-2-test");
        earthquake2.setTimeDate(timeDate2);
        earthquake2.setDepth(new BigDecimal("32.66"));
        earthquake2.setMagnitude(new BigDecimal("8.45"));
        earthquake2.setLatitude(90L);
        earthquake2.setLongitude(60L);
        
        earthquakes.add(earthquake2);

        List<EarthquakeDTO> earthquakeDTOs = target.map(earthquakes);

        assertNotNull(earthquakeDTOs);
        assertEquals(2, earthquakeDTOs.size());

        EarthquakeDTO earthquakeDTO1 = earthquakeDTOs.get(0);
        assertEarthquakesEqual(station1, region1, earthquake1, earthquakeDTO1);

        EarthquakeDTO earthquakeDTO2 = earthquakeDTOs.get(1);
        assertEarthquakesEqual(station2, region2, earthquake2, earthquakeDTO2);

    }

    private void assertEarthquakesEqual(Station station, Region region, Earthquake earthquake, EarthquakeDTO earthquakeDTO) {
        assertEquals(earthquake.getEqId(), earthquakeDTO.getEqId());
        assertEquals(region.getName(), earthquakeDTO.getRegionName());
        assertEquals(station.getSrc(), earthquakeDTO.getStationSrc());
        assertEquals(earthquake.getTimeDate(), earthquakeDTO.getTimeDate());
        assertEquals(earthquake.getDepth(), earthquakeDTO.getDepth());
        assertEquals(earthquake.getMagnitude(), earthquakeDTO.getMagnitude());
        assertEquals(earthquake.getLongitude(), earthquakeDTO.getLongitude());
        assertEquals(earthquake.getLatitude(), earthquakeDTO.getLatitude());
    }
}
