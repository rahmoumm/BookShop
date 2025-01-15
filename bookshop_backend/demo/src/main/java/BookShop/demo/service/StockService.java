package BookShop.demo.service;

import BookShop.demo.Exceptions.NoContentFoundException;
import BookShop.demo.Exceptions.RessourceNotFoundException;
import BookShop.demo.Exceptions.UserNotAuthorizedToDoThisActionException;
import BookShop.demo.model.Book;
import BookShop.demo.model.Stock;
import BookShop.demo.model.StockCreator;
import BookShop.demo.model.User;
import BookShop.demo.repository.BookRepository;
import BookShop.demo.repository.StockRepository;
import BookShop.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Stock> findStockOfUser( int userId)
            throws NoContentFoundException, RessourceNotFoundException {

        List<Stock> usersStock = stockRepository.findByUserId(userId);

        if(usersStock == null){
            throw new RessourceNotFoundException(String.format("The user with the id %d have no books in stock", userId) );
        }

        if(usersStock.size() == 0){
            throw new NoContentFoundException("The user with the id %d have an empty stock");
        }
        return usersStock;
    }

    public List<Stock> findStockOfBooks( int bookId)
            throws NoContentFoundException, RessourceNotFoundException{
        List<Stock> booksStock = stockRepository.findByBookId(bookId);

        if(booksStock == null){
            throw new RessourceNotFoundException(String.format("The book with the id %d does not exists", bookId) );
        }

        if(booksStock.size() == 0){
            throw new NoContentFoundException("The book with the id %d exists in no stock");
        }
        return booksStock;
    }

    public Stock findStockByUserAndBook(int userId, int bookId)
        throws NoContentFoundException{
        Stock stock = stockRepository.findByUserIdAndBookId(userId, bookId);

        if(stock == null){
            throw new NoContentFoundException("There is no stock with the equivalent properties");
        }
        return stock;
    }

    public void restockBook
            (int userId, int bookId,
              Stock newStock, UserDetails userDetails)
        throws RessourceNotFoundException, UserNotAuthorizedToDoThisActionException {

        Stock stock = stockRepository.findByUserIdAndBookId(userId, bookId);
        User userAuth = userRepository.findByEmail(userDetails.getUsername());

        String role = userDetails.getAuthorities().toString();

        if(userAuth.getId() != userId &&  !role.contains("ROLE_ADMIN")){
            throw new UserNotAuthorizedToDoThisActionException("You are not allowed to modify this stock data");
        }
        if(stock == null){
            throw new RessourceNotFoundException("The stock you want to modify does not exist");
        }

        if(newStock.getPrice() != -1d){
            stock.setPrice(newStock.getPrice());
        }
        if(newStock.getAvailableQuantity() != -1){
            stock.setAvailableQuantity(stock.getAvailableQuantity() + newStock.getAvailableQuantity());
        }
        stockRepository.save(stock);
    }

    public URI createStock
            (StockCreator stockCreator, UriComponentsBuilder ucb,
             UserDetails userDetails)
            throws UserNotAuthorizedToDoThisActionException {

        Book book = bookRepository.findById(stockCreator.getBookId());
        User user = userRepository.findById(stockCreator.getUserId());

        String role = userDetails.getAuthorities().toString();

        if(userDetails.getUsername() != user.getEmail() && !role.contains("ROLE_ADMIN")){
            throw new UserNotAuthorizedToDoThisActionException("User is not allowed to create this new stock");
        }

        Stock stock = new Stock(user, book, stockCreator.getAvailableQuantity(), stockCreator.getPrice());

        stock = stockRepository.save(stock);

        Map<String, Integer> map = new HashMap<>();

        map.put("bookId", stock.getBook().getId());
        map.put("userId", stock.getUser().getId());
        URI uri = ucb
                .path("/stocks/ofUser/{userId}/ofBook{bookId}")
                .buildAndExpand(map)
                .toUri();

        return uri;
    }

    public void deleteStock
            (int userId, int bookId, UserDetails userDetails)
            throws UserNotAuthorizedToDoThisActionException, RessourceNotFoundException{
        Stock stock = stockRepository.findByUserIdAndBookId(userId, bookId);
        User user = userRepository.findById(userId);

        String email = userDetails.getUsername();
        String role = userDetails.getAuthorities().toString();

        if(!role.contains("ROLE_ADMIN") && !email.equals(user.getEmail())){
            throw new UserNotAuthorizedToDoThisActionException("User is not allowed to create this new stock");
        }

        if(stock == null){
            throw new RessourceNotFoundException("The stock you want to delete does not exist");
        }
        stockRepository.delete(stock);
    }

}
