package BookShop.demo.repository;


import BookShop.demo.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookRepository extends
        CrudRepository<Book, Integer>, PagingAndSortingRepository<Book, Integer> {

    Book findById(int id);
    List<Book> findAll();
    Void deleteById(int id);

    List<Book> findByOrderByRatingDesc();

}
