package au.com.auspost.repository;

import au.com.auspost.domain.Earthquake;
import au.com.auspost.domain.Region;
import au.com.auspost.domain.Station;
import au.com.auspost.dto.EarthquakeDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class EarthquakeDTOToEntityConverter extends ModelMapper implements Converter<EarthquakeDTO, Earthquake> {
    @Inject
    private StationRepository stationRepository;
    @Inject
    private RegionRepository regionRepository;

    public EarthquakeDTOToEntityConverter()
    {
        TypeMap<EarthquakeDTO, Earthquake> typeMap = createTypeMap(EarthquakeDTO.class, Earthquake.class);
        typeMap.setPostConverter(this);

    }
    @Override
    public Earthquake convert(MappingContext<EarthquakeDTO, Earthquake> mappingContext) {

        //Get the station entity
        Station station = stationRepository.findBySrc(mappingContext.getSource().getStationSrc());
        //Get the region entity
        Region region = regionRepository.findByName(mappingContext.getSource().getRegionName());
        //Assignments
        mappingContext.getDestination().setStation(station);
        mappingContext.getDestination().setRegion(region);
        return mappingContext.getDestination();
    }

    public Earthquake map(EarthquakeDTO earthquakeDTO)
    {
        return map(earthquakeDTO, Earthquake.class);
    }
}
