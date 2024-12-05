package BookShop.demo.controller;


import BookShop.demo.model.*;
import BookShop.demo.repository.BasketRepository;
import BookShop.demo.repository.BookRepository;
import BookShop.demo.repository.StockRepository;
import BookShop.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping
public class BasketController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private BookRepository bookRepository;

    // A user should only be allowed to acces its basket
    @GetMapping("/basket/personal")
    public ResponseEntity<Basket> getUsersOwnBasket(@AuthenticationPrincipal UserDetails userDetails){

        User actualUser = userRepository.findByEmail(userDetails.getUsername());

        if(basketRepository.existsById(actualUser.getId())){
           Basket basket = basketRepository.findByPurchaserId(actualUser.getId());
           return ResponseEntity.ok(basket);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/basket/{userId}")
    public ResponseEntity<Basket> getUserBasket(@PathVariable int userId){

        if(basketRepository.existsByPurchaserId(userId)){
            return ResponseEntity.notFound().build();
        }
        Basket basket = basketRepository.findByPurchaserId(userId);
        return ResponseEntity.ok(basket);
    }

    // We add from a stock
    @PutMapping("/basket/{userId}/addBook")
    public ResponseEntity<Void> modifyBasket
        (@PathVariable int userId, @RequestBody StockCreator bookStock,
         @AuthenticationPrincipal UserDetails userDetails){

        User basketOwner = userRepository.findById(userId);
        User authUser = userRepository.findByEmail(userDetails.getUsername());
        String authRole = userDetails.getAuthorities().toString();

        if(!authRole.contains("ROLE_ADMIN") && authUser.getId() != basketOwner.getId() ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Stock sourceStock = stockRepository.findByUserIdAndBookId(bookStock.getUser_id(), bookStock.getBook_id());
        Book wantedBook = bookRepository.findById(bookStock.getBook_id());

        Basket relatedBasket = basketRepository.findByPurchaserId(authUser.getId());
        relatedBasket.getWantedBooks().add(wantedBook);
        relatedBasket.addAmount(sourceStock.getPrice());

        basketRepository.save(relatedBasket);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/basket/{userId}/removeBook")
    public ResponseEntity<Void> removeBook
            (@PathVariable int userId, @RequestBody StockCreator bookStock,
             @AuthenticationPrincipal UserDetails userDetails){

        User basketOwner = userRepository.findById(userId);
        User authUser = userRepository.findByEmail(userDetails.getUsername());
        String authRole = userDetails.getAuthorities().toString();

        if(!authRole.contains("ROLE_ADMIN") && authUser.getId() != basketOwner.getId() ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Stock sourceStock = stockRepository.findByUserIdAndBookId(bookStock.getUser_id(), bookStock.getBook_id());
        Book removedBook = bookRepository.findById(bookStock.getBook_id());

        Basket relatedBasket = basketRepository.findByPurchaserId(authUser.getId());
        relatedBasket.getWantedBooks().remove(removedBook);
        relatedBasket.deductAmount(sourceStock.getPrice());

        basketRepository.save(relatedBasket);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/basket/{userId}/creation")
    public ResponseEntity<Void> createBasket
            (@PathVariable int userId, @AuthenticationPrincipal UserDetails userDetails,
             UriComponentsBuilder ucb){

        User basketOwner = userRepository.findById(userId);
        User authUser = userRepository.findByEmail(userDetails.getUsername());
        String authRole = userDetails.getAuthorities().toString();

        if(!authRole.contains("ROLE_ADMIN") && authUser.getId() != basketOwner.getId() ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Basket newBasket = new Basket(authUser);

        basketRepository.save(newBasket);

        URI uri = ucb
                .path("/basket/{userId}")
                .buildAndExpand(authUser.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/basket/{userId}")
    public ResponseEntity<Void> deleteBasketOfUser
            (@AuthenticationPrincipal UserDetails userDetails, @PathVariable int userId){

        User actualUser = userRepository.findByEmail(userDetails.getUsername());
        String authRole = userDetails.getAuthorities().toString();
        if(!authRole.contains("ROLE_ADMIN") && actualUser.getId() != userId){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        basketRepository.delete(basketRepository.findByPurchaserId(userId));
        return ResponseEntity.ok().build();
    }

}
