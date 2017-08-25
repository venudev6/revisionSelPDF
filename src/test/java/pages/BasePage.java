package pages;

import com.fasterxml.jackson.databind.ser.Serializers;
import config.MainLogger;
import org.openqa.selenium.WebDriver;
import org.testng.IConfigurable;
import org.testng.IConfigureCallBack;
import org.testng.ITestResult;

public class BasePage implements IConfigurable {

    protected long pageLoadStartTime;
    protected long pageLoadEndTime;
    protected final int PAGETIMEOUT =15;
    protected UIUtility uiUtility=null;

    public BasePage(WebDriver driver){

        uiUtility = new UIUtility(driver);
    }

    public void run(IConfigureCallBack callBack, ITestResult testResult){    }

    protected void markPageLoadStartTime() {
        pageLoadStartTime = System.currentTimeMillis();
    }
    protected long markPageLoadEndTime() {
        pageLoadEndTime = System.currentTimeMillis();
        return pageLoadEndTime - pageLoadStartTime;

        //logger.debug("it took " + duration + "ms to load "+ this.getClass().toString());
    }

}
