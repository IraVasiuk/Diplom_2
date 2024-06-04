import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
public class OrdersFromUserTest {
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
        order = new Order(List.of("61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa7a"));
        orderSteps.createOrderWithAuth(accessToken, order);
    }

    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя")
    @Description("Получение списка заказов пользователей путем передачи токена")
    public void getListOfOrdersAuth() {
        ValidatableResponse responseGetList = orderSteps.listOfOrdersWithAuth(accessToken);
        userSteps.checkAnswerSuccess(responseGetList);
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованного пользователя")
    @Description("Получение списка заказов пользователей путем передачи токена")
    public void getListOfOrdersNonAuth() {
        ValidatableResponse responseGetList = orderSteps.listOfOrdersWithoutAuth();
        orderSteps.checkAnswerGetListWithoutAuth(responseGetList);
    }

    @After
    public void close() {
        userSteps.deletingUsersAfterTests(accessToken);
    }

}