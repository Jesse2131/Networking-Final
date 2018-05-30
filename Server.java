import java.io.*;
import java.net.*;

public class Server{
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		Manager m = new Manager();
		int portNumber = 4444;
		int referenceNum = 0;
		try{
			ServerSocket serverSocket = new ServerSocket(portNumber);
			while(true){
				System.out.println("Waiting for Connection");
				Socket clientSocket = serverSocket.accept();
				referenceNum++;
				ServerThread st = new ServerThread(clientSocket,m,referenceNum);
				Thread thread = new Thread(st);
				thread.start();
				m.addThread(st);
			}
		} 
		catch (IOException ex){
			System.out.println("Server: Error listening for a connection");
			System.out.println(ex.getMessage());
		} 
	}
}
