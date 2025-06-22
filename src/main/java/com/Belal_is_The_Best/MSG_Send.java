package com.Belal_is_The_Best;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class MSG_Send  implements Runnable {
	private Socket socket;
	private String clientID;
	private int chatPort;

	


	public MSG_Send(Socket socket) {
		this(socket, null, -1);
	}

	// Client‐side constructor
	public MSG_Send(Socket socket, String clientID, int chatPort) {
		this.socket = socket;
		ServerSender.rw=false;
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
			Scanner input = new Scanner(System.in);
			String lineofConsole="";
			out.println("");
			out.flush();
//			System.out.println(" +--+ send "+socket.getRemoteSocketAddress());
			while (true) {
				// your normal chat messages
				 lineofConsole = input.nextLine();
//				System.out.println("line #2 is "+lineofConsole.length());
				if(lineofConsole.length()==0)
					lineofConsole=".....";
				
				
				if(lineofConsole.equalsIgnoreCase("Exit")) {
					ServerSender.rw=true;
					break;
					
				}
				out.println("MSG " + lineofConsole);
				out.flush();
				System.out.println("-->" + lineofConsole);
//				System.out.println("   The send  Port is "+socket.getRemoteSocketAddress());
//				System.out.println("line #"+lineofConsole.length());
			}
		} catch (IOException ignored) {
			ignored.printStackTrace();
		}
		
	}

}

