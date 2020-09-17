package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEventPo;
import com.thoughtworks.rslist.dto.UserPo;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.*;
import java.util.Optional;

@RestController
public class RsController {

  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;

    public RsController()  {
    }


  @GetMapping("/rs/{rsEventId}")
    public ResponseEntity get_index_list(@PathVariable  int rsEventId) {
      Optional<RsEventPo> rsEventPo = rsEventRepository.findById(rsEventId);
      if (!rsEventPo.isPresent()) {
        throw new RsEventNotValidException("invalid rsEventId");
      }
      return ResponseEntity.ok(rsEventPo.get());
  }

  @GetMapping("/rsEvent")
    public ResponseEntity get_rsEvent( ) {
      return ResponseEntity.ok(rsEventRepository.findAll());
  }


  @GetMapping("/users")
  public ResponseEntity get_all_user() {
    return ResponseEntity.ok(userRepository.findAll());
  }

  @PostMapping("/rs/rsEvent")
    public ResponseEntity should_add_rsEvent(@RequestBody RsEvent rsEvent) throws JsonProcessingException {
    Optional<UserPo> userPo = userRepository.findById(rsEvent.getUserId());
      if (!userPo.isPresent()) {
        return ResponseEntity.badRequest().build();
      }
      RsEventPo rsEventPo = RsEventPo.builder()
              .userPo(userPo.get())
              .keyword(rsEvent.getKeyWord())
              .eventName(rsEvent.getEventName())
              .voteNum(rsEvent.getVoteNum())
              .build();
    rsEventRepository.save(rsEventPo);
      return ResponseEntity.created(null).build();
  }



  @PatchMapping("/rs/{rsEventId}")
  public ResponseEntity should_update_rsEvent(@PathVariable int rsEventId, @RequestBody @Valid RsEvent rsEvent) {
    RsEventPo rsEventPo = rsEventRepository.findById(rsEventId).get();
    if (rsEventPo.getUserPo().getId() == rsEvent.getUserId()) {
      if (rsEvent.getEventName() != null) {
        rsEventPo.setEventName(rsEvent.getEventName());
      }
      if (rsEvent.getKeyWord() != null) {
        rsEventPo.setKeyword(rsEvent.getKeyWord());
      }
      rsEventRepository.save(rsEventPo);
      return ResponseEntity.ok().build();
    }else {
      return ResponseEntity.badRequest().build();
    }

  }

}


