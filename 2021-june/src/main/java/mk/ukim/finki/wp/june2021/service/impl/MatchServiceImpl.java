package mk.ukim.finki.wp.june2021.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.june2021.model.Match;
import mk.ukim.finki.wp.june2021.model.MatchType;
import mk.ukim.finki.wp.june2021.model.exceptions.InvalidMatchIdException;
import mk.ukim.finki.wp.june2021.repository.MatchRepository;
import mk.ukim.finki.wp.june2021.service.MatchLocationService;
import mk.ukim.finki.wp.june2021.service.MatchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;
    private final MatchLocationService matchLocationService;

    @Override
    public List<Match> listAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match findById(Long id) {
        return matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
    }

    @Override
    public Match create(String name, String description, Double price, MatchType type, Long location) {
        Match match = new Match(name, description, price, type, matchLocationService.findById(location));

        return matchRepository.save(match);
    }

    @Override
    public Match update(Long id, String name, String description, Double price, MatchType type, Long location) {
        Match match = findById(id);

        match.setName(name);
        match.setDescription(description);
        match.setPrice(price);
        match.setType(type);
        match.setLocation(matchLocationService.findById(location));

        return matchRepository.save(match);
    }

    @Override
    public Match delete(Long id) {
        Match match = findById(id);
        matchRepository.delete(match);

        return match;
    }

    @Override
    public Match follow(Long id) {
        Match match = findById(id);
        match.setFollows(match.getFollows() + 1);

        return matchRepository.save(match);
    }

    @Override
    public List<Match> listMatchesWithPriceLessThanAndType(Double price, MatchType type) {
        if(price != null && type != null) {
            return listAllMatches()
                    .stream()
                    .filter(match -> match.getPrice() <= price
                            && match.getType().equals(type))
                    .toList();
        }

        if(price != null) {
            return listAllMatches()
                    .stream()
                    .filter(match -> match.getPrice() <= price)
                    .toList();
        }

        if(type != null) {
            return listAllMatches()
                    .stream()
                    .filter(match -> match.getType().equals(type))
                    .toList();
        }

        return listAllMatches();
    }
}
