package emcer.cg.fr.gestionutilisateuronline.security;

import emcer.cg.fr.gestionutilisateuronline.ecoleuser.EcoleUser;
import emcer.cg.fr.gestionutilisateuronline.ecoleuser.MyUserPrincipal;
import emcer.cg.fr.gestionutilisateuronline.ecoleuser.converter.UserToUserDtoConverter;
import emcer.cg.fr.gestionutilisateuronline.ecoleuser.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }


    public Map<String, Object> createLoginInfo(Authentication authentication) {
        //Create UserInfo.
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        EcoleUser user = principal.getEcoleUser();
        UserDto userDto = this.userToUserDtoConverter.convert(user);


        //Craete JWT
        String token =  this.jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("userInfo",userDto);
        loginResultMap.put("token",token);

        return loginResultMap;
    }
}