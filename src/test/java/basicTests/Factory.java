package basicTests;

import enhancements.AssertLogger;
import enhancements.MainLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sivar on 14-06-2017.
 */
public class Factory{

    DesiredCapabilities desiredCapabilities = null;
    WebDriver driver = null;
    MainLogger log = null;
    AssertLogger assertLogger = null;


    public Factory() {
        this.driver = driver;
        this.desiredCapabilities = desiredCapabilities;
    }


    @BeforeSuite
    @Parameters({"ifGrid","browser"})
    public void startWebDriver(String ifGrid, String browser ) throws MalformedURLException {

        if(ifGrid.equalsIgnoreCase("true")){
            if (browser.equalsIgnoreCase("chrome")){
                desiredCapabilities = DesiredCapabilities.chrome();
            } else if(browser.equalsIgnoreCase("firefox")) {
                desiredCapabilities = DesiredCapabilities.firefox();
            }  else if(browser.equalsIgnoreCase("ie") || browser.equalsIgnoreCase("internet explorer")) {
                desiredCapabilities = DesiredCapabilities.internetExplorer();
            }else
                desiredCapabilities = DesiredCapabilities.firefox();
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), desiredCapabilities);
        }

        else{
            if (browser.equalsIgnoreCase("chrome")){

                System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\src\\test\\resources\\chromedriver.exe");
                //Adding chrome options
                ChromeOptions cOptions = new ChromeOptions();
                cOptions.addArguments("start-maximized");
                cOptions.addArguments("disable-infobars");
                driver = new ChromeDriver(cOptions);
            } else if(browser.equalsIgnoreCase("firefox"))
                     driver = new FirefoxDriver();

            else if(browser.equalsIgnoreCase("ie") || browser.equalsIgnoreCase("internet explorer"))
                    driver = new InternetExplorerDriver();

            else
                    driver = new FirefoxDriver();
             }

        driver.manage().window().maximize();

        log = new MainLogger();
        assertLogger = new AssertLogger();
    }


    @AfterSuite
    public void closeBrowser(){
        driver.close();
        log.info("Closed browser.");

    }


}
