package helpers;

import org.apache.log4j.Logger;

/**
 * Created by Justin on 17/04/2017.
 */
public class LogHelper {

    final static  Logger logger = Logger.getLogger(LogHelper.class);
    public static void writeLog(Object obj, String msg, String method){

        StringBuilder strBuilder = new StringBuilder(obj.toString());
        strBuilder.append(" ").append(method).append(" ").append(msg);
        logger.debug(strBuilder.toString());
        logger.info("info" + strBuilder.toString());
        logger.trace("trace" + strBuilder.toString());

    }
}
