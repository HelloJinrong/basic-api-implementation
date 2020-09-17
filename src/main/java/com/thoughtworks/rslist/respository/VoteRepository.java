package com.thoughtworks.rslist.respository;

import com.thoughtworks.rslist.dto.VotePo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<VotePo, Integer> {
    @Override
    List<VotePo> findAll();

    //@Query(value = "select v from VoteDto v where v.userDto.id = :userId and v.rsEventDto.id = :rsEventId", nativeQuery=true )
    List<VotePo> findAllByUserIdAndRsEventId(int userId, int rsEventId);
}
