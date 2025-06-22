package com.Belal_is_The_Best;

import java.net.*;
import java.io.*;


public class ServerRcv implements Runnable {
	private final Socket socket;
	private final String clientID;
	private final int chatPort;



	/** Client-side constructor */
	public ServerRcv(Socket socket,  String clientID, int chatPort) {
		this.socket = socket;

		this.clientID = clientID;
		this.chatPort = chatPort;
		try {
			// Disable Nagle’s algorithm for low-latency chat
			this.socket.setTcpNoDelay(true);
		} catch (SocketException e) {
			System.err.println("Could not disable Nagle: " + e.getMessage());
		}
	}

	@Override
	public void run() {
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String line;

			
				while (true) {
					if(in==null)
						continue;
					if((line = in.readLine()) == null)
						continue;
					
//					System.out.println(" i am ServerRcv take it "+line);
					// skip spurious blank lines
					if (line.isEmpty())
						continue;
					// ─── Client (peer) logic ────────────────────────────
					if (line.startsWith("ACTIVE ")) {
						System.out.println("Active clients: " + line.substring(7));

					} else if (line.startsWith("IPPORT ")) {// IP  Port
						// Format: IPPORT <id> <ip> <port>

						System.out.println("the line come from IPPORT is : "+line);

						String[] p = line.split("\\s+");
						if (p.length == 4) {
							String target = p[1], ip = p[2];
							int port = Integer.parseInt(p[3]);
							
							Socket peer = new Socket(ip, port);
							peer.setTcpNoDelay(true);
							System.out.println("Chat connected to " + target);

							Thread peerRcv = new Thread(new ClientMSG_Rcv(peer));
							peerRcv.setPriority(Thread.MAX_PRIORITY);
							peerRcv.start();

							Thread peerSending = new Thread(new MSG_Send(peer));
							peerSending.setPriority(Thread.MAX_PRIORITY);
							peerSending.start();
						}

					}  else if (line.equalsIgnoreCase("EXIT")) {
						return;
					}
				}
			
		} catch (IOException e) {
			System.err.println("IOException in ServerRcv: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Clean up
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				socket.close();
			} catch (IOException ignored) {
			}
		}
	}
}
