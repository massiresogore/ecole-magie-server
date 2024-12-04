package emcer.cg.fr.gestionutilisateuronline.wizard;

import emcer.cg.fr.gestionutilisateuronline.power.Power;
import emcer.cg.fr.gestionutilisateuronline.power.PowerRepository;
import emcer.cg.fr.gestionutilisateuronline.system.exception.ObjectNotFoundException;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "dev")
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @Mock
    PowerRepository powerRepository;


    @InjectMocks
    WizardService wizardService;


    List<Wizard> wizardList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setName("Albus Dumbledore");
        this.wizardList.add(w1);

        Wizard w2 = new Wizard();
        w2.setName("Harry Potter");
        this.wizardList.add(w2);

        Wizard w3 = new Wizard();
        w3.setName("Harry Potter");
        this.wizardList.add(w3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        //Given
        given(this.wizardRepository.findAll()).willReturn(this.wizardList);

        //When
        List<Wizard> actualWizards = wizardService.findAll();

        //Then
        assertThat(actualWizards.size()).isEqualTo(this.wizardList.size());
        verify(wizardRepository,times(1)).findAll();
    }

    @Test
    void testSaveWizardSuccess()
    {
        //Given
        Wizard wizard = new Wizard();
        wizard.setId(1L);
        wizard.setName("Albus Dumbledore");

        given(wizardRepository.save(wizard)).willReturn(wizard);

        //When
        Wizard savedWizard = wizardService.save(wizard);

        //then
        assertThat(savedWizard.getId()).isEqualTo(wizard.getId());
        assertThat(savedWizard.getName()).isEqualTo(wizard.getName());
        verify(wizardRepository,times(1)).save(wizard);
    }

    @Test
    void findByIdSuccess()
    {
        Power power = new Power();
        power.setId(1L);
        power.setName("Resurrection Stone");
        power.setImageUrl("ImageUrl6");
        power.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");

        Wizard wizard = new Wizard();
        wizard.setId(4L);
        wizard.setName("Massire");
        wizard.addArtifact(power);

        given(wizardRepository.findById(4L)).willReturn(Optional.of(wizard));

        //When
        Wizard returnWizard = wizardService.findById(4L);

        //Then
        assertThat(returnWizard.getId()).isEqualTo(wizard.getId());
        assertThat(returnWizard.getName()).isEqualTo(wizard.getName());

        verify(wizardRepository, times(1)).findById(4L);


    }

    @Test
    void testFindByIdNotFound()
    {
        //Given
        given(wizardRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()->{
            Wizard returnWizard = wizardService.findById(4L);
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Wizard with id:4");
        verify(wizardRepository,Mockito.times(1)).findById(4L);
    }

    @Test
    void testUpdateSuccess()
    {
        //Given
        Power oldPower = new Power();
        oldPower.setId(2L);
        oldPower.setName("Invisibility Cloak");
        oldPower.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldPower.setImageUrl("ImageUrl");

        Wizard oldWizard = new Wizard();
        oldWizard.setId(1L);
        oldWizard.setName("Massire");
        oldWizard.addArtifact(oldPower);

        Wizard updateWizard = new Wizard();
        updateWizard.setId(1L);
        updateWizard.setName("new Massire name");
        given(wizardRepository.findById(1L)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(oldWizard)).willReturn(oldWizard);

        //When
        Wizard updatedWizard = wizardService.update(1L,updateWizard);

        //Then
        assertThat(updatedWizard.getId()).isEqualTo(updateWizard.getId());
        assertThat(updatedWizard.getName()).isEqualTo(updateWizard.getName());
        verify(wizardRepository,times(1)).findById(1L);
        verify(wizardRepository,times(1)).save(oldWizard);

    }

    @Test
    void  testDeleteWizardSuccess()
    {
        //Given
        Wizard wizard = new Wizard();
        wizard.setId(1L);
        wizard.setName("Massire");

        given(wizardRepository.findById(1L)).willReturn(Optional.of(wizard));
        doNothing().when(wizardRepository).deleteById(1L);

        //When
        wizardService.delete(1L);

        //Then
        verify(wizardRepository,times(1)).deleteById(1L);
    }

    @Test
    void testDeleteWizardNotFound()
    {
        //Given
        given(wizardRepository.findById(1L)).willReturn(Optional.empty());


        //When
        assertThrows(ObjectNotFoundException.class,()->{
            wizardService.delete(1L);
        });
        //Then
        verify(wizardRepository,times(1)).findById(1L);
    }

    @Test
    void testAssignArtifactSuccess()
    {
        //Given
        Power a = new Power();
        a.setId(2L);
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl2");

        Wizard w2 = new Wizard();
        w2.setId(2L);
        w2.setName("Harry potter");
        w2.addArtifact(a);

        Wizard w3 = new Wizard();
        w3.setId(3L);
        w3.setName("Neville Longbottom");

        given(this.powerRepository.findById(2L)).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3L)).willReturn(Optional.of(w3));

        //When
        this.wizardService.assignArtifact(3L,2L);

        //then
        assertThat(a.getOwner().getId()).isEqualTo(3L);
        assertThat(w3.getArtifacts()).contains(a);
    }

    @Test
    void testAssignmentArtifactErrorWithNonExistentWizardId()
    {
        //Given
        Power a = new Power();
        a.setId(2L);
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl2");

        Wizard w2 = new Wizard();
        w2.setId(2L);
        w2.setName("Harry potter");
        w2.addArtifact(a);


        given(this.powerRepository.findById(2L)).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3L)).willReturn(Optional.empty());

        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,()->{
            this.wizardService.assignArtifact(3L,2L);

        });
        //then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Wizard with id:3");
        assertThat(a.getOwner().getId()).isEqualTo(2L);
    }


    @Test
    void testAssignmentArtifactErrorWithNonExistentArtifactId()
    {
        //Given

        /*Pas besoin de wizard car dans WizardService le foundArtifactById lève une exception
            et du coup la ligne suivante ne sera pas exécutée.
                Wizard w3 = new Wizard();
                w3.setId(3);
                w3.setName("Neville Longbottom");
         */

        given(this.powerRepository.findById(2L)).willReturn(Optional.empty());

        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,()->{
            this.wizardService.assignArtifact(3L,2L);

        });
        //then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Power with id:2");
    }
}