package com.in28minutes.rest.webservices.restfulwebservices.user;

import com.in28minutes.rest.webservices.restfulwebservices.jpa.PostRepository;
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


    private UserRepository userRepository;
    private PostRepository postRepository;

    public UserJpaResource(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id)
    {
        User userFound=this.userRepository.findById(id).orElse(null);

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
        User savedUser=this.userRepository.save(user);
        URI location= ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id){
        this.userRepository.deleteById(id);
    }

    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id){
        User userFound=this.userRepository.findById(id).orElse(null);

        if (userFound==null){
            throw new UserNotFoundException("id:"+id);
        }

        return userFound.getPosts();

    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPostForUser(@PathVariable int id,@Valid @RequestBody Post post){
        User userFound=this.userRepository.findById(id).orElse(null);

        if (userFound==null){
            throw new UserNotFoundException("id:"+id);
        }

        post.setUser(userFound);
        //userFound.getPosts().add(post);

        Post savedPost=postRepository.save(post);

        URI location= ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }


    @GetMapping("/jpa/users/{id}/posts/{post_id}")
    public Post retrievePostsForUserById(@PathVariable int id,@PathVariable int post_id){
        User userFound=this.userRepository.findById(id).orElse(null);

        if (userFound==null){
            throw new UserNotFoundException("user_id:"+id);
        }

        Post postFound=this.postRepository.findById(post_id).orElse(null);
        if (postFound==null){
            throw new PostNotFoundException("post_id:"+post_id);
        }


        return postFound;

    }

}
