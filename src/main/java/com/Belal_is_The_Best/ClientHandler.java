package com.Belal_is_The_Best;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class ClientHandler implements Runnable {
    private final Socket sock;
    private final DirectoryService dir;

    ClientHandler(Socket sock, DirectoryService dir) {
        this.sock = sock;
        this.dir = dir;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(sock.getInputStream()));
             PrintWriter out = new PrintWriter(
                     sock.getOutputStream(), true)) {

            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("ALIVE ")) {
                    // Format: ALIVE <clientID> <chatPort>
                    String[] parts = line.split("\\s+");
                    String clientID = parts[1];
                    String ip       = sock.getInetAddress().getHostAddress();
                    int    port     = Integer.parseInt(parts[2]);
                    dir.recordClient(clientID, ip, port);
                    System.out.println("<-- Active "+ ip+" : "+sock.getPort());

                } else if (line.equals("TAB")) {
                    List<String> ids = dir.getActiveClients();
                    String str=String.join(",", ids);
                    System.out.println("ACTIVE "+str);
                    out.println("ACTIVE "+str);

                } else if (line.startsWith("GET ")) {//GET Alice
                    String target = line.substring(4).trim();
                    Optional<InetSocketAddress> addr = dir.getClientAddress(target);
                    if (addr.isPresent()) {
                        InetSocketAddress a = addr.get();
                        String strIP="IPPORT "+target+" "+a.getAddress().getHostAddress() + " " + a.getPort();
                        System.out.println("--> "+strIP);
                        out.println(strIP);
                    } else {
                        out.println("NOT_FOUND");
                    }

                } else {
                    out.println("UNKNOWN_COMMAND");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}