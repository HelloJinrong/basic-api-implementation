package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.Po.RsEventPo;
import com.thoughtworks.rslist.Po.UserPo;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.fasterxml.jackson.databind.MapperFeature.USE_ANNOTATIONS;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;


    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    public void should_register_user() throws Exception {
        User user = new User("he","female",20,"a@b.com","12345678901");
        objectMapper.configure(USE_ANNOTATIONS, false);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserPo> all =  userRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(20, all.get(0).getAge());
        assertEquals("a@b.com", all.get(0).getEmail());
        assertEquals("female", all.get(0).getGender());
        assertEquals("he", all.get(0).getUserName());
        assertEquals("12345678901", all.get(0).getPhone());

    }

    @Test
    public void should_register_user_name_less_8() throws Exception {
        User user = new User("heeeeeeeeee","female",20,"a@b.com","12345678675");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    public void should_register_user_gender_notNull() throws Exception {
        User user = new User("he",null,20,"a@b.com","12345678675");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    public void should_register_user_age_more_then_18() throws Exception {
        User user = new User("he","female",10,"a@b.com","12345678675");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    public void should_register_user_email_regular() throws Exception {
        User user = new User("he","female",20,"ab.com","12345678675");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    public void should_register_user_phone_regular() throws Exception {
        User user = new User("he","female",20,"a@b.com","1234567867");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    public void should_delete_user() throws Exception {
        UserPo save = userRepository.save(UserPo.builder().email("a@b.com").age(19).gender("female")
                .phone("18888888888").userName("xiaohe").voteNum(10).build());
        RsEventPo rsEventPo = RsEventPo.builder().eventName("eventName")
                .keyword("keyWord").userPo(save).build();
        rsEventRepository.save(rsEventPo);
        mockMvc.perform(delete("/user/{id}", save.getId())).andExpect(status().isOk());
        assertEquals(0,userRepository.findAll().size());
        assertEquals(0,rsEventRepository.findAll().size());

    }

    @Test
    public void should_get_user_by_id() throws Exception {
        UserPo newUser = userRepository.save(UserPo.builder().email("a@b.com").age(18).gender("female")
                .phone("18888888889").userName("hjr").voteNum(10).build());
        userRepository.save(newUser);
        mockMvc.perform(get("/user/{id}",newUser.getId()))
                .andExpect(jsonPath("$.userName",is("hjr")))
                .andExpect(jsonPath("$.email",is("a@b.com")))
                .andExpect(jsonPath("$.gender",is("female")))
                .andExpect(jsonPath("$.phone",is("18888888889")))
                .andExpect(jsonPath("$.age",is(18)))
                .andExpect(jsonPath("$.voteNum",is(10)))
                .andExpect(status().isOk());

    }

}
