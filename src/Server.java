import ServerConfig.ServerConfigReader;
import ServerLogger.ServerLog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    private Server(int port) {

        try {
            ServerSocket server = new ServerSocket(port);
            while (true) {
                Socket ss = server.accept();
                Connection con = new Connection(ss);
                con.start();
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
            System.out.println(confi.getPort());
            new Server(confi.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
