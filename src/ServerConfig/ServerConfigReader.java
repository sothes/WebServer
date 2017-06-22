package ServerConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Eric on 20.06.2017.
 * This class reads the config.properties file in /res and creates global variables
 */
public class ServerConfigReader {

    private InputStream inputStream;

    private int Port;
    private String FileName;
    private String DirName;
    private String DocFolder;
    private int MaxLogCount;
    private String ExceptionDir;
    private String LogFolder;
    private String LogPattern;
    private String LogOutFormat;

    public ServerConfigReader() throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            //Date time = new Date(System.currentTimeMillis());

            // get the property value and print it out
            String portTemp = prop.getProperty("port");
            Port = Integer.parseInt(portTemp);
            FileName = prop.getProperty("fileName");
            DocFolder = prop.getProperty("docFolder");
            DirName = prop.getProperty("dirName");
            LogFolder = prop.getProperty("logFolder");
            String maxLogCount = prop.getProperty("maxLogCount");
            MaxLogCount = Integer.parseInt(maxLogCount);
            ExceptionDir = prop.getProperty("exceptionsFolder");
            LogPattern = prop.getProperty("logPattern");
            LogOutFormat = prop.getProperty("logOutFormat");

            checkExists(getLogFolder());

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            assert inputStream != null;
            inputStream.close();
        }


    }

    public boolean checkLogCount(int maxLogCount) {
        File logDir = new File(DocFolder + File.separator + "Logs");
        if (logDir.listFiles().length > maxLogCount) {
            int count = logDir.listFiles().length;
            for (File file : logDir.listFiles()) {
                if (count > maxLogCount) {
                    file.delete();
                    count--;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void checkExists(File logFolder) {
        if (!(logFolder.exists())) {
            if (!(new File(logFolder.getParent()).exists()))
                if ((new File(logFolder.getParent())).mkdir()) System.out.println("Doc Directory Created");
            if (logFolder.mkdir()) System.out.println("Log Directory Created");
        }

    }

    public File getFile() {
        return new File(DocFolder + File.separator + getDirName() + File.separator + FileName);
    }

    public int getPort() {
        return Port;
    }

    public String getFileName() {
        return FileName;
    }

    public String getDirName() {
        return DirName;
    }

    public String getDocFolder() {
        return DocFolder;
    }

    public int getMaxLogCount() {
        return MaxLogCount;
    }

    public String getExceptionDir() {
        return ExceptionDir;
    }

    public File getLogFolder() {
        return new File(DocFolder + File.separator + LogFolder);
    }

    public String getLogPattern() {
        return LogPattern;
    }

    public String getLogOutFormat() {
        return LogOutFormat;
    }
}
