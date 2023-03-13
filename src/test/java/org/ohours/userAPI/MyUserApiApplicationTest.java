package org.ohours.userAPI;


import org.junit.jupiter.api.Test;
import org.ohours.userAPI.model.Country;
import org.ohours.userAPI.model.Gender;
import org.ohours.userAPI.model.MyUser;
import org.ohours.userAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MyUserApiApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repository;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        MyUser userBob = new MyUser("Bob", LocalDate.parse("1990-01-20"), Country.FRANCE, null, Gender.MALE);
        when(repository.findByUsername("Bob")).thenReturn(Optional.of(userBob));
        this.mockMvc.perform(get("/user/Bob")).andDo(print()).andExpect(status().isOk());
    }

}