import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class Connection extends Thread {

	private BufferedReader r;
	final static String bla="\r\n";
	private PrintWriter w;
	private DataOutputStream ou;
	private Socket ss;
	private long bytes;
	private String body;
	
	private String FileName="info.html";
	private String DirName="Doc";
	private File file;

	private final static Logger conLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


	Connection(Socket ss){



		file = new File(DirName+File.separator+FileName);
		try {
			r=new BufferedReader(new InputStreamReader(ss.getInputStream()));
			w=new PrintWriter(new OutputStreamWriter(ss.getOutputStream()));
			ou= new DataOutputStream(ss.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		//startLogging();

	}

	public void run(){
		String line;
		FileInputStream fis=null;
		System.out.println(file.getAbsolutePath());

		conLogger.info("Connection established");

		try {
			while((line=r.readLine())!=null){


				if(line.startsWith("GET")){
					/*StringTokenizer token=new StringTokenizer(line);
					FileName=token.nextToken();
					FileName=token.nextToken();
					FileName=FileName.substring(1);*/
					conLogger.info("Path: "+FileName);




					if(FileName.equals(null)){
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
					if(FileName.equals("info.html")){
					    conLogger.info("Sending website in html format");
						String content = contentType(FileName);
						Lesen();
						bytes=file.length();


						bytes=file.length();
						w.println("HTTP/1.1 200 OK");
						w.println("Server: MyServer");
						w.println("Connection: close");
						//w.println("Content-Length: "+bytes);
						w.println("Content-Type: "+content);
						w.println("");
						w.println(body);
						w.close();
						conLogger.info("Connection closed");


					}}
				System.out.println("Server: "+line);
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

	private static void sendBytes(FileInputStream fis, OutputStream ou){
		byte[] buffer = new byte[1024];
		int bytes=0;
		try {
			while((bytes=fis.read(buffer))!= -1){
				ou.write(buffer,0,bytes);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void Lesen() {
		String x;
		FileReader fr;
		try {
			fr = new FileReader(DirName+File.separator+FileName);
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