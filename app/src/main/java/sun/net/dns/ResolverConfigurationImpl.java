package sun.net.dns;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import sun.net.dns.ResolverConfiguration;

/* loaded from: rt.jar:sun/net/dns/ResolverConfigurationImpl.class */
public class ResolverConfigurationImpl extends ResolverConfiguration {
    private static Object lock;
    private final ResolverConfiguration.Options opts = new OptionsImpl();
    private static boolean changed;
    private static long lastRefresh;
    private static final int TIMEOUT = 120000;
    private static String os_searchlist;
    private static String os_nameservers;
    private static LinkedList<String> searchlist;
    private static LinkedList<String> nameservers;
    static final /* synthetic */ boolean $assertionsDisabled;

    static native void init0();

    static native void loadDNSconfig0();

    static native int notifyAddrChange0();

    static {
        $assertionsDisabled = !ResolverConfigurationImpl.class.desiredAssertionStatus();
        lock = new Object();
        changed = false;
        lastRefresh = -1L;
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.net.dns.ResolverConfigurationImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("net");
                return null;
            }
        });
        init0();
        AddressChangeListener addressChangeListener = new AddressChangeListener();
        addressChangeListener.setDaemon(true);
        addressChangeListener.start();
    }

    private LinkedList<String> stringToList(String str) {
        LinkedList<String> linkedList = new LinkedList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(str, ", ");
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (!linkedList.contains(strNextToken)) {
                linkedList.add(strNextToken);
            }
        }
        return linkedList;
    }

    private void loadConfig() {
        if (!$assertionsDisabled && !Thread.holdsLock(lock)) {
            throw new AssertionError();
        }
        if (changed) {
            changed = false;
        } else if (lastRefresh >= 0 && System.currentTimeMillis() - lastRefresh < 120000) {
            return;
        }
        loadDNSconfig0();
        lastRefresh = System.currentTimeMillis();
        searchlist = stringToList(os_searchlist);
        nameservers = stringToList(os_nameservers);
        os_searchlist = null;
        os_nameservers = null;
    }

    ResolverConfigurationImpl() {
    }

    @Override // sun.net.dns.ResolverConfiguration
    public List<String> searchlist() {
        List<String> list;
        synchronized (lock) {
            loadConfig();
            list = (List) searchlist.clone();
        }
        return list;
    }

    @Override // sun.net.dns.ResolverConfiguration
    public List<String> nameservers() {
        List<String> list;
        synchronized (lock) {
            loadConfig();
            list = (List) nameservers.clone();
        }
        return list;
    }

    @Override // sun.net.dns.ResolverConfiguration
    public ResolverConfiguration.Options options() {
        return this.opts;
    }

    /* loaded from: rt.jar:sun/net/dns/ResolverConfigurationImpl$AddressChangeListener.class */
    static class AddressChangeListener extends Thread {
        AddressChangeListener() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (ResolverConfigurationImpl.notifyAddrChange0() == 0) {
                synchronized (ResolverConfigurationImpl.lock) {
                    boolean unused = ResolverConfigurationImpl.changed = true;
                }
            }
        }
    }
}
