package BookShop.demo.controller;


import BookShop.demo.Exceptions.NoContentFoundException;
import BookShop.demo.Exceptions.RessourceNotFoundException;
import BookShop.demo.model.Book;
import BookShop.demo.model.ErrorReport;
import BookShop.demo.repository.BookRepository;
import BookShop.demo.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class BookController {

    private BookController(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
    private final BookRepository bookRepository;

    @Autowired
    private BookService bookService;

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

    @GetMapping("/nonAuth/books/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable int bookId) throws RessourceNotFoundException {
            Book book = bookService.getBookById(bookId);
            return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/nonAuth/books")
    public ResponseEntity<List<Book>> findAllBooks(){
        List<Book> allBooks = bookService.findAllBooks();
        return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }

    @GetMapping("/nonAuth/books/sorted")
    public ResponseEntity<List<Book>> getSortedBooksByRating(){
        List<Book> sortedBooks = bookService.sortedByRating();
        return new ResponseEntity<>(sortedBooks, HttpStatus.OK);
    }

    @PutMapping("/books/{bookId}")
    public ResponseEntity<Void> updateBook
            (@PathVariable("bookId") int bookId, @RequestBody Book newBook)
            throws RessourceNotFoundException{

        bookService.updateBook(bookId, newBook);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/seller/books")
    public ResponseEntity<URI> createBook(@RequestBody Book newBook, UriComponentsBuilder ucb){
        URI location = bookService.createBook(newBook, ucb);
        log.info(location.toString());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/admin/books/{bookId}")
    public void deleteBook(@PathVariable("bookId") int bookId)
            throws RessourceNotFoundException{

        bookService.deleteBook(bookId);
    }

}
