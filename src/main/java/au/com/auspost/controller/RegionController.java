package au.com.auspost.controller;

import au.com.auspost.domain.Region;
import au.com.auspost.dto.RegionDTO;
import au.com.auspost.repository.RegionRepository;
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
public class RegionController {
    @Inject
    private RegionRepository regionRepository;
    @RequestMapping(value = "/regions", method = RequestMethod.GET)
    public ResponseEntity<Iterable<RegionDTO>> getRegions() {

        List<Region> regions = IteratorUtils.toList(regionRepository.findAll().iterator());

        ModelMapper mapper = new ModelMapper();
        Type listType = new TypeToken<List<RegionDTO>>() {}.getType();

        List<RegionDTO> regionDTOs = mapper.map(regions, listType);

        return new ResponseEntity<Iterable<RegionDTO>>(regionDTOs, HttpStatus.OK);

    }
}