package BookShop.demo.model;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name= "BASKET")
public class Basket {

    @Id
    @Column(name ="basket_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User purchaser;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "books_in_basket",
            joinColumns = @JoinColumn(name="basket_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> wantedBooks = new LinkedList<>();

    @Column(name = "total_amount")
    private Double totalAmount;

    public Basket(User purchaser) {
        this.purchaser = purchaser;
        this.id = purchaser.getId();
        this.totalAmount = 0d;
    }

    public Basket(){}



    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getId() {
        return this.id;
    }

    public List<Book> getWantedBooks() {
        return this.wantedBooks;
    }

    public void addAmount(Double amountToAdd){
        this.totalAmount += amountToAdd;
    }

    public void deductAmount(Double amountToAdd){
        this.totalAmount -= amountToAdd;
    }

    public void setPurchaser(User purchaser) {
        this.purchaser = purchaser;
    }

    public void addBook(Book book){
        this.wantedBooks.add(book);
    }

    @Override
    public String toString() {
        return "Basket{" +
                "id=" + id +
                ", purchaser=" + purchaser +
                ", wantedBooks=" + wantedBooks +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
