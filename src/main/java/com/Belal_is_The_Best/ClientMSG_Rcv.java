package com.Belal_is_The_Best;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ClientMSG_Rcv  implements Runnable {
	private final Socket socket;




	/** Client-side constructor */
	public ClientMSG_Rcv(Socket socket) {
		this.socket = socket;
		
		try {
			// Disable Nagle’s algorithm for low-latency chat
			this.socket.setTcpNoDelay(true);
		} catch (SocketException e) {
			System.err.println("Could not disable Nagle: " + e.getMessage());
		}
	}

	@Override
	public void run() {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String line;
//			while((line = in.readLine()) != null)
//			if(line!=null&&line.equals("+--+")) {
//				System.out.println(" the line is here "+line); // " the line is here +--+
//				break;
//			}
				
				while ((line = in.readLine()) != null) {
					// skip spurious blank lines
					if (line.isEmpty())
						continue; 
//					System.out.println("Boolean "+ServerSender.rw);
//					System.out.println(" i am client MSG RCV take it "+line);
					if (line.contains("MSG ")) {

						System.out.println("<- Peer: " + line.substring(4));
//						System.out.println("   The rcv Port is "+socket.getRemoteSocketAddress());

					}  else if (line.equalsIgnoreCase("EXIT")) {
						// peer closed chat
						return;
					}
				}
			
		} catch (IOException e) {
			System.err.println("IOException in ClientMSG_Rcv: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Clean up
			try {
				if (in != null)
					in.close();
				socket.close();
			} catch (IOException ignored) {
			}
		}
	}
}