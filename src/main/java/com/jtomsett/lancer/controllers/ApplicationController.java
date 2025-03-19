package com.jtomsett.lancer.controllers;

import com.jtomsett.lancer.entities.Application;
import com.jtomsett.lancer.services.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    @PostMapping("/add")
    public Application addApplication(@RequestBody Application application){
        if(application != null){
            if(application.getId() != null){
                application.setId(null);
            }
            return applicationService.addApplication(application);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid application provided.");
    }

    @GetMapping("/{id}")
    public Application getByApplicationById(@PathVariable Long id){
        if(id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid id provided.");
        }
        return applicationService.findApplicationById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteApplicationById(@PathVariable Long id){
        if(id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid id provided.");
        }
        applicationService.deleteApplication(id);
        return "Successfully deleted application with id "+id+".";
    }
}
