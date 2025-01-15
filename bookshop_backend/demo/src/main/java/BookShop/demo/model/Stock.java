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
    private Integer availableQuantity = -1;
    
    @Column(name = "price")
    private Double price = -1d;

    private Stock(){}

    public Stock(int userId, int bookId, int availableQuantity, double price){
        this.availableQuantity = availableQuantity;
        this.price = price;
        log.info("création stock avant stockKey");
        this.stock_id = new StockKey(userId, bookId);
        log.info("création stock apres stockKey");

    }

    public Stock(User user, Book book, int availableQuantity, double price){
        log.info("ICIII");
        this.book = book;
        log.info("ICIII 2");
        this.user = user;
        log.info("ICIII 3");
        this.availableQuantity = availableQuantity;
        this.price = price;
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
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stock_id=" + stock_id +
                ", user=" + user +
                ", book=" + book +
                ", availableQuantity=" + availableQuantity +
                ", price=" + price +
                '}';
    }
}
