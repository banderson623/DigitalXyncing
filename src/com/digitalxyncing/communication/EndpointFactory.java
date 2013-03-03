package com.digitalxyncing.communication;

public interface EndpointFactory {

    <T> Endpoint<T> buildClientEndpoint(String hostAddress, int hostPort, String token, Class<T> type,
                                        MessageHandlerFactory<T> messageHandlerFactory);

    <T> HostEndpoint<T> buildHostEndpoint(int port, MessageHandlerFactory<T> messageHandlerFactory);

    <T> HostEndpoint<T> buildHostEndpoint(int port, MessageHandlerFactory messageHandlerFactory,
                                          Authenticator authenticator);
}
