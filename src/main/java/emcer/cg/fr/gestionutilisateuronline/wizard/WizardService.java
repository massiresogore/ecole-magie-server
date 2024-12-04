package emcer.cg.fr.gestionutilisateuronline.wizard;

import emcer.cg.fr.gestionutilisateuronline.power.Power;
import emcer.cg.fr.gestionutilisateuronline.power.PowerRepository;
import emcer.cg.fr.gestionutilisateuronline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class WizardService {

    public WizardRepository wizardRepository;
    public PowerRepository powerRepository;

    public WizardService(WizardRepository wizardRepository, PowerRepository powerRepository) {
        this.wizardRepository = wizardRepository;
        this.powerRepository = powerRepository;
    }

    public List<Wizard> findAll()
    {
        return this.wizardRepository.findAll();
    }

    public Wizard save(Wizard wizard)
    {
        return this.wizardRepository.save(wizard);
    }
    public void delete(Long idWizard)
    {
        Wizard wisardToBeDeleted=  this.wizardRepository.findById(idWizard).
                orElseThrow(()->new ObjectNotFoundException(Wizard.class.getSimpleName(),idWizard));

        //Avnat de supprimer on doit supprimer les lien entre ces powers
        wisardToBeDeleted.removeAllArtifacts();
        this.wizardRepository.deleteById(idWizard);
    }


    public Wizard findById(Long id) {
        return wizardRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException(Wizard.class.getSimpleName(),id));
    }

    public Wizard update(Long id,Wizard wizard)
    {
        return this.wizardRepository.findById(id)
                .map(oldWizard->{
                    oldWizard.setName(wizard.getName());
                    return this.wizardRepository.save(oldWizard);
                })
                .orElseThrow(()->new ObjectNotFoundException(Wizard.class.getSimpleName(),id));
    }

    public void assignArtifact(Long wizardId, Long artifactId)
    {

        //Find artifact by id From DB
        Power powerTobeAssigned = this.powerRepository.findById(artifactId).orElseThrow(
                () -> new ObjectNotFoundException(Power.class.getSimpleName(), artifactId)
        );

        //Find wizard by id from DB
        Wizard wizard = this.wizardRepository.findById(wizardId).orElseThrow(
                () -> new ObjectNotFoundException(Wizard.class.getSimpleName(), wizardId)
        );

        //Power assignment
        //We need to see if the artifact is already owned by some wizard
        if(powerTobeAssigned.getOwner() != null){
            powerTobeAssigned.getOwner().removeArtifact(powerTobeAssigned);
        }

        wizard.addArtifact(powerTobeAssigned);

        this.powerRepository.save(powerTobeAssigned);

    }




}