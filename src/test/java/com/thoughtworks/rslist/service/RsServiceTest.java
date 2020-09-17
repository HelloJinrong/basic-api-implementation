package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventPo;
import com.thoughtworks.rslist.dto.UserPo;
import com.thoughtworks.rslist.dto.VotePo;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class RsServiceTest {
    rsService rsService;
    @Mock
    RsEventRepository rsEventRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    VoteRepository voteRepository;
    LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        initMocks(this);
        rsService = new rsService(rsEventRepository, userRepository, voteRepository);
        localDateTime = LocalDateTime.now();
    }

    @Test
    public void should_vote_success() {
        Vote vote = Vote.builder()
                .voteNum(2)
                .localDateTime(localDateTime)
                .userId(1)
                .rsEventId(2)
                .build();
        UserPo userPo = UserPo.builder()
                .userName("xzq")
                .age(25)
                .email("a@b.com")
                .gender("male")
                .phone("12345678987")
                .voteNum(4)
                .build();
        RsEventPo rsEventPo = RsEventPo.builder()
                .userPo(userPo)
                .eventName("eventName")
                .keyword("keyword")
                .voteNum(1)
                .id(1)
                .build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventPo));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userPo));

        rsService.vote(vote, 1);

        verify(voteRepository).save(VotePo.builder()
        .user(userPo)
        .rsEvent(rsEventPo)
        .localDateTime(localDateTime)
        .voteNum(2)
        .build());
        verify(rsEventRepository).save(rsEventPo);
        verify(userRepository).save(userPo);
        assertEquals(userPo.getVoteNum(),2);
        assertEquals(rsEventPo.getVoteNum(), 3);
    }

    @Test
    public void should_throw_exception_when_voteNum_invalid() {
        Vote vote = Vote.builder()
                .voteNum(5)
                .localDateTime(localDateTime)
                .userId(1)
                .rsEventId(2)
                .build();
        UserPo userPo = UserPo.builder()
                .userName("xzq")
                .age(25)
                .email("a@b.com")
                .gender("male")
                .phone("12345678987")
                .voteNum(4)
                .build();
        RsEventPo rsEventPo = RsEventPo.builder()
                .userPo(userPo)
                .eventName("eventName")
                .keyword("keyword")
                .voteNum(1)
                .build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventPo));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userPo));

        assertThrows(RuntimeException.class, () -> rsService.vote(vote, 1));
    }

    @Test
    public void should_throw_exception_when_rsEventId_not_exit() {
        Vote vote = Vote.builder()
                .voteNum(2)
                .localDateTime(localDateTime)
                .userId(1)
                .rsEventId(2)
                .build();
        UserPo userPo = UserPo.builder()
                .userName("xzq")
                .age(25)
                .email("a@b.com")
                .gender("male")
                .phone("12345678987")
                .voteNum(4)
                .build();
        RsEventPo rsEventPo = RsEventPo.builder()
                .userPo(userPo)
                .eventName("eventName")
                .keyword("keyword")
                .voteNum(1)
                .id(1)
                .build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userPo));

        assertThrows(RuntimeException.class, () -> rsService.vote(vote, 1));
    }

    @Test
    public void should_throw_exception_when_userId_not_exit() {
        Vote vote = Vote.builder()
                .voteNum(2)
                .localDateTime(localDateTime)
                .userId(1)
                .rsEventId(2)
                .build();
        UserPo userPo = UserPo.builder()
                .userName("xzq")
                .age(25)
                .email("a@b.com")
                .gender("male")
                .phone("12345678987")
                .voteNum(4)
                .id(1)
                .build();
        RsEventPo rsEventPo = RsEventPo.builder()
                .userPo(userPo)
                .eventName("eventName")
                .keyword("keyword")
                .voteNum(1)
                .id(1)
                .build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventPo));
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rsService.vote(vote, 1));
    }
}
