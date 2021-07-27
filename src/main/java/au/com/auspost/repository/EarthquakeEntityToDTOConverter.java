package au.com.auspost.repository;

import au.com.auspost.domain.Earthquake;
import au.com.auspost.dto.EarthquakeDTO;
import org.apache.commons.collections.IteratorUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class EarthquakeEntityToDTOConverter  extends ModelMapper  {
    public List<EarthquakeDTO> map(Iterable<Earthquake> earthquakes)
    {
        Type listType = new TypeToken<List<EarthquakeDTO>>() {}.getType();
        return map(IteratorUtils.toList(earthquakes.iterator()), listType);
    }
    public EarthquakeDTO map (Earthquake earthquake)
    {
        return map(earthquake, EarthquakeDTO.class);
    }
}