package java.nio.channels;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.util.Set;

/* loaded from: rt.jar:java/nio/channels/NetworkChannel.class */
public interface NetworkChannel extends Channel {
    NetworkChannel bind(SocketAddress socketAddress) throws IOException;

    SocketAddress getLocalAddress() throws IOException;

    <T> NetworkChannel setOption(SocketOption<T> socketOption, T t2) throws IOException;

    <T> T getOption(SocketOption<T> socketOption) throws IOException;

    Set<SocketOption<?>> supportedOptions();
}
