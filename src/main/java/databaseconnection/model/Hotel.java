package databaseconnection.model;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String address;
    private String contact;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Rating> rating;
}
