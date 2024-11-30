package emcer.cg.fr.gestionutilisateuronline.artifact.converter;

import emcer.cg.fr.gestionutilisateuronline.artifact.Artifact;
import emcer.cg.fr.gestionutilisateuronline.artifact.dto.ArtifactDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {

    @Override
    public ArtifactDto convert(Artifact source) {
        return new ArtifactDto(
               source.getId(),
               source.getName(),
               source.getDescription(),
               source.getImageUrl(),
                source.getOwner() !=null ? source.getOwner().getId() : null
        );
    }
}
