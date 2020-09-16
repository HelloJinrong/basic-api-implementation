package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.thoughtworks.rslist.domain.UserList.userList;

@RestController
public class RsController {
    private List<RsEvent> rsList = rsEventListInit();
    public List<RsEvent> rsEventListInit() {
        User user = new User("hjr","female",20,"a@b.com","12345678901");
        userList.add(user);
        List<RsEvent> rsList = new ArrayList<>();
        rsList.add(new RsEvent("第一条事件", "无标签",user));
        rsList.add(new RsEvent("第二条事件", "无标签",user));
        rsList.add(new RsEvent("第三条事件", "无标签",user));
        return rsList;

    }

    @GetMapping("/rs/{index}")
    public ResponseEntity get_index_list(@PathVariable  int index) {
        return ResponseEntity.ok(rsList.get(index-1));
    }

    @GetMapping("/rs/list")
    public ResponseEntity get_list_between(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start != null || end != null) {
            return ResponseEntity.ok(rsList.subList(start-1,end));
        }
        return ResponseEntity.ok(rsList);
    }

    @PostMapping("/rs/event")
    public ResponseEntity should_add_rsevent(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
        User user = rsEvent.getUser();
        String userName = user.getName();
        Boolean find = false;
        for (User us : userList) {
            if (us.getName().equals(userName)) {
                find = true;
                break;
            }
        }
        if (!find) {
            userList.add(user);
        }
        rsList.add(rsEvent);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/rs/{index}")
    public void should_delete_rsEvent(@PathVariable int index) {
        //System.out.println("delete"+index);
        rsList.remove(index-1);
    }

    @PostMapping("/rs/{index}")
    public void should_change(@PathVariable int index, @RequestBody String reString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent rsEvent = objectMapper.readValue(reString, RsEvent.class);
        RsEvent replaceRsEvent = rsList.get(index-1);
        if(rsEvent.getEventName()!=null) {
            replaceRsEvent.setEventName(rsEvent.getEventName());
        }

        if(rsEvent.getKeyWord()!=null) {
            replaceRsEvent.setKeyWord(rsEvent.getKeyWord());
        }

    }
}
