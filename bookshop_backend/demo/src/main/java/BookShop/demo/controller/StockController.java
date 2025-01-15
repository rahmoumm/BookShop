package BookShop.demo.controller;


import BookShop.demo.Exceptions.NoContentFoundException;
import BookShop.demo.Exceptions.RessourceNotFoundException;
import BookShop.demo.Exceptions.UserNotAuthorizedToDoThisActionException;
import BookShop.demo.model.*;

import BookShop.demo.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class StockController {

    @Autowired
    private StockService stockService;

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

    @GetMapping("/nonAuth/stocks/ofUser/{userId}")
    public ResponseEntity<List<Stock>> findStockOfUser(@PathVariable int userId)
            throws NoContentFoundException, RessourceNotFoundException {

        List<Stock> usersStock = stockService.findStockOfUser(userId);

        return ResponseEntity.ok(usersStock);
    }

    @GetMapping("/nonAuth/stocks/ofBook/{bookId}")
    public ResponseEntity<List<Stock>> findStockOfBooks(@PathVariable int bookId)
            throws NoContentFoundException, RessourceNotFoundException{
        List<Stock> booksStock = stockService.findStockOfBooks(bookId);

        return ResponseEntity.ok(booksStock);
    }

    @GetMapping("/nonAuth/stocks/ofUser/{userId}/ofBook/{bookId}")
    public ResponseEntity<Stock> findStockByUserAndBook(@PathVariable int userId, @PathVariable int bookId)
            throws NoContentFoundException{
        Stock stock = stockService.findStockByUserAndBook(userId, bookId );

        return ResponseEntity.ok(stock);
    }

    @PutMapping("/seller/stocks/ofUser/{userId}/ofBook/{bookId}")
    public ResponseEntity<Void> restockBook
            (@PathVariable int userId, @PathVariable int bookId,
             @RequestBody Stock newStock, @AuthenticationPrincipal UserDetails userDetails)
            throws RessourceNotFoundException, UserNotAuthorizedToDoThisActionException {
        log.info(newStock.toString());
        stockService.restockBook(userId, bookId, newStock, userDetails);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/seller/stocks")
    public ResponseEntity<Void> createStock
            (@RequestBody StockCreator stockCreator, UriComponentsBuilder ucb,
             @AuthenticationPrincipal UserDetails userDetails)
            throws UserNotAuthorizedToDoThisActionException {
        log.info(stockCreator.toString());

        URI location = stockService.createStock(stockCreator, ucb, userDetails);


        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/seller/stocks/ofUser/{userId}/ofBook/{bookId}")
    public ResponseEntity<Void> deleteStock
            (@PathVariable int userId, @PathVariable int bookId,
             @AuthenticationPrincipal UserDetails userDetails)
            throws UserNotAuthorizedToDoThisActionException, RessourceNotFoundException{

        stockService.deleteStock(userId, bookId, userDetails);
        return ResponseEntity.ok().build();
    }

}
