package mate.academy.carsharing.dto.rental;

public record RentalSearchParams(
        String user_id,
        Boolean is_active
) {
}
