package ServerLogger;

import java.io.IOException;
import java.util.logging.*;

/**
 * Created by Eric on 15.06.2017.
 */
public class ServerLog{

    static public FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    static public FileHandler fileHTML;
    static private Formatter formatterHTML;
    //public ServerLogger.ServerLog() {

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
            fileTxt = new FileHandler("Logging.txt");
            fileHTML = new FileHandler("Logging.html");

            // create a TXT formatter
            formatterTxt = new SimpleFormatter();
            fileTxt.setFormatter(formatterTxt);
            logger.addHandler(fileTxt);

        }
    }
