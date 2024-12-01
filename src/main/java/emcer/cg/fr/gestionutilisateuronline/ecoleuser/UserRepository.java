package emcer.cg.fr.gestionutilisateuronline.ecoleuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<EcoleUser, Long> {
    Optional<EcoleUser> findByUsername(String username);
}
