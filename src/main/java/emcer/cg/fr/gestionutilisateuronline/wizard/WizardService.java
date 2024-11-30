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
                orElseThrow(()->new ObjectNotFoundException("name",idWizard));

        //Avnat de supprimer on doit supprimer les lien entre ces artifacts
        wisardToBeDeleted.removeAllArtifacts();
        this.wizardRepository.deleteById(idWizard);
    }


    public Wizard findById(Long id) {
        return wizardRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("name",id));
    }

    public Wizard update(Long id,Wizard wizard)
    {
        return this.wizardRepository.findById(id)
                .map(oldWizard->{
                    oldWizard.setName(wizard.getName());
                    return this.wizardRepository.save(oldWizard);
                })
                .orElseThrow(()->new ObjectNotFoundException("",id));
    }

    public void assignArtifact(Long wizardId, Long artifactId)
    {

        //Find artifact by id From DB
        Artifact artifactTobeAssigned = this.artifactRepository.findById(artifactId).orElseThrow(
                () -> new ObjectNotFoundException("artifact", artifactId)
        );

        //Find wizard by id from DB
        Wizard wizard = this.wizardRepository.findById(wizardId).orElseThrow(
                () -> new ObjectNotFoundException("wizard", wizardId)
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