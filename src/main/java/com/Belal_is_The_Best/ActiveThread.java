package com.Belal_is_The_Best;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ActiveThread  implements Runnable {
	private Socket socket;
	private String clientID;
	private int chatPort;
	
	
	public ActiveThread(Socket socket) {
		this(socket, null, -1);
	}

	// Client‐side constructor
	public ActiveThread(Socket socket, String clientID, int chatPort) {
		this.socket = socket;

		this.clientID = clientID;
		this.chatPort = chatPort;
		try {
			// low-latency
			this.socket.setTcpNoDelay(true);
		} catch (SocketException ignore) {
		}
	}
	
	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				
				out.println("ALIVE " + clientID + " " + chatPort);
				Thread.sleep(10000);
			}
		} catch (InterruptedException | IOException ignored) {
		}
		
	}

}
