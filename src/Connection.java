import ServerConfig.ServerConfigReader;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class Connection extends Thread {

	private final static String bla="\r\n";
    private final static Logger conLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private BufferedReader r;
    private PrintWriter w;
    private String body;
	private File file;


	Connection(Socket ss){

        //get properties value
        try {
            file = new ServerConfigReader().getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        conLogger.info("Connections established on port: " + ss.getLocalPort());

		try {
			r=new BufferedReader(new InputStreamReader(ss.getInputStream()));
			w=new PrintWriter(new OutputStreamWriter(ss.getOutputStream()));
			DataOutputStream ou = new DataOutputStream(ss.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

    private static void sendBytes(FileInputStream fis, OutputStream ou) {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        try {
            while ((bytes = fis.read(buffer)) != -1) {
                ou.write(buffer, 0, bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void run(){
		String line;
		FileInputStream fis=null;
		System.out.println(file.getAbsolutePath());

		synchronized (conLogger) {

			conLogger.info("Connection established");

			try {
				while ((line = r.readLine()) != null) {


					if (line.startsWith("GET")) {

                        conLogger.info("Path: " + file.getPath());


                        if (new ServerConfigReader().getFileName().equals(null)) {
                            conLogger.warning("File not found, throwing 404");
                            System.out.println("404");
							w.println("HTTP/1.1 404 Not Found");
							w.println("Server: MyServer");
							w.println("Connection: close");
							w.println("Content-Type: text/html");
							w.println("");
							w.println("<html> <head> <title> bla </title> </head> <body bgcolor=red> hjshdf </body> </html>");
							w.close();
							r.close();
							//ss.close();
							conLogger.info("Connection closed");
						}
                        if (new ServerConfigReader().getFileName().equals("info.html")) {
                            conLogger.info("Sending website in html format");
                            String content = contentType(new ServerConfigReader().getFileName());
                            Lesen();
                            long bytes = file.length();


							bytes = file.length();
							w.println("HTTP/1.1 200 OK");
							w.println("Server: MyServer");
							w.println("Connection: close");
							//w.println("Content-Length: "+bytes);
							w.println("Content-Type: " + content);
							w.println("");
							w.println(body);
							w.close();
							conLogger.info("Connection closed");


						}
					}
					System.out.println("Server: " + line);
				}
				r.close();

			} catch (IOException e) {
				conLogger.warning("File not found, exception, showing 404");
				w.println("HTTP/1.1 404 Not Found");
				w.println("Server: MyServer");
				w.println("Connection: close");
				w.println("Content-Type: text/html");
				w.println("");
				w.println("<html> <head> <title> bla </title> </head> <body bgcolor=red> hjshdf </body> </html>");
				w.close();
			}
		}
	}

	private String contentType(String FileName) {
		// TODO Auto-generated method stub
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

	private void Lesen() {
		String x;
		FileReader fr;
		try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            while ((x=br.readLine())!=null){
				body=body+x+bla;
			}


		}catch(IOException e){

			w.println("HTTP/1.1 404 Not Found");
			w.println("Server: MyServer");
			w.println("Connection: close");
			w.println("Content-Type: text/plain");
			w.println("");
			w.println("nix da");
			w.close();

		}
	}
}