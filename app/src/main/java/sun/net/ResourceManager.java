package sun.net;

import java.net.SocketException;
import java.security.AccessController;
import java.util.concurrent.atomic.AtomicInteger;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/net/ResourceManager.class */
public class ResourceManager {
    private static final int DEFAULT_MAX_SOCKETS = 25;
    private static final int maxSockets;
    private static final AtomicInteger numSockets;

    static {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.net.maxDatagramSockets"));
        int i2 = 25;
        if (str != null) {
            try {
                i2 = Integer.parseInt(str);
            } catch (NumberFormatException e2) {
            }
        }
        maxSockets = i2;
        numSockets = new AtomicInteger(0);
    }

    public static void beforeUdpCreate() throws SocketException {
        if (System.getSecurityManager() != null && numSockets.incrementAndGet() > maxSockets) {
            numSockets.decrementAndGet();
            throw new SocketException("maximum number of DatagramSockets reached");
        }
    }

    public static void afterUdpClose() {
        if (System.getSecurityManager() != null) {
            numSockets.decrementAndGet();
        }
    }
}
