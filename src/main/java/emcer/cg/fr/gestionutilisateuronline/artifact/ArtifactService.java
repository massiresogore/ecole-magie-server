package emcer.cg.fr.gestionutilisateuronline.artifact;

import emcer.cg.fr.gestionutilisateuronline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    public ArtifactService(ArtifactRepository artifactRepository) {
        this.artifactRepository = artifactRepository;
    }

    public Artifact create(Artifact artifact) {
        return artifactRepository.save(artifact);
    }
    public Artifact findById(Long id){
       return this.artifactRepository.findById(id).orElseThrow(()->new ObjectNotFoundException("Artifact not found", id));
    }

    public List<Artifact> findAll(){
        return this.artifactRepository.findAll();
    }
    public Artifact save(Artifact artifact){
        return this.artifactRepository.save(artifact);
    }

    public Artifact update(Long artifactId, Artifact newArtifact){
       return this.artifactRepository.findById(artifactId).map(old->{
            old.setName(newArtifact.getName());
            old.setDescription(newArtifact.getDescription());
            old.setImageUrl(newArtifact.getImageUrl());
           return this.artifactRepository.save(old);
        }).orElseThrow(()->new ObjectNotFoundException(Artifact.class.getSimpleName(),artifactId));
    }

    public void delete(Long artifactId){
        this.findById(artifactId);
        this.artifactRepository.deleteById(artifactId);
    }

}
