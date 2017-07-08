package com.kNoAPP.MS;

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
			
			while(!ss.isClosed()) {
				Socket s = ss.accept();
				Thread t = new Thread(new MapConnection(s));
				t.start();
				System.out.println("Connected to " + s.getInetAddress().toString());
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
}
