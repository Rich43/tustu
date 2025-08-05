package sun.net.dns;

import java.util.List;

/* loaded from: rt.jar:sun/net/dns/ResolverConfiguration.class */
public abstract class ResolverConfiguration {
    private static final Object lock = new Object();
    private static ResolverConfiguration provider;

    public abstract List<String> searchlist();

    public abstract List<String> nameservers();

    public abstract Options options();

    protected ResolverConfiguration() {
    }

    public static ResolverConfiguration open() {
        ResolverConfiguration resolverConfiguration;
        synchronized (lock) {
            if (provider == null) {
                provider = new ResolverConfigurationImpl();
            }
            resolverConfiguration = provider;
        }
        return resolverConfiguration;
    }

    /* loaded from: rt.jar:sun/net/dns/ResolverConfiguration$Options.class */
    public static abstract class Options {
        public int attempts() {
            return -1;
        }

        public int retrans() {
            return -1;
        }
    }
}
