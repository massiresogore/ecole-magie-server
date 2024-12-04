package emcer.cg.fr.gestionutilisateuronline.wizard;


import emcer.cg.fr.gestionutilisateuronline.system.Result;
import emcer.cg.fr.gestionutilisateuronline.system.StatusCode;
import emcer.cg.fr.gestionutilisateuronline.wizard.converter.WizardDtoToWizardConverter;
import emcer.cg.fr.gestionutilisateuronline.wizard.converter.WizartToWizardDtoConverter;
import emcer.cg.fr.gestionutilisateuronline.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {

    WizardService wizardService;
    WizartToWizardDtoConverter wizartToWizardDtoConverter;
    WizardDtoToWizardConverter wizardDtoToWizardConverter;

    public WizardController(WizardService wizardService,WizartToWizardDtoConverter wizartToWizardDtoConverter,WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizartToWizardDtoConverter = wizartToWizardDtoConverter;
        this.wizardDtoToWizardConverter=wizardDtoToWizardConverter;
    }

    @GetMapping
    public Result findAllWizards()
    {
        List<Wizard>  wizardList = wizardService.findAll();
        List<WizardDto> foundWizardDto = wizardList.stream().map(wizard -> wizartToWizardDtoConverter.convert(wizard)).toList();
        return new Result(true,"Find all wizards success", StatusCode.SUCCESS,foundWizardDto);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto)
    {
        Wizard savedWizard = this.wizardService.save(Objects.requireNonNull(this.wizardDtoToWizardConverter.convert(wizardDto)));

        WizardDto savedWizardDto = this.wizartToWizardDtoConverter.convert(savedWizard);
        return new Result(true,"Add wizard success",StatusCode.SUCCESS,savedWizardDto);
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable("wizardId") Integer wizardId)
    {
        return new Result(true,"Find one wizard success",StatusCode.SUCCESS,wizartToWizardDtoConverter.convert(wizardService.findById(Long.valueOf(wizardId))));
    }

    @PutMapping("/{wizardId}")
    public Result updatedWizard(@PathVariable("wizardId") Integer wizardId,@Valid @RequestBody WizardDto wizardDto)
    {
        //Convert  WizardDto to Wizard
        Wizard wizard = this.wizardDtoToWizardConverter.convert(wizardDto);

        //saveWizard
        Wizard savedWizard= this.wizardService.update(Long.valueOf(wizardId),wizard);

        //Convert saveWizard to savedWizardDto
        WizardDto updatedWizard = this.wizartToWizardDtoConverter.convert(savedWizard);

        return new Result(true,"Update wizard success",StatusCode.SUCCESS,updatedWizard);

    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId)
    {
        this.wizardService.delete(Long.valueOf(wizardId));

        return new Result(true,"Delete wizard success",StatusCode.SUCCESS);
    }

    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact(@PathVariable String wizardId, @PathVariable String artifactId)
    {
        this.wizardService.assignArtifact(Long.parseLong(wizardId),Long.valueOf(artifactId));
        return new Result(true,"Power assignment success",StatusCode.SUCCESS);
    }


}