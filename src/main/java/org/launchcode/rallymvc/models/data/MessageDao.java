package org.launchcode.rallymvc.models.data;

import org.launchcode.rallymvc.models.Message;
import org.launchcode.rallymvc.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface MessageDao extends CrudRepository<Message, Integer> {
    public Iterable<Message> findByUserId(Integer user_id);
}