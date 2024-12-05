package emcer.cg.fr.gestionutilisateuronline.power;

import emcer.cg.fr.gestionutilisateuronline.system.exception.ObjectNotFoundException;
import emcer.cg.fr.gestionutilisateuronline.wizard.Wizard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "dev")
class PowerServiceTest {


    @Mock
    PowerRepository powerRepository;

    @InjectMocks
    PowerService powerService;

    List<Power> powerList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Power a1 = new Power();
        a1.setId(1L);
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");
        this.powerList.add(a1);

        Power a2 = new Power();
        a2.setId(1L);
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl2");
        this.powerList.add(a2);
    }

    @Test
    void testFindByIdSuccess() {
        //Given .Arrange inputs and targets. Define the behavior of Mock object artifactRepository.|
            /*
              "id": 1,
              "name": "Invisibility Cloak",
              "description": "An invisibility cloak is used to make the wearer invisible.",
              "imageUrl": "ImageUrl",

              Wizard
              "owner": {
              "id": 2,
              "name": "Harry Potter",
              "numberOfPowers": 2
      }
             */
        Power a = new Power();
        a.setId(Long.valueOf("1"));
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2L);
        w.setName("Harry Potter");

       // a.setOwner(w);

        //Define the behavior of the mock Objet.
        given(powerRepository.findById(1L)).willReturn(Optional.of(a));


        //When . Act on the target behavior. When steps should cover the method to be tested
        Power returnArtFact =  powerService.findById(1L);

        //Then Assert expected outcomes
        assertThat(returnArtFact.getId()).isEqualTo(a.getId());
        assertThat(returnArtFact.getName()).isEqualTo(a.getName());
        assertThat(returnArtFact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnArtFact.getImageUrl()).isEqualTo(a.getImageUrl());

        verify(powerRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound()
    {
        //Given
        given(powerRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());


        //When
        //Power returnArtFact =  artifactService.findById("1250808601744904192");
        Throwable thrown = catchThrowable(()->{
            Power returnArtFact =  powerService.findById(1L);
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Power not found with id:1");

        verify(powerRepository, times(1)).findById(Long.valueOf("1"));
    }

    @Test
    void testFindAllSuccess()
    {
        //Given
        given(powerRepository.findAll()).willReturn(this.powerList);

        //When
        List<Power> actualPowers = powerService.findAll();

        //Then
        assertThat(actualPowers.size()).isEqualTo(this.powerList.size());
        //artifactRepository doit etre appelé une fois
        verify(powerRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess()
    {
        //Given
        Power power = new Power();
        power.setId(1L);
        power.setName("Power 3");
        power.setDescription("Description...");
        power.setImageUrl("Image Url");

//        given(1L).willReturn(1L);
        given(powerRepository.save(power)).willReturn(power);

        //When
        Power savedPower = powerService.save(power);

        //then
        assertThat(savedPower.getId()).isEqualTo(1L);
        assertThat(savedPower.getName()).isEqualTo(power.getName());
        assertThat(savedPower.getDescription()).isEqualTo(power.getDescription());
        assertThat(savedPower.getImageUrl()).isEqualTo(power.getImageUrl());
        verify(powerRepository, times(1)).save(power);
    }

    @Test
    void testUpdateSuccess()
    {
        //Given
        Power oldPower = new Power();
        oldPower.setId(1L);
        oldPower.setName("Invisibility Cloak");
        oldPower.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldPower.setImageUrl("ImageUrl");

        Power update = new Power();
        //update.setId("1250808601744904192");La doc n'a pas d'id, donc initule
        update.setName("Invisibility Cloak");
        update.setDescription("A new Description");
        update.setImageUrl("ImageUrl");

        given(powerRepository.findById(1L)).willReturn(Optional.of(oldPower));
        given(powerRepository.save(oldPower)).willReturn(oldPower);

        //When
        Power updatedPower = powerService.update(1L, update);

        //Then
        assertThat(updatedPower.getId()).isEqualTo(1L);
        assertThat(updatedPower.getDescription()).isEqualTo(update.getDescription());
        verify(powerRepository, times(1)).findById(1L);
        verify(powerRepository, times(1)).save(oldPower);
    }

    @Test
    void testUpdateNotFound()
    {
        //Given(Power qui a été transmis)
        Power update = new Power();
        update.setId(1L);
        update.setName("Invisibility Cloak");
        update.setDescription("A new Description");
        update.setImageUrl("ImageUrl");

        given(powerRepository.findById(1L)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class,()->{
            powerService.update(1L,update);
        });

        //Then(assurer que la recherche Id, et save soit appellé au moins une fois)
        verify(powerRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteSuccess()
    {
        //Given
        Power power = new Power();
        power.setId(1L);
        power.setName("Invisibility Cloak");
        power.setDescription("An invisibility cloak is used to make the wearer invisible.");
        power.setImageUrl("ImageUrl2");

        given(powerRepository.findById(1L)).willReturn(Optional.of(power));
        doNothing().when(powerRepository).deleteById(1L);//une fois que cette méthode est appellé , ne fait rien

        //When
        powerService.delete(1L);

        //Then
        verify(powerRepository, times(1)).deleteById(1L);

    }
    @Test
    void testDeleteNotFound()
    {
        //Given
        given(powerRepository.findById(1L)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class,()->{
            powerService.delete(1L);
        });
        //Then
        verify(powerRepository, times(1)).findById(1L);

    }


}