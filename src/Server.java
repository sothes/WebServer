import ServerLogger.ServerLog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.*;

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

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {public void run(){ServerLog.fileTxt.close();ServerLog.fileHTML.close();System.out.println("LogsClosed");}}));


		try{
			ServerLog.setup();
		}catch(IOException e){
			e.printStackTrace();
		}

		int port=1312;

		//loggi.fine("Server starting on port: "+port);
		new Server(port);





	}

}
