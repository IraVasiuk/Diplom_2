import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginUserTest {

    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        ValidatableResponse responseCreate = userSteps.createUser(Data.RANDOM_EMAIL, Data.RANDOM_PASSWORD, Data.RANDOM_NAME);
        accessToken = userSteps.getAuthToken(responseCreate);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("При вводе действительного адреса электронной почты и пароля успешный запрос возвращает токен")
    public void loginUser() {
        ValidatableResponse responseLogin = userSteps.login(Data.RANDOM_EMAIL, Data.RANDOM_PASSWORD);
        userSteps.checkAnswerSuccess(responseLogin);
    }

    @Test
    @DisplayName("Логин с неверной электронной почтой")
    @Description("При вводе неверной электронной почты будет возвращен код 401 Unauthorized")
    public void loginUserWithWrongEmail() {
        ValidatableResponse responseLogin = userSteps.login("XXXXXX@yandex.ru", Data.RANDOM_PASSWORD);
        userSteps.checkAnswerWithWrongData(responseLogin);
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("При вводе неверного пароля будет возвращен код ответа 401 Unauthorized")
    public void loginUserWithWrongPass() {
        ValidatableResponse responseLogin = userSteps.login(Data.RANDOM_EMAIL, "012345");
        userSteps.checkAnswerWithWrongData(responseLogin);
    }

    @After
    public void close() {
        userSteps.deletingUsersAfterTests(accessToken);
    }

}
