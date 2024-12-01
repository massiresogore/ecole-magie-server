package emcer.cg.fr.gestionutilisateuronline.ecoleuser;

import static org.junit.jupiter.api.Assertions.*;


import emcer.cg.fr.gestionutilisateuronline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "dev")
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;


    List<EcoleUser> users = new ArrayList<>();

    @BeforeEach
    void setUp() {
        //Create the newest users
        EcoleUser user1 = new EcoleUser();
        user1.setUsername("Massire");
        user1.setEnable(true);
        user1.setPassword("massire");
        user1.setRoles("admin user");
        this.users.add(user1);

        EcoleUser user2 = new EcoleUser();
        user2.setUsername("BingYang");
        user2.setPassword("bingyang");
        user2.setEnable(true);
        user2.setRoles("user");
        this.users.add(user2);

        EcoleUser user3 = new EcoleUser();
        user3.setUsername("Binta");
        user3.setPassword("binta");
        user3.setEnable(false);
        user3.setRoles("user");
        this.users.add(user3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        //Given
        given(this.userRepository.findAll()).willReturn(this.users);

        //When
        List<EcoleUser> actualUsers = this.userService.findAll();

        //Then
        assertThat(actualUsers.size()).isEqualTo(this.users.size());
        verify(userRepository,times(1)).findAll();
    }

    @Test
    void testFindUserByIdSuccess()
    {
        EcoleUser user1 = new EcoleUser();
        user1.setId(1L);
        user1.setUsername("Massire");
        user1.setEnable(true);
        user1.setPassword("massire");
        user1.setRoles("admin user");

        //Given
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));

        //When
        EcoleUser returnUser = userService.findById(1L);

        //Then
        assertThat(returnUser.getId()).isEqualTo(user1.getId());
        assertThat(returnUser.getUsername()).isEqualTo(user1.getUsername());
        assertThat(returnUser.getRoles()).isEqualTo(user1.getRoles());
        verify(userRepository,times(1)).findById(1L);

    }

    @Test
    void testFindUserByIdNotFound()
    {
        //Given
        given(userRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()->{
            EcoleUser returnUser = userService.findById(1L);
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with id:1");
        verify(userRepository,times(1)).findById(1L);
    }

    @Test
    void testAddUserSuccess()
    {

        //Given
        EcoleUser user = new EcoleUser();
        user.setId(1L);
        user.setUsername("Massire");
        user.setEnable(true);
        user.setPassword("massire");
        user.setRoles("admin user");

        given(this.passwordEncoder.encode(user.getPassword())).willReturn("Encoded Password");
        given(userRepository.save(user)).willReturn(user);

        //When
        EcoleUser savedUser =userService.save(user);

        //Then
        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(savedUser.getRoles()).isEqualTo(user.getRoles());
        verify(userRepository,times(1)).save(user);
    }

    @Test
    void testUpdateUserSuccess()
    {

        //Given
        //envoyé au formulaire
        EcoleUser oldUser = new EcoleUser();
        oldUser.setId(1L);
        oldUser.setUsername("Massire");
        oldUser.setEnable(true);
        oldUser.setPassword("massire");
        oldUser.setRoles("admin user");

        //crée de la base de donnée
        EcoleUser update = new EcoleUser();
        update.setId(1L);
        update.setUsername("Massire");
        update.setEnable(true);
        update.setRoles("admin user");

        given(userRepository.findById(1L)).willReturn(Optional.of(oldUser));
        given(userRepository.save(oldUser)).willReturn(oldUser);

        //When
        EcoleUser updatedUser = userService.update(1L,update);

        //Then
        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getUsername()).isEqualTo("Massire");
        assertThat(updatedUser.getRoles()).isEqualTo("admin user");
        verify(userRepository,times(1)).findById(1L);
        verify(userRepository,times(1)).save(oldUser);
    }

    @Test
    void testUpdateUserNotFound()
    {
        //Given
        EcoleUser update = new EcoleUser();
        update.setId(1L);
        update.setUsername("massire");
        update.setRoles("user");
        update.setPassword("massire");

        given(userRepository.findById(1L)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class,()->{
            userService.update(1L,update);
        });

        //Then
        verify(userRepository,times(1)).findById(1L);
    }

    @Test
    void testDeleUserSuccess()
    {
        //Given
        EcoleUser user = new EcoleUser();
        user.setId(1L);
        user.setUsername("massire");
        user.setEnable(true);
        user.setRoles("user");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);

        //When
        userService.delete(1L);

        //Then
        verify(userRepository,times(1)).findById(1L);

    }

    @Test
    void testDeleteUserNotFound()
    {
        //Given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        //Then
        assertThrows(ObjectNotFoundException.class,()->{
            userService.delete(1L);
        });

        //When
        verify(userRepository,times(1)).findById(1L);
    }
}