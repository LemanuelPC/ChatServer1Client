package io.codeforall.javatars;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverName = args[0];
        int serverPortNumber = Integer.parseInt(args[1]);

        String text = getMessage();

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(8000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        byte[] sendBuffer = text.getBytes();

        DatagramPacket sendPacket = null;
        try {
            sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(serverName), serverPortNumber);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        try {
            socket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] recvBuffer = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(recvBuffer, recvBuffer.length);
        try {
            socket.receive(receivePacket); // blocks while packet not received
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        socket.close();

    }

    private static String getMessage() {

        Scanner reader = new Scanner(System.in);
        System.out.println("Message to be sent: ");
        return reader.nextLine();

    }
}
