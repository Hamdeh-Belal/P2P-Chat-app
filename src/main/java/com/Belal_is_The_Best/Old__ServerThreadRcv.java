package com.Belal_is_The_Best;

import java.net.*;
import java.io.*;
import java.util.List;

public class Old__ServerThreadRcv implements Runnable {
	private final Socket socket;

	

	
	public Old__ServerThreadRcv(Socket socket) {
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
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String line;
				while ((line = in.readLine()) != null) {
					// skip spurious blank lines
					if (line.isEmpty())
						continue;
					// ─── Directory logic ────────────────────────────────
					String[] parts = line.split("\\s+");
					switch (parts[0].toUpperCase()) {
					case "ALIVE":
						if (parts.length == 3) {
							String id = parts[1];
							int port = Integer.parseInt(parts[2]);
							Old___ServerMain.recordClient(id, socket.getInetAddress(), port);
							System.out.println(
									"<- Received ALIVE from " + id + " @ " + socket.getInetAddress() + ":" + port);
							System.out.println(" the connection from " + socket.getRemoteSocketAddress());
						}
						break;

					case "TAB":
						out.println("TABLE");
						List<String> active = Old___ServerMain.getActiveClients();
						String msg = "ACTIVE " + String.join(",", active);
						out.println(msg);
						System.out.println("-> Sent to " + socket.getRemoteSocketAddress() + ": " + msg);
						break;

					case "GET":
						if (parts.length == 2) {
							String target = parts[1];
							InetSocketAddress info = Old___ServerMain.getClientAddress(target);
							if (info != null) {
								String resp = "IPPORT " + target + " " + info.getAddress().getHostAddress() + " "
										+ info.getPort();
								out.println(resp);
								System.out.println("→ Replied to GET " + target + " with [" + resp + "]");
							} else {
								out.println("ERROR UnknownClient " + target);
								System.out.println("→ GET " + target + " — unknown");
							}
						}
						break;

					case "EXIT":
						out.println("EXIT");
						// fall through to close
						return;

					default:
						// ignore any other directory commands
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
