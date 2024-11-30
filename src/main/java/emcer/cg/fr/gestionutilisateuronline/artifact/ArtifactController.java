package emcer.cg.fr.gestionutilisateuronline.artifact;

import emcer.cg.fr.gestionutilisateuronline.artifact.converter.ArtifactDtoToArtifactConverter;
import emcer.cg.fr.gestionutilisateuronline.artifact.converter.ArtifactToArtifactDtoConverter;
import emcer.cg.fr.gestionutilisateuronline.artifact.dto.ArtifactDto;
import emcer.cg.fr.gestionutilisateuronline.system.Result;
import emcer.cg.fr.gestionutilisateuronline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RequestMapping("${api.endpoint.base-url}/artifacts")
@RestController
public class ArtifactController {
    private final ArtifactService artifactService;
    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;
    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;


    public ArtifactController(ArtifactService artifactService, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter) {
        this.artifactService = artifactService;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
    }

    @GetMapping
    public Result findAllArtifacts() {
        return new Result(true,
                "Find All Success",
                StatusCode.SUCCESS,
                this.artifactService.findAll()
                        .stream()
                        .map(artifactToArtifactDtoConverter::convert));
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable("artifactId") String artifactId) {
        return new Result(true,"Find One Success",
                StatusCode.SUCCESS,
                this.artifactToArtifactDtoConverter
                        .convert(artifactService.findById(Long.parseLong(artifactId))));
    }

    /*Récupère Dto, le converti en Artifact le met à jour puis récupre la mis à jour
    * puis on converti lobjet mis à jour en Dto
    * et on le renvoie
    * */
    @PutMapping("/{updateId}")
    public Result updateArtifactById(@PathVariable("updateId") Long updateId, @RequestBody ArtifactDto artifactDto) {

        return new Result(true,
                "Update success",
                StatusCode.SUCCESS,
                this.artifactToArtifactDtoConverter.convert(
                        this.artifactService.
                                update(Long.parseLong(String.valueOf(updateId)),
                                        this.artifactDtoToArtifactConverter.convert(artifactDto)
                                )
                )
                );
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifactById(@PathVariable("artifactId") Long artifactId) {
        this.artifactService.delete(artifactId);
        return new Result(true,"Artifact deleted", StatusCode.SUCCESS);
    }

    @PostMapping
    public Result save(@Valid @RequestBody ArtifactDto artifactDto) {

        return new Result(true,
                "Add Success",
                StatusCode.SUCCESS,
                this.artifactToArtifactDtoConverter
                        .convert(this.artifactService
                                .save(artifactDtoToArtifactConverter.convert(artifactDto))
                )
        );
    }


}
