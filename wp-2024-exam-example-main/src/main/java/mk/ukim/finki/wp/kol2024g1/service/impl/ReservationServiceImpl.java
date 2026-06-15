package mk.ukim.finki.wp.kol2024g1.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2024g1.model.Reservation;
import mk.ukim.finki.wp.kol2024g1.model.RoomType;
import mk.ukim.finki.wp.kol2024g1.model.exceptions.InvalidReservationIdException;
import mk.ukim.finki.wp.kol2024g1.repository.ReservationRepository;
import mk.ukim.finki.wp.kol2024g1.service.HotelService;
import mk.ukim.finki.wp.kol2024g1.service.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static mk.ukim.finki.wp.kol2024g1.service.impl.FieldFilterSpecification.filterContainsText;
import static mk.ukim.finki.wp.kol2024g1.service.impl.FieldFilterSpecification.filterEqualsV;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final HotelService hotelService;
    private final ReservationRepository reservationRepository;

    @Override
    public List<Reservation> listAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElseThrow(InvalidReservationIdException::new);
    }

    @Override
    public Reservation create(String guestName, LocalDate dateCreated, Integer daysOfStay, RoomType roomType, Long hotelId) {
        Reservation reservation = new Reservation(guestName, dateCreated, daysOfStay, roomType, hotelService.findById(hotelId));
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation update(Long id, String guestName, LocalDate dateCreated, Integer daysOfStay, RoomType roomType, Long hotelId) {
        Reservation reservation = findById(id);

        reservation.setGuestName(guestName);
        reservation.setDateCreated(dateCreated);
        reservation.setDaysOfStay(daysOfStay);
        reservation.setRoomType(roomType);
        reservation.setHotel(hotelService.findById(hotelId));

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation delete(Long id) {
        Reservation reservation = findById(id);
        reservationRepository.delete(reservation);
        return reservation;
    }

    @Override
    public Reservation extendStay(Long id) {
        Reservation reservation = findById(id);
        reservation.setDaysOfStay(reservation.getDaysOfStay() + 1);
        return reservationRepository.save(reservation);
    }

    @Override
    public Page<Reservation> findPage(String guestName, RoomType roomType, Long hotel, int pageNum, int pageSize) {
        Specification<Reservation> specification = Specification.allOf(
                filterContainsText(Reservation.class, "guestName", guestName),
                filterEqualsV(Reservation.class,"roomType", roomType),
                filterEqualsV(Reservation.class,"hotel.id", hotel)
        );

        return reservationRepository.findAll(specification, PageRequest.of(pageNum, pageSize));
    }
}
