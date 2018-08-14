package com.kNoAPP.MS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MapServer {
	
	public static List<MapConnection> connections = new ArrayList<MapConnection>();
	public static ServerSocket ss;

	public static void main(String args[]) {
		int port = 14555;
		
		try {
			System.out.println("MapServer 1.0");
			System.out.println("Listening on port " + port);
			ss = new ServerSocket(port);
			
			new Thread(new UserInput()).start();
			
			while(!ss.isClosed()) {
				Socket s = ss.accept();
				Thread t = new Thread(new MapConnection(s));
				t.start();
			}
		} catch(Exception ex) {
			System.out.println("Oops!");
		}
	}
	
	public static void end() {
		try {
			ss.close();
		} catch(Exception ex) {}
		System.out.println("Shutting down... Goodbye!");
		System.exit(0);
	}
	
	//TODO Finish this input thread after class.
	public static class UserInput implements Runnable {

		private BufferedReader in;
		
		public UserInput() {
			in = new BufferedReader(new InputStreamReader(System.in));
		}
		
		public void run() {
			while(true) {
				try {
					String input = in.readLine();
					if(input != null) {
						System.out.println(">> " + input);
						if(input.equalsIgnoreCase("end")) end();
					}
					Thread.sleep(500);
				} catch (Exception ex) {}
			}
		}
		
	}
}
