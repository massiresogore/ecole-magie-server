package emcer.cg.fr.gestionutilisateuronline.power;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PowerRepository extends JpaRepository<Power, Long> {
}
