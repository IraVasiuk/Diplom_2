import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    @Description("Если введены правильные учетные данные, то при успешном выполнении запроса - возвращается токен")
    public void createUniqueUser() {
        ValidatableResponse responseCreate = userSteps.createUser(Data.RANDOM_EMAIL, Data.RANDOM_PASSWORD, Data.RANDOM_NAME);
        userSteps.checkAnswerSuccess(responseCreate);
        accessToken = userSteps.getAuthToken(responseCreate);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Создание пользователя, который уже зарегистрирован, и проверка ответа")
    public void createDuplicationUser() {
        ValidatableResponse responseCreate = userSteps.createUser(Data.RANDOM_EMAIL, Data.RANDOM_PASSWORD, Data.RANDOM_NAME);
        accessToken = userSteps.getAuthToken(responseCreate);
        ValidatableResponse responseIdentical = userSteps.createUser(Data.RANDOM_EMAIL, Data.RANDOM_PASSWORD, Data.RANDOM_NAME);
        userSteps.checkAnswerAlreadyHave(responseIdentical);
    }

    @Test
    @DisplayName("Создание пользователя без электронной почты")
    @Description("Создание пользователя без электронной почты и проверка ответа")
    public void createUserWithoutEmail() {
        ValidatableResponse responseCreate = userSteps.createUser("", Data.RANDOM_PASSWORD, Data.RANDOM_NAME);
        userSteps.checkAnswerTaboo(responseCreate);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Создание пользователя без пароля и проверка ответа")
    public void createUserWithoutPassword() {
        ValidatableResponse responseCreate = userSteps.createUser(Data.RANDOM_EMAIL, "", Data.RANDOM_NAME);
        userSteps.checkAnswerTaboo(responseCreate);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Создание пользователя без имени и проверка ответа")
    public void createUserWithoutName() {
        ValidatableResponse responseCreate = userSteps.createUser(Data.RANDOM_EMAIL, Data.RANDOM_PASSWORD, "");
        userSteps.checkAnswerTaboo(responseCreate);
    }

    @After
    public void close() {
        userSteps.deletingUsersAfterTests(accessToken);
    }

}