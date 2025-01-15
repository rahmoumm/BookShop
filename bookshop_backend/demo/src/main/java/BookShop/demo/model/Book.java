package BookShop.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "BOOK")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id")
    private Integer id;
    private String name;
    private Double rating = 0d;

    // MappedBy est utilisé quand on veut que le relation soit bidirectionnel
    @OneToMany(mappedBy = "book")
    List<Stock> presentIn = new ArrayList<>();

//    @ManyToMany(mappedBy = "wantedBooks")
//    Set<Basket> baskets = new HashSet<>();

    protected Book(){}

    public Book(String name, Double rating){
        this.name = name;
        this.rating = rating;
    }


    @Override
    public String toString(){
        return String.format("Book [id = %d, name = %s, rating = %f]",
                id, name, rating);
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

    public Double getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
