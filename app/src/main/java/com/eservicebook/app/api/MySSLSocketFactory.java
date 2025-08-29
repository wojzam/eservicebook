package com.eservicebook.app.api;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class MySSLSocketFactory extends SSLSocketFactory {

    private final String protocol = "TLSv1.2";
    SSLSocketFactory sslSocketFactory;

    public MySSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        super();
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return sslSocketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return sslSocketFactory.getSupportedCipherSuites();
    }

    @Override
    public SSLSocket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(s, host, port, autoClose);
        socket.setEnabledProtocols(new String[]{protocol});
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(host, port);
        socket.setEnabledProtocols(new String[]{protocol});
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(host, port, localHost, localPort);
        socket.setEnabledProtocols(new String[]{protocol});
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(host, port);
        socket.setEnabledProtocols(new String[]{protocol});
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
            throws IOException {
        SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(address, port, localAddress, localPort);
        socket.setEnabledProtocols(new String[]{protocol});
        return socket;
    }
}