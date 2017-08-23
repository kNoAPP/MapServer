package com.kNoAPP.MS;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MapConnection implements Runnable {

	private Socket s;
	private int id;
	private Scanner in;
	private PrintWriter out;
	
	public MapConnection(Socket s) {
		this.s = s;
		id = 0;
		
		while(getMC(getClient()) != null) id++;
		
		MapServer.connections.add(this);
		System.out.println("Connected to " + getClient());
	}
	
	public void run() {
		String client = getClient();
		try {
			in = new Scanner(s.getInputStream());
			out = new PrintWriter(s.getOutputStream(), true);
			
			out(client);
			for(MapConnection mc : MapServer.connections) if(mc != this) out(mc.getClient() + " add");
			while(valid()) {
				in(in.nextLine());
			}
		} catch(Exception ex) {}
		close();
	}
	
	public String getClient() {
		return s.getInetAddress().toString() + "/" + id;
	}
	
	public void out(String s) {
		if(out != null) {
			out.println(s);
			if(out.checkError()) close();
		}
	}
	
	private void in(String s) {
		String client = getClient();
		if(client.equals("/172.79.103.233/0") && s.equals("end")) MapServer.end();
		else for(MapConnection mc : MapServer.connections) {
			if(mc != this) {
				System.out.println("Sending \"" + s + "\" to " + mc.getClient() + " from " + getClient());
				mc.out(client + " " + s);
			}
		}
	}
	
	public void close() {
		System.out.println("Connection to " + getClient() + " lost!");
		MapServer.connections.remove(this);
		try {
			s.close();
		} catch (IOException ex) {}
		in("remove");
	}
	
	public boolean valid() {
		return MapServer.connections.contains(this);
	}
	
	public static MapConnection getMC(String client) {
		for(MapConnection mc : MapServer.connections) {
			if(mc.getClient().equals(client)) return mc;
		}
		return null;
	}
}
