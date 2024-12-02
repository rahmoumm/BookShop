package BookShop.demo.Controllers;

import BookShop.demo.model.Book;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Arrays;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    //	##### AUTHENTICATION TESTS
    @Test
    void shouldNotExecutRequestIfNotAuthentified(){

        // With no authentification
        ResponseEntity<String> reponse = restTemplate

                .getForEntity("/books", String.class);
        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldNotExecutRequestIfBadCredentials(){

        // With bad credentials
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abaxc")
                .getForEntity("/books", String.class);
        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    //	#### GET TESTS
    @Test
    void shouldReturnAllBooksWhenDataExists(){
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/books", String.class);
        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.OK);

        DocumentContext jsonVals = JsonPath.parse(reponse.getBody());
        int numOfBooks = jsonVals.read("$.length()");
        Assertions.assertEquals(numOfBooks, 3);

        ArrayList<Integer> ids = jsonVals.read("$[*].id");
        ArrayList<Integer> realIds = new ArrayList<>(
                Arrays.asList(11, 12, 13)
        );

        Assertions.assertIterableEquals(ids, realIds);
    }

    @Test
    void shouldReturnABookWhenItExists(){
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/books/11", String.class);

        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.OK);

        DocumentContext jsonVals = JsonPath.parse(reponse.getBody());

        Number bookId = jsonVals.read("$.id");

        Assertions.assertEquals(bookId, 11);
    }

    @Test
    void shouldReturnNotFoundIfDoNotExist(){
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/books/1111", String.class);

        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    // ###### PUT TESTS
    @Test
    @DirtiesContext
    void shouldChangeABookPriceWhenItExists(){
        HttpEntity<Book> newBook = new HttpEntity<>(new Book(null, 187d, null));
        // exchange
        ResponseEntity<Void> responseEntity = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/books/reserved/11", HttpMethod.PUT, newBook, Void.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        ResponseEntity<Book> book = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/books/11", Book.class);
        Assertions.assertEquals(book.getBody().getPrice(), 187L);
    }

    @Test
    void shouldNotModifyIfDoNotExist(){
        HttpEntity<Book> newBook = new HttpEntity<>(new Book(null, 187d, null));

        ResponseEntity<Void> responseEntity = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/books/reserved/1111", HttpMethod.PUT, newBook, Void.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }



    // ####### POST TESTS
    @Test
    @DirtiesContext
    void shouldCreateNewBook(){
        HttpEntity<Book> newBook = new HttpEntity<>(new Book("9l3at sraghna", 1440d, 5.0));

        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .postForEntity("/books/reserved", newBook, Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }


    //	###### DELETE TESTS
    @Test
    @DirtiesContext
    void shouldeDeleteBookWhenExists(){

        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/books/reserved/11", HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/books/11", Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    void shouldeNotDeleteBookIfNotExists(){

        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/books/reserved/1111", HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

    }
}
