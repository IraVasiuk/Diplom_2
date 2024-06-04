import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class OrderCreationTest {
    private UserSteps userSteps;
    private Order order;
    private OrderSteps orderSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        userSteps.createUser(Data.RANDOM_EMAIL, Data.RANDOM_PASSWORD, Data.RANDOM_NAME);
        ValidatableResponse responseLogin = userSteps.login(Data.RANDOM_EMAIL, Data.RANDOM_PASSWORD);
        accessToken = userSteps.getAuthToken(responseLogin);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOderWithAuthorization() {
        order = new Order(List.of("61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa7a"));
        ValidatableResponse responseCreateAuth = orderSteps.createOrderWithAuth(accessToken, order);
        userSteps.checkAnswerSuccess(responseCreateAuth);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOderWithoutAuthorization() {
        order = new Order(List.of("61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa7a"));
        ValidatableResponse responseCreateAuth = orderSteps.createOrderWithoutAuth(order);
        userSteps.checkAnswerSuccess(responseCreateAuth);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией без ингредиентов")
    public void createOderAuthWithoutIngredients() {
        order = new Order();
        ValidatableResponse responseCreateAuth = orderSteps.createOrderWithAuth(accessToken, order);
        orderSteps.checkAnswerWithoutIngredients(responseCreateAuth);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    public void createOderNonAuthWithoutIngredients() {
        order = new Order();
        ValidatableResponse responseCreateAuth = orderSteps.createOrderWithoutAuth(order);
        orderSteps.checkAnswerWithoutIngredients(responseCreateAuth);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией с неверным хешем ингредиентов")
    public void createOderAuthWithWrongHashIngredients() {
        order = new Order(List.of("665dbe*"));
        ValidatableResponse responseCreateAuth = orderSteps.createOrderWithAuth(accessToken, order);
        orderSteps.checkAnswerWithWrongHash(responseCreateAuth);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с неверным хешем ингредиентов")
    public void createOderNonAuthWithWrongHashIngredients() {
        order = new Order(List.of("665dbe*"));
        ValidatableResponse responseCreateAuth = orderSteps.createOrderWithoutAuth(order);
        orderSteps.checkAnswerWithWrongHash(responseCreateAuth);
    }

    @After
    public void close() {
        userSteps.deletingUsersAfterTests(accessToken);
    }

}