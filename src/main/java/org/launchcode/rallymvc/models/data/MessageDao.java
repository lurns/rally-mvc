package org.launchcode.rallymvc.models.data;

import org.launchcode.rallymvc.models.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface MessageDao extends CrudRepository<Message, Integer> {
}