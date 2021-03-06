package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.Po.RsEventPo;
import com.thoughtworks.rslist.Po.UserPo;
import com.thoughtworks.rslist.Po.VotePo;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteRepository voteRepository;

    UserPo userPo;
    RsEventPo rsEventPo;
    VotePo votePo;
    LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        userPo = userRepository.save(UserPo.builder().email("a@b.com").age(19).gender("female")
                .phone("18888888888").userName("hjr").voteNum(10).build());
        rsEventPo = rsEventRepository.save(RsEventPo.builder().eventName("hava money")
                .keyword("wish").userPo(userPo).voteNum(0).build());
        localDateTime = LocalDateTime.of(2020,9,19,0,0,0);
    }

    @AfterEach
    void tearDown() {

    }

    /*@Test
    public void should_get_vote_record() throws Exception {
        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userPo.getId()))
                .param("rsEventId", String.valueOf(rsEventPo.getId())))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(5)));

    }*/

    @Test
    public void should_get_vote_record() throws Exception{
        for(int i=0;i<8;i++)
        {
            votePo = VotePo.builder().rsEvent(rsEventPo).voteNum(i+1).user(userPo)
                    .localDateTime(LocalDateTime.now()).build();
            voteRepository.save(votePo);
        }

        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userPo.getId()))
                .param("rsEventId", String.valueOf(rsEventPo.getId()))
                .param("pageIndex", "1"))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[1].voteNum", is(2)))
                .andExpect(jsonPath("$[2].voteNum", is(3)))
                .andExpect(jsonPath("$[3].voteNum", is(4)))
                .andExpect(jsonPath("$[4].voteNum", is(5)));

        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userPo.getId()))
                .param("rsEventId", String.valueOf(rsEventPo.getId()))
                .param("pageIndex", "2"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(6)))
                .andExpect(jsonPath("$[1].voteNum", is(7)))
                .andExpect(jsonPath("$[2].voteNum", is(8)));
    }

    @Test
    public void should_get_voteRecord_between_days() throws Exception {
        for(int i=0;i<8;i++)
        {
           localDateTime = localDateTime.plusDays(1);
            votePo = VotePo.builder().rsEvent(rsEventPo).voteNum(i+1).user(userPo)
                    .localDateTime(localDateTime).build();
            voteRepository.save(votePo);
        }
        LocalDateTime startTime = LocalDateTime.of(2020,9,1,0,0,0);
        LocalDateTime endTime = LocalDateTime.of(2020,10,1,0,0,0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        mockMvc.perform(get("/voteRecord/time")
                .param("startTimeString", df.format(startTime))
                .param("endTimeString", df.format(endTime)))
                .andExpect(jsonPath("$",hasSize(8)))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_null_voteRecord_between_days() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(2020,7,1,0,0,0);
        LocalDateTime endTime = LocalDateTime.of(2020,8,1,0,0,0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        mockMvc.perform(get("/voteRecord/time")
                .param("startTimeString", df.format(startTime))
                .param("endTimeString", df.format(endTime)))
                .andExpect(jsonPath("$",hasSize(0)))
                .andExpect(status().isOk());
    }


}
