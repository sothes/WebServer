import ServerConfig.ServerConfigReader;
import ServerLogger.ServerLog;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Eric on 04.06.2017.
 * This class contains the main and the server
 */

public class Server {
    static final String END_OF_HEADER = "";

    private Server(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println(InetAddress.getLocalHost() + ":" + port);
            while (true) {
                Socket ss = server.accept();
                new Connection(ss);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerConfigReader config;
        try {
            ServerLog.setup();
            config = new ServerConfigReader();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                ServerLog.fileTxt.close();
                System.out.println("LogsClosed");
                config.checkLogCount(config.getMaxLogCount());
            }));
            new Server(config.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
