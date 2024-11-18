import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CallbackTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }

    }

    @Test
    void scorrectFillingOfTheForm() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        input.get(0).sendKeys("Олег Чебоксаров");
        input.get(1).sendKeys("+78911728346");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text);
    }

    @Test
    void thePhoneFieldHasMoreThan11Digits() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        input.get(0).sendKeys("Олег Чебоксаров");
        input.get(1).sendKeys("+789117283463123");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button__text")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void thePhoneDoesntStartWithAPlus() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        input.get(0).sendKeys("Олег Чебоксаров");
        input.get(1).sendKeys("8911728346");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button__text")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void firstNameSurnameHyphenated() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        input.get(0).sendKeys("Олег-Чебоксаров");
        input.get(1).sendKeys("+78911728346");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text);
    }

    @Test
    void firstNameLastNameViaInEnglish() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        input.get(0).sendKeys("Oleg Cheboksarov");
        input.get(1).sendKeys("+78911728346");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button__text")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText().trim();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void minimumNumberOfDigitsInANumber() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        input.get(0).sendKeys("Олег Чебоксаров");
        input.get(1).sendKeys("8");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button__text")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void permissionIsNotChecked() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        input.get(0).sendKeys("Олег Чебоксаров");
        input.get(1).sendKeys("+78911728346");
        driver.findElement(By.cssSelector(".button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).getText().trim();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text);
    }

    @Test
    void noName() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        input.get(1).sendKeys("+78911728346");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void noNumber() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        input.get(0).sendKeys("Олег Чебоксаров");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", text);
    }
}