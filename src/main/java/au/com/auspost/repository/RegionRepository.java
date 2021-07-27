package au.com.auspost.repository;

import au.com.auspost.domain.Region;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RegionRepository extends CrudRepository<Region, Long> {
    @Query(value="select * from REGION where name = ?1", nativeQuery = true)
    public Region findByName(String name);
}
