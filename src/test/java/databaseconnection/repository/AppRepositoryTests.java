package databaseconnection.repository;

import databaseconnection.model.Hotel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AppRepositoryTests {
    @Autowired
    HotelRepository hotelRepositoryTest ;

    @Test
    public void hotelRepository_SaveAll_ReturnSavedHotel(){
        //Arrange
        Hotel hotel = Hotel.builder()
                .name("TestHotel")
                .address("TestAddress")
                .contact("8345433322").build();
        //Act
        Hotel savedHotel = hotelRepositoryTest.save(hotel);

        //Assert
        Assertions.assertThat(savedHotel).isNotNull();
        Assertions.assertThat(savedHotel.getId()).isGreaterThan(0);
    }
}
