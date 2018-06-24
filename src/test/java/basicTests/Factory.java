package basicTests;

import config.AssertLogger;
import config.MainLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.server.handler.GetAllWindowHandles;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sivar on 14-06-2017.
 */
public class Factory{

    DesiredCapabilities desiredCapabilities = null;
    public WebDriver driver = null;
    public MainLogger log = null;
    AssertLogger assertLogger = null;
    String OSName = System.getProperty("os.name");
    //String sysSep = System.getProperty("file.separator");
    public String baseURL = null;


    public Factory() {
        this.driver = driver;
        this.desiredCapabilities = desiredCapabilities;
        System.out.println("The class name is : "+this.getClass().getSuperclass().getName());
        this.log = new MainLogger(this.getClass().getSuperclass().getName());
    }


    @BeforeSuite
    @Parameters({"ifGrid","browser","baseURL"})
    public void startWebDriver(String ifGrid, String browser, String baseURL ) throws MalformedURLException {

        //Checking if Grid
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

        //Non Grid testing
        else{
            if (browser.equalsIgnoreCase("chrome")){

                setPropertyChromeDriver();
                //Adding chrome options
                ChromeOptions cOptions = new ChromeOptions();
                cOptions.addArguments("start-maximized");
                cOptions.addArguments("disable-infobars");
                driver = new ChromeDriver(cOptions);
            }

            else if(browser.equalsIgnoreCase("firefox") ||
                             browser.equalsIgnoreCase("ff")) {
                setPropertyFirefoxDriver();
                driver = new FirefoxDriver();
            }

            else if(browser.equalsIgnoreCase("ie") || browser.equalsIgnoreCase("internet explorer")) {
                if(!OSName.toLowerCase().contains("win")) {
                    log.error("Cannot run tests for IE in a linux system",new Exception());
                    System.exit(1);
                }
                setPropertyIEDriver();
                driver = new InternetExplorerDriver();
            }

            else if(browser.equalsIgnoreCase("edge")) {
                setSystemProperty("webdriver.edge.driver",
                        "\\src\\test\\resources\\win\\MicrosoftWebDriver.exe");
                driver = new EdgeDriver();
            }

            else {
                setPropertyFirefoxDriver();
                driver = new FirefoxDriver();
            }
        }

        driver.manage().window().maximize();

        //log = new MainLogger();
        assertLogger = new AssertLogger();
        this.baseURL=baseURL;
    }




    //Set System properties for chrome
    public void setPropertyChromeDriver() {

        this.log.info("The chrome system property is being set ");
        //set for windwos
        if (OSName.toLowerCase().contains("windows")) {
            setSystemProperty("webdriver.chrome.driver",
                    "\\src\\test\\resources\\win\\chromedriver_win32\\chromedriver.exe");
            log.info("The chrome system property is set successfuly.");
            return;
        }

        //check for linux
        if (OSName.toLowerCase().contains("linux")) {
            this.log.info("The OS is : "+OSName);
            if(checkOSBitType(getOSArch("getconf LONG_BIT"),"64")){
                setSystemProperty("webdriver.chrome.driver",
                        "/src/test/resources/linux/chromedriver_linux64/chromedriver");
            } else
               setSystemProperty("webdriver.chrome.driver",
                        "/src/test/resources/linux/chromedriver_linux32/chromedriver");
            return;
        }

        //check for mac
        if (OSName.toLowerCase().contains("Mac")) {
            setSystemProperty("webdriver.chrome.driver",
                    "/src/test/resources/mac/chromedriver_mac64/chromedriver");
            return;
        }

    }




    //Set System properties for Fireofox
    public void setPropertyFirefoxDriver() {
        //set for windows
        if (OSName.toLowerCase().contains("win")) {
            if(checkOSBitType(getOSArch("wmic os get osarchitecture"),"64")){
                setSystemProperty("webdriver.gecko.driver",
                        "\\src\\test\\resources\\win\\geckodriver-v0.17.0-win64\\geckodriver.exe");
            } else
                setSystemProperty("webdriver.gecko.driver",
                        "\\src\\test\\resources\\win\\geckodriver-v0.17.0-win32\\geckodriver.exe");
        }
        //check for linux
        if (OSName.toLowerCase().contains("linux")) {
            if(checkOSBitType(getOSArch("getconf LONG_BIT"),"64")){
                setSystemProperty("webdriver.gecko.driver",
                        "/src/test/resources/linux/geckodriver-v0.17.0-linux64/geckodriver");
            } else
                setSystemProperty("webdriver.gecko.driver",
                        "/src/test/resources/linux/geckodriver-v0.17.0-linux32/geckodriver");
        }
        //check for mac
        if (OSName.toLowerCase().contains("Mac")) {
            setSystemProperty("webdriver.gecko.driver",
                    "/src/test/resources/linux/geckodriver-v0.17.0-macos/geckodriver");
        }

    }


    //Set System properties for IE
    public void setPropertyIEDriver() {


        if(checkOSBitType(getOSArch("wmic os get osarchitecture"),"64")){
                setSystemProperty("webdriver.ie.driver",
                        "\\src\\test\\resources\\win\\IEDriverServer_x64_3.4.0\\IEDriverServer.exe");
            } else
                setSystemProperty("webdriver.ie.driver",
                        "\\src\\test\\resources\\win\\IEDriverServer_Win32_3.4.0\\IEDriverServer.exe");

    }



    //Set System propertu for Webdriver
    private void setSystemProperty(String driverType, String location)
    {
        System.setProperty(driverType, System.getProperty("user.dir") + location);
    }

    //Get the OS architecure of System. If the OS is a 64 bit or 32 bit
    public BufferedReader getOSArch(String command){

        System.out.println("Trying to find OS Type");
        Runtime rt = Runtime.getRuntime();
        try{
            Process pr = rt.exec(command);
            return new BufferedReader(new InputStreamReader(pr.getInputStream()));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    //Check if string OS Bit Type is present in the Buffered Reader
    public boolean checkOSBitType(BufferedReader reader, String bitType){
        String s;

        try {
            while ((s = reader.readLine()) != null) {
                if(s.toLowerCase().contains(bitType)) {
                    System.out.println(s);
                    return true;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @AfterSuite
    public void closeBrowser(){
        //String windowTitle = driver.getTitle();
        driver.close();

        try {
            if(driver.getWindowHandles().isEmpty()){
                log.info("Closed browser.");
            }
            else
                driver.quit();
        }catch (Exception e) {}
    }


}
