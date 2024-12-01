package emcer.cg.fr.gestionutilisateuronline.system;


import emcer.cg.fr.gestionutilisateuronline.artifact.Artifact;
import emcer.cg.fr.gestionutilisateuronline.artifact.ArtifactRepository;
import emcer.cg.fr.gestionutilisateuronline.ecoleuser.EcoleUser;
import emcer.cg.fr.gestionutilisateuronline.ecoleuser.UserService;
import emcer.cg.fr.gestionutilisateuronline.wizard.Wizard;
import emcer.cg.fr.gestionutilisateuronline.wizard.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//@Profile("dev")
@Component // spring will pick it up this class, and initialize it as a bean
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;
    private final WizardRepository wizardRepository;
    private final UserService userService;

    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository, UserService userService) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
        this.userService = userService;
    }


    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {

        Artifact a1 = new Artifact();
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl2");

        Artifact a3 = new Artifact();
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl3");

        Artifact a4 = new Artifact();
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl4");

        Artifact a5 = new Artifact();
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl5");

        Artifact a6 = new Artifact();
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl6");


        Wizard w1 = new Wizard();
        w1. setName ("Albus Dumbledore") ;
        w1.addArtifact(a1);
        w1.addArtifact(a3);

        Wizard w2 = new Wizard();
        w2. setName ("Harry Potter");
        w2.addArtifact(a2);
        w2.addArtifact(a4);

        Wizard w3 = new Wizard();
        w3. setName ("Neville Longbotton");
        w3.addArtifact(a5);


        //Create the newest users
        EcoleUser user1 = new EcoleUser();
        user1.setUsername("massire");
        user1.setEnable(true);
        user1.setPassword("123456");
        user1.setRoles("admin user");

        EcoleUser user2 = new EcoleUser();
        user2.setUsername("Corintin Rosita");
        user2.setPassword("123456");
        user2.setEnable(true);
        user2.setRoles("user");

        EcoleUser user3 = new EcoleUser();
        user3.setUsername("Michèl Sébatier");
        user3.setPassword("123456");
        user3.setEnable(false);//Désactive le compte
        user3.setRoles("user");

        //save Users
        userService.save(user1);
        userService.save(user2);
        userService.save(user3);



        this.wizardRepository.save(w1);
        this.wizardRepository.save(w2);
        this.wizardRepository.save(w3);
        this.artifactRepository.save(a6);

    }
}