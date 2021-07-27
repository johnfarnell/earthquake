package au.com.auspost.controller;

import au.com.auspost.domain.Station;
import au.com.auspost.dto.StationDTO;
import au.com.auspost.repository.StationRepository;
import org.apache.commons.collections.IteratorUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.List;

@RestController
public class StationController {
    @Inject
    private StationRepository stationRepository;
    @RequestMapping(value = "/stations", method = RequestMethod.GET)
    public ResponseEntity<Iterable<StationDTO>> getStations() {

        List<Station> stations = IteratorUtils.toList(stationRepository.findAll().iterator());
        ModelMapper mapper = new ModelMapper();
        Type listType = new TypeToken<List<StationDTO>>() {}.getType();
        List<StationDTO> stationDTOs = mapper.map(stations, listType);
        return new ResponseEntity<Iterable<StationDTO>>(stationDTOs, HttpStatus.OK);

    }
}