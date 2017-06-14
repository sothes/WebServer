import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Connection extends Thread {

	private BufferedReader r;
	final static String bla="\r\n";
	private PrintWriter w;
	private DataOutputStream ou;
	private Socket ss;
	private long bytes;
	private String body;

	Logger logCon;



	Connection(Socket ss){
		logCon=Logger.getLogger(Connection.class.getName());

		logCon.setLevel(Level.ALL);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logCon.addHandler(handler);
		handler.setFormatter(new SimpleFormatter());
		logCon.fine("hello world");

		try {
			r=new BufferedReader(new InputStreamReader(ss.getInputStream()));
			w=new PrintWriter(new OutputStreamWriter(ss.getOutputStream()));
			ou= new DataOutputStream(ss.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run(){
		String line;
		FileInputStream fis=null;
		boolean fileExists =true;
		String fileName="";
		try {
			while((line=r.readLine())!=null){

				if(line.startsWith("GET")){
					StringTokenizer token=new StringTokenizer(line);
					fileName=token.nextToken();
					fileName=token.nextToken();
					fileName=fileName.substring(1);
					System.out.println(fileName);




					if(fileName.equals("")){
						System.out.println("jsndf");
						w.println("HTTP/1.1 404 Not Found");
						w.println("Server: MyServer");
						w.println("Connection: close");
						w.println("Content-Type: text/html");
						w.println("");
						w.println("<html> <head> <title> bla </title> </head> <body bgcolor=red> hjshdf </body> </html>");
						w.close();
						r.close();
						//ss.close();
					}
					if(fileName!=""){
						String content=contentType(fileName);
						Lesen(fileName);
						bytes=gross(fileName);


						bytes=gross(fileName);
						w.println("HTTP/1.1 200 OK");
						w.println("Server: MyServer");
						w.println("Connection: close");
						//w.println("Content-Length: "+bytes);
						w.println("Content-Type: "+content);
						w.println("");
						w.println(body);
						w.close();


					}}
				System.out.println("Server: "+line);

			}
			r.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			w.println("HTTP/1.1 404 Not Found");
			w.println("Server: MyServer");
			w.println("Connection: close");
			w.println("Content-Type: text/html");
			w.println("");
			w.println("<html> <head> <title> bla </title> </head> <body bgcolor=red> hjshdf </body> </html>");
			w.close();


		}
	}

	public String contentType(String fileName) {
		// TODO Auto-generated method stub
		if(fileName.endsWith(".htm") || fileName.endsWith(".html")){

			return "text/html";
		}
		if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")){
			return "image/jpeg";

		}
		if(fileName.endsWith(".gif")){
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


	public String Lesen(String f) {
		String datei="";
		String x;
		FileReader fr;
		try {
			fr = new FileReader("Doc/"+f);
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
		return datei;


	}

	public long gross(String f){
		File file=new File(f);
		return file.length();
	}







}
