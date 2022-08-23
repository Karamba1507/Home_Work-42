package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private final int port;
    private final String host;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public Client(int port, String host) {
        this.port = port;
        this.host = host;
    }

    private void connect() {
        try (Socket socket = new Socket(host, port)) {
            send(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(Socket socket) {
        try (OutputStream outputStream = socket.getOutputStream()) {
            Scanner scn = new Scanner(System.in);
            pool.submit(() -> getMsg(socket));

            while (true) {
                String msg = scn.nextLine();
                PrintWriter pw = new PrintWriter(outputStream);
                pw.write(msg);
                pw.write(System.lineSeparator());
                pw.flush();
                if (msg.equalsIgnoreCase("Quit")) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    private void getMsg(Socket socket) {
        InputStream input = null;
        try {
            input = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            InputStreamReader isr = new InputStreamReader(input);
            Scanner scn = new Scanner(isr);
            String msg = scn.nextLine();
            System.out.println(msg);
        }
    }
}
