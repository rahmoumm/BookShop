package BookShop.demo.Controllers;


import BookShop.demo.model.User;
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
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


    // GET TESTS
    @Test
    void shouldReturnAllUsersWhenDataExists(){
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/users", String.class);
        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.OK);

        DocumentContext jsonVals = JsonPath.parse(reponse.getBody());
        int numOfUsers = jsonVals.read("$.length()");
        Assertions.assertEquals(numOfUsers, 3);

        ArrayList<Integer> ids = jsonVals.read("$[*].id");
        ArrayList<Integer> realIds = new ArrayList<>(
                Arrays.asList(11, 12, 13)
        );

        Assertions.assertIterableEquals(ids, realIds);
    }

    @Test
    void shouldReturnAUserWhenItExists(){
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/users/11", String.class);

        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.OK);

        DocumentContext jsonVals = JsonPath.parse(reponse.getBody());

        Number bookId = jsonVals.read("$.id");

        Assertions.assertEquals(bookId, 11);
    }

    @Test
    void shouldReturnNotFoundIfDoNotExist(){
        ResponseEntity<String> reponse = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/users/159", String.class);

        Assertions.assertEquals(reponse.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    // PUT TESTS
    @Test
    @DirtiesContext
    void shouldChangeAUserWhenItExists(){
        HttpEntity<User> newUser = new HttpEntity<>(new User(null, "ahlam", null, null));
        // exchange
        ResponseEntity<Void> responseEntity = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/users/13", HttpMethod.PUT, newUser, Void.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        ResponseEntity<User> user = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/users/13", User.class);
        Assertions.assertEquals(user.getBody().getLastName(), "ahlam");
    }

    @Test
    void shouldNotModifyIfDoNotExist(){
        HttpEntity<User> newUser = new HttpEntity<>(new User(null, "Samar", null, null));

        ResponseEntity<Void> responseEntity = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/users/159", HttpMethod.PUT, newUser, Void.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    // POST TESTS
    @Test
    @DirtiesContext
    void shouldCreateNewUser(){
        HttpEntity<User> newUser = new HttpEntity<>(new User("Halima", "Samar", "h@yahoo.fr", "123"));

        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .postForEntity("/users/register", newUser, Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }



    @Test
    @DirtiesContext
    void shouldeDeleteUserWhenExists(){
        // Faire attention parceque tu deletais le user avec lequel tu travaillais
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/admin/users/13", HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .getForEntity("/nonAuth/users/13", Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void shouldeNotDeleteUserIfNotExists(){

        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("m@gmail.com", "abc")
                .exchange("/admin/users/159", HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

    }


}
