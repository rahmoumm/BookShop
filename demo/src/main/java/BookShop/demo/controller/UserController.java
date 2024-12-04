package BookShop.demo.controller;


import BookShop.demo.model.User;
import BookShop.demo.repository.UserRepository;
import BookShop.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class UserController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    UserService userService;

    private UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/nonAuth/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int userId){
        User user = userRepository.findById(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/nonAuth/users")
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> allUsers = userRepository.findAll();
        if(allUsers != null){
            return ResponseEntity.ok(allUsers);
        }else{
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Void> modifyUserInfos
            (@PathVariable("id") int userId, @RequestBody User newUser, @AuthenticationPrincipal UserDetails userDetails){
        User user = userRepository.findById(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();

        String email = userDetails.getUsername();
        String role = userDetails.getAuthorities().toString();

        if(!role.contains("ROLE_ADMIN") && !email.equals(user.getEmail()) ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(newUser.getEmail() != null){
            user.setEmail(newUser.getEmail());
        }
        if(newUser.getFirstName() != null){
            user.setFirstName(newUser.getFirstName());
        }
        if(newUser.getLastName() != null){
            user.setLastName(newUser.getLastName());
        }
        if(newUser.getPassword() != null){
            user.setPassword(  new BCryptPasswordEncoder(10).encode(newUser.getPassword()) );
        }
        if(newUser.getImage() != null){
            user.setImage(newUser.getImage());
        }

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/image")
    public ResponseEntity<Void> modifyProfileImage
            (@PathVariable("id") int userId, @RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails)
            throws IOException {
        User user = userRepository.findById(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }

        String email = userDetails.getUsername();
        String role = userDetails.getAuthorities().toString();

        if(!role.contains("ROLE_ADMIN") && !email.equals(user.getEmail())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        user.setImage(file.getBytes());


        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/register")
    public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucb){

        if(userRepository.existsByEmail(user.getEmail())){
            return ResponseEntity.badRequest().build();
        }

        userService.register(user);
        URI location = ucb
                .path("/users/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> deletUserById(@PathVariable("id") int userId){
        User user = userRepository.findById(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(userId);
        return ResponseEntity.ok().build();
    }

}
