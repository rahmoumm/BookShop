package BookShop.demo.model;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

@Slf4j
@Embeddable
public class StockKey implements Serializable {

    @Column(name = "user_id")
    Integer user;

    @Column(name = "book_id")
    Integer book;

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getBook() {
        return book;
    }

    public void setBook(Integer book) {
        this.book = book;
    }

    private StockKey(){}

    public StockKey(Integer user_id_key, Integer book_id_key) {
        log.info("ICIII DANS LA CLASSE STOCKKEY");
        this.user = user_id_key;
        this.book = book_id_key;
    }

    @Override
    public boolean equals(Object obj){
        log.info("ICIII DANS EQUALS");

        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        StockKey o = (StockKey ) obj;
        if(this.user == o.getUser() && this.book == o.getBook()){return true;}

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.user, this.book);
    }

}
