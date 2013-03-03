package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ZmqEndpointFactory implements EndpointFactory {

    @Override
    public <T> Endpoint<T> buildClientEndpoint(String hostAddress, int hostPort, String token, Class<T> type,
                                               MessageHandlerFactory<T> messageHandlerFactory) {
        int hostSharePort = 0;
        int port = 0;
        Socket socket = null;
        DataInputStream inputStream = null;
        DataOutputStream outputStream = null;
        try {
            System.out.println("Requesting a connection to " + hostAddress);
            socket = new Socket(InetAddress.getByName(hostAddress), hostPort);
            port = socket.getLocalPort();
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(token);
            outputStream.flush();
            String response = inputStream.readUTF();
            if (response.startsWith("0")) {
                System.out.println("Request failed authentication");
                return null;
            }
            hostSharePort = Integer.valueOf(response.substring(response.indexOf(" ") + 1));
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (socket != null)
                    socket.close();
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ZmqClientEndpoint<T>(hostAddress, hostSharePort, port, messageHandlerFactory);
        }
    }

    @Override
    public <T> HostEndpoint<T> buildHostEndpoint(int port, MessageHandlerFactory<T> messageHandlerFactory) {
        return new ZmqHostEndpoint<T>(port, messageHandlerFactory);
    }

    @Override
    public <T> HostEndpoint<T> buildHostEndpoint(int port, MessageHandlerFactory messageHandlerFactory, Authenticator
            authenticator) {
        return new ZmqHostEndpoint<T>(port, messageHandlerFactory, authenticator);
    }
}
