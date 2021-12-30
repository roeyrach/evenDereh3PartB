package test;


import test.Commands.DefaultIO;
import test.Server.ClientHandler;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class AnomalyDetectionHandler implements ClientHandler{
	SocketIO sio;
	@Override
	public void handleClient(Socket client) {
		this.sio = new SocketIO(client);
		CLI cli = new CLI(sio);
		cli.start();
//		try {
			this.sio.close();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
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
		public String readText() {
			String txt = new String();
			try{
				txt = in.readLine();
			}catch (IOException e){
				e.printStackTrace();
			}
			return txt;
		}

		@Override
		public void write(String text) {
			out.write(text);
		}

		@Override
		public float readVal() {
			float val = -1;
			try{
				String line =  in.readLine();
				val = Float.parseFloat(line);
			}catch(IOException e){
				e.printStackTrace();
			}
			return val;
		}

		@Override
		public void write(float val) {
			out.print(val);
		}

		void close() {
//			in.close();
			out.close();
		}
	}


}
