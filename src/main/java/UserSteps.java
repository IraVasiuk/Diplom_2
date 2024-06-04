import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class UserSteps extends CustomerSteps {

    @Step("Создать пользователя")
    public ValidatableResponse createUser(String email, String password, String name) {
        User user = new User(email, password, name);
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(Urls.REGISTER)
                .then();
    }

    @Step("Удалить пользователя")
    public void deleteUser(String accessToken) {
        given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .when()
                .delete(Urls.USER);
    }

    @Step("Авторизация с токеном доступа")
    public ValidatableResponse authorizationWithAuth(String accessToken, String email, String password, String name) {
        User user = new User(email, password, name);
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body(user)
                .when()
                .patch(Urls.USER)
                .then();
    }

    @Step("Авторизация без токена доступа")
    public ValidatableResponse authorizationWithoutAuth(String email, String password, String name) {
        User user = new User(email, password, name);
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(Urls.USER)
                .then();
    }

    @Step("Вход пользователя в систему")
    public ValidatableResponse login(String email, String password) {
        Identification credentials = new Identification(email, password);
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(Urls.LOGIN)
                .then();
    }

    @Step("Получение токена доступа")
    public String getAuthToken(ValidatableResponse validatableResponse) {
        return validatableResponse.extract().path("accessToken");
    }

    @Step("Проверка ответа сервера при создании, изменении пользователя или получении списка заказов - 200")
    public void checkAnswerSuccess(ValidatableResponse validatableResponse) {
        validatableResponse
                .body("success", is(true))
                .statusCode(200);
    }

    @Step("Проверка ответа после создания уже зарегистрированного пользователя")
    public void checkAnswerAlreadyHave(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(403);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("User already exists", actualMessage);
    }

    @Step("Проверка подлинности ответа, созданного без обязательного поля электронной почты, пароля и имени пользователя")
    public void checkAnswerTaboo(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(403);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("Email, password and name are required fields", actualMessage);
    }

    @Step("Проверка ответа после входа в систему с неправильными учетными данными")
    public void checkAnswerWithWrongData(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(401);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("email or password are incorrect", actualMessage);
    }

    @Step("Проверка ответа после изменения пользовательских данных без токена")
    public void checkAnswerWithoutToken(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(401);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("You should be authorised", actualMessage);
    }

    @Step("Удаление пользователя после тестирования")
    public void deletingUsersAfterTests(String accessToken) {
        if (accessToken != null) {
            deleteUser(accessToken);
        } else {
            given().spec(getSpec())
                    .when()
                    .delete(Urls.USER);
        }
    }
}