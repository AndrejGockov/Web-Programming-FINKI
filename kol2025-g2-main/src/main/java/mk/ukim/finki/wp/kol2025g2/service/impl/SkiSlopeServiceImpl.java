package mk.ukim.finki.wp.kol2025g2.service.impl;


import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2025g2.model.SkiSlope;
import mk.ukim.finki.wp.kol2025g2.model.SlopeDifficulty;
import mk.ukim.finki.wp.kol2025g2.model.exceptions.InvalidSkiResortIdException;
import mk.ukim.finki.wp.kol2025g2.model.exceptions.InvalidSkiSlopeIdException;
import mk.ukim.finki.wp.kol2025g2.repository.SkiSlopeRepository;
import mk.ukim.finki.wp.kol2025g2.service.SkiResortService;
import mk.ukim.finki.wp.kol2025g2.service.SkiSlopeService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static mk.ukim.finki.wp.kol2025g2.service.impl.FieldFilterSpecification.*;
import static mk.ukim.finki.wp.kol2025g2.service.impl.FieldFilterSpecification.filterEqualsV;

@Service
@AllArgsConstructor
public class SkiSlopeServiceImpl implements SkiSlopeService {
    private final SkiSlopeRepository skiSlopeRepository;
    private final SkiResortService skiResortService;

    @Override
    public List<SkiSlope> listAll() {
        return skiSlopeRepository.findAll();
    }

    @Override
    public SkiSlope findById(Long id) {
        return skiSlopeRepository.findById(id).orElseThrow(InvalidSkiSlopeIdException::new);
    }

    @Override
    public SkiSlope create(String name, Integer length, SlopeDifficulty difficulty, Long skiResort) {
        SkiSlope skiSlope = new SkiSlope(name, length, difficulty, skiResortService.findById(skiResort));
        return skiSlopeRepository.save(skiSlope);
    }

    @Override
    public SkiSlope update(Long id, String name, Integer length, SlopeDifficulty difficulty, Long skiResort) {
        SkiSlope skiSlope = findById(id);

        skiSlope.setName(name);
        skiSlope.setLength(length);
        skiSlope.setDifficulty(difficulty);
        skiSlope.setSkiResort(skiResortService.findById(skiResort));

        return skiSlopeRepository.save(skiSlope);
    }

    @Override
    public SkiSlope delete(Long id) {
        SkiSlope skiSlope = findById(id);
        skiSlopeRepository.delete(skiSlope);

        return skiSlope;
    }

    @Override
    public SkiSlope close(Long id) {
        SkiSlope skiSlope = findById(id);
        skiSlope.setClosed(true);

        return skiSlopeRepository.save(skiSlope);
    }

    @Override
    public Page<SkiSlope> findPage(String name, Integer length, SlopeDifficulty difficulty, Long skiResort, int pageNum, int pageSize) {
        Specification<SkiSlope>specification = Specification.allOf(
                filterContainsText(SkiSlope.class, "name", name),
                greaterThan(SkiSlope.class, "length", length),
                filterEqualsV(SkiSlope.class, "difficulty", difficulty),
                filterEqualsV(SkiSlope.class, "skiResort.id", skiResort)
        );

        return skiSlopeRepository.findAll(specification, PageRequest.of(pageNum, pageSize));
    }
}
