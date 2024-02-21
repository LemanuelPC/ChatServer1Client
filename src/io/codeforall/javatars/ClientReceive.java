package io.codeforall.javatars;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClientReceive {
    public static void main(String[] args) {
        String serverName = args[0];
        int serverPortNumber = Integer.parseInt(args[1]);

        while (true) {

            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket(8000);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }

            byte[] recvBuffer = new byte[1024];

            DatagramPacket receivePacket = new DatagramPacket(recvBuffer, recvBuffer.length);
            try {
                socket.receive(receivePacket); // blocks while packet not received
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println(new String(receivePacket.getData()));

            socket.close();

        }

    }
}
