package ar.edu.unq.solotravel.backend.api.specifications;

import org.springframework.data.jpa.domain.Specification;

public interface SpecBuilder<T, B> {
    Specification<T> buildCriteriaSpecs(B params);
}
