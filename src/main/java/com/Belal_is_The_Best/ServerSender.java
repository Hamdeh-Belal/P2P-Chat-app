package com.Belal_is_The_Best;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ServerSender implements Runnable {
	private Socket socket;
	private String clientID;
	private int chatPort;
	static boolean rw=true;
	// Directory‐side constructor
	public ServerSender(Socket socket) {
		this(socket, null, -1);
	}

	// Client‐side constructor
	public ServerSender(Socket socket, String clientID, int chatPort) {
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
		try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);				
				Scanner input = new Scanner(System.in);
				) {
				

			out.println("ALIVE " + clientID + " " + chatPort);
			
			String lineofConsole="";
			while (true) {
				if(rw) {
				lineofConsole = input.nextLine();
//				System.out.println(" i am ServerSender i tack it "+lineofConsole);
				}
				else {
					lineofConsole="";
					continue;
				}
				if (!(lineofConsole).equals("")) {
					if (lineofConsole.equalsIgnoreCase("Tab")) {
						// send the new “TAB” command to the server
						out.println("TAB");

					} else if (lineofConsole.startsWith("chat ")) { 
						out.println("GET " + lineofConsole.substring(5).trim());
//						input.close();
						rw=false;

					} else if (lineofConsole.equalsIgnoreCase("exit")) {
						out.println("EXIT");
						out.close();
						input.close();
						socket.close();
					} 

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
