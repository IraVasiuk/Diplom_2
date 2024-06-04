import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class OrderSteps extends CustomerSteps {
    @Step("Создать заказ без токена доступа")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(Urls.ORDERS)
                .then();
    }

    @Step("Создать заказ с токеном доступа")
    public ValidatableResponse createOrderWithAuth(String accessToken, Order order) {
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body(order)
                .when()
                .post(Urls.ORDERS)
                .then();
    }

    @Step("Список заказов без токена доступа")
    public ValidatableResponse listOfOrdersWithoutAuth() {
        return given()
                .spec(getSpec())
                .body("")
                .when()
                .get(Urls.ORDERS)
                .then();
    }

    @Step("Список заказов с токеном доступа")
    public ValidatableResponse listOfOrdersWithAuth(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body("")
                .when()
                .get(Urls.ORDERS)
                .then();
    }

    @Step("Проверка ответа при создании заказа без ингредиентов")
    public void checkAnswerWithoutIngredients(ValidatableResponse validatableResponse) {
        validatableResponse
                .body("success", is(false))
                .statusCode(400);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("Ingredient ids must be provided", actualMessage);
    }

    @Step("Проверка ответа при создании заказа с с неверным хешем ингредиентов")
    public void checkAnswerWithWrongHash(ValidatableResponse validatableResponse) {
        validatableResponse
                .statusCode(500);
    }

    @Step("Проверка ответа при получении списка заказов от неавторизованного пользователя")
    public void checkAnswerGetListWithoutAuth(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(401);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("You should be authorised", actualMessage);
    }

}