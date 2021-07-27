package au.com.auspost.repository;

import au.com.auspost.domain.Earthquake;
import org.springframework.data.repository.CrudRepository;

public interface EarthquakeRepository  extends CrudRepository<Earthquake, Long> {
}
