package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.respository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class voteController {
//    @Autowired
    VoteRepository voteRepository;
    public voteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> should_get_record(@RequestParam int userId, @RequestParam int rsEventId,@RequestParam int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex-1,5);
        return ResponseEntity.ok(
                voteRepository. findAcoordingToUserIdAndRsEventId(userId, rsEventId,pageable).stream()
                .map(item -> Vote.builder().rsEventId(item.getRsEvent().getId()).userId(item.getUser().getId())
                .localDateTime(item.getLocalDateTime()).voteNum(item.getVoteNum()).build()).collect(Collectors.toList())
        );
    }

    //查询投票记录接口： 参数传入起止时间，查询在该时间范围内的所有投票记录，写测试验证
    @GetMapping("/voteRecord/time")
    public ResponseEntity<List<Vote>> should_get_record_by_time(@RequestParam String startTimeString, @RequestParam String endTimeString) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(startTimeString,df);
        LocalDateTime endTime = LocalDateTime.parse(endTimeString,df);
        return ResponseEntity.ok(
                voteRepository.findAll().stream()
                        .filter( item -> item.getLocalDateTime().isBefore(endTime) && item.getLocalDateTime().isAfter(startTime))
                        .map(item -> Vote.builder().rsEventId(item.getRsEvent().getId()).userId(item.getUser().getId())
                                .localDateTime(item.getLocalDateTime()).voteNum(item.getVoteNum()).build()).collect(Collectors.toList())
        );
    }
}
