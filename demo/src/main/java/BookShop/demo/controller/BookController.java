package BookShop.demo.controller;


import BookShop.demo.model.Book;
import BookShop.demo.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookController(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
    private final BookRepository bookRepository;

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getUserById(@PathVariable int bookId){
        Book book = bookRepository.findById(bookId);
        if(book != null){
            return ResponseEntity.ok(book);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks(){
        List<Book> allBooks = bookRepository.findAll();
        if(allBooks != null){
            return ResponseEntity.ok(allBooks);
        }else{
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/reserved/{bookId}")
    public ResponseEntity<Void> updateBook(@PathVariable("bookId") int bookId, @RequestBody Book newBook){
        Book book = bookRepository.findById(bookId);
        if(book == null){
            return ResponseEntity.notFound().build();
        }else{
            if(newBook.getPrice() != -1){
                book.setPrice(newBook.getPrice());
            }
            if(newBook.getRating() != -1){
                book.setRating(newBook.getRating());
            }
            if(newBook.getName() != null){
                book.setName(newBook.getName());
            }
            bookRepository.save(book);
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/reserved")
    public ResponseEntity<Void> createBook(@RequestBody Book newBook, UriComponentsBuilder ucb){



        Book book = bookRepository.save(newBook);
        URI location = ucb
                .path("/books/{id}")
                .buildAndExpand(newBook.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/reserved/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable("bookId") int bookId){
        Book book = bookRepository.findById(bookId);
        if(book == null){
            return ResponseEntity.notFound().build();
        }else{
            bookRepository.deleteById(bookId);
            return ResponseEntity.ok().build();
        }
    }

}
