package com.digitalxyncing.communication;

public interface EndpointFactory {

    <T> Endpoint<T> buildClientEndpoint(String hostAddress, int hostDiscoveryPort, String token, Class<T> type,
                                        MessageHandlerFactory<T> messageHandlerFactory);

    <T> HostEndpoint<T> buildHostEndpoint(int port, MessageHandlerFactory<T> messageHandlerFactory);

    <T> HostEndpoint<T> buildHostEndpoint(int port, MessageHandlerFactory<T> messageHandlerFactory,
                                          Authenticator authenticator);
}
