package com.thoughtworks.rslist;

import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import com.thoughtworks.rslist.service.rsService;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class AppConfig {
    RsEventRepository rsEventRepository;
    UserRepository userRepository;
    VoteRepository voteRepository;

    @Bean
    public rsService userRsService() {
        return new rsService( rsEventRepository,  userRepository,  voteRepository);
    }
}
