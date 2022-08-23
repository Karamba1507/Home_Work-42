package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    private final int port;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final NameUser nameUser = new NameUser();

    private EchoServer(int port) {
        this.port = port;
    }

    static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    void run() {
        try (var server = new ServerSocket(port)) {
            System.out.println("Connection...");
            // обработка подключения
            while (!server.isClosed()) {
                Socket clientSocket = server.accept();
                NameUser.addUser(clientSocket);
                pool.submit(() -> Service.handle(clientSocket));
            }
        } catch (IOException e) {
            System.out.printf("Most likely port %s is busy.%n", port);
            e.printStackTrace();
        }
    }
}

