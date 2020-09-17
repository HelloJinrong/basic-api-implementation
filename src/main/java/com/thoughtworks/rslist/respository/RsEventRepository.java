package com.thoughtworks.rslist.respository;

import com.thoughtworks.rslist.dto.RsEventPo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventPo, Integer> {

    @Override
    List<RsEventPo> findAll();


}
