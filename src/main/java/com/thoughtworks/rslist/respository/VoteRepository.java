package com.thoughtworks.rslist.respository;

import com.thoughtworks.rslist.Po.VotePo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VotePo, Integer> {
    @Override
    List<VotePo> findAll();

    @Query("select v from VotePo v where v.user.id = :userId and v.rsEvent.id = :rsEventId")
    List<VotePo> findAcoordingToUserIdAndRsEventId(int userId, int rsEventId, Pageable pageble);
}
