package emcer.cg.fr.gestionutilisateuronline.power.converter;

import emcer.cg.fr.gestionutilisateuronline.power.Power;
import emcer.cg.fr.gestionutilisateuronline.power.dto.PowerDto;
import emcer.cg.fr.gestionutilisateuronline.wizard.WizardService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PowerDtoToPowerConverter implements Converter<PowerDto, Power> {
private final WizardService wizardService;

    public PowerDtoToPowerConverter(WizardService wizardService) {
        this.wizardService = wizardService;
    }

    @Override
    public Power convert(PowerDto source) {

        return new Power(
                source.id(),
                source.name(),
                source.description(),
                source.imageUrl(),
                source.ownerId() !=null  ? this.wizardService.findById(source.ownerId()) :null

        );
    }
}
