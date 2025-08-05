package java.nio.channels.spi;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import sun.nio.ch.DefaultSelectorProvider;

/* loaded from: rt.jar:java/nio/channels/spi/SelectorProvider.class */
public abstract class SelectorProvider {
    private static final Object lock = new Object();
    private static SelectorProvider provider = null;

    public abstract DatagramChannel openDatagramChannel() throws IOException;

    public abstract DatagramChannel openDatagramChannel(ProtocolFamily protocolFamily) throws IOException;

    public abstract Pipe openPipe() throws IOException;

    public abstract AbstractSelector openSelector() throws IOException;

    public abstract ServerSocketChannel openServerSocketChannel() throws IOException;

    public abstract SocketChannel openSocketChannel() throws IOException;

    protected SelectorProvider() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("selectorProvider"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean loadProviderFromProperty() {
        String property = System.getProperty("java.nio.channels.spi.SelectorProvider");
        if (property == null) {
            return false;
        }
        try {
            provider = (SelectorProvider) Class.forName(property, true, ClassLoader.getSystemClassLoader()).newInstance();
            return true;
        } catch (ClassNotFoundException e2) {
            throw new ServiceConfigurationError(null, e2);
        } catch (IllegalAccessException e3) {
            throw new ServiceConfigurationError(null, e3);
        } catch (InstantiationException e4) {
            throw new ServiceConfigurationError(null, e4);
        } catch (SecurityException e5) {
            throw new ServiceConfigurationError(null, e5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean loadProviderAsService() {
        Iterator it = ServiceLoader.load(SelectorProvider.class, ClassLoader.getSystemClassLoader()).iterator();
        while (it.hasNext()) {
            try {
                provider = (SelectorProvider) it.next();
                return true;
            } catch (ServiceConfigurationError e2) {
                if (!(e2.getCause() instanceof SecurityException)) {
                    throw e2;
                }
            }
        }
        return false;
    }

    public static SelectorProvider provider() {
        synchronized (lock) {
            if (provider != null) {
                return provider;
            }
            return (SelectorProvider) AccessController.doPrivileged(new PrivilegedAction<SelectorProvider>() { // from class: java.nio.channels.spi.SelectorProvider.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public SelectorProvider run() {
                    if (!SelectorProvider.loadProviderFromProperty() && !SelectorProvider.loadProviderAsService()) {
                        SelectorProvider unused = SelectorProvider.provider = DefaultSelectorProvider.create();
                        return SelectorProvider.provider;
                    }
                    return SelectorProvider.provider;
                }
            });
        }
    }

    public Channel inheritedChannel() throws IOException {
        return null;
    }
}
