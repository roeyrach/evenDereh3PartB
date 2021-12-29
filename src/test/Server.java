package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {


	public interface ClientHandler{
		// define...
		void handleClient(InputStream inFromClient, OutputStream outToClient);
		void close();
	}
	int clientLimit;
	volatile boolean stop;

	public Server() {
		stop=false;
	}
	
	
	private void startServer(int port, ClientHandler ch){
		// implement here the server...
			try{
				ServerSocket server = new ServerSocket(port);
				server.setSoTimeout(1000);
				while(!stop) {
					try {
						Socket aClient = server.accept(); // blocking call
						ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
						ch.close();
						aClient.close();
					} catch (SocketTimeoutException e) {}
				}
				server.close();
			}catch(IOException e){
				e.printStackTrace();
		}
	}
	
	// runs the server in its own thread
	public void start(int port, ClientHandler ch) {
		new Thread(()->startServer(port,ch)).start();
	}
	
	public void stop() {
		stop=true;
	}
}
