package BookShop.demo.model;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Entity
@Table(name ="STOCK")
public class Stock {

    @EmbeddedId
    private StockKey stock_id;

    @ManyToOne
    @MapsId("user")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("book")
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "available_quantity")
    private Integer availableQuantity;

    private Stock(){}

    public Stock(int userId, int bookId, int availableQuantity){
        this.availableQuantity = availableQuantity;
        log.info("création stock avant stockKey");
        this.stock_id = new StockKey(userId, bookId);
        log.info("création stock apres stockKey");

    }

    public Stock(User user, Book book, int availableQuantity){
        log.info("ICIII");
        this.book = book;
        log.info("ICIII 2");
        this.user = user;
        log.info("ICIII 3");
        this.availableQuantity = availableQuantity;
        this.stock_id = new StockKey(user.getId(), book.getId());
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public User getUser(){
        return this.user;
    }

    public Book getBook(){
        return this.book;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}