package au.com.auspost.controller;

import au.com.auspost.domain.Earthquake;
import au.com.auspost.dto.EarthquakeAllDTO;
import au.com.auspost.dto.EarthquakeDTO;
import au.com.auspost.dto.ResponseDTO;
import au.com.auspost.exception.ResourceNotFoundException;
import au.com.auspost.repository.EarthquakeDTOToEntityConverter;
import au.com.auspost.repository.EarthquakeEntityToDTOConverter;
import au.com.auspost.repository.EarthquakeRepository;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class EarthquakeController {
    @Inject
    private EarthquakeRepository earthquakeRepository;
    @Inject
    private EarthquakeEntityToDTOConverter earthquakeEntityToDTOConverter;
    @Inject
    private EarthquakeDTOToEntityConverter earthquakeDTOToEntityConverter;

    @RequestMapping(value="/earthquakes", method= RequestMethod.GET)
    public ResponseEntity<ResponseDTO<EarthquakeAllDTO>> getEarthquakes() {

        /*
        Get all the earthquakes via a repository call
         */
        Iterable<Earthquake> earthquakes = earthquakeRepository.findAll();
        /*
        Map to the DTO
         */
        List<EarthquakeDTO> earthquakeDTOS = earthquakeEntityToDTOConverter.map(earthquakes);
        /*
        Assign to the Generic EarthquakeAllDTO and return the JSON output
         */
        ResponseDTO<EarthquakeAllDTO> result = getResponseDTO(earthquakeDTOS);
        return new ResponseEntity<ResponseDTO<EarthquakeAllDTO>>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/earthquakes/{earthquakeId}", method=RequestMethod.GET)
    public ResponseEntity<ResponseDTO<EarthquakeDTO>> getEarthquake(@PathVariable Long earthquakeId) {
        /*
        Get the earthquakes for the supplied id
         */
        Earthquake earthquake = findEarthquake(earthquakeId);
        EarthquakeDTO earthquakeDTO = earthquakeEntityToDTOConverter.map(earthquake);
        return new ResponseEntity<ResponseDTO<EarthquakeDTO>>(getResponseDTO(earthquakeDTO), HttpStatus.OK);
    }


    @RequestMapping(value="/earthquakes", method= RequestMethod.POST)
    public ResponseEntity<Void> createEarthquake(@Valid @RequestBody EarthquakeDTO earthquakeDTO) {

        /*
        Map the supplied DTO to the Earthquake entity
         */
        Earthquake earthquake = earthquakeDTOToEntityConverter.map(earthquakeDTO);
        /*
        Just confirm we could find the station and region ok
         */
        if (earthquake.getStation() == null)
        {
            throw new ResourceNotFoundException("Station with src name " + earthquakeDTO.getStationSrc() + " not found");
        }

        if (earthquake.getRegion() == null)
        {
            throw new ResourceNotFoundException("Region with name " + earthquakeDTO.getRegionName() + " not found");
        }

        //Save the earthquake
        earthquake = earthquakeRepository.save(earthquake);
        // Set the location header for the newly created resource
        HttpHeaders headers = new HttpHeaders();
        URI newUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{earthquakeId}").buildAndExpand(earthquake.getIid()).toUri();
        headers.setLocation(newUri);

        return new ResponseEntity<Void>(null, headers, HttpStatus.CREATED);
    }

    private  ResponseDTO<EarthquakeAllDTO> getResponseDTO(List<EarthquakeDTO> earthquakeDTOs) {

        ResponseDTO<List<ResponseDTO<EarthquakeDTO>>> responseDTOs = new ResponseDTO<List<ResponseDTO<EarthquakeDTO>>>();
        List<ResponseDTO<EarthquakeDTO>> earthquakeResponseDTOs = new ArrayList<>();
        earthquakeDTOs.forEach((earthquakeDTO) -> {
                ResponseDTO<EarthquakeDTO> responseDTO = getResponseDTO(earthquakeDTO);
                earthquakeResponseDTOs.add(responseDTO);
        });

        responseDTOs.setUrl(linkTo(methodOn(EarthquakeController.class).getEarthquakes()).withRel("earthquakes"));
        responseDTOs.setData(earthquakeResponseDTOs);
        EarthquakeAllDTO earthquakesAllDTO = new EarthquakeAllDTO();
        earthquakesAllDTO.setEarthquakes(responseDTOs);

        ResponseDTO<EarthquakeAllDTO> responseAllDTOs = new ResponseDTO<EarthquakeAllDTO>();
        responseAllDTOs.setUrl(linkTo(methodOn(EarthquakeController.class).getEarthquakes()).withRel("earthquakes"));
        responseAllDTOs.setData(earthquakesAllDTO);

        return responseAllDTOs;

    }
    private ResponseDTO<EarthquakeDTO> getResponseDTO(EarthquakeDTO earthquakeDTO) {
        Link link = linkTo(methodOn(EarthquakeController.class).getEarthquakes()).slash(earthquakeDTO.getIid()).withSelfRel();

        ResponseDTO<EarthquakeDTO> responseDTO = new  ResponseDTO<EarthquakeDTO>();
        responseDTO.setData(earthquakeDTO);
        responseDTO.setUrl(linkTo(methodOn(EarthquakeController.class).getEarthquakes()).slash(earthquakeDTO.getIid()).withSelfRel());
        responseDTO.addLink(linkTo(methodOn(EarthquakeController.class).getEarthquakes()).withRel("earthquakes"));
        return responseDTO;
    }

    protected Earthquake findEarthquake(Long earthquakeId) throws ResourceNotFoundException {
        Earthquake earthquake = earthquakeRepository.findOne(earthquakeId);
        if(earthquake == null) {
            throw new ResourceNotFoundException("Earthquake with id " + earthquakeId + " not found");
        }

        return earthquake;
    }
}
