import ServerConfig.ServerConfigReader;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class Connection extends Thread {

	private final static String bla="\r\n";
	private final static Logger conLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private BufferedReader r;
	private PrintWriter w;
	private File file;
	private DataOutputStream ou;


	Connection(Socket ss){

		//get properties value
		try {
			file = new ServerConfigReader().getFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		conLogger.info("Connections established on: " + ss.getLocalAddress() + ":" + ss.getLocalPort());

		try {
			r=new BufferedReader(new InputStreamReader(ss.getInputStream()));
			w=new PrintWriter(new OutputStreamWriter(ss.getOutputStream()));
			ou = new DataOutputStream(ss.getOutputStream());
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendBytes(File fileToSend) {
		InputStream fis;
		byte[] buffer = new byte[4096];
		int bytes = 0;
		try {
			fis = new FileInputStream(fileToSend);

			while ((bytes = fis.read(buffer)) > 0) {
				ou.write(buffer, 0, bytes);
			}
			fis.close();
		} catch (IOException e) {
			w.println("HTTP/1.1 404 Not Found");
			w.println("Server: MyServer");
			w.println("Connection: close");
			w.println("Content-Type: text/html");
			w.println("");
			w.println("<html> <head> <title> bla </title> </head> <body bgcolor=red> hjshdf </body> </html>");
			w.close();
		}
	}

	public void run(){
		String line;
		System.out.println(file.getAbsolutePath());

		synchronized (conLogger) {

			conLogger.info("Connection established");

			try {
				while ((line = r.readLine()) != null) {
					System.out.println("Server: " + line);
					if (line.startsWith("GET")) {
						StringTokenizer token = new StringTokenizer(line);
						String reqFile = token.nextToken();
						reqFile = token.nextToken();
						reqFile = reqFile.substring(1);
						System.out.println(reqFile);
						if (!(reqFile.isEmpty()))
							getRequest(new File(new ServerConfigReader().getDocFolder() + File.separator + new ServerConfigReader().getDirName() + File.separator + reqFile));
						else {
							getRequest(file);
						}
					}
					if (line.equals(Server.END_OF_HEADER)) break;
				}
				ou.close();
				r.close();
				w.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String contentType(String FileName) {
		if(FileName.endsWith(".htm") || FileName.endsWith(".html")){

			return "text/html";
		}
		if(FileName.endsWith(".jpg") || FileName.endsWith(".jpeg")){
			return "image/jpeg";

		}
		if(FileName.endsWith(".gif")){
			return "image/gif";

		}
		return "application/octet-stream";

	}

	private void getRequest(File fileToSend) {
		try {
			if (fileToSend.exists()) {
				conLogger.info("Sending website in html format");
				String content = contentType(new ServerConfigReader().getFileName());

				ou.writeBytes("HTTP/1.1 200 OK" + bla);
				ou.writeBytes("Server: MyServer" + bla);
				ou.writeBytes("Connection: close");
				ou.writeBytes("Content-Type: " + content + bla);
				ou.writeBytes(bla);
				sendBytes(fileToSend);
			}
			if (!(file.exists())) {
				conLogger.warning("File not found, throwing 404");
				System.out.println("404");
				w.println("HTTP/1.1 404 Not Found");
				w.println("Server: MyServer");
				w.println("Connection: close");
				w.println("Content-Type: text/html");
				w.println("");
				w.println("<html> <head> <title> bla </title> </head> <body bgcolor=red> hjshdf </body> </html>");
				conLogger.info("Connection closed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}