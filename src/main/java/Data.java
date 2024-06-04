import org.apache.commons.lang3.RandomStringUtils;

public class Data {
    //генерирование случайных значений для полей email, password, name
    public static String RANDOM_EMAIL = RandomStringUtils.randomAlphabetic(10) + "@burger.ru";
    public static String RANDOM_PASSWORD = RandomStringUtils.randomNumeric(10);
    public static String RANDOM_NAME = RandomStringUtils.randomAlphabetic(10);
}
