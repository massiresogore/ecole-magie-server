package emcer.cg.fr.gestionutilisateuronline.ecoleuser;


import emcer.cg.fr.gestionutilisateuronline.ecoleuser.converter.UserDtoToUserConverter;
import emcer.cg.fr.gestionutilisateuronline.ecoleuser.converter.UserToUserDtoConverter;
import emcer.cg.fr.gestionutilisateuronline.ecoleuser.dto.UserDto;
import emcer.cg.fr.gestionutilisateuronline.system.Result;
import emcer.cg.fr.gestionutilisateuronline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserDtoToUserConverter userDtoToUserConverter;

    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }

    @GetMapping
    public Result findAllUsers()
    {

        return  new Result(true,"Find all success", StatusCode.SUCCESS,
                this.userService.findAll().stream()
                        .map(userToUserDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable String userId)
    {
        return  new Result(true,"Find one success", StatusCode.SUCCESS,
                userToUserDtoConverter.convert(this.userService.findById(Long.valueOf(userId))));
    }

    @PostMapping
    public Result addUser( @Valid @RequestBody EcoleUser hogwartUser)
    {
        //On utilise HogWartUser et non user Dto cart on fera le changement de password prochainement
        return  new Result(true,"Create user success", StatusCode.SUCCESS,
                this.userToUserDtoConverter.convert(this.userService.save(hogwartUser)));
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable String userId,@Valid @RequestBody UserDto userDto)
    {
        EcoleUser updatedUser =  this.userService.update(Long.valueOf(userId), userDtoToUserConverter.convert(userDto));


        return  new Result(true,"Update user success", StatusCode.SUCCESS,
                this.userToUserDtoConverter.convert(updatedUser));
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable String userId)
    {
        this.userService.delete(Long.valueOf(userId));

        return new Result(true,"Delete user success",StatusCode.SUCCESS);
    }

}