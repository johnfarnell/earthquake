package au.com.auspost.repository;

import au.com.auspost.domain.Station;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface StationRepository extends CrudRepository<Station, Long> {
    @Query(value="select * from STATION where src = ?1", nativeQuery = true)
    public Station findBySrc(String src);
}
