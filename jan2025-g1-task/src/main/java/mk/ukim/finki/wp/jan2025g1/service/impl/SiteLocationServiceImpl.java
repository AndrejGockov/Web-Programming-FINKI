package mk.ukim.finki.wp.jan2025g1.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.jan2025g1.model.SiteLocation;
import mk.ukim.finki.wp.jan2025g1.model.exceptions.InvalidSiteLocationIdException;
import mk.ukim.finki.wp.jan2025g1.repository.SiteLocationRepository;
import mk.ukim.finki.wp.jan2025g1.service.SiteLocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SiteLocationServiceImpl implements SiteLocationService {
    final private SiteLocationRepository siteLocationRepository;

    @Override
    public SiteLocation findById(Long id) {
        return siteLocationRepository.findById(id).orElseThrow(InvalidSiteLocationIdException::new);
    }

    @Override
    public List<SiteLocation> listAll() {
        return siteLocationRepository.findAll();
    }

    @Override
    public SiteLocation create(String city, String country) {
        return siteLocationRepository.save(new  SiteLocation(city,country));
    }
}
