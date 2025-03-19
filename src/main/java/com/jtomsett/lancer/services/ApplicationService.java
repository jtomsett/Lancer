package com.jtomsett.lancer.services;

import com.jtomsett.lancer.entities.EntityNotFoundException;
import com.jtomsett.lancer.entities.Application;
import com.jtomsett.lancer.respositories.ApplicationRepository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository){
        this.applicationRepository = applicationRepository;
    }

    public Application addApplication(@NonNull Application application){
        return applicationRepository.save(application);
    }

    public Application findApplicationById(@NonNull Long id) throws NoSuchElementException {
        return applicationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public void deleteApplication(@NonNull Long id){
        applicationRepository.deleteById(id);
    }
}
