package emcer.cg.fr.gestionutilisateuronline.power;

import emcer.cg.fr.gestionutilisateuronline.wizard.Wizard;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Power implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String imageUrl;

    @ManyToOne
    private Wizard owner;

    public Power() {
    }


    public Power(Long id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Power(Long id, String name, String description, String imageUrl, Wizard owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public Wizard getOwner() {
        return owner;
    }

    public void setOwner(Wizard owner) {
        this.owner = owner;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
