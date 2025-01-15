package BookShop.demo.service;


import BookShop.demo.Exceptions.NoContentFoundException;
import BookShop.demo.Exceptions.RessourceNotFoundException;
import BookShop.demo.Exceptions.UserNotAuthorizedToDoThisActionException;
import BookShop.demo.model.*;
import BookShop.demo.repository.BasketRepository;
import BookShop.demo.repository.BookRepository;
import BookShop.demo.repository.StockRepository;
import BookShop.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class BasketService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private BookRepository bookRepository;

    public Basket getUsersOwnBasket(UserDetails userDetails)
    throws NoContentFoundException {

        User actualUser = userRepository.findByEmail(userDetails.getUsername());

        if(basketRepository.existsByPurchaserId(actualUser.getId())){
            Basket basket = basketRepository.findByPurchaserId(actualUser.getId());
            return basket;
        }

        throw new NoContentFoundException("The user does not have any basket");
    }

    public Basket getUserBasket(int userId)
    throws RessourceNotFoundException{

        if(basketRepository.existsByPurchaserId(userId)){
            Basket basket = basketRepository.findByPurchaserId(userId);
            return basket;
        }
        throw new RessourceNotFoundException(String.format("There is no basket allocated to this user with the id %d", userId));
    }

    public void addBook
            (int userId, StockCreator bookStock, UserDetails userDetails)
        throws RessourceNotFoundException, UserNotAuthorizedToDoThisActionException {

        User basketOwner = userRepository.findById(userId);
        User authUser = userRepository.findByEmail(userDetails.getUsername());
        String authRole = userDetails.getAuthorities().toString();

        if(!authRole.contains("ROLE_ADMIN") && authUser.getId() != basketOwner.getId() ){
            throw new UserNotAuthorizedToDoThisActionException("User is not allowed to modify this basket");
        }

        Stock sourceStock = stockRepository.findByUserIdAndBookId(bookStock.getUserId(), bookStock.getBookId());
        Book wantedBook = bookRepository.findById(bookStock.getBookId());

        Basket relatedBasket = basketRepository.findByPurchaserId(authUser.getId());

        if(relatedBasket == null){
            throw new RessourceNotFoundException("Basket not found");
        }
        relatedBasket.addBook(wantedBook);

        relatedBasket.addAmount(sourceStock.getPrice());

        basketRepository.save(relatedBasket);
    }

    public void removeBook
            (int userId, StockCreator bookStock, UserDetails userDetails)
            throws RessourceNotFoundException, UserNotAuthorizedToDoThisActionException  {

        User basketOwner = userRepository.findById(userId);
        User authUser = userRepository.findByEmail(userDetails.getUsername());
        String authRole = userDetails.getAuthorities().toString();

        if(!authRole.contains("ROLE_ADMIN") && authUser.getId() != basketOwner.getId() ){
            throw new UserNotAuthorizedToDoThisActionException("User is not allowed to modify this basket");
        }

        Stock sourceStock = stockRepository.findByUserIdAndBookId(bookStock.getUserId(), bookStock.getBookId());
        Book removedBook = bookRepository.findById(bookStock.getBookId());

        Basket relatedBasket = basketRepository.findByPurchaserId(authUser.getId());
        if(relatedBasket == null){
            throw new RessourceNotFoundException("Basket not found");
        }
        relatedBasket.getWantedBooks().remove(removedBook);
        relatedBasket.deductAmount(sourceStock.getPrice());

        basketRepository.save(relatedBasket);
    }

    public URI createBasket
            (int userId, UserDetails userDetails, UriComponentsBuilder ucb)
            {

        User basketOwner = userRepository.findById(userId);
        User authUser = userRepository.findByEmail(userDetails.getUsername());
        String authRole = userDetails.getAuthorities().toString();

//        if(!authRole.contains("ROLE_ADMIN") && authUser.getId() != basketOwner.getId() ){
//
//            throw new UserNotAuthorizedToDoThisActionException("User is not allowed to modify this basket");
//        }
        log.info("Helloo");
        Basket newBasket = new Basket(basketOwner);

        basketRepository.save(newBasket);

        URI uri = ucb
                .path("/basket/{userId}")
                .buildAndExpand(basketOwner.getId())
                .toUri();
        return uri;
    }

    public ResponseEntity<Void> deleteBasketOfUser(UserDetails userDetails, int userId)
            throws RessourceNotFoundException, UserNotAuthorizedToDoThisActionException {

        User actualUser = userRepository.findByEmail(userDetails.getUsername());
        String authRole = userDetails.getAuthorities().toString();

        if(!authRole.contains("ROLE_ADMIN") && actualUser.getId() != userId){
            throw new UserNotAuthorizedToDoThisActionException("User not allowed to do this action");
        }
        Basket basket = basketRepository.findByPurchaserId(userId);

        if(basket == null){
            throw new RessourceNotFoundException("Basket not found");
        }

        // It is important to do setPurchaser(null), because our basket has
        // a relationship with User, and it will not delete it if it is linked to a user
        basket.setPurchaser(null);
        basketRepository.deleteById(basket.getId());
        return ResponseEntity.ok().build();
    }
}
