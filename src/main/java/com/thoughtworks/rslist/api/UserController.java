package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.Po.UserPo;
import com.thoughtworks.rslist.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    List<User> userList = new ArrayList<>();
//    @Autowired
    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user")
    public ResponseEntity user_register(@RequestBody @Valid User user) {
        UserPo userPo = new UserPo();
        userPo.setAge(user.getAge());
        userPo.setEmail(user.getEmail());
        userPo.setGender(user.getGender());
        userPo.setPhone(user.getPhone());
        userPo.setUserName(user.getName());
        userPo.setVoteNum(user.getVoteNum());
        userRepository.save(userPo);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity should_delete_user_and_rsEvent(@PathVariable int id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity should_get_user_by_id(@PathVariable int id) {
        Optional<UserPo> user = userRepository.findById(id);
        return ResponseEntity.ok(user.get());
    }

}
