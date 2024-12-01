package emcer.cg.fr.gestionutilisateuronline.ecoleuser.converter;

import emcer.cg.fr.gestionutilisateuronline.ecoleuser.EcoleUser;
import emcer.cg.fr.gestionutilisateuronline.ecoleuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, EcoleUser> {
    /**
     * Convert the source object of type {@code S} to target type {@code T}.
     *
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return the converted object, which must be an instance of {@code T} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public EcoleUser convert(UserDto source) {
        EcoleUser user = new EcoleUser();
        user.setId(source.id());
        user.setUsername(source.username());
        user.setEnable(source.enable());
        user.setRoles(source.roles());
        return user;
    }
}