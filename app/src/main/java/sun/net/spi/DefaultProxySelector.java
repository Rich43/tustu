package sun.net.spi;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.SocksConsts;
import java.net.URI;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.net.NetProperties;
import sun.net.SocksProxy;

/* loaded from: rt.jar:sun/net/spi/DefaultProxySelector.class */
public class DefaultProxySelector extends ProxySelector {
    static final String[][] props = {new String[]{"http", "http.proxy", "proxy", "socksProxy"}, new String[]{"https", "https.proxy", "proxy", "socksProxy"}, new String[]{"ftp", "ftp.proxy", "ftpProxy", "proxy", "socksProxy"}, new String[]{"gopher", "gopherProxy", "socksProxy"}, new String[]{"socket", "socksProxy"}};
    private static final String SOCKS_PROXY_VERSION = "socksProxyVersion";
    private static boolean hasSystemProxies;

    private static native boolean init();

    /* JADX INFO: Access modifiers changed from: private */
    public native synchronized Proxy getSystemProxy(String str, String str2);

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.String[], java.lang.String[][]] */
    static {
        hasSystemProxies = false;
        Boolean bool = (Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.net.spi.DefaultProxySelector.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                return NetProperties.getBoolean("java.net.useSystemProxies");
            }
        });
        if (bool != null && bool.booleanValue()) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.net.spi.DefaultProxySelector.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    System.loadLibrary("net");
                    return null;
                }
            });
            hasSystemProxies = init();
        }
    }

    /* loaded from: rt.jar:sun/net/spi/DefaultProxySelector$NonProxyInfo.class */
    static class NonProxyInfo {
        String hostsSource;
        Pattern pattern;
        final String property;
        final String defaultVal;
        static final String defStringVal = "localhost|127.*|[::1]|0.0.0.0|[::0]";
        static NonProxyInfo ftpNonProxyInfo = new NonProxyInfo("ftp.nonProxyHosts", null, null, defStringVal);
        static NonProxyInfo httpNonProxyInfo = new NonProxyInfo("http.nonProxyHosts", null, null, defStringVal);
        static NonProxyInfo socksNonProxyInfo = new NonProxyInfo("socksNonProxyHosts", null, null, defStringVal);

        NonProxyInfo(String str, String str2, Pattern pattern, String str3) {
            this.property = str;
            this.hostsSource = str2;
            this.pattern = pattern;
            this.defaultVal = str3;
        }
    }

    @Override // java.net.ProxySelector
    public List<Proxy> select(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI can't be null.");
        }
        final String scheme = uri.getScheme();
        String host = uri.getHost();
        if (host == null) {
            String authority = uri.getAuthority();
            if (authority != null) {
                int iIndexOf = authority.indexOf(64);
                if (iIndexOf >= 0) {
                    authority = authority.substring(iIndexOf + 1);
                }
                int iLastIndexOf = authority.lastIndexOf(58);
                if (iLastIndexOf >= 0) {
                    authority = authority.substring(0, iLastIndexOf);
                }
                host = authority;
            }
        }
        if (scheme == null || host == null) {
            throw new IllegalArgumentException("protocol = " + scheme + " host = " + host);
        }
        ArrayList arrayList = new ArrayList(1);
        NonProxyInfo nonProxyInfo = null;
        if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
            nonProxyInfo = NonProxyInfo.httpNonProxyInfo;
        } else if ("ftp".equalsIgnoreCase(scheme)) {
            nonProxyInfo = NonProxyInfo.ftpNonProxyInfo;
        } else if ("socket".equalsIgnoreCase(scheme)) {
            nonProxyInfo = NonProxyInfo.socksNonProxyInfo;
        }
        final NonProxyInfo nonProxyInfo2 = nonProxyInfo;
        final String lowerCase = host.toLowerCase();
        arrayList.add((Proxy) AccessController.doPrivileged(new PrivilegedAction<Proxy>() { // from class: sun.net.spi.DefaultProxySelector.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Proxy run2() {
                String str;
                String str2 = null;
                for (int i2 = 0; i2 < DefaultProxySelector.props.length; i2++) {
                    if (DefaultProxySelector.props[i2][0].equalsIgnoreCase(scheme)) {
                        int i3 = 1;
                        while (i3 < DefaultProxySelector.props[i2].length) {
                            str2 = NetProperties.get(DefaultProxySelector.props[i2][i3] + "Host");
                            if (str2 != null && str2.length() != 0) {
                                break;
                            }
                            i3++;
                        }
                        if (str2 == null || str2.length() == 0) {
                            if (DefaultProxySelector.hasSystemProxies) {
                                if (scheme.equalsIgnoreCase("socket")) {
                                    str = "socks";
                                } else {
                                    str = scheme;
                                }
                                Proxy systemProxy = DefaultProxySelector.this.getSystemProxy(str, lowerCase);
                                if (systemProxy != null) {
                                    return systemProxy;
                                }
                            }
                            return Proxy.NO_PROXY;
                        }
                        if (nonProxyInfo2 != null) {
                            String str3 = NetProperties.get(nonProxyInfo2.property);
                            synchronized (nonProxyInfo2) {
                                if (str3 == null) {
                                    if (nonProxyInfo2.defaultVal != null) {
                                        str3 = nonProxyInfo2.defaultVal;
                                    } else {
                                        nonProxyInfo2.hostsSource = null;
                                        nonProxyInfo2.pattern = null;
                                    }
                                } else if (str3.length() != 0) {
                                    str3 = str3 + "|localhost|127.*|[::1]|0.0.0.0|[::0]";
                                }
                                if (str3 != null && !str3.equals(nonProxyInfo2.hostsSource)) {
                                    nonProxyInfo2.pattern = DefaultProxySelector.toPattern(str3);
                                    nonProxyInfo2.hostsSource = str3;
                                }
                                if (DefaultProxySelector.shouldNotUseProxyFor(nonProxyInfo2.pattern, lowerCase)) {
                                    return Proxy.NO_PROXY;
                                }
                            }
                        }
                        int iIntValue = NetProperties.getInteger(DefaultProxySelector.props[i2][i3] + RuntimeModeler.PORT, 0).intValue();
                        if (iIntValue == 0 && i3 < DefaultProxySelector.props[i2].length - 1) {
                            for (int i4 = 1; i4 < DefaultProxySelector.props[i2].length - 1; i4++) {
                                if (i4 != i3 && iIntValue == 0) {
                                    iIntValue = NetProperties.getInteger(DefaultProxySelector.props[i2][i4] + RuntimeModeler.PORT, 0).intValue();
                                }
                            }
                        }
                        if (iIntValue == 0) {
                            iIntValue = i3 == DefaultProxySelector.props[i2].length - 1 ? DefaultProxySelector.this.defaultPort("socket") : DefaultProxySelector.this.defaultPort(scheme);
                        }
                        InetSocketAddress inetSocketAddressCreateUnresolved = InetSocketAddress.createUnresolved(str2, iIntValue);
                        if (i3 == DefaultProxySelector.props[i2].length - 1) {
                            return SocksProxy.create(inetSocketAddressCreateUnresolved, NetProperties.getInteger(DefaultProxySelector.SOCKS_PROXY_VERSION, 5).intValue());
                        }
                        return new Proxy(Proxy.Type.HTTP, inetSocketAddressCreateUnresolved);
                    }
                }
                return Proxy.NO_PROXY;
            }
        }));
        return arrayList;
    }

    @Override // java.net.ProxySelector
    public void connectFailed(URI uri, SocketAddress socketAddress, IOException iOException) {
        if (uri == null || socketAddress == null || iOException == null) {
            throw new IllegalArgumentException("Arguments can't be null.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int defaultPort(String str) {
        if ("http".equalsIgnoreCase(str)) {
            return 80;
        }
        if ("https".equalsIgnoreCase(str)) {
            return 443;
        }
        if ("ftp".equalsIgnoreCase(str)) {
            return 80;
        }
        if ("socket".equalsIgnoreCase(str)) {
            return SocksConsts.DEFAULT_PORT;
        }
        if ("gopher".equalsIgnoreCase(str)) {
            return 80;
        }
        return -1;
    }

    static boolean shouldNotUseProxyFor(Pattern pattern, String str) {
        if (pattern == null || str.isEmpty()) {
            return false;
        }
        return pattern.matcher(str).matches();
    }

    static Pattern toPattern(String str) {
        boolean z2 = true;
        StringJoiner stringJoiner = new StringJoiner(CallSiteDescriptor.OPERATOR_DELIMITER);
        for (String str2 : str.split("\\|")) {
            if (!str2.isEmpty()) {
                z2 = false;
                stringJoiner.add(disjunctToRegex(str2.toLowerCase()));
            }
        }
        if (z2) {
            return null;
        }
        return Pattern.compile(stringJoiner.toString());
    }

    static String disjunctToRegex(String str) {
        String strQuote;
        if (str.startsWith("*")) {
            strQuote = ".*" + Pattern.quote(str.substring(1));
        } else if (str.endsWith("*")) {
            strQuote = Pattern.quote(str.substring(0, str.length() - 1)) + ".*";
        } else {
            strQuote = Pattern.quote(str);
        }
        return strQuote;
    }
}
