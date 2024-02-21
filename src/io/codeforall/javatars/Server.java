package io.codeforall.javatars;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Server {
    public static void main(String[] args) {

        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Local Server running at IP: " + hostName);
        InetAddress serverIP;
        try {
            serverIP = InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        int portNumber = 8080;

        byte[] sendBuffer;
        byte[] recvBuffer = new byte[1024];

        //Receive

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(portNumber, serverIP);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(socket.getLocalAddress().getHostAddress());

        DatagramPacket receivePacket = new DatagramPacket(recvBuffer, recvBuffer.length);
        try {
            socket.receive(receivePacket); // blocks while packet not received
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Convert Data
        String text = new String(receivePacket.getData());
        text = text.toUpperCase();


        //Send
        sendBuffer = text.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, receivePacket.getAddress(), receivePacket.getPort());
        try {
            socket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        socket.close();

    }
}
