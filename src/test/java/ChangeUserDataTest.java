import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangeUserDataTest {

    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("Изменение данных пользователя с передачей токена авторизации в api/auth/user")
    public void changingDataWithAuth() {
        ValidatableResponse responseCreate = userSteps.createUser(Data.RANDOM_EMAIL, Data.RANDOM_PASSWORD, Data.RANDOM_NAME);
        userSteps.checkAnswerSuccess(responseCreate);
        ValidatableResponse responseLogin = userSteps.login(Data.RANDOM_EMAIL, Data.RANDOM_PASSWORD);
        userSteps.checkAnswerSuccess(responseLogin);
        accessToken = userSteps.getAuthToken(responseLogin);
        ValidatableResponse responseChangeWithToken = userSteps.authorizationWithAuth(accessToken, "q" + Data.RANDOM_EMAIL, "q" + Data.RANDOM_PASSWORD, "q" + Data.RANDOM_NAME);
        userSteps.checkAnswerSuccess(responseChangeWithToken);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Изменение данных пользователя без передачи токена авторизации в api/auth/user")
    public void changingDataWithoutAuth() {
        ValidatableResponse responseCreate = userSteps.createUser(Data.RANDOM_EMAIL, Data.RANDOM_PASSWORD, Data.RANDOM_NAME);
        userSteps.checkAnswerSuccess(responseCreate);
        ValidatableResponse responseLogin = userSteps.login(Data.RANDOM_EMAIL, Data.RANDOM_PASSWORD);
        userSteps.checkAnswerSuccess(responseLogin);
        accessToken = userSteps.getAuthToken(responseLogin);
        ValidatableResponse responseChangeWithoutToken = userSteps.authorizationWithoutAuth("q" + Data.RANDOM_EMAIL, "q" +  Data.RANDOM_PASSWORD, "q" + Data.RANDOM_NAME);
        userSteps.checkAnswerWithoutToken(responseChangeWithoutToken);
    }

    @After
    public void close() {
        userSteps.deletingUsersAfterTests(accessToken);
    }

}