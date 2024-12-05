package BookShop.demo.controller;


import BookShop.demo.model.Book;
import BookShop.demo.model.Stock;
import BookShop.demo.model.StockCreator;
import BookShop.demo.model.User;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/nonAuth/stocks/ofUser/{userId}")
    public ResponseEntity<List<Stock>> findStockOfUser(@PathVariable int userId){

        List<Stock> usersStock = stockRepository.findByUserId(userId);

        if(usersStock.size() == 0){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usersStock);
    }

    @GetMapping("/nonAuth/stocks/ofBook/{bookId}")
    public ResponseEntity<List<Stock>> findStockOfBooks(@PathVariable int bookId){
        List<Stock> booksStock = stockRepository.findByBookId(bookId);

        if(booksStock.size() == 0){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(booksStock);
    }

    @GetMapping("/nonAuth/stocks/ofUser/{userId}/ofBook/{bookId}")
    public ResponseEntity<Stock> findStockByUserAndBook(@PathVariable int userId, @PathVariable int bookId){
        Stock stock = stockRepository.findByUserIdAndBookId(userId, bookId );

        if(stock == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(stock);
    }

    @PutMapping("/seller/stocks/ofUser/{userId}/ofBook/{bookId}")
    public ResponseEntity<Void> restockBook
            (@PathVariable int userId, @PathVariable int bookId,
             @RequestBody Stock newStock, @AuthenticationPrincipal UserDetails userDetails){
        Stock stock = stockRepository.findByUserIdAndBookId(userId, bookId);
        User userAuth = userRepository.findByEmail(userDetails.getUsername());

        String role = userDetails.getAuthorities().toString();

        if(userAuth.getId() != userId &&  !role.contains("ROLE_ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(stock == null){
            return ResponseEntity.notFound().build();
        }

        if(newStock.getPrice() != -1d){
            stock.setPrice(newStock.getPrice());
        }
        if(newStock.getAvailableQuantity() != -1){
            stock.setAvailableQuantity(stock.getAvailableQuantity() + newStock.getAvailableQuantity());
        }
        stockRepository.save(stock);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/seller/stocks")
    public ResponseEntity<Void> createStock
            (@RequestBody StockCreator stockCreator, UriComponentsBuilder ucb,
             @AuthenticationPrincipal UserDetails userDetails){

        Book book = bookRepository.findById(stockCreator.getBook_id());
        User user = userRepository.findById(stockCreator.getUser_id());

        String role = userDetails.getAuthorities().toString();

        if(userDetails.getUsername() != user.getEmail() && !role.contains("ROLE_ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Stock stock = new Stock(user, book, stockCreator.getAvailabe_quantity(), stockCreator.getPrice());

        stock = stockRepository.save(stock);

        Map<String, Integer> map = new HashMap<>();

        map.put("bookId", stock.getBook().getId());
        map.put("userId", stock.getUser().getId());
        URI uri = ucb
                .path("/stocks/ofUser/{userId}/ofBook{bookId}")
                .buildAndExpand(map)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/seller/stocks/ofUser/{userId}/ofBook/{bookId}")
    public ResponseEntity<Void> deleteStock
            (@PathVariable int userId, @PathVariable int bookId,
             @AuthenticationPrincipal UserDetails userDetails){
        Stock stock = stockRepository.findByUserIdAndBookId(userId, bookId);
        User user = userRepository.findById(userId);

        String email = userDetails.getUsername();
        String role = userDetails.getAuthorities().toString();

        if(!role.contains("ROLE_ADMIN") && !email.equals(user.getEmail())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(stock == null){
            return ResponseEntity.notFound().build();
        }
        stockRepository.delete(stock);
        return ResponseEntity.ok().build();
    }

}
