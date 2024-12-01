package emcer.cg.fr.gestionutilisateuronline.ecoleuser;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.io.Serializable;


import jakarta.persistence.*;
        import jakarta.validation.constraints.NotEmpty;
@Entity
@Table(name = "ecole_user")
public class EcoleUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "username is required")
    private String username;
    @NotEmpty(message = "password is required")
    private String password;

    @Column(name = "enabled")
    private Boolean enable;
    @NotEmpty(message = "roles are required")
    private String roles; //Space separete string

    public EcoleUser() {
    }

    public EcoleUser(Long id, String username, String password, Boolean enable, String roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enable = enable;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
    public boolean isEnabled() {
        return this.enable;
    }
}