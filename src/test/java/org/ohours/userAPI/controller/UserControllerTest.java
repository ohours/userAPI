package org.ohours.userAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ohours.userAPI.controller.exception.UserIsMinorException;
import org.ohours.userAPI.controller.exception.UserNotFoundException;
import org.ohours.userAPI.controller.exception.UsernameTakenException;
import org.ohours.userAPI.model.Country;
import org.ohours.userAPI.model.Gender;
import org.ohours.userAPI.model.MyUser;
import org.ohours.userAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeAll() {
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnCreated() throws Exception {

        MyUser user = new MyUser("Boby", LocalDate.parse("1990-11-29"), Country.FRANCE, null, Gender.MALE);

        given(userService.createUser(any(MyUser.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print()).
                andExpect(status().isCreated());
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnTaken() throws Exception {

        MyUser user = new MyUser("Boby", LocalDate.parse("1990-11-29"), Country.FRANCE, null, null);

        given(userService.createUser(any(MyUser.class)))
                .willThrow(new UsernameTakenException(user.getUsername()));

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print()).
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].error", is("UsernameTaken")))
                .andExpect(jsonPath("$.errors[0].message", is("Username Boby is taken, choose another")));
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnIsMinor() throws Exception {

        MyUser user = new MyUser("Boby", LocalDate.parse("1990-11-29"), Country.FRANCE, null, null);

        given(userService.createUser(any(MyUser.class)))
                .willThrow(new UserIsMinorException());

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));
        ;

        response.andDo(print()).
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].error", is("UserIsMinor")))
                .andExpect(jsonPath("$.errors[0].message", is("Currently only accepting adult users")));
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnWrongDateFormat() throws Exception {

        MyUser user = new MyUser("Boby", LocalDate.parse("1990-11-29"), Country.FRANCE, null, null);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user).replace("1990-11-29", "199-11-29")));
        ;

        response.andDo(print()).
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].error", is("birthdate")))
                .andExpect(jsonPath("$.errors[0].message", is("199-11-29")));
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnWrongCountry() throws Exception {

        MyUser user = new MyUser("Boby", LocalDate.parse("1990-11-29"), Country.FRANCE, null, null);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user).replace("FRANCE", "USA")));
        ;

        response.andDo(print()).
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].error", is("countryOfResidence")))
                .andExpect(jsonPath("$.errors[0].message", is("Only French residents allowed, found USA")));
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnWrongGender() throws Exception {

        MyUser user = new MyUser("Boby", LocalDate.parse("1990-11-29"), Country.FRANCE, null, Gender.OTHER);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user).replace("OTHER", "FEMME")));
        ;

        response.andDo(print()).
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].error", is("gender")))
                .andExpect(jsonPath("$.errors[0].message", is("Gender found FEMME, allowed from [MALE, FEMALE, OTHER]")));
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnWrongPhone() throws Exception {

        MyUser user = new MyUser("Boby", LocalDate.parse("1990-11-29"), Country.FRANCE, "(555) 555-1234", null);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));
        ;

        response.andDo(print()).
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].error", is("phoneNumber")))
                .andExpect(jsonPath("$.errors[0].message", is("french number only, format (0|+33|0033)123456789")));
    }

    @Test
    public void givenUsernameString_whenGetUser_thenReturnUser() throws Exception {

        MyUser user = new MyUser(52L, "Boby", LocalDate.parse("1990-11-29"), Country.FRANCE, "0623456789", Gender.OTHER);

        given(userService.getUser(user.getUsername()))
                .willReturn(Optional.of(user));

        ResultActions response = mockMvc.perform(get("/users/{username}", user.getUsername()));

        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username", is("Boby")))
                .andExpect(jsonPath("$.birthdate", is("1990-11-29")))
                .andExpect(jsonPath("$.countryOfResidence", is("FRANCE")))
                .andExpect(jsonPath("$.phoneNumber", is("0623456789")))
                .andExpect(jsonPath("$.gender", is("OTHER")));
    }

    @Test
    public void givenUsernameString_whenGetUser_thenReturnNotFound() throws Exception {

        MyUser user = new MyUser(52L, "Boby", LocalDate.parse("1990-11-29"), Country.FRANCE, "0623456789", Gender.OTHER);

        given(userService.getUser(user.getUsername()))
                .willThrow(new UserNotFoundException(user.getUsername()));

        ResultActions response = mockMvc.perform(get("/users/{username}", user.getUsername()));

        response.andDo(print()).
                andExpect(status().isNotFound());
    }

}