package com.Belal_is_The_Best;


import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

import org.apache.hadoop.conf.Configuration;

public class ClientMain {
    public static final String DIR_HOST = "localhost";
    public static final int DIR_PORT = 6666;


    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println("Usage: java ClientMain <clientID> <chatPort>");
            return;
        }
        String clientID = args[0];
        int chatPort = Integer.parseInt(args[1]);

        // 1) Connect to Directory
        Socket dirSocket = new Socket(DIR_HOST, DIR_PORT);
        System.out.println("Connected to Directory at " + DIR_HOST + ":" + DIR_PORT);
        dirSocket.setTcpNoDelay(true);
        // 2)  I/O threads to the Directory socket


        Thread active = new Thread(new ActiveThread(dirSocket,clientID,chatPort));
        active.start();

        Thread serverSend = new Thread(new ServerSender(dirSocket, clientID, chatPort));// this thread to send to Server
        serverSend.setPriority(Thread.MAX_PRIORITY);
        serverSend.start();

        Thread serverRcv = new Thread(new ServerRcv(dirSocket, clientID, chatPort));// this thread to rcv to Server

        serverRcv.setPriority(Thread.MAX_PRIORITY);
        serverRcv.start();

        // 3) Listen for incoming peer‐chat connections
        Thread chat = new Thread(new ChatPeer(chatPort));
        chat.setPriority(Thread.MAX_PRIORITY);
        chat.start();

    }
}

