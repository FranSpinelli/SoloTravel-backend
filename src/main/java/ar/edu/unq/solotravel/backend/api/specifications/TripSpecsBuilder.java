package ar.edu.unq.solotravel.backend.api.specifications;

import ar.edu.unq.solotravel.backend.api.dtos.SearchTripParamsDto;
import ar.edu.unq.solotravel.backend.api.models.Trip;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TripSpecsBuilder implements SpecBuilder<Trip, SearchTripParamsDto>{

    @Override
    public Specification<Trip> buildCriteriaSpecs(SearchTripParamsDto params) {
        return Specification
                .where(this.withDestination(params.getDestination()))
                .and(this.withDate(params.getDate()));
    }

    private Specification<Trip> withDestination(String destination) {
        return (root, query, cb) -> destination == null ? null :
                cb.like(cb.lower(root.get("destination")), "%" + destination.toLowerCase() + "%");
    }

    private Specification<Trip> withDate(LocalDate date) {
        return (root, query, cb) -> date == null ? null :
                cb.greaterThanOrEqualTo(root.get("startDate"), date);
    }
}
