package BookShop.demo.Controllers;

import BookShop.demo.model.Stock;
import BookShop.demo.model.StockCreator;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnStocksByUserWhenItExists(){
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/stocks/ofUser/12", String.class);

        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.OK);

        DocumentContext jsonVals = JsonPath.parse(reponse.getBody());
        int numOfStocks = jsonVals.read("$.length()");
        Assertions.assertEquals(numOfStocks, 2);

        ArrayList<Integer> ids = jsonVals.read("$[*].book.id");
        ArrayList<Integer> realIds = new ArrayList<>(
                Arrays.asList(11, 12)
        );
        Assertions.assertIterableEquals(ids, realIds);
    }

    @Test
    void shouldNotReturnStocksByUserWhenItDoesNotExist(){
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/stocks/ofUser/189", String.class);

        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    void shouldReturnStocksByBookWhenItExists(){
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/stocks/ofBook/12", String.class);

        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.OK);

        DocumentContext jsonVals = JsonPath.parse(reponse.getBody());
        int numOfStocks = jsonVals.read("$.length()");
        Assertions.assertEquals(numOfStocks, 3);

        ArrayList<Integer> ids = jsonVals.read("$[*].user.id");
        ArrayList<Integer> realIds = new ArrayList<>(
                Arrays.asList(11, 12, 13)
        );
        Assertions.assertIterableEquals(ids, realIds);
    }

    @Test
    void shouldNotReturnStocksByBookWhenItDoesNotExist(){
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/stocks/ofBook/16", String.class);

        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    void shouldReturnStockByBookAndUserWhenItExists(){
        ResponseEntity<Stock> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/stocks/ofUser/11/ofBook/12", Stock.class);

        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.OK);

        Assertions.assertEquals(reponse.getBody().getAvailableQuantity(), 15);
    }

    @Test
    void shouldNotReturnStockByBookAndUserWhenItDoesNotExist(){
        ResponseEntity<Stock> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/stocks/ofUser/151/ofBook/129", Stock.class);

        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @DirtiesContext
    void shouldUpdateWhenExists(){
        // on ajoute la valeur 1650 à celle précédente, la précédente est 35
        HttpEntity<Stock> newStock = new HttpEntity<>(new Stock(12, 12, 1650, 10.99));

        ResponseEntity<Void> responseEntity = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/seller/stocks/ofUser/12/ofBook/12", HttpMethod.PUT, newStock, Void.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        ResponseEntity<Stock> stock = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/stocks/ofUser/12/ofBook/12", Stock.class);
        Assertions.assertEquals(stock.getBody().getAvailableQuantity(), 1685);
    }

    @Test
    @DirtiesContext
    void shouldAddNewStock(){
        // le stock n'existe pas dans la DB
        HttpEntity<StockCreator> newStock = new HttpEntity<>(new StockCreator(13, 11, 50 ,15.99));

        ResponseEntity<Void> stock = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .postForEntity("/seller/stocks", newStock, Void.class);

        Assertions.assertEquals(stock.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    @DirtiesContext
    void shouldeDeleteStockWhenExists(){
        // Faire attention parceque tu deletais le user avec lequel tu travaillais
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/seller/stocks/ofUser/13/ofBook/12", HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/stocks/ofUser/13/ofBook/12", Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
    }


    @Test
    @DirtiesContext
    void shouldeNotDeleteStockIfNotExists(){

        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/seller/stocks/ofUser/130/ofBook/13", HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

    }


}
