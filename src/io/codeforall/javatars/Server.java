package io.codeforall.javatars;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Server {
    public static void main(String[] args) {

        String hostName = null;
        int portNumber = 8080;
        try {
            hostName = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Local Server running at IP: " + hostName + ":" + portNumber);
        InetAddress serverIP;
        try {
            serverIP = InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        InetAddress[] clients = new InetAddress[20];
        int[] clientsPorts = new int[20];

        while(true) {

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
            String newText = receivePacket.getAddress().getHostAddress() + ": " + text;

            for(int i = 0; i < 20; i++){
                if(clients[i] == null){
                    clients[i] = receivePacket.getAddress();
                    clientsPorts[i] = receivePacket.getPort();
                }
            }


            //Send
            sendBuffer = newText.getBytes();
            DatagramPacket sendPacket = null;
            for (int i = 0; i < 20; i++){
                if(clients[i] != null){
                    sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clients[i], clientsPorts[i]);
                }
            }

            try {
                socket.send(sendPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            socket.close();
        }

    }
}
