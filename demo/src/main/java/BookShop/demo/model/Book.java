package BookShop.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOOK")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id")
    private Integer id;
    private String name;
    private Double price = -1.0;
    private Double rating = -1.0;

    @OneToMany(mappedBy = "book")
    List<Stock> presentIn = new ArrayList<>();


    protected Book(){}

    public Book(String name, Double price, Double rating){
        this.name = name;
        this.price = price;
        this.rating = rating;
    }


    @Override
    public String toString(){
        return String.format("Book [id = %d, name = %s, rating = %d, price = %d]",
                id, name, price, rating);
    }


//     In the getters that are bellow, note that it is important not to return the primitive variable
//     because if the field of id is null for example, you would get an error of "we are unable to
//     do null.intValue()" and it would cause problems

//    You faced this with the exchange / PUT methode when it tried to serialize the body request
//    to a JSON

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Double getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
