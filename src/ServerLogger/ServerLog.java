package ServerLogger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Created by Eric on 15.06.2017.
 */
public class ServerLog{

    static public FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    static public FileHandler fileHTML;
    static private Formatter formatterHTML;

    static private String dirName="Doc"+ File.separator+"Logs"+File.separator;

        static public void setup() throws IOException {

            // get the global logger to configure it
            Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

            // suppress the logging output to the console
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            if (handlers[0] instanceof ConsoleHandler) {
                rootLogger.removeHandler(handlers[0]);
            }

            logger.setLevel(Level.INFO);

            String pattern = "yyyy-MM-dd_HH-mm-ss.S";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String date = simpleDateFormat.format(new Date());
            System.out.println(date);
            fileTxt = new FileHandler(dirName+"log_"+date+".log");
            fileHTML = new FileHandler(dirName+"log_"+date+".html");

            // create a TXT formatter
            formatterTxt = new SimpleFormatter();
            fileTxt.setFormatter(formatterTxt);
            logger.addHandler(fileTxt);

        }
    }
