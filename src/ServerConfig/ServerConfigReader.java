package ServerConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Eric on 20.06.2017.
 */
public class ServerConfigReader {

    private InputStream inputStream;

    private int Port;
    private String FileName;
    private String DirName;
    private String DocFolder;

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

            Date time = new Date(System.currentTimeMillis());

            // get the property value and print it out
            String portTemp = prop.getProperty("port");
            Port = Integer.parseInt(portTemp);
            FileName = prop.getProperty("fileName");
            DocFolder = prop.getProperty("docFolder");
            DirName = prop.getProperty("dirName");

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }


    }

    public void checkLogCount(int maxLogCount) {
        File logDir = new File(DocFolder + File.separator + "Logs");
        if (logDir.listFiles().length > maxLogCount) {
            int count = logDir.listFiles().length;
            for (File file : logDir.listFiles()) {
                if (count > maxLogCount) {
                    file.delete();
                    count--;
                }
            }
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
}
