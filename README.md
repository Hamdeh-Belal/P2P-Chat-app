# Distributed P2P Chat with HDFS-Backed Directory Service

A hybrid peer-to-peer chat application that leverages Hadoop’s HDFS for registry storage and discovery. Clients register themselves in HDFS via a lightweight Directory Server and then communicate directly over TCP for low-latency messaging.

---

## Table of Contents

- [Overview](#overview)  
- [Features](#features)  
- [Prerequisites](#prerequisites)  
- [Installation & Build](#installation--build)  
- [Configuration](#configuration)   
- [Usage](#usage)  
- [Project Structure](#project-structure)  
- [Authors](#authors)  
- [License](#license)  

---

## Overview

This project implements a peer-to-peer (P2P) chat system with:

1. **Hybrid architecture**:  
   - **Directory Server** for bootstrapping and HDFS access.  
   - **HDFS-backed registry** (`clients.txt`) storing `<clientID, IP, port>`.  
   - **Direct TCP** client-to-client messaging.  

2. **Core components**:  
   - **ServerMain**: initializes Hadoop, opens port 6666, spawns `ClientHandler` threads.  
   - **DirectoryService**: encapsulates HDFS file operations (create/read/update registry).  
   - **ClientMain**: registers with server, spawns threads for “alive” messages, server I/O, and peer chat.  

3. **Concurrency**: multithreaded design for responsiveness (keep-alive, discovery, chat send/receive).

---

## Features

- **Dynamic peer discovery** via HDFS.  
- **Low-latency chat**: direct TCP connections between clients.  
- **Multithreading**: separate threads for heartbeats, server communication, and peer chats.  

---

## Prerequisites

- **Java 8+** (Corretto 1.8 tested)  
- **Maven** (for build)  
- **Hadoop 3.x** cluster (or standalone) with HDFS & YARN  
- UNIX-style shell (for Hadoop scripts)

---

## Installation & Build

1. **Clone the repo**  
   ```bash
   git https://github.com/Hamdeh-Belal/P2P-Chat-app.git
   cd P2P Chat app
    ```

2. **Build with Maven**
    
    ```bash
    mvn clean package
    ```
    
    This produces `target/distributed-chat-1.0.jar`.
    

---

## Configuration

1. **Hadoop configuration**
    
    - Place your `core-site.xml` & `hdfs-site.xml` in `$HADOOP_HOME/etc/hadoop`.
        
    - Ensure `fs.defaultFS` points to your NameNode (e.g., `hdfs://localhost:9000`).
        
2. **Registry path**
    
    - By default, the registry file is `clients.txt` in HDFS root.
        
    - You can override via Hadoop configs or change the `new Path("clients.txt")` in `DirectoryService`.
        

---
## Usage

1. **Automatic registration**
    
    - On startup, each client sends an `ALIVE <ClientID> <ChatPort>` to the Directory Server every few seconds.
        
2. **List active users**
    
    - Type `TAB` in your console to see all active `<ClientID>`s.
        
3. **Chat initiation**
    
    - Type `GET <TargetID>` to fetch `IPPORT <TargetID> <IP> <Port>`.
        
    - The client then opens a TCP connection to `<IP>:<Port>` for chat.
        
4. **Chat messaging**
    
    - Send: `MSG <your message>`
        
    - Receive: `MSG <their message>`
        

---

## Project Structure

```
.
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/yourpackage/
│   │   │   ├── ServerMain.java
│   │   │   ├── ClientMain.java
│   │   │   ├── ClientHandler.java
│   │   │   ├── DirectoryService.java
│   │   │   ├── ActiveThread.java
│   │   │   ├── ServerSender.java
│   │   │   ├── ServerRcv.java
│   │   │   └── ChatPeer.java
│   │   └── resources/
│   │       ├── core-site.xml
│   │       └── hdfs-site.xml
└── README.md
```

---

## Authors

- **Belal Hamdeh** (1210148)
    
- **Abedalkareem Injas** (1200507)
    

---

## License

Feel Free to use it.
