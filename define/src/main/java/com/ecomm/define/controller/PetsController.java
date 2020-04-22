package com.ecomm.define.controller;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */


import com.ecomm.define.domain.Pets;
import com.ecomm.define.repository.PetsRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PetsController.class);

    @Autowired
    private PetsRepository repository;

    @GetMapping(value = "/")
    public List<Pets> getAllPets() {
        LOGGER.info("in getMapping findAll()");
        return repository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Pets getPetById(@PathVariable("id") ObjectId id) {
        LOGGER.info("findById {}",id.toString());
        return repository.findBy_id(id);
    }

    @PutMapping(value= "/{id}")
    public void modifyPetById(@PathVariable("id") ObjectId id, @Valid @RequestBody Pets pets) {
        pets.set_id(id);
        repository.save(pets);
    }

    @PostMapping(value = "/")
    public Pets createPet(@Valid @RequestBody Pets pets) {
        pets.set_id(ObjectId.get());
        repository.save(pets);
        return pets;
    }

    @DeleteMapping(value = "/{id}")
    public void deletePet(@PathVariable ObjectId id) {
        repository.delete(repository.findBy_id(id));
    }
}