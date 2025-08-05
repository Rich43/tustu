package sun.nio.ch;

import java.nio.channels.spi.AsynchronousChannelProvider;

/* loaded from: rt.jar:sun/nio/ch/DefaultAsynchronousChannelProvider.class */
public class DefaultAsynchronousChannelProvider {
    private DefaultAsynchronousChannelProvider() {
    }

    public static AsynchronousChannelProvider create() {
        return new WindowsAsynchronousChannelProvider();
    }
}
