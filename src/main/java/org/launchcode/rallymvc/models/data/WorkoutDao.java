package org.launchcode.rallymvc.models.data;

import org.launchcode.rallymvc.models.Workout;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface WorkoutDao extends CrudRepository<Workout, Integer> {
    public Iterable<Workout> findByUserId(Integer user_id);
}