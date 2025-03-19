package com.jtomsett.lancer.respositories;

import com.jtomsett.lancer.entities.Application;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<Application,Long> {
}
