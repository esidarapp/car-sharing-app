package mate.academy.carsharing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@ToString
@NamedEntityGraph(name = "Rental.details",
        attributeNodes = {
                @NamedAttributeNode("car"),
                @NamedAttributeNode("user")})
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate rentalDate = LocalDate.now();
    @Column(nullable = false)
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Car car;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
