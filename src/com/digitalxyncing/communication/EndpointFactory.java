package com.digitalxyncing.communication;

public interface EndpointFactory {

    Endpoint buildClientEndpoint(String hostAddress, int hostDiscoveryPort, String token,
                                 MessageHandlerFactory messageHandlerFactory);

    HostEndpoint buildHostEndpoint(int port, MessageHandlerFactory messageHandlerFactory);

    HostEndpoint buildHostEndpoint(int port, MessageHandlerFactory messageHandlerFactory,
                                   Authenticator authenticator);
}
