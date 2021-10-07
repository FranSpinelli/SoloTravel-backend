package ar.edu.unq.solotravel.backend.api.repositories;

import ar.edu.unq.solotravel.backend.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends JpaRepository<T, Integer> {
    Optional<T> findById(Integer id);
    Optional<T> findByEmail(String email);
}