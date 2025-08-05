package java.nio.channels.spi;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import sun.nio.ch.DefaultAsynchronousChannelProvider;

/* loaded from: rt.jar:java/nio/channels/spi/AsynchronousChannelProvider.class */
public abstract class AsynchronousChannelProvider {
    public abstract AsynchronousChannelGroup openAsynchronousChannelGroup(int i2, ThreadFactory threadFactory) throws IOException;

    public abstract AsynchronousChannelGroup openAsynchronousChannelGroup(ExecutorService executorService, int i2) throws IOException;

    public abstract AsynchronousServerSocketChannel openAsynchronousServerSocketChannel(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException;

    public abstract AsynchronousSocketChannel openAsynchronousSocketChannel(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException;

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("asynchronousChannelProvider"));
            return null;
        }
        return null;
    }

    private AsynchronousChannelProvider(Void r3) {
    }

    protected AsynchronousChannelProvider() {
        this(checkPermission());
    }

    /* loaded from: rt.jar:java/nio/channels/spi/AsynchronousChannelProvider$ProviderHolder.class */
    private static class ProviderHolder {
        static final AsynchronousChannelProvider provider = load();

        private ProviderHolder() {
        }

        private static AsynchronousChannelProvider load() {
            return (AsynchronousChannelProvider) AccessController.doPrivileged(new PrivilegedAction<AsynchronousChannelProvider>() { // from class: java.nio.channels.spi.AsynchronousChannelProvider.ProviderHolder.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public AsynchronousChannelProvider run() {
                    AsynchronousChannelProvider asynchronousChannelProviderLoadProviderFromProperty = ProviderHolder.loadProviderFromProperty();
                    if (asynchronousChannelProviderLoadProviderFromProperty == null) {
                        AsynchronousChannelProvider asynchronousChannelProviderLoadProviderAsService = ProviderHolder.loadProviderAsService();
                        if (asynchronousChannelProviderLoadProviderAsService != null) {
                            return asynchronousChannelProviderLoadProviderAsService;
                        }
                        return DefaultAsynchronousChannelProvider.create();
                    }
                    return asynchronousChannelProviderLoadProviderFromProperty;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static AsynchronousChannelProvider loadProviderFromProperty() {
            String property = System.getProperty("java.nio.channels.spi.AsynchronousChannelProvider");
            if (property == null) {
                return null;
            }
            try {
                return (AsynchronousChannelProvider) Class.forName(property, true, ClassLoader.getSystemClassLoader()).newInstance();
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
        public static AsynchronousChannelProvider loadProviderAsService() {
            Iterator it = ServiceLoader.load(AsynchronousChannelProvider.class, ClassLoader.getSystemClassLoader()).iterator();
            while (it.hasNext()) {
                try {
                    return (AsynchronousChannelProvider) it.next();
                } catch (ServiceConfigurationError e2) {
                    if (!(e2.getCause() instanceof SecurityException)) {
                        throw e2;
                    }
                }
            }
            return null;
        }
    }

    public static AsynchronousChannelProvider provider() {
        return ProviderHolder.provider;
    }
}
