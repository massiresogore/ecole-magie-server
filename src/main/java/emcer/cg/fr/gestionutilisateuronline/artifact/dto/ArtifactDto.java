package emcer.cg.fr.gestionutilisateuronline.artifact.dto;
import emcer.cg.fr.gestionutilisateuronline.wizard.Wizard;
import jakarta.validation.constraints.NotEmpty;

public record ArtifactDto(
   Long id,
   @NotEmpty(message = "name is required.") String name,
   @NotEmpty(message = "description is required.") String description,
   @NotEmpty(message = "imageUrl is required.") String imageUrl,
             Long ownerId
) {
}
