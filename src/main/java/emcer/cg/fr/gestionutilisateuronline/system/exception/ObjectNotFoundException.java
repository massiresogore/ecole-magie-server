package emcer.cg.fr.gestionutilisateuronline.system.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String ObjectName, Long id) {
        super("Could not find "+ObjectName+" with id:"+id);
    }
}
