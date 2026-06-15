package mk.ukim.finki.wp.jan2025g2.service.impl;


import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.jan2025g2.model.NationalPark;
import mk.ukim.finki.wp.jan2025g2.model.ParkLocation;
import mk.ukim.finki.wp.jan2025g2.model.ParkType;
import mk.ukim.finki.wp.jan2025g2.model.exceptions.InvalidParkLocationIdException;
import mk.ukim.finki.wp.jan2025g2.repository.NationalParkRepository;
import mk.ukim.finki.wp.jan2025g2.service.NationalParkService;
import mk.ukim.finki.wp.jan2025g2.service.ParkLocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Filter;

import static mk.ukim.finki.wp.jan2025g2.service.impl.FieldFilterSpecification.*;

@Service
@AllArgsConstructor
public class NationalParkServiceImpl implements NationalParkService {
    private final NationalParkRepository nationalParkRepository;
    private final ParkLocationService parkLocationService;

    @Override
    public List<NationalPark> listAll() {
        return nationalParkRepository.findAll();
    }

    @Override
    public NationalPark findById(Long id) {
        return nationalParkRepository.findById(id).orElseThrow(InvalidParkLocationIdException::new);
    }

    @Override
    public NationalPark create(String name, Double areaSize, Double rating, ParkType parkType, Long locationId) {
        NationalPark nationalPark = new NationalPark(name, areaSize, rating, parkType, parkLocationService.findById(locationId));
        return nationalParkRepository.save(nationalPark);
    }

    @Override
    public NationalPark update(Long id, String name, Double areaSize, Double rating, ParkType parkType, Long locationId) {
        NationalPark park = findById(id);
        ParkLocation location = parkLocationService.findById(locationId);

        park.setName(name);
        park.setAreaSize(areaSize);
        park.setRating(rating);
        park.setParkType(parkType);
        park.setLocation(location);

        return nationalParkRepository.save(park);
    }

    @Override
    public NationalPark delete(Long id) {
        NationalPark park = findById(id);
        nationalParkRepository.delete(park);
        return park;
    }

    @Override
    public NationalPark close(Long id) {
        NationalPark park = findById(id);
        park.setClosed(true);
        nationalParkRepository.save(park);
        return park;
    }

    @Override
    public Page<NationalPark> findPage(String name, Double areaSize, Double rating, ParkType parkType, Long locationId, int pageNum, int pageSize) {
        Specification<NationalPark>parkSpecification = Specification.allOf(
                filterContainsText(NationalPark.class, "name", name),
                greaterThan(NationalPark.class,  "areaSize", areaSize),
                greaterThan(NationalPark.class,  "rating", rating),
                // Enum
                filterEqualsV(NationalPark.class, "parkType", parkType),
                // Location id
                filterEqualsV(NationalPark.class, "location.id", locationId)

        );
        return nationalParkRepository.findAll(parkSpecification, PageRequest.of(pageNum, pageSize));
    }
}
