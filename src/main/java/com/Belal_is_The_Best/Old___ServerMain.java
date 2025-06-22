package com.Belal_is_The_Best;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Old___ServerMain {
	public static final int DIR_PORT = 6666;
	// clientID -> (IP, port)
	static ConcurrentMap<String, InetSocketAddress> record = new ConcurrentHashMap<>();

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(DIR_PORT);
		System.out.println("Directory Server listening on port " + DIR_PORT); // :contentReference[oaicite:0]{index=0}

		while (true) {
			Socket clientSocket = serverSocket.accept();
			 System.out.println("→ Accepted connection from " 
		                + clientSocket.getRemoteSocketAddress());
			new Thread(new Old__ServerThreadRcv(clientSocket)).start();
			
			
		}
	}

	// Called by ServerRcv when an ALIVE ping arrives
	public static void recordClient(String clientID, InetAddress addr, int port) {
		record.put(clientID, new InetSocketAddress(addr, port));
	}

	// Used by ServerSender to build the ACTIVE list
	public static List<String> getActiveClients() {
		return new ArrayList<>(record.keySet());
	}

	// Called by ServerRcv on GET requests
	public static InetSocketAddress getClientAddress(String clientID) {
		return record.get(clientID);
	}
}
