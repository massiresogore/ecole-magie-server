package emcer.cg.fr.gestionutilisateuronline.wizard;

import emcer.cg.fr.gestionutilisateuronline.power.Power;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;

import java.util.ArrayList;


@Entity
public class Wizard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "name is required.")
    @NonNull
    private String name;

    //one wizard are many artifact,
    // Le nom de mappedBy doit correspondre au nom donné dans @ManyToOne
        /*
         @ManyToOne
            private Wizard owner;
         */
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner")
    List<Power> powers = new ArrayList<>();

    public List<Power> getArtifacts() {
        return powers;
    }

    public void setArtifacts(List<Power> powers) {
        this.powers = powers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addArtifact(Power power) {
        //We set the owner of the power to the current wizard
        power.setOwner(this);
        //we add the power to this wizard
        this.powers.add(power);
    }

    public Integer getNumberOfArtifacts() {
        return this.getArtifacts().size();
    }

    public void removeAllArtifacts()
    {
        this.powers.stream().forEach(artifact->artifact.setOwner(null));
        this.powers = null;
    }

    public void removeArtifact(Power powerTobeAssigned) {
        //remove artifact owner
        powerTobeAssigned.setOwner(null);
        this.powers.remove(powerTobeAssigned);
    }
}