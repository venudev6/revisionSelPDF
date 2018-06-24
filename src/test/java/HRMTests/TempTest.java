package HRMTests;

import org.aspectj.lang.annotation.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class TempTest {

    WebDriver driver ;


    @Test
    public void sample(){

        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") +
                "/src/test/resources/linux/geckodriver-v0.17.0-linux64/geckodriver");

        WebDriver driver = new FirefoxDriver();
        driver.get("http://opensource.demo.orangehrmlive.com/index.php");
        driver.findElement(By.id("txtUsername")).sendKeys("admin");
        try {
            Thread.sleep(5000);
        }catch(InterruptedException e) {}

        driver.quit();
    }

}
