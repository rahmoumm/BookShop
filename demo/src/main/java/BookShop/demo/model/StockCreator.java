package BookShop.demo.model;

public class StockCreator {

    private int user_id;
    private int book_id;
    private int availabe_quantity;

    public StockCreator(int user_id, int book_id, int availabe_quantity) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.availabe_quantity = availabe_quantity;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getAvailabe_quantity() {
        return availabe_quantity;
    }

    public void setAvailabe_quantity(int availabe_quantity) {
        this.availabe_quantity = availabe_quantity;
    }
}
