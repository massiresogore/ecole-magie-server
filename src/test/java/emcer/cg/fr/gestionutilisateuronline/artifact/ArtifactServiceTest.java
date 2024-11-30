package emcer.cg.fr.gestionutilisateuronline.artifact;

import emcer.cg.fr.gestionutilisateuronline.system.exception.ObjectNotFoundException;
import emcer.cg.fr.gestionutilisateuronline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
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
class ArtifactServiceTest {


    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifactList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId(1L);
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");
        this.artifactList.add(a1);

        Artifact a2 = new Artifact();
        a2.setId(1L);
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl2");
        this.artifactList.add(a2);
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
              "numberOfArtifacts": 2
      }
             */
        Artifact a = new Artifact();
        a.setId(Long.valueOf("1"));
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2L);
        w.setName("Harry Potter");

       // a.setOwner(w);

        //Define the behavior of the mock Objet.
        given(artifactRepository.findById(1L)).willReturn(Optional.of(a));


        //When . Act on the target behavior. When steps should cover the method to be tested
        Artifact returnArtFact =  artifactService.findById(1L);

        //Then Assert expected outcomes
        assertThat(returnArtFact.getId()).isEqualTo(a.getId());
        assertThat(returnArtFact.getName()).isEqualTo(a.getName());
        assertThat(returnArtFact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnArtFact.getImageUrl()).isEqualTo(a.getImageUrl());

        verify(artifactRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound()
    {
        //Given
        given(artifactRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());


        //When
        //Artifact returnArtFact =  artifactService.findById("1250808601744904192");
        Throwable thrown = catchThrowable(()->{
            Artifact returnArtFact =  artifactService.findById(1L);
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Artifact not found with id:1");

        verify(artifactRepository, times(1)).findById(Long.valueOf("1"));
    }

    @Test
    void testFindAllSuccess()
    {
        //Given
        given(artifactRepository.findAll()).willReturn(this.artifactList);

        //When
        List<Artifact> actualArtifacts = artifactService.findAll();

        //Then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifactList.size());
        //artifactRepository doit etre appelé une fois
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess()
    {
        //Given
        Artifact artifact = new Artifact();
        artifact.setId(1L);
        artifact.setName("Artifact 3");
        artifact.setDescription("Description...");
        artifact.setImageUrl("Image Url");

//        given(1L).willReturn(1L);
        given(artifactRepository.save(artifact)).willReturn(artifact);

        //When
        Artifact savedArtifact = artifactService.save(artifact);

        //then
        assertThat(savedArtifact.getId()).isEqualTo(1L);
        assertThat(savedArtifact.getName()).isEqualTo(artifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(artifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(artifact.getImageUrl());
        verify(artifactRepository, times(1)).save(artifact);
    }

    @Test
    void testUpdateSuccess()
    {
        //Given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId(1L);
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact  update = new Artifact();
        //update.setId("1250808601744904192");La doc n'a pas d'id, donc initule
        update.setName("Invisibility Cloak");
        update.setDescription("A new Description");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById(1L)).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        //When
        Artifact updatedArtifact = artifactService.update(1L, update);

        //Then
        assertThat(updatedArtifact.getId()).isEqualTo(1L);
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
        verify(artifactRepository, times(1)).findById(1L);
        verify(artifactRepository, times(1)).save(oldArtifact);
    }

    @Test
    void testUpdateNotFound()
    {
        //Given(Artifact qui a été transmis)
        Artifact  update = new Artifact();
        update.setId(1L);
        update.setName("Invisibility Cloak");
        update.setDescription("A new Description");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById(1L)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class,()->{
            artifactService.update(1L,update);
        });

        //Then(assurer que la recherche Id, et save soit appellé au moins une fois)
        verify(artifactRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteSuccess()
    {
        //Given
        Artifact artifact = new Artifact();
        artifact.setId(1L);
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("ImageUrl2");

        given(artifactRepository.findById(1L)).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById(1L);//une fois que cette méthode est appellé , ne fait rien

        //When
        artifactService.delete(1L);

        //Then
        verify(artifactRepository, times(1)).deleteById(1L);

    }
    @Test
    void testDeleteNotFound()
    {
        //Given
        given(artifactRepository.findById(1L)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class,()->{
            artifactService.delete(1L);
        });
        //Then
        verify(artifactRepository, times(1)).findById(1L);

    }


}