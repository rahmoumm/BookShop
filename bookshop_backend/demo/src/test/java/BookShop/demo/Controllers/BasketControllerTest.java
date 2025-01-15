package BookShop.demo.Controllers;

import BookShop.demo.model.Basket;
import BookShop.demo.model.Book;
import BookShop.demo.model.Stock;
import BookShop.demo.model.StockCreator;
import BookShop.demo.repository.BasketRepository;
import BookShop.demo.repository.UserRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasketControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnAuthUserBasketWhenItExists(){
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/basket/personal", String.class);

        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.OK);

        DocumentContext jsonVals = JsonPath.parse(reponse.getBody());

        Number basketId = jsonVals.read("$.id");
        // in the DB 11 is the id of the account used for auth
        Assertions.assertEquals(basketId, 11);
    }

    @Test
    void shouldReturnWantedUserBasketWhenItExists(){
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/basket/12", String.class);

        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.OK);

        DocumentContext jsonVals = JsonPath.parse(reponse.getBody());

        Number basketId = jsonVals.read("$.id");
        // in the DB 11 is the id of the account used for auth
        Assertions.assertEquals(basketId, 12);
    }

    @Test
    @DirtiesContext
    void shouldCreateUsersBasket(){

        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .postForEntity("/basket/13/creation", null, Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);

    }

    @Test
    @DirtiesContext
    void shouldAddBookToBasket(){

        HttpEntity<StockCreator> stock = new HttpEntity<>(new StockCreator(11, 13));
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/basket/11/addBook", HttpMethod.PUT, stock, Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        ResponseEntity<Basket> basketResponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/basket/11", Basket.class);

        Basket basket = basketResponse.getBody();
        int numOfBooks = basket.getWantedBooks().size();


        ArrayList<Integer> realIds = new ArrayList<>(
                Arrays.asList(11, 12, 13)
        );
        List<Integer> ids = basket.getWantedBooks().stream()
                .map(Book::getId).collect(Collectors.toList());


        Assertions.assertEquals(3,numOfBooks);
        Assertions.assertIterableEquals(ids, realIds);

    }

    @Test
    @DirtiesContext
    void shouldRemoveBookFromBasket(){

        HttpEntity<StockCreator> stock = new HttpEntity<>(new StockCreator(11, 12));
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/basket/11/removeBook", HttpMethod.PUT, stock, Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        ResponseEntity<Basket> basketResponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/basket/11", Basket.class);

        Basket basket = basketResponse.getBody();
        int numOfBooks = basket.getWantedBooks().size();


        ArrayList<Integer> realIds = new ArrayList<>(
                Arrays.asList(11)
        );
        List<Integer> ids = basket.getWantedBooks().stream()
                .map(Book::getId).collect(Collectors.toList());


        Assertions.assertEquals(1,numOfBooks);
        Assertions.assertIterableEquals(ids, realIds);

    }

    @Test
    @DirtiesContext
    void shouldDeletBasketWhenExists(){

        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/basket/11", HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/basket/11", Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
