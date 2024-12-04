package emcer.cg.fr.gestionutilisateuronline.power;

import emcer.cg.fr.gestionutilisateuronline.power.converter.PowerDtoToPowerConverter;
import emcer.cg.fr.gestionutilisateuronline.power.converter.PowerToPowerDtoConverter;
import emcer.cg.fr.gestionutilisateuronline.power.dto.PowerDto;
import emcer.cg.fr.gestionutilisateuronline.system.Result;
import emcer.cg.fr.gestionutilisateuronline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RequestMapping("${api.endpoint.base-url}/artifacts")
@RestController
public class PowerController {
    private final PowerService powerService;
    private final PowerDtoToPowerConverter powerDtoToArtifactConverter;
    private final PowerToPowerDtoConverter powerToPowerDtoConverter;


    public PowerController(PowerService powerService, PowerDtoToPowerConverter powerDtoToArtifactConverter, PowerToPowerDtoConverter powerToPowerDtoConverter) {
        this.powerService = powerService;
        this.powerDtoToArtifactConverter = powerDtoToArtifactConverter;
        this.powerToPowerDtoConverter = powerToPowerDtoConverter;
    }

    @GetMapping
    public Result findAllArtifacts() {
        return new Result(true,
                "Find All Success",
                StatusCode.SUCCESS,
                this.powerService.findAll()
                        .stream()
                        .map(powerToPowerDtoConverter::convert));
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable("artifactId") String artifactId) {
        return new Result(true,"Find One Success",
                StatusCode.SUCCESS,
                this.powerToPowerDtoConverter
                        .convert(powerService.findById(Long.parseLong(artifactId))));
    }

    /*Récupère Dto, le converti en Power le met à jour puis récupre la mis à jour
    * puis on converti lobjet mis à jour en Dto
    * et on le renvoie
    * */
    @PutMapping("/{updateId}")
    public Result updateArtifactById(@PathVariable("updateId") Long updateId,@Valid @RequestBody PowerDto powerDto) {

        return new Result(true,
                "Update success",
                StatusCode.SUCCESS,
                this.powerToPowerDtoConverter.convert(
                        this.powerService.
                                update(updateId, this.powerDtoToArtifactConverter.convert(powerDto))

                )
                );
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifactById(@PathVariable("artifactId") Long artifactId) {
        this.powerService.delete(artifactId);
        return new Result(true,"Power deleted", StatusCode.SUCCESS);
    }

    @PostMapping
    public Result save(@Valid @RequestBody PowerDto powerDto) {


        return new Result(true,
                "Add Success",
                StatusCode.SUCCESS,
                this.powerToPowerDtoConverter
                        .convert(this.powerService
                                .save(powerDtoToArtifactConverter.convert(powerDto))
                )
        );
    }


}
