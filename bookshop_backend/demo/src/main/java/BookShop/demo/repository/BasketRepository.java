package BookShop.demo.repository;

import BookShop.demo.model.Basket;
import org.springframework.data.repository.CrudRepository;

public interface BasketRepository extends CrudRepository<Basket, Integer> {

    Basket findByPurchaserId(Integer userId);
    boolean existsByPurchaserId(Integer userId);
    Void deleteById(int id);

}
