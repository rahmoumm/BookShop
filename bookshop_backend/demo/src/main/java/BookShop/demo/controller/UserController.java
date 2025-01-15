package BookShop.demo.controller;


import BookShop.demo.Exceptions.EmailAlreadyExistsException;
import BookShop.demo.Exceptions.NoContentFoundException;
import BookShop.demo.Exceptions.RessourceNotFoundException;
import BookShop.demo.Exceptions.UserNotAuthorizedToDoThisActionException;
import BookShop.demo.model.ErrorReport;
import BookShop.demo.model.Role;
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
    private UserService userService;

    @ExceptionHandler(value = RessourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorReport handleRessourceNotFoundException(RessourceNotFoundException ex) {
        return new ErrorReport(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(value = NoContentFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ErrorReport handleNoContentFoundException(NoContentFoundException ex) {
        return new ErrorReport(HttpStatus.NO_CONTENT.value(), ex.getMessage());
    }

    @ExceptionHandler(value = UserNotAuthorizedToDoThisActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorReport handleUserNotAuthorizedToDoThisActionException(UserNotAuthorizedToDoThisActionException ex) {
        return new ErrorReport(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }

    @ExceptionHandler(value = EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorReport handleUserEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return new ErrorReport(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    private UserController(UserRepository userRepository){

    }

    @GetMapping("/nonAuth/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int userId) throws RessourceNotFoundException{
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/nonAuth/users")
    public ResponseEntity<List<User>> findAllUsers() throws NoContentFoundException{
        List<User> allUsers = userService.findAllUsers();
        return ResponseEntity.ok(allUsers);

    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Void> modifyUserInfos
            (@PathVariable("id") int userId, @RequestBody User newUser, @AuthenticationPrincipal UserDetails userDetails)
            throws RessourceNotFoundException, UserNotAuthorizedToDoThisActionException {

        userService.modifyUserInfos(userId, newUser, userDetails);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/image")
    public ResponseEntity<Void> modifyProfileImage
            (@PathVariable("id") int userId, @RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails)
            throws IOException, UserNotAuthorizedToDoThisActionException, RessourceNotFoundException {

        userService.modifyProfileImage(userId, file, userDetails);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/nonAuth/register")
    public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucb)
    throws EmailAlreadyExistsException {

        URI location = userService.register(user, ucb);

        log.info("############################################## Log from the Controller");
        log.info(user.toString());
        log.info("############################################## Log from the Controller");

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/checkLogin")
    public ResponseEntity<User> login(@RequestBody User user, UriComponentsBuilder ucb)
        throws RessourceNotFoundException{

        User userFound = userService.userExists(user, ucb);
        return ResponseEntity.ok(userFound);
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> deletUserById(@PathVariable("id") int userId, @AuthenticationPrincipal UserDetails userDetails)
        throws RessourceNotFoundException, UserNotAuthorizedToDoThisActionException{
        userService.deletUserById(userId, userDetails);
        return ResponseEntity.ok().build();
    }

}
