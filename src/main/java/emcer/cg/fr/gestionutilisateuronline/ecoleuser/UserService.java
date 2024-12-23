package emcer.cg.fr.gestionutilisateuronline.ecoleuser;



import emcer.cg.fr.gestionutilisateuronline.system.exception.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<EcoleUser> findAll()
    {
        return  this.userRepository.findAll();
    }

    public EcoleUser findById(Long userId)
    {
        return userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new ObjectNotFoundException("user", userId));
    }

    public EcoleUser save(EcoleUser user)
    {
        //Encodé le mot de passe prochainement...
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public EcoleUser update(Long userId, EcoleUser update)
    {
        return this.userRepository.findById(userId)
                .map(oldUser->{
                    oldUser.setUsername(update.getUsername());
                    oldUser.setEnable(update.getEnable());
                    oldUser.setRoles(update.getRoles());

                    return this.userRepository.save(oldUser);
                })
                .orElseThrow(()->new ObjectNotFoundException("user",userId));
    }

    public void delete(Long userId)
    {
        userRepository.findById(userId).orElseThrow(()->new ObjectNotFoundException("user", userId));

        this.userRepository.deleteById(userId);

    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return   this.userRepository.findByUsername(username)
                .map(ecoleUser -> new MyUserPrincipal(ecoleUser))
                .orElseThrow(()->new UsernameNotFoundException("username "+ username + " is not found"));
    }

}