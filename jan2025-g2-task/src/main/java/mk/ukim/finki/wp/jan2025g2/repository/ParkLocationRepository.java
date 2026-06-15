package mk.ukim.finki.wp.jan2025g2.repository;

import mk.ukim.finki.wp.jan2025g2.model.NationalPark;
import mk.ukim.finki.wp.jan2025g2.model.ParkLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkLocationRepository extends JpaRepository<ParkLocation, Long> {
}
