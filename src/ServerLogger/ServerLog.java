package ServerLogger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Created by Eric on 15.06.2017.
 * ServerLog for logging of warnings, info and errors
 */
public class ServerLog {

    static public FileHandler fileTxt;

    static public FileHandler fileHTML;
    static private Formatter formatterHTML;

    static public void setup(File dirName, String pattern, String outputFormat) throws IOException {



        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        logger.setLevel(Level.ALL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date());
        System.out.println(date);

        if (outputFormat.equals("txt")) {
            System.out.println("TXT");
            fileTxt = new FileHandler(dirName.getPath() + File.separator + "log_" + date + ".log", true);
            SimpleFormatter formatterTxt = new SimpleFormatter();
            fileTxt.setFormatter(formatterTxt);
            logger.addHandler(fileTxt);
        }
        if (outputFormat.equals("html")) {
            System.out.println("HTML");
            fileHTML = new FileHandler(dirName.getPath() + File.separator + "log_" + date + ".html", true);
            formatterHTML = new ServerLogFormatter();
            fileHTML.setFormatter(formatterHTML);
            logger.addHandler(fileHTML);
        }


    }
}

