package emcer.cg.fr.gestionutilisateuronline.artifact.converter;

import emcer.cg.fr.gestionutilisateuronline.artifact.Artifact;
import emcer.cg.fr.gestionutilisateuronline.artifact.ArtifactService;
import emcer.cg.fr.gestionutilisateuronline.artifact.dto.ArtifactDto;
import emcer.cg.fr.gestionutilisateuronline.wizard.WizardService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDtoToArtifactConverter implements Converter<ArtifactDto, Artifact> {
private final WizardService wizardService;

    public ArtifactDtoToArtifactConverter(WizardService wizardService) {
        this.wizardService = wizardService;
    }

    @Override
    public Artifact convert(ArtifactDto source) {

        return new Artifact(
                source.id(),
                source.name(),
                source.description(),
                source.imageUrl(),
                this.wizardService.findById(source.ownerId())

        );
    }
}
