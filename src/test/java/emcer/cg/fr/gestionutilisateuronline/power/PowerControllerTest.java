package emcer.cg.fr.gestionutilisateuronline.power;

import com.fasterxml.jackson.databind.ObjectMapper;
import emcer.cg.fr.gestionutilisateuronline.power.dto.PowerDto;
import emcer.cg.fr.gestionutilisateuronline.system.StatusCode;
import emcer.cg.fr.gestionutilisateuronline.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PowerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PowerService powerService;

    @Autowired
    ObjectMapper objectMapper;


    @Value("${api.endpoint.base-url}")
    String baseUrl;

    List<Power> powers;

    @BeforeEach
    void setUp() {
        this.powers = new ArrayList<>();

        Power a1 = new Power();
        a1.setId(1L);
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");
        this.powers.add(a1);

        Power a2 = new Power();
        a2.setId(2L);
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl2");
        this.powers.add(a2);

        Power a3 = new Power();
        a3.setId(3L);
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl3");
        this.powers.add(a3);

        Power a4 = new Power();
        a4.setId(4L);
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl4");
        this.powers.add(a4);

        Power a5 = new Power();
        a5.setId(5L);
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl5");
        this.powers.add(a5);

        Power a6 = new Power();
        a6.setId(6L);
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl6");
        this.powers.add(a6);
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
        given(powerService.findById(1L)).willThrow(new ObjectNotFoundException("artifact",1L));
        //When(86) and then
        /*Fake http GET Request*/
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+ "/pouvoirs/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id:1"))
                .andExpect(jsonPath("$.data").isEmpty());


    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        //Given
        given(powerService.findById(1L)).willReturn(this.powers.get(0));
        //When and then
        /*Fake http GET Request*/
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+ "/pouvoirs/1") .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Deluminator"));
    }

    @Test
    void testAllArtifactsSuccess() throws Exception {
        //Given
        given(this.powerService.findAll()).willReturn(powers);
        //Wen and Then
        this.mockMvc.perform(MockMvcRequestBuilders.
                        get(baseUrl+ "/pouvoirs").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.powers.size())))
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
        PowerDto powerDto = new PowerDto(
                2L,
                "Massire",
                "Etudiant 3em année Web",
                "massire.png",
                null
        );
        //fake input data to define output methode
        String jsonArt =  this.objectMapper.writeValueAsString(powerDto);
        Power savedPower = new Power();
        savedPower.setId(1L);
        savedPower.setName("Massire");
        savedPower.setDescription("Etudiant 3em année Web");
        savedPower.setImageUrl("massire.png");
        //Simulation de donnée qui provient du front-end
        given(this.powerService.save(Mockito.any(Power.class))).willReturn(savedPower);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl+ "/pouvoirs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonArt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedPower.getName()))
                .andExpect(jsonPath("$.data.description").value(savedPower.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedPower.getImageUrl())
                );
    }

    /*@Test
    void testUpdateArtifactSuccess() throws Exception {
        //Given, fournit par le front-end
        PowerDto artifactDto = new PowerDto(
                1L,
                "Invisibility Cloak",
                "A new Description",
                "ImageUrl",
                null
        );
        String json = this.objectMapper.writeValueAsString(artifactDto);//Reçu depuis le fropnt-end

        Power updateArtifact = new Power();
        updateArtifact.setId(1L);
        updateArtifact.setName("Invisibility Cloak");
        updateArtifact.setDescription("A new Description");
        updateArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.update(eq(1L),Mockito.any(Power.class))).willReturn(updateArtifact);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/powers/1")
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
    }*/
    @Test
    void testUpdateArtifactSuccess() throws Exception {
        // Given
        PowerDto powerDto = new PowerDto(1L,
                "Invisibility Cloak",
                "A new description.",
                "ImageUrl",
                null);
        String json = this.objectMapper.writeValueAsString(powerDto);

        Power updatedPower = new Power();
        updatedPower.setId(1L);
        updatedPower.setName("Invisibility Cloak");
        updatedPower.setDescription("A new description.");
        updatedPower.setImageUrl("ImageUrl");

        given(this.powerService.update(eq(1L), Mockito.any(Power.class))).willReturn(updatedPower);

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/pouvoirs/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(updatedPower.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedPower.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedPower.getImageUrl()));
    }



    @Test
    void testUpdateArtifactErrorWithNonexistentiId() throws Exception {
        //Given, fournit par le front-end
        PowerDto powerDto = new PowerDto(
                12L,
                "Invisibility Cloak",
                "A new Description",
                "ImageUrl",
                null
        );
        String json = this.objectMapper.writeValueAsString(powerDto);//Reçu depuis le fropnt-end



        given(this.powerService.update(eq(12L),Mockito.any(Power.class))).willThrow(new ObjectNotFoundException("artifact",12L));

        //When and Then
        this.mockMvc.perform(put(baseUrl+ "/pouvoirs/12")
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
        doNothing().when(this.powerService).delete(1L);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+ "/pouvoirs/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Power deleted"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactErrorWithNoExistId() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("artifact",12L)).when(this.powerService).delete(12L);
        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+ "/pouvoirs/12")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id:12"))
                .andExpect(jsonPath("$.data").isEmpty());
    }



}