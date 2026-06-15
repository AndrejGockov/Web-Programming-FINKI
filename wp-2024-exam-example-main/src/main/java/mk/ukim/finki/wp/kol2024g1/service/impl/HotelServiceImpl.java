package mk.ukim.finki.wp.kol2024g1.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2024g1.model.Hotel;
import mk.ukim.finki.wp.kol2024g1.model.exceptions.InvalidHotelIdException;
import mk.ukim.finki.wp.kol2024g1.repository.HotelRepository;
import mk.ukim.finki.wp.kol2024g1.service.HotelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

    @Override
    public Hotel findById(Long id) {
        return hotelRepository.findById(id).orElseThrow(InvalidHotelIdException::new);
    }

    @Override
    public List<Hotel> listAll() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel create(String name) {
        return hotelRepository.save(new Hotel(name));
    }
}
