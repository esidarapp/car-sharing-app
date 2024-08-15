package mate.academy.carsharing.repository.rental.filter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.carsharing.model.Rental;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentalSpecificationProviderManager
        implements SpecificationProviderManager<Rental> {
    private final List<SpecificationProvider<Rental>> rentalSpecificationProviders;

    @Override
    public SpecificationProvider<Rental> getSpecificationProvider(String key) {
        return rentalSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find "
                        + "correct specification provider for key " + key));
    }
}
