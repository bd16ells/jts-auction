package com.example.auctionapp.domain.user;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {

    private UserServiceImpl userService;
    private UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@RequestBody Optional<User> user){
        if(user.isPresent()){
            user.get().setPassword(encoder().encode(user.get().getPassword()));
            return new ResponseEntity<User>(userService.save(user.get()), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/users")
    public ResponseEntity deleteUser(@RequestBody Optional<User> user){
        if(user.isPresent()){

            userService.delete(user.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(Principal principal,
                                     @RequestBody Optional<User> incoming){

        Optional<User> current = userRepository.findByUsername(principal.getName());
        if(current.isPresent() && incoming.isPresent()){
            incoming.get().setPassword(encoder().encode(incoming.get().getPassword()));
            return new ResponseEntity<User>(userService.update(incoming.get(), current.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Bean
    PasswordEncoder encoder(){
        return new BCryptPasswordEncoder(12);
    }
}
