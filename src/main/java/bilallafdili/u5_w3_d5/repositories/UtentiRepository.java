package bilallafdili.u5_w3_d5.repositories;

import bilallafdili.u5_w3_d5.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtentiRepository extends JpaRepository<Utente, UUID> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Utente> findByEmail(String email);
}
