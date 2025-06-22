package com.Belal_is_The_Best;


import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Optional;

import org.apache.hadoop.conf.Configuration;

public class ServerMain {
    private static final int PORT = 6666;

    public static void main(String[] args) throws Exception {
        Configuration hadoopConf = new Configuration();
        hadoopConf.set("fs.defaultFS", "file:///");
        DirectoryService dir = new DirectoryService(hadoopConf);

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Directory broker listening on port " + PORT);

        while (true) {
            Socket clientSock = serverSocket.accept();
            new Thread(new ClientHandler(clientSock, dir)).start();
        }
    }


}