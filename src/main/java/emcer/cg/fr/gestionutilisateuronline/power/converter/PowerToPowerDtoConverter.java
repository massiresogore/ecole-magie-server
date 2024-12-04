package emcer.cg.fr.gestionutilisateuronline.power.converter;

import emcer.cg.fr.gestionutilisateuronline.power.Power;
import emcer.cg.fr.gestionutilisateuronline.power.dto.PowerDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PowerToPowerDtoConverter implements Converter<Power, PowerDto> {

    @Override
    public PowerDto convert(Power source) {
        return new PowerDto(
               source.getId(),
               source.getName(),
               source.getDescription(),
               source.getImageUrl(),
                source.getOwner() !=null ? source.getOwner().getId() : null
        );
    }
}
