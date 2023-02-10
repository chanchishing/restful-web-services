package com.in28minutes.rest.webservices.restfulwebservices.user;

import com.in28minutes.rest.webservices.restfulwebservices.jpa.UserRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJpaResource {


    private UserRepository repository;

    public UserJpaResource(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return repository.findAll();
    }

    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id)
    {
        User userFound=this.repository.findById(id).orElse(null);

        if (userFound==null){
            throw new UserNotFoundException("id:"+id);

        }

        EntityModel<User> entityModelUser=EntityModel.of(userFound);
        WebMvcLinkBuilder retrieveAllUserLink=linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModelUser.add(retrieveAllUserLink.withRel("retrieve all users"));
        return entityModelUser;

    }

    @PostMapping("/jpa/users")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user){
        User savedUser=this.repository.save(user);
        URI location= ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id){
        this.repository.deleteById(id);
    }


}
