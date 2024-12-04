package emcer.cg.fr.gestionutilisateuronline.power;

import emcer.cg.fr.gestionutilisateuronline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PowerService {

    private final PowerRepository powerRepository;

    public PowerService(PowerRepository powerRepository) {
        this.powerRepository = powerRepository;
    }

    public Power create(Power power) {
        return powerRepository.save(power);
    }
    public Power findById(Long id){
       return this.powerRepository.findById(id).orElseThrow(()->new ObjectNotFoundException("Power not found", id));
    }

    public List<Power> findAll(){
        return this.powerRepository.findAll();
    }
    public Power save(Power power){
        return this.powerRepository.save(power);
    }

    public Power update(Long artifactId, Power newPower){
       return this.powerRepository.findById(artifactId).map(old->{
            old.setName(newPower.getName());
            old.setDescription(newPower.getDescription());
            old.setImageUrl(newPower.getImageUrl());
           return this.powerRepository.save(old);
        }).orElseThrow(()->new ObjectNotFoundException(Power.class.getSimpleName(),artifactId));
    }

    public void delete(Long artifactId){
        this.findById(artifactId);
        this.powerRepository.deleteById(artifactId);
    }

}
