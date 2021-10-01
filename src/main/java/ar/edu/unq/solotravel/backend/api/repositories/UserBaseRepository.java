package ar.edu.unq.solotravel.backend.api.repositories;

import ar.edu.unq.solotravel.backend.api.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends CrudRepository<T, Integer> {
    Optional<T> findById(Integer id);
}