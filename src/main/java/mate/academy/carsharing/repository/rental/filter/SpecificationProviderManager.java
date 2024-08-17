package mate.academy.carsharing.repository.rental.filter;

public interface SpecificationProviderManager<T> {
    SpecificationProvider<T> getSpecificationProvider(String key);
}
