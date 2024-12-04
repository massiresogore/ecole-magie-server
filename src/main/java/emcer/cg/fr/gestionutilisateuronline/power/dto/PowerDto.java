package emcer.cg.fr.gestionutilisateuronline.power.dto;
import jakarta.validation.constraints.NotEmpty;

public record PowerDto(
   Long id,
   @NotEmpty(message = "name is required.") String name,
   @NotEmpty(message = "description is required.") String description,
   @NotEmpty(message = "imageUrl is required.") String imageUrl,
             Long ownerId
) {
}
