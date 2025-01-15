package BookShop.demo.model;

public class StockCreator {

    private int userId;
    private int bookId;
    private int availableQuantity;

    private double price;

    public StockCreator(int user_id, int book_id, int availableQuantity, double price) {
        this.userId = user_id;
        this.bookId = book_id;
        this.availableQuantity = availableQuantity;
        this.price = price;
    }

    public StockCreator(int user_id, int book_id) {
        this.userId = user_id;
        this.bookId = book_id;
    }

    public StockCreator(){}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StockCreator{" +
                "user_id=" + userId +
                ", book_id=" + bookId +
                ", availabeQuantity=" + availableQuantity +
                ", price=" + price +
                '}';
    }
}
