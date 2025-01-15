package BookShop.demo.service;

import BookShop.demo.Exceptions.NoContentFoundException;
import BookShop.demo.Exceptions.RessourceNotFoundException;
import BookShop.demo.model.Book;
import BookShop.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book getBookById(int bookId) throws RessourceNotFoundException {
        Book book = bookRepository.findById(bookId);
        if(book == null){
            throw new RessourceNotFoundException(String.format("Book with the id %d is not found", bookId));
        }
        return book;
    }

    public List<Book> findAllBooks() {

        List<Book> allBooks = bookRepository.findAll();
        return allBooks;
    }

    public List<Book> sortedByRating(){
        List<Book> sortedBooks = bookRepository.findByOrderByRatingDesc();
        return sortedBooks;
    }

    public void updateBook(int bookId, Book newBook) throws RessourceNotFoundException{

        Book book = bookRepository.findById(bookId);
        if(book == null){
            throw new RessourceNotFoundException("The book you want to modify does not exist");
        }else{
            if(newBook.getRating() != -1){
                book.setRating(newBook.getRating());
            }
            if(newBook.getName() != null){
                book.setName(newBook.getName());
            }
            bookRepository.save(book);
        }
    }

    public URI createBook(Book newBook, UriComponentsBuilder ucb) {

        bookRepository.save(newBook);
        URI location = ucb
                .path("/books/{id}")
                .buildAndExpand(newBook.getId())
                .toUri();
        return location;
    }

    public void deleteBook(int bookId) throws RessourceNotFoundException{
        Book book = bookRepository.findById(bookId);
        if(book == null){
            throw new RessourceNotFoundException("The book you want to Delete does not exist");
        }else{
            bookRepository.deleteById(bookId);
        }
    }

}
