import ServerConfig.ServerConfigReader;
import ServerLogger.ServerLog;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

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

        ServerConfigReader confi;

        try {
            ServerLog.setup();
            confi = new ServerConfigReader();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                ServerLog.fileTxt.close();
                System.out.println("LogsClosed");
                confi.checkLogCount(4);
            }));
            new Server(confi.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
