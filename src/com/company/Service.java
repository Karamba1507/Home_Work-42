package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

class Service {

    static void handle(Socket socket) {

        System.out.printf("Connected client <%s> by socket %d %n", NameUser.getName(socket), socket.getPort());
        PrintWriter helloMsg = null;
        try {
            helloMsg = getWriter(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert helloMsg != null;
        helloMsg.write("Hello " + NameUser.getName(socket));
        helloMsg.write(System.lineSeparator());
        helloMsg.flush();

        InputStream input = null;
        try {
            input = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader reader = new InputStreamReader(input);
        Scanner scanner = new Scanner(reader);
        while (true) {
            String msg = scanner.nextLine();
            System.out.println(NameUser.getName(socket) + ": " + msg);
            try {
                sendToClients(socket, msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream stream = socket.getOutputStream();
        return new PrintWriter(stream);
    }


    private static void sendToClients(Socket socket, String msg) throws IOException {
        msg = String.format("<%s>%s", NameUser.getName(socket), msg);
        for (Map.Entry<Socket, String> map : NameUser.getSockets().entrySet()) {
            if (map.getKey().getPort() != socket.getPort()) {
                sendToOne(map.getKey(), msg);
            }
        }
    }

    private static void sendToOne(Socket socket, String msg) throws IOException {
        PrintWriter pw = getWriter(socket);
        pw.write(msg);
        pw.write(System.lineSeparator());
        pw.flush();
    }
}
