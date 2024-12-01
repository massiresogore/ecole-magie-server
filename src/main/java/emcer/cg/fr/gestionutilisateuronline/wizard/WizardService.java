package emcer.cg.fr.gestionutilisateuronline.wizard;

import emcer.cg.fr.gestionutilisateuronline.artifact.Artifact;
import emcer.cg.fr.gestionutilisateuronline.artifact.ArtifactRepository;
import emcer.cg.fr.gestionutilisateuronline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {

    public WizardRepository wizardRepository;
    public ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
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

        //Avnat de supprimer on doit supprimer les lien entre ces artifacts
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
        Artifact artifactTobeAssigned = this.artifactRepository.findById(artifactId).orElseThrow(
                () -> new ObjectNotFoundException(Artifact.class.getSimpleName(), artifactId)
        );

        //Find wizard by id from DB
        Wizard wizard = this.wizardRepository.findById(wizardId).orElseThrow(
                () -> new ObjectNotFoundException(Wizard.class.getSimpleName(), wizardId)
        );

        //Artifact assignment
        //We need to see if the artifact is already owned by some wizard
        if(artifactTobeAssigned.getOwner() != null){
            artifactTobeAssigned.getOwner().removeArtifact(artifactTobeAssigned);
        }

        wizard.addArtifact(artifactTobeAssigned);

        this.artifactRepository.save(artifactTobeAssigned);

    }




}