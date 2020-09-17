package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventPo;
import com.thoughtworks.rslist.dto.UserPo;
import com.thoughtworks.rslist.dto.VotePo;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class rsService {
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public rsService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public void vote(Vote vote, int rsEventId) {
        Optional<RsEventPo> rsEventDto = rsEventRepository.findById(rsEventId);
        Optional<UserPo> userDto = userRepository.findById(vote.getUserId());
        if (!rsEventDto.isPresent() || !userDto.isPresent()
            || vote.getVoteNum() > userDto.get().getVoteNum()) {
            throw new RuntimeException();
        }
        VotePo votePo =
                VotePo.builder()
                .user(userDto.get())
                .rsEvent(rsEventDto.get())
                .localDateTime(vote.getLocalDateTime())
                .voteNum(vote.getVoteNum())
                .build();
        voteRepository.save(votePo);
        UserPo addUserPo = userDto.get();
        addUserPo.setVoteNum(addUserPo.getVoteNum()-vote.getVoteNum());
        userRepository.save(addUserPo);
        RsEventPo newRsEventPo = rsEventDto.get();
        newRsEventPo.setVoteNum(newRsEventPo.getVoteNum()+vote.getVoteNum());
        rsEventRepository.save(newRsEventPo);
    }
}
