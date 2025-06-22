package com.Belal_is_The_Best;

import javax.sound.midi.SysexMessage;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

    public class DirectoryService {
        private final FileSystem fs;
        private final Path registry = new Path("clients.txt");

        public DirectoryService(Configuration conf) throws IOException {
            fs = FileSystem.get(conf);
            if (!fs.exists(registry.getParent())) {
                fs.mkdirs(registry.getParent());
            }
            if (!fs.exists(registry)) {
                try (FSDataOutputStream out = fs.create(registry, true)) {
                    System.out.println();
                }
            }
        }

        public synchronized void recordClient(String clientID, String ip, int port) throws IOException {
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(registry)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith(clientID + ",")) {
                        lines.add(line);
                    }
                }
            }
            lines.add(clientID + "," + ip + "," + port);

            Path tmp = new Path(registry.getParent(), "clients.txt.tmp");
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fs.create(tmp, true)))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            }
            fs.delete(registry, false);
            fs.rename(tmp, registry);
        }

        public List<String> getActiveClients() throws IOException {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(registry)))) {
                return br.lines()
                        .map(l -> l.split(",", 2)[0])
                        .collect(Collectors.toList());
            }
        }

        public Optional<InetSocketAddress> getClientAddress(String clientID) throws IOException {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(registry)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",", 3);
                    if (parts[0].equals(clientID)) {
                        return Optional.of(
                                new InetSocketAddress(parts[1], Integer.parseInt(parts[2]))
                        );
                    }
                }
            }
            return Optional.empty();
        }
    }

