package com.company;

import java.net.Socket;
import java.util.*;

public class NameUser {
    private final static List<String> userName = new ArrayList<>();
    private final static Map<Socket, String> sockets = new HashMap<>();
    private final static Random rnd = new Random();

    NameUser() {
        fillNames();
    }

    private void fillNames() {
        userName.add("Ben");
        userName.add("Jack");
        userName.add("Sofia");
        userName.add("Mark");
        userName.add("Tom");
        userName.add("Kira");
        userName.add("John");
        userName.add("Max");
        userName.add("Peter");
        userName.add("Michael");
        userName.add("Lucy");
    }

    static void addUser(Socket socket) {
        int idx = rnd.nextInt(userName.size());
        sockets.put(socket, userName.get(idx));
        userName.remove(idx);
    }

    public static String getName(Socket socket) {
        return sockets.get(socket);
    }

    public static Map<Socket, String> getSockets() {
        return sockets;
    }
}
