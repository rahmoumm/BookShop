package BookShop.demo.controller;


import BookShop.demo.Exceptions.NoContentFoundException;
import BookShop.demo.Exceptions.RessourceNotFoundException;
import BookShop.demo.Exceptions.UserNotAuthorizedToDoThisActionException;
import BookShop.demo.model.*;

import BookShop.demo.service.BasketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping
public class BasketController {

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

    @Autowired
    private BasketService basketService;

    // A user should only be allowed to acces its basket
    @GetMapping("/basket/personal")
    public ResponseEntity<Basket> getUsersOwnBasket(@AuthenticationPrincipal UserDetails userDetails)
    throws NoContentFoundException {

        Basket basket = basketService.getUsersOwnBasket(userDetails);

        return ResponseEntity.ok(basket);
    }

    @GetMapping("/basket/{userId}")
    public ResponseEntity<Basket> getUserBasket(@PathVariable int userId)
    throws RessourceNotFoundException{

        Basket basket = basketService.getUserBasket(userId);

        return ResponseEntity.ok(basket);
    }

    // We add from a stock
    @PutMapping("/basket/{userId}/addBook")
    public ResponseEntity<Void> addBook
        (@PathVariable int userId, @RequestBody StockCreator bookStock,
         @AuthenticationPrincipal UserDetails userDetails)
            throws RessourceNotFoundException, UserNotAuthorizedToDoThisActionException {

        basketService.addBook(userId, bookStock, userDetails);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/basket/{userId}/removeBook")
    public ResponseEntity<Void> removeBook
            (@PathVariable int userId, @RequestBody StockCreator bookStock,
             @AuthenticationPrincipal UserDetails userDetails)
            throws RessourceNotFoundException, UserNotAuthorizedToDoThisActionException{
        log.info("££££££££££££££££££  :  " + bookStock.toString() );
        log.info("££££££££££££££££££  :  " +  userId);


        basketService.removeBook(userId, bookStock, userDetails);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/basket/{userId}/creation")
    public ResponseEntity<String> createBasket
            (@PathVariable int userId, @AuthenticationPrincipal UserDetails userDetails,
             UriComponentsBuilder ucb)
            {
        log.info("in creation???????????????????????????????????????????????????????????????????????????");
        URI uri = basketService.createBasket(userId, userDetails, ucb);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/basket/{userId}")
    public ResponseEntity<Void> deleteBasketOfUser
            (@AuthenticationPrincipal UserDetails userDetails, @PathVariable int userId)
            throws RessourceNotFoundException, UserNotAuthorizedToDoThisActionException{
        basketService.deleteBasketOfUser(userDetails, userId);
        return ResponseEntity.ok().build();
    }

}
