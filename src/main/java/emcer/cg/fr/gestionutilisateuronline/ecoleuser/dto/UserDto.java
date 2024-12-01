package emcer.cg.fr.gestionutilisateuronline.ecoleuser.dto;

import jakarta.validation.constraints.NotEmpty;

//Ne pas envoyer le mdp au client
public record UserDto(
        Long id,
        @NotEmpty(message = "usernamee is required.")
        String username,
        Boolean enable,
        @NotEmpty(message = "roles are required.")
        String roles
){};