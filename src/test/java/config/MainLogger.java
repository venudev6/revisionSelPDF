package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by sivar on 14-06-2017.
 */
public class MainLogger {

    Logger logger  = LoggerFactory.getLogger(this.getClass().getName());

     Logger getLogger(){

        return logger;
    }

     public MainLogger(){

        this.logger=logger;
    }

    public void debug(String message){
        logger.debug(message);
    }

    public void info(String message){
        logger.info(message);
    }

    public void error(String message, Exception e){
        logger.error(message,e);
    }

    public void warn(String message){
        logger.warn(message);
    }

}
