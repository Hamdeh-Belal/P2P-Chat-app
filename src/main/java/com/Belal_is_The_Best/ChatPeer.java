package com.Belal_is_The_Best;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatPeer implements Runnable {
	private int chatPort;


	public ChatPeer(int chatPort) {
		super();
		this.chatPort = chatPort;
	}

	@Override
	public void run() {
		try (ServerSocket chatListener = new ServerSocket(chatPort)) {
			System.out.println("Chat listener on port " + chatPort);

			while (true) {

				Socket peer = chatListener.accept();
				peer.setTcpNoDelay(true);
				System.out.println("Peer connected: " + peer.getRemoteSocketAddress());
				
				Thread rcvThread = new Thread(new ClientMSG_Rcv(peer));
				rcvThread.setPriority(Thread.MAX_PRIORITY);
				rcvThread.start();
				
				Thread sendingThread = new Thread(new MSG_Send(peer));
				sendingThread.setPriority(Thread.MAX_PRIORITY);
				sendingThread.start();
				
				

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
