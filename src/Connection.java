import ServerConfig.ServerConfigReader;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 04.06.2017.
 * This class will send and receive the requests of the client
 */

public class Connection extends Thread {

    final static Logger conLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final static String NextLine = System.getProperty("line.separator");
	private BufferedReader r;
	private PrintWriter w;
	private File file;
	private DataOutputStream ou;
    private String ReqIP;


	Connection(Socket ss, File file) {
        ReqIP = ss.getRemoteSocketAddress().toString();
        //get properties value
		try {
			this.file = file;
			conLogger.info("Connections established on: " + InetAddress.getLocalHost() + ":" + ss.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			r = new BufferedReader(new InputStreamReader(ss.getInputStream()));
			w = new PrintWriter(new OutputStreamWriter(ss.getOutputStream()));
			ou = new DataOutputStream(ss.getOutputStream());
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendBytes(File fileToSend) {
		InputStream fis;
		byte[] buffer = new byte[4096];
		int bytes;
		try {
			fis = new FileInputStream(fileToSend);

			while ((bytes = fis.read(buffer)) > 0) {
				ou.write(buffer, 0, bytes);
			}
			fis.close();
		} catch (IOException e) {
			conLogger.log(Level.SEVERE, "File wasn't found!", e);
			w.println("HTTP/1.1 404 Not Found");
			w.println("Server: MyServer");
			w.println("Connection: close");
			w.println("Content-Type: text/html");
			w.println("");
            w.println("<html> <head> <title> 404 </title> </head> <body bgcolor=red> File not found on this server, sorry </body> </html>");
            w.close();
		}
	}

	public void run(){
		String line;

        conLogger.info("Connection established to " + ReqIP);

		try {
			ArrayList<String> tempLine = new ArrayList<>();
			while ((line = r.readLine()) != null) {
				if (!line.isEmpty()) tempLine.add(NextLine + line);
				if (line.startsWith("GET")) {
					StringTokenizer token = new StringTokenizer(line);
					String reqFile = token.nextToken();
					reqFile = token.nextToken();
					reqFile = reqFile.substring(1);
                    conLogger.info(ReqIP + " requested file: " + reqFile);
                    if (!(reqFile.isEmpty()))
						getRequest(new File(file.getParent() + File.separator + reqFile));
					else {
						getRequest(file);
					}
				}
				if (line.equals(Server.END_OF_HEADER)) break;
			}
            conLogger.info(ReqIP + " request: " + tempLine);
            ou.close();
			r.close();
		} catch (Exception e) {
			conLogger.log(Level.SEVERE, "File wasn't found!", e);
			e.printStackTrace();
		}
	}


	private String contentType(String FileName) {
		//What filetype is the requested file?
		if (FileName.endsWith(".htm") || FileName.endsWith(".html")) return "text/html";
        if (FileName.endsWith(".jpg") || FileName.endsWith(".jpeg") || FileName.endsWith(".JPG") || FileName.endsWith(".JPEG"))
            return "image/jpeg";
        if (FileName.endsWith(".gif")) return "image/gif";
        if (FileName.endsWith(".ico")) return "image/ico";
        return "application/octet-stream";
	}

	private void getRequest(File fileToSend) {
        System.out.println("Requested to send file: " + fileToSend.getAbsolutePath() + " to " + ReqIP);
        conLogger.info("Requested to send file: " + fileToSend.getAbsolutePath() + " to " + ReqIP);
        try {
			if (fileToSend.exists()) {
                System.out.println("Sending file: " + fileToSend.getAbsolutePath() + " to " + ReqIP);
                conLogger.info("Sending file: " + fileToSend.getAbsolutePath() + " to " + ReqIP);
                String content = contentType(new ServerConfigReader().getFileName());

				ou.writeBytes("HTTP/1.1 200 OK" + NextLine);
				ou.writeBytes("Server: MyServer" + NextLine);
				ou.writeBytes("Connection: close");
				ou.writeBytes("Content-Type: " + content + NextLine);
				ou.writeBytes(NextLine);
				sendBytes(fileToSend);
				conLogger.info("file was completely send");
			}
			if (!(fileToSend.exists())) {
                System.out.println("Requested file " + fileToSend.getAbsolutePath() + " not found, throwing 404");
                conLogger.log(Level.SEVERE, "Requested file " + fileToSend.getAbsolutePath() + " not found, throwing 404");
                w.println("HTTP/1.1 404 Not Found");
				w.println("Server: MyServer");
				w.println("Connection: close");
				w.println("Content-Type: text/html");
				w.println("");
				w.println("<html> <head> <title> Error 404: Site not found </title> </head> <body bgcolor=red> Error 404: Site not found </body> </html>");
                conLogger.info("Connection to " + ReqIP + " closed");
                w.close();
			}
		} catch (IOException e) {
			conLogger.log(Level.SEVERE, "Exception in getRequest void", e);
			e.printStackTrace();
		}
	}
}