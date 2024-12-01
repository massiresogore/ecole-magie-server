package emcer.cg.fr.gestionutilisateuronline.ecoleuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import emcer.cg.fr.gestionutilisateuronline.system.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for User API endpoints")
@Tag("integration")
@ActiveProfiles(value = "dev")
public class EcoleUserIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @Value("${api.endpoint.base-url}")
    String baseUrl;


    @BeforeEach
    void setUp() throws Exception {
        // User john has all permissions.
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("massire", "123456"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getJSONObject("data").getString("token");
    }

    @Test
    @DisplayName("Check findAllUsers (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindAllUsersSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Check findUserById (GET): User with ROLE_admin Accessing Any User's Info")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindUserByIdWithAdminAccessingAnyUsersInfo() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find one success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.username").value("Corintin Rosita"));
    }

    @Test
    @DisplayName("Check findUserById (GET): User with ROLE_user Accessing Own Info")
    void testFindUserByIdWithUserAccessingOwnInfo() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("Corintin Rosita", "123456"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        this.mockMvc.perform(get(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find one success"))
                .andExpect(jsonPath("$.data.id").value(2L))
                .andExpect(jsonPath("$.data.username").value("Corintin Rosita"));
    }

    /*Restriction User prochainement*/
    /*@Test
    @DisplayName("Check findUserById (GET): User with ROLE_user Accessing Another Users Info")
    void testFindUserByIdWithUserAccessingAnotherUsersInfo() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("eric", "654321"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        this.mockMvc.perform(get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission."))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }*/

    @Test
    @DisplayName("Check findUserById with non-existent id (GET)")
    void testFindUserByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/users/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id:5"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check addUser with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddUserSuccess() throws Exception {
        EcoleUser ecoleUser = new EcoleUser();
        ecoleUser.setUsername("lily");
        ecoleUser.setPassword("123456");
        ecoleUser.setEnable(true);
        ecoleUser.setRoles("admin user"); // The delimiter is space.

        String json = this.objectMapper.writeValueAsString(ecoleUser);

        this.mockMvc.perform(post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Create user success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("lily"))
                .andExpect(jsonPath("$.data.enable").value(true))
                .andExpect(jsonPath("$.data.roles").value("admin user"));
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
    }

    @Test
    @DisplayName("Check addUser with invalid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddUserErrorWithInvalidInput() throws Exception {
        EcoleUser ecoleUser = new EcoleUser();
        ecoleUser.setUsername(""); // Username is not provided.
        ecoleUser.setPassword(""); // Password is not provided.
        ecoleUser.setRoles(""); // Roles field is not provided.
        ecoleUser.setEnable(false);

        String json = this.objectMapper.writeValueAsString(ecoleUser);

        this.mockMvc.perform(post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided Arguments are invalid, see data for details. "))
                .andExpect(jsonPath("$.data.username").value("username is required"))
                .andExpect(jsonPath("$.data.password").value("password is required"))
                .andExpect(jsonPath("$.data.roles").value("roles are required"));
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Check updateUser with valid input (PUT)")
    void testUpdateUserWithAdminUpdatingAnyUsersInfo() throws Exception {
        EcoleUser ecoleUser = new EcoleUser();
        ecoleUser.setUsername("tom123"); // Username is changed. It was tom.
        ecoleUser.setEnable(false);
        ecoleUser.setRoles("user");

        String json = this.objectMapper.writeValueAsString(ecoleUser);

        this.mockMvc.perform(put(this.baseUrl + "/users/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update user success"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.username").value("tom123"))
                .andExpect(jsonPath("$.data.enable").value(false))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    @DisplayName("Check updateUser with non-existent id (PUT)")
    void testUpdateUserErrorWithNonExistentId() throws Exception {
        EcoleUser ecoleUser = new EcoleUser();
        ecoleUser.setId(5L); // This id does not exist in the database.
        ecoleUser.setUsername("john123"); // Username is changed.
        ecoleUser.setEnable(true);
        ecoleUser.setRoles("admin user");

        String json = this.objectMapper.writeValueAsString(ecoleUser);

        this.mockMvc.perform(put(this.baseUrl + "/users/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id:5"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check updateUser with invalid input (PUT)")
    void testUpdateUserErrorWithInvalidInput() throws Exception {
        EcoleUser ecoleUser = new EcoleUser();
        ecoleUser.setId(1L); // Valid id
        ecoleUser.setUsername(""); // Updated username is empty.
        ecoleUser.setRoles(""); // Updated roles field is empty.
        ecoleUser.setEnable(false);

        String json = this.objectMapper.writeValueAsString(ecoleUser);

        this.mockMvc.perform(put(this.baseUrl + "/users/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided Arguments are invalid, see data for details. "))
                .andExpect(jsonPath("$.data.username").value("usernamee is required."))
                .andExpect(jsonPath("$.data.roles").value("roles are required."));
        this.mockMvc.perform(get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find one success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("massire"));
    }

    @Test
    @DisplayName("Check updateUser with valid input (PUT): User with ROLE_user Updating Own Info")
    void testUpdateUserWithUserUpdatingOwnInfo() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("Corintin Rosita", "123456"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        EcoleUser ecoleUser = new EcoleUser();
        ecoleUser.setUsername("Corintin Rosito 123"); // Username is changed. It was eric.
        ecoleUser.setEnable(true);
        ecoleUser.setRoles("user");

        String ecoleUserJson = this.objectMapper.writeValueAsString(ecoleUser);

        this.mockMvc.perform(put(this.baseUrl + "/users/2").contentType(MediaType.APPLICATION_JSON).content(ecoleUserJson).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update user success"))
                .andExpect(jsonPath("$.data.id").value(2L))
                .andExpect(jsonPath("$.data.username").value("Corintin Rosito 123"))
                .andExpect(jsonPath("$.data.enable").value(true))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    //A faire prochainement restrick user update
    /*@Test
    @DisplayName("Check updateUser with valid input (PUT): User with ROLE_user Updating Another Users Info")
    void testUpdateUserWithUserUpdatingAnotherUsersInfo() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("eric", "654321"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        EcoleUser ecoleUser = new EcoleUser();
        ecoleUser.setUsername("tom123"); // Username is changed. It was tom.
        ecoleUser.setEnable(false);
        ecoleUser.setRoles("user");

        String ecoleUserJson = this.objectMapper.writeValueAsString(ecoleUser);

        this.mockMvc.perform(put(this.baseUrl + "/users/3").contentType(MediaType.APPLICATION_JSON).content(ecoleUserJson).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission."))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }*/

    @Test
    @DisplayName("Check deleteUser with valid input (DELETE)")
    void testDeleteUserSuccess() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete user success"))
                .andExpect(jsonPath("$.data").isEmpty());
        this.mockMvc.perform(get(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id:2"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check deleteUser with non-existent id (DELETE)")
    void testDeleteUserErrorWithNonExistentId() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/users/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id:5"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check deleteUser with insufficient permission (DELETE)")
    void testDeleteUserNoAccessAsRoleUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("Corintin Rosita", "123456"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        this.mockMvc.perform(delete(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].username").value("massire"));
    }

    //A faire prochainement changement de mot de passe
   /* @Test
    @DisplayName("Check changeUserPassword with valid input (PATCH)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testChangeUserPassword() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("eric", "654321"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        // Given
        Map<String, String> passwordMap = new HashMap<>();
        passwordMap.put("oldPassword", "654321");
        passwordMap.put("newPassword", "Abc12345");
        passwordMap.put("confirmNewPassword", "Abc12345");

        String passwordMapJson = this.objectMapper.writeValueAsString(passwordMap);

        this.mockMvc.perform(patch(this.baseUrl + "/users/2/password").contentType(MediaType.APPLICATION_JSON).content(passwordMapJson).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Change Password Success"));
    }

    @Test
    @DisplayName("Check changeUserPassword with wrong old password (PATCH)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testChangeUserPasswordWithWrongOldPassword() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("eric", "654321"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        // Given
        Map<String, String> passwordMap = new HashMap<>();
        passwordMap.put("oldPassword", "123456"); // Wrong old password.
        passwordMap.put("newPassword", "Abc12345");
        passwordMap.put("confirmNewPassword", "Abc12345");

        String passwordMapJson = this.objectMapper.writeValueAsString(passwordMap);

        this.mockMvc.perform(patch(this.baseUrl + "/users/2/password").contentType(MediaType.APPLICATION_JSON).content(passwordMapJson).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.UNAUTHORIZED))
                .andExpect(jsonPath("$.message").value("username or password is incorrect."))
                .andExpect(jsonPath("$.data").value("Old password is incorrect."));
    }

    @Test
    @DisplayName("Check changeUserPassword with new password not matching confirm new password (PATCH)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testChangeUserPasswordWithNewPasswordNotMatchingConfirmNewPassword() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("eric", "654321"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        // Given
        Map<String, String> passwordMap = new HashMap<>();
        passwordMap.put("oldPassword", "654321");
        passwordMap.put("newPassword", "Abc12345");
        passwordMap.put("confirmNewPassword", "Abc123456");

        String passwordMapJson = this.objectMapper.writeValueAsString(passwordMap);

        this.mockMvc.perform(patch(this.baseUrl + "/users/2/password").contentType(MediaType.APPLICATION_JSON).content(passwordMapJson).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("New password and confirm new password do not match."));
    }

    @Test
    @DisplayName("Check changeUserPassword with new password not conforming to password policy (PATCH)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testChangeUserPasswordWithNewPasswordNotConformingToPasswordPolicy() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("eric", "654321"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        // Given
        Map<String, String> passwordMap = new HashMap<>();
        passwordMap.put("oldPassword", "654321");
        passwordMap.put("newPassword", "short"); // New password is too short.
        passwordMap.put("confirmNewPassword", "short");

        String passwordMapJson = this.objectMapper.writeValueAsString(passwordMap);

        this.mockMvc.perform(patch(this.baseUrl + "/users/2/password").contentType(MediaType.APPLICATION_JSON).content(passwordMapJson).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("New password does not conform to password policy."));
    }
    */
}