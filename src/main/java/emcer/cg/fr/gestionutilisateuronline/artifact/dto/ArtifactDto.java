package emcer.cg.fr.gestionutilisateuronline.artifact.dto;
import emcer.cg.fr.gestionutilisateuronline.wizard.Wizard;
import jakarta.validation.constraints.NotEmpty;

public record ArtifactDto(
   Long id,
   @NotEmpty String name,
   @NotEmpty String description,
   @NotEmpty String imageUrl,
             Long ownerId
) {
}
