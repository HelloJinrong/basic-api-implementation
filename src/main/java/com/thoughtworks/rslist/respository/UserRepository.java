package com.thoughtworks.rslist.respository;

import com.thoughtworks.rslist.dto.UserPo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserPo, Integer> {
    @Override
    List<UserPo> findAll();
}
