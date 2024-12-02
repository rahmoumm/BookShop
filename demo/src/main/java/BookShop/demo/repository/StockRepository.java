package BookShop.demo.repository;

import BookShop.demo.model.Stock;
import BookShop.demo.model.StockKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface StockRepository extends CrudRepository<Stock, StockKey> {
    List<Stock> findByUserId(Integer userId);
    List<Stock> findByBookId(Integer bookId);
    Stock findByUserIdAndBookId(Integer userId, Integer bookId);
}
