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
        int currentClients = 0;

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

            boolean doesClientExistAlready = false;
            System.out.println("receiving packet address: " + receivePacket.getAddress());
            if(currentClients == 0){
                System.out.println("New client[" + currentClients + "]: " + receivePacket.getAddress());
                clients[currentClients] = receivePacket.getAddress();
                clientsPorts[currentClients] = receivePacket.getPort();
                currentClients++;
            }
            for(int i = 0; i < currentClients; i++){
                if(clients[i].getHostAddress().equals(receivePacket.getAddress().getHostAddress())){
                    break;
                }
                System.out.println("New client[" + currentClients + "]: " + receivePacket.getAddress());
                clients[currentClients] = receivePacket.getAddress();
                clientsPorts[currentClients] = receivePacket.getPort();
                currentClients++;
            }
            socket.close();

            //Send
            sendBuffer = newText.getBytes();
            DatagramPacket sendPacket = null;
            for (int i = 0; i < currentClients; i++){
                try {
                    if(!clients[i].getHostAddress().equals(InetAddress.getLocalHost().getHostAddress())){
                        socket = null;
                        try {
                            socket = new DatagramSocket(portNumber, serverIP);
                        } catch (SocketException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(clients[i]);
                        System.out.println(clientsPorts[i]);
                        sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clients[i], clientsPorts[i]);
                        socket.send(sendPacket);
                        socket.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
