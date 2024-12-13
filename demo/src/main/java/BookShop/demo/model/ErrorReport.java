package BookShop.demo.model;


import lombok.Data;

@Data
public class ErrorReport {

    int statusCode;
    String message;

    public ErrorReport(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }
}
