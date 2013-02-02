package com.digitalxyncing.client;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class DatagramClient {

    private static final int PORT = 1337;

    private String destination;

    public DatagramClient(String destination) {
        this.destination = destination;
    }

    public void sendData() {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(destination);
            System.out.println("Attemping to connect to " + address + ") via UDP port 9876");

            byte[] sendData = new byte[1024];
            int val = 0;
            sendData = ByteBuffer.allocate(4).putInt(val).array();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, PORT);
            while (true) {
                clientSocket.send(sendPacket);
                Thread.sleep(1);
                sendPacket.setData(ByteBuffer.allocate(4).putInt(++val).array());
            }
        } catch (SocketException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void listen() {
        long lastResponse = 0;
        long packetsReceived = 0;
        long totalTime = 0;
        long lateResponseCount = 0;
        int  packetSummaryCount = 1000;
        int  lastPacketId = 0;

        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT);
            byte[] receiveData = new byte[1024];
            while (true) {
                receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                //blocking!
                serverSocket.receive(receivePacket);
                packetsReceived++;

                int expectedId = lastPacketId+1;

                ByteBuffer wrapped = ByteBuffer.wrap(receivePacket.getData());
                lastPacketId = wrapped.getInt();

                if(expectedId != lastPacketId){
                    System.out.println("Yikes - expecting: " + expectedId + " but received " + lastPacketId);
                }


                if(lastResponse != 0){
                    long delta = System.currentTimeMillis() - lastResponse;
                    totalTime += delta;
//                    System.out.print(".");

                    if(delta > 10){
                        lateResponseCount++;
                    }

                    if(packetsReceived % packetSummaryCount == 0) {
                        System.out.println("Average Time: " + (totalTime/packetsReceived) + "ms");
                        System.out.println("Late Packets: " + lateResponseCount);

                        totalTime = 0;
                        packetsReceived = 0;
                        lateResponseCount = 0;

                    }
                }
                lastResponse = System.currentTimeMillis();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public static void main(String args[]) throws Exception
//    {
//        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//        DatagramSocket clientSocket = new DatagramSocket();
//        InetAddress IPAddress = InetAddress.getByName("192.168.0.6");
//        byte[] sendData = new byte[1024];
//        byte[] receiveData = new byte[1024];
//        String sentence = inFromUser.readLine();
//        sendData = sentence.getBytes();
//
//        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 1337);
//        clientSocket.send(sendPacket);
//        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//        clientSocket.receive(receivePacket);
//        String modifiedSentence = new String(receivePacket.getData());
//        System.out.println("FROM SERVER:" + modifiedSentence);
//        clientSocket.close();
//    }
}
