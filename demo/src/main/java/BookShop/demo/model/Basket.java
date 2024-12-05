package BookShop.demo.model;


import jakarta.persistence.*;

import java.util.HashSet;
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

    @ManyToMany
    @JoinTable(
            name = "books_in_basket",
            joinColumns = @JoinColumn(name="basket_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> wantedBooks = new HashSet<>();

    public Basket(User purchaser) {
        this.purchaser = purchaser;
        this.totalAmount = 0d;
    }



    @Column(name = "total_amount")
    private Double totalAmount;

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getId() {
        return this.id;
    }

    public Set<Book> getWantedBooks() {
        return this.wantedBooks;
    }

    public void addAmount(Double amountToAdd){
        this.totalAmount += amountToAdd;
    }

    public void deductAmount(Double amountToAdd){
        this.totalAmount -= amountToAdd;
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
