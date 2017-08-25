package pages;

import config.AssertLogger;
import config.MainLogger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;



public class UIUtility {

    WebDriver driver = null;

    public UIUtility(WebDriver driver){
        this.driver =driver;
    }


    MainLogger log = new MainLogger(this.getClass().getName());
    AssertLogger check = new AssertLogger();

    /**
     * Safe wait for element visibility.
     *
     * @param timeout the timeout
     * @param element web element
     * @return true, if successful
     */
    public boolean safeWaitForElementVisibility(int timeout, WebElement element) {
        try {
            new WebDriverWait(driver, timeout).until(ExpectedConditions
                    .visibilityOf(element));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Safe click.
     *
     * @param element web element
     */
    public boolean safeClick(WebElement element) {
        try {
            safeWaitForElementVisibility(15,element);
            element.click();
            sleep(1000);
            return true;
        } catch (NoSuchElementException e) {
            // GetScreenshot.takeScreenShot(driver);
            check.AssertFail("Unable to find the element " + e.getMessage());
            return false;
        } catch (Exception e) {
            // GetScreenshot.takeScreenShot(driver);
            check.AssertFail("Unable to click on the element"
                    + e.getMessage());
            return false;
        }
    }
    /**
     * Sleep.
     *
     * @param time the time
     */
    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
//            logger.warn("for some reason I failed to sleep for the entire timeout");
        }
    }

    /**
     * Switch to next tab.
     *
     */
        public void switchToNextTab(){
            String switchHandle = null;
            for(String handle : driver.getWindowHandles()){
                switchHandle = handle;
            }
            driver.switchTo().window(switchHandle);
        }


    /**
     * getPDFDataInStringFromPage.
     *
     * @param strURL the url of the page in string
     */
    public String getPDFDataInStringFromPage(String strURL){
        PDDocument pdDoc = null;
        String parsedText = null;
        URL url;

        try {
            url = new URL(strURL);
            InputStream is = url.openStream();
            BufferedInputStream fileToParse = new BufferedInputStream(is);
            pdDoc = PDDocument.load(fileToParse);
            parsedText = new PDFTextStripper().getText(pdDoc);
        } catch (MalformedURLException e2) {
            log.error("URL string could not be parsed ",e2);
        } catch (IOException e) {
            log.error("Unable to open PDF Parser. " , e);
        } finally{
            if(pdDoc!=null)
                try {
                    pdDoc.close();
                }catch(IOException e)
                {
                    log.error("Unable to close the document ",e);
                }
        }

        return parsedText;
    }

}
