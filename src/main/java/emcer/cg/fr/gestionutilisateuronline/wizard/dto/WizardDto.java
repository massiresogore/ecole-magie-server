package emcer.cg.fr.gestionutilisateuronline.wizard.dto;


import jakarta.validation.constraints.NotEmpty;

public record WizardDto(
        Long id,
        @NotEmpty(message = "name is required")String name,
        Integer numberOfPowers) {
}