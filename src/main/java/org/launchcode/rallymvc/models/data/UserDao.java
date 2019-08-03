package org.launchcode.rallymvc.models.data;

import org.launchcode.rallymvc.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

//data access object/interface
@Repository
@Transactional
public interface UserDao extends CrudRepository<User, Integer> {
    public User findByEmail(String email);
}