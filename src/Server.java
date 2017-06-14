import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server {

	public static Logger loggi;
	
	public Server(int port){
		try{
			ServerSocket server = new ServerSocket(port);
			while(true){
				Socket ss=server.accept();
				Connection con= new Connection(ss);
				con.start();
			}
		}
		catch (IOException e){
			e.printStackTrace();
			
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Server.loggi = Logger.getLogger(Connection.class.getName());
		loggi.setLevel(Level.ALL);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		loggi.addHandler(handler);
		handler.setFormatter(new SimpleFormatter());
		loggi.fine("hello world");

		int port=1312;

		loggi.fine("Server starting on port: "+port);
		new Server(port);


		
	}

}
