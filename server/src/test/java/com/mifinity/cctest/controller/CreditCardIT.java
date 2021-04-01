package com.mifinity.cctest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mifinity.cctest.model.Creditcard;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Integration tests for the {@Link CreditcardController} REST controller.
 */
@MicronautTest(transactional = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreditCardIT {

  @Inject
  @Client("/")
  RxHttpClient client;

  @Test
  public void getCreditcardForAdminUser() {
    Optional<String> accessTokenOptional = getAccessToken("admin", "admin");
    Assertions.assertTrue(accessTokenOptional.isPresent());

    Page<Creditcard> creditcardsPage = client.retrieve(HttpRequest.GET("/api/creditcards/number/510").
        bearerAuth(accessTokenOptional.get()), Argument.of(Page.class, Creditcard.class)).blockingFirst();
    Creditcard creditcard = creditcardsPage.getContent().get(0);

    Assertions.assertNotNull(creditcard);
    Assertions.assertEquals(creditcard.getId(), 1);
  }

  @Test
  public void getCreditcardForLoggedInUser() {
    Optional<String> accessTokenOptional = getAccessToken("user", "user");
    Assertions.assertTrue(accessTokenOptional.isPresent());

    Page<Creditcard> creditcardsPage = client.retrieve(HttpRequest.GET("/api/creditcards/number/510").
      bearerAuth(accessTokenOptional.get()), Argument.of(Page.class, Creditcard.class)).blockingFirst();
    Creditcard creditcard = creditcardsPage.getContent().get(0);

    Assertions.assertNotNull(creditcard);
    Assertions.assertEquals(creditcard.getId(), 1);
  }

  @Test
  public void getAllCreditcardsForLoggedInUser() {
    Optional<String> accessTokenOptional = getAccessToken("user", "user");
    Assertions.assertTrue(accessTokenOptional.isPresent());

    Page<Creditcard> creditcardsPage = client.retrieve(HttpRequest.GET("/api/creditcards").
      bearerAuth(accessTokenOptional.get()), Argument.of(Page.class, Creditcard.class)).blockingFirst();

    Assertions.assertNotNull(creditcardsPage);
    Assertions.assertEquals(creditcardsPage.getContent().size(), 1);
  }

  public Optional<String> getAccessToken(String username, String password) {
    HttpResponse<JsonNode> response = client.exchange(HttpRequest.POST("/api/authenticate", new UsernamePasswordCredentials(username, password)), JsonNode.class)
      .blockingFirst();
    return response.getBody().map(jsonNode -> jsonNode.findValue("access_token").textValue());
  }
}
