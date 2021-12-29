package test;


import test.Commands.DefaultIO;
import test.Server.ClientHandler;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class AnomalyDetectionHandler implements ClientHandler{

	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) {

	}

	@Override
	public void close() {

	}

	public class SocketIO implements DefaultIO{
		BufferedReader in;
		PrintWriter out;

		public SocketIO(Socket s){
			try{
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				out = new PrintWriter(s.getOutputStream(),true);
			}catch(SocketException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		@Override
		public String readText() throws IOException {
			return in.readLine();
		}

		@Override
		public void write(String text) {
			out.write(text);
		}

		@Override
		public float readVal() throws IOException {
			String line =  in.readLine();
			float val = Float.parseFloat(line);
			return val;
		}

		@Override
		public void write(float val) {
			out.print(val);
		}
	}


}
