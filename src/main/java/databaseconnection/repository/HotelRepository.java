package databaseconnection.repository;

import databaseconnection.model.Hotel;
import databaseconnection.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> , JpaSpecificationExecutor<Hotel> {
    List<Hotel> findByNameContaining(String partialName);

    @Query ("SELECT h from Hotel h WHERE h.name = :name")
            List<Hotel> findHotelByName(@Param("name")String name);
    @Query("SELECT h.rating from Hotel h where h.id=:id")
    List<Rating> findRatingByHotelId(@Param("id")Long id);
    @Query("SELECT h.rating from Hotel h")
    List<Rating> findAllRatings();
    @Modifying
    @Transactional
    @Query("DELETE FROM Rating")
    void deleteRatingsByHotelId(@Param("id") Long id);
    @Modifying
    @Transactional
    @Query("DELETE FROM Hotel")
    void deleteAllHotels();
    @Modifying
    @Transactional
    @Query("DELETE FROM Rating")
    void deleteAllRatings();
}
