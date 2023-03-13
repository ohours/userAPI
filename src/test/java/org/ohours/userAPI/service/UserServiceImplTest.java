package org.ohours.userAPI.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ohours.userAPI.controller.exception.UserIsMinorException;
import org.ohours.userAPI.controller.exception.UsernameTakenException;
import org.ohours.userAPI.model.Country;
import org.ohours.userAPI.model.Gender;
import org.ohours.userAPI.model.MyUser;
import org.ohours.userAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void givenUserObject_whenCreateUser_thenReturnCreated() throws Exception {
        LocalDate birthdate = LocalDate.now().minusYears(18);

        MyUser user = new MyUser("Boby", birthdate, Country.FRANCE, "0123456789", Gender.MALE);

        given(userRepository.save(any(MyUser.class)))
                .willReturn(user);

        MyUser savedUser = userService.createUser(user);

        assertEquals(user, savedUser);
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnTaken() throws Exception {

        MyUser user = new MyUser("Boby", LocalDate.parse("1990-11-29"), Country.FRANCE, null, Gender.FEMALE);

        given(userRepository.existsByUsername(user.getUsername()))
                .willReturn(true);

        UsernameTakenException exception = assertThrows(UsernameTakenException.class, () -> {
            userService.createUser(user);
        });

        assertEquals(exception.getTitle(), "UsernameTaken");
        assertEquals(exception.getMessage(), "Username Boby is taken, choose another");
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnIsMinor() throws Exception {
        LocalDate birthdate = LocalDate.now().minusYears(18).plusDays(1);

        MyUser user = new MyUser("Boby", birthdate, Country.FRANCE, null, Gender.FEMALE);

        UserIsMinorException exception = assertThrows(UserIsMinorException.class, () -> {
            userService.createUser(user);
        });

        assertEquals(exception.getTitle(), "UserIsMinor");
        assertEquals(exception.getMessage(), "Currently only accepting adult users");
    }

    @Test
    public void givenUsernameString_whenGetUser_thenReturnUser() throws Exception {

        MyUser user = new MyUser(52L, "Boby", LocalDate.parse("1990-11-29"), Country.FRANCE, "0623456789", Gender.OTHER);

        given(userRepository.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

        MyUser returnedUser = userService.getUser(user.getUsername()).get();

        assertEquals(user, returnedUser);
    }

    @Test
    public void givenUsernameString_whenGetUser_thenReturnNotFound() throws Exception {

        given(userRepository.findByUsername("toto"))
                .willReturn(Optional.empty());

        assertTrue(userService.getUser("toto").isEmpty());
    }

    @Test
    void createUser() {
    }

    @Test
    void getUser() {
    }
}