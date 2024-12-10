package BookShop.demo.controller;


import BookShop.demo.model.*;
import BookShop.demo.repository.BasketRepository;
import BookShop.demo.repository.BookRepository;
import BookShop.demo.repository.StockRepository;
import BookShop.demo.repository.UserRepository;
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

        if(basketRepository.existsByPurchaserId(actualUser.getId())){
           Basket basket = basketRepository.findByPurchaserId(actualUser.getId());
           return ResponseEntity.ok(basket);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/basket/{userId}")
    public ResponseEntity<Basket> getUserBasket(@PathVariable int userId){

        if(basketRepository.existsByPurchaserId(userId)){
            Basket basket = basketRepository.findByPurchaserId(userId);
            return ResponseEntity.ok(basket);
        }
        return ResponseEntity.notFound().build();

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
        relatedBasket.addBook(wantedBook);

        relatedBasket.addAmount(sourceStock.getPrice());

        basketRepository.save(relatedBasket);
        log.info(basketRepository.findByPurchaserId(userId).toString());
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
    public ResponseEntity<String> createBasket
            (@PathVariable int userId, @AuthenticationPrincipal UserDetails userDetails,
             UriComponentsBuilder ucb){

        User basketOwner = userRepository.findById(userId);
        User authUser = userRepository.findByEmail(userDetails.getUsername());
        String authRole = userDetails.getAuthorities().toString();

        log.info("Avant l'auht");
        if(!authRole.contains("ROLE_ADMIN") && authUser.getId() != basketOwner.getId() ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("Apres l'auht");

        Basket newBasket = new Basket(basketOwner);
        log.info(newBasket.toString());
        log.info("Apres la création de basket");

        basketRepository.save(newBasket);
        log.info("Apres la sauvegarde de basket");

        URI uri = ucb
                .path("/basket/{userId}")
                .buildAndExpand(basketOwner.getId())
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
        Basket basket = basketRepository.findByPurchaserId(userId);
        log.info(basket.toString());
        // It is important to do setPurchaser(null), because our basket has
        // a relationship with User, and it will not delete it if it is linked to a user
        basket.setPurchaser(null);
        basketRepository.deleteById(basket.getId());
        return ResponseEntity.ok().build();
    }

}
