import ServerLogger.ServerLog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	static int Port=1312;
	
	private Server(){

		try{
			ServerSocket server = new ServerSocket(Port);
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

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {ServerLog.fileTxt.close();ServerLog.fileHTML.close();System.out.println("LogsClosed");}));


		try{
			ServerLog.setup();
		}catch(IOException e){
			e.printStackTrace();
		}

		new Server();





	}

}
