package emcer.cg.fr.gestionutilisateuronline.artifact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
}
