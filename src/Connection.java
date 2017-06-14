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



	Connection(Socket ss){
		file = new File(DirName+File.separator+FileName);
		try {
			r=new BufferedReader(new InputStreamReader(ss.getInputStream()));
			w=new PrintWriter(new OutputStreamWriter(ss.getOutputStream()));
			ou= new DataOutputStream(ss.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run(){
		String line;
		FileInputStream fis=null;
		System.out.println(file.getAbsolutePath());

		try {
			while((line=r.readLine())!=null){


				if(line.startsWith("GET")){
					/*StringTokenizer token=new StringTokenizer(line);
					FileName=token.nextToken();
					FileName=token.nextToken();
					FileName=FileName.substring(1);*/
					System.out.println(FileName);




					if(FileName.equals(null)){
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
					if(FileName.equals("info.html")){
						String content=contentType(FileName);
						Lesen(file);
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

	public String contentType(String FileName) {
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


	public void Lesen(File f) {
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