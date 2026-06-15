package mk.ukim.finki.wp.june2021.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.june2021.model.MatchLocation;
import mk.ukim.finki.wp.june2021.model.exceptions.InvalidMatchLocationIdException;
import mk.ukim.finki.wp.june2021.repository.MatchLocationRepository;
import mk.ukim.finki.wp.june2021.service.MatchLocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MatchLocationServiceImpl implements MatchLocationService {
    private final MatchLocationRepository matchLocationRepository;

    @Override
    public MatchLocation findById(Long id) {
        return matchLocationRepository.findById(id).orElseThrow(InvalidMatchLocationIdException::new);
    }

    @Override
    public List<MatchLocation> listAll() {
        return matchLocationRepository.findAll();
    }

    @Override
    public MatchLocation create(String name) {
        return matchLocationRepository.save(new MatchLocation(name));
    }
}
