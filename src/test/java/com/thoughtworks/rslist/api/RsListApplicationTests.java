package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.Po.RsEventPo;
import com.thoughtworks.rslist.Po.UserPo;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsListApplicationTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    UserPo userPo;
    RsEventPo rsEventPo;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        userPo = userRepository.save(UserPo.builder().email("a@b.com").age(19).gender("female")
                .phone("18888888888").userName("hehe").voteNum(10).build());
        rsEventPo = rsEventRepository.save(RsEventPo.builder().eventName("have money")
                .keyword("economy").userPo(userPo).voteNum(0).build());
        objectMapper = new ObjectMapper();

    }

    @Test
    @Order(1)
    void should_get_rsEvent() throws Exception {
        mockMvc.perform(get("/rsEvent"))
                .andExpect(jsonPath("$[0].eventName", is("have money")))
                .andExpect(jsonPath("$[0].keyword", is("economy")))
                .andExpect(jsonPath("$[0].voteNum", is(0)))
                .andExpect(jsonPath("$[0]", not(hasKey("userPo"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void should_get_rsEvent_by_rsEventId() throws Exception {
        mockMvc.perform(get("/rs/{rsEventId}", String.valueOf(rsEventPo.getId())))
                .andExpect(jsonPath("$.eventName", is("have money")))
                .andExpect(jsonPath("$.keyword", is("economy")))
                .andExpect(jsonPath("$.voteNum", is(0)))
                .andExpect(jsonPath("$", not(hasKey("userPo"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void should_throw_exception_when_rsEventId_invalid() throws Exception {
        mockMvc.perform(get("/rs/{rsEventId}", String.valueOf(100)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid rsEventId")));
    }


    @Test
    public void should_add_new_rsEvent_when_user_exists() throws Exception {
        String jsonString = "{\"eventName\":\"hello\",\"keyWord\":\"h\",\"userId\": " + userPo.getId()+ " }";
        mockMvc.perform(post("/rs/rsEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void should_not_add_new_rsEvent_when_user_not_exists() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价啦\",\"keyWord\":\"经济\",\"userId\": 100 }";
        mockMvc.perform(post("/rs/rsEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }


    @Test
    public void should_show_all_users() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].userName", is("hehe")))
                .andExpect(jsonPath("$[0].gender", is("female")))
                .andExpect(jsonPath("$[0].email", is("a@b.com")))
                .andExpect(jsonPath("$[0].phone", is("18888888888")))
                .andExpect(jsonPath("$[0].age", is(19)))
                .andExpect(jsonPath("$[0].voteNum", is(10)));
    }


    @Test
    public void should_update_rsEvent_when_rsEventId_match_userId() throws Exception {
        RsEventPo oldRsEventPo = rsEventRepository.save(RsEventPo.builder().eventName("a")
                .keyword("k").userPo(userPo).build());
        String jsonString = "{\"eventName\":\"a2\",\"keyWord\":\"k2\",\"userId\": " + userPo.getId() + "}";
        mockMvc.perform(patch("/rs/{rsEventId}",String.valueOf(oldRsEventPo.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        RsEventPo newRsEvent = rsEventRepository.findById(oldRsEventPo.getId()).get();
        assertEquals("a2", newRsEvent.getEventName());
        assertEquals("k2", newRsEvent.getKeyword());


    }

    @Test
    public void should_throw_exception_when_userId_null() throws Exception {
        RsEventPo oldRsEventPo = rsEventRepository.save(RsEventPo.builder().eventName("abc")
                .keyword("d").userPo(userPo).build());
        String jsonString = "{\"eventName\":\"a\",\"keyWord\":\"k\",\"userId\": }";
        mockMvc.perform(patch("/rs/{rsEventId}",String.valueOf(oldRsEventPo.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_update_rsEvent_when_only_eventName() throws Exception {
        RsEventPo oldRsEventPo = rsEventRepository.save(RsEventPo.builder().eventName("abc")
                .keyword("d").userPo(userPo).build());
        String jsonString = "{\"eventName\":\"a\",\"keyWord\":null,\"userId\": " + userPo.getId() + "}";
        mockMvc.perform(patch("/rs/{rsEventId}",String.valueOf(oldRsEventPo.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        RsEventPo newRsEvent = rsEventRepository.findById(oldRsEventPo.getId()).get();
        assertEquals("a", newRsEvent.getEventName());
        assertEquals("d", newRsEvent.getKeyword());
    }

    @Test
    public void should_update_rsEvent_when_only_keyword() throws Exception {
        RsEventPo oldRsEventPo = rsEventRepository.save(RsEventPo.builder().eventName("a")
                .keyword("b").userPo(userPo).build());
        String jsonString = "{\"eventName\":null,\"keyWord\":\"c\",\"userId\": " + userPo.getId() + "}";
        mockMvc.perform(patch("/rs/{rsEventId}",String.valueOf(oldRsEventPo.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        RsEventPo newRsEvent = rsEventRepository.findById(oldRsEventPo.getId()).get();
        assertEquals("a", newRsEvent.getEventName());
        assertEquals("c", newRsEvent.getKeyword());
    }

}
