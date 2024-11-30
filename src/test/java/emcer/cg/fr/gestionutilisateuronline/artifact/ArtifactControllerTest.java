package emcer.cg.fr.gestionutilisateuronline.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import emcer.cg.fr.gestionutilisateuronline.artifact.dto.ArtifactDto;
import emcer.cg.fr.gestionutilisateuronline.system.StatusCode;
import emcer.cg.fr.gestionutilisateuronline.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;


    @Value("${api.endpoint.base-url}")
    String baseUrl;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setId(1L);
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setId(2L);
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl2");
        this.artifacts.add(a2);

        Artifact a3 = new Artifact();
        a3.setId(3L);
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl3");
        this.artifacts.add(a3);

        Artifact a4 = new Artifact();
        a4.setId(4L);
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl4");
        this.artifacts.add(a4);

        Artifact a5 = new Artifact();
        a5.setId(5L);
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl5");
        this.artifacts.add(a5);

        Artifact a6 = new Artifact();
        a6.setId(6L);
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl6");
        this.artifacts.add(a6);
    }


    @Test
    void testFindArtifactByIdNotFound() throws Exception {
        /*
        {
            "flag": true,
            "code" :200,
            "message": "Find One Success",
            "data": {|
            "id": "1250808601744904191".
            "name": "Deluminator".
            "description":"A Deluminator is a device invented by...",
            "imageUr1" : "ImageUr1"
            “owner"：｛日
            "id" : 1,
            "name": "Albus Dumbledore",
            "numberofArtifacts" :2
        }
         */
        //Given
        given(artifactService.findById(1L)).willThrow(new ObjectNotFoundException("artifact",1L));
        //When(86) and then
        /*Fake http GET Request*/
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/artifacts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id:1"))
                .andExpect(jsonPath("$.data").isEmpty());


    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        //Given
        given(artifactService.findById(1L)).willReturn(this.artifacts.get(0));
        //When and then
        /*Fake http GET Request*/
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/artifacts/1") .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Deluminator"));
    }

    @Test
    void testAllArtifactsSuccess() throws Exception {
        //Given
        given(this.artifactService.findAll()).willReturn(artifacts);
        //Wen and Then
        this.mockMvc.perform(MockMvcRequestBuilders.
                        get(baseUrl+"/artifacts").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("Deluminator"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].name").value("Invisibility Cloak")
                );
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        //Given
        ArtifactDto artifactDto = new ArtifactDto(
                null,
                "Massire",
                "Etudiant 3em année Web",
                "massire.png",
                null
        );
        //fake input data to define output methode
        String jsonArt =  this.objectMapper.writeValueAsString(artifactDto);
        Artifact savedArtifact = new Artifact();
        savedArtifact.setId(1L);
        savedArtifact.setName("Massire");
        savedArtifact.setDescription("Etudiant 3em année Web");
        savedArtifact.setImageUrl("massire.png");
        //Simulation de donnée qui provient du front-end
        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl+"/artifacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonArt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl())
                );
    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
        //Given, fournit par le front-end
        ArtifactDto artifactDto = new ArtifactDto(
                1L,
                "Invisibility Cloak",
                "A new Description",
                "ImageUrl",
                null
        );
        String json = this.objectMapper.writeValueAsString(artifactDto);//Reçu depuis le fropnt-end

        Artifact updateArtifact = new Artifact();
        updateArtifact.setId(1L);
        updateArtifact.setName("Invisibility Cloak");
        updateArtifact.setDescription("A new Description");
        updateArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.update(eq(1L),Mockito.any(Artifact.class))).willReturn(updateArtifact);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/artifacts/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(updateArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updateArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updateArtifact.getImageUrl()));
    }

    @Test
    void testUpdateArtifactErrorWithNonexistentiId() throws Exception {
        //Given, fournit par le front-end
        ArtifactDto artifactDto = new ArtifactDto(
                12L,
                "Invisibility Cloak",
                "A new Description",
                "ImageUrl",
                null
        );
        String json = this.objectMapper.writeValueAsString(artifactDto);//Reçu depuis le fropnt-end



        given(this.artifactService.update(eq(12L),Mockito.any(Artifact.class))).willThrow(new ObjectNotFoundException("artifact",12L));

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/artifacts/12")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id:12"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactSuccess() throws Exception {
        //Given
        doNothing().when(this.artifactService).delete(1L);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/artifacts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact deleted"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactErrorWithNoExistId() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("artifact",12L)).when(this.artifactService).delete(12L);
        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/artifacts/12")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id:12"))
                .andExpect(jsonPath("$.data").isEmpty());
    }



}