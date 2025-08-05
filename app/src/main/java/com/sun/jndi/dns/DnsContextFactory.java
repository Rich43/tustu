package com.sun.jndi.dns;

import com.sun.jndi.toolkit.url.UrlUtil;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import org.apache.commons.net.ftp.FTP;
import sun.net.dns.ResolverConfiguration;

/* loaded from: rt.jar:com/sun/jndi/dns/DnsContextFactory.class */
public class DnsContextFactory implements InitialContextFactory {
    private static final String DEFAULT_URL = "dns:";
    private static final int DEFAULT_PORT = 53;

    @Override // javax.naming.spi.InitialContextFactory
    public Context getInitialContext(Hashtable<?, ?> hashtable) throws NamingException {
        if (hashtable == null) {
            hashtable = new Hashtable<>(5);
        }
        return urlToContext(getInitCtxUrl(hashtable), hashtable);
    }

    public static DnsContext getContext(String str, String[] strArr, Hashtable<?, ?> hashtable) throws NamingException {
        return new DnsContext(str, strArr, hashtable);
    }

    public static DnsContext getContext(String str, DnsUrl[] dnsUrlArr, Hashtable<?, ?> hashtable) throws NamingException {
        String[] strArrServersForUrls = serversForUrls(dnsUrlArr);
        DnsContext context = getContext(str, strArrServersForUrls, hashtable);
        if (platformServersUsed(dnsUrlArr)) {
            context.setProviderUrl(constructProviderUrl(str, strArrServersForUrls));
        }
        return context;
    }

    public static boolean platformServersAvailable() {
        return !filterNameServers(ResolverConfiguration.open().nameservers(), true).isEmpty();
    }

    private static Context urlToContext(String str, Hashtable<?, ?> hashtable) throws NamingException {
        try {
            DnsUrl[] dnsUrlArrFromList = DnsUrl.fromList(str);
            if (dnsUrlArrFromList.length == 0) {
                throw new ConfigurationException("Invalid DNS pseudo-URL(s): " + str);
            }
            String domain = dnsUrlArrFromList[0].getDomain();
            for (int i2 = 1; i2 < dnsUrlArrFromList.length; i2++) {
                if (!domain.equalsIgnoreCase(dnsUrlArrFromList[i2].getDomain())) {
                    throw new ConfigurationException("Conflicting domains: " + str);
                }
            }
            return getContext(domain, dnsUrlArrFromList, hashtable);
        } catch (MalformedURLException e2) {
            throw new ConfigurationException(e2.getMessage());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0059  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String[] serversForUrls(com.sun.jndi.dns.DnsUrl[] r4) throws javax.naming.NamingException {
        /*
            r0 = r4
            int r0 = r0.length
            if (r0 != 0) goto Lf
            javax.naming.ConfigurationException r0 = new javax.naming.ConfigurationException
            r1 = r0
            java.lang.String r2 = "DNS pseudo-URL required"
            r1.<init>(r2)
            throw r0
        Lf:
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r1.<init>()
            r5 = r0
            r0 = 0
            r6 = r0
        L19:
            r0 = r6
            r1 = r4
            int r1 = r1.length
            if (r0 >= r1) goto L8e
            r0 = r4
            r1 = r6
            r0 = r0[r1]
            java.lang.String r0 = r0.getHost()
            r7 = r0
            r0 = r4
            r1 = r6
            r0 = r0[r1]
            int r0 = r0.getPort()
            r8 = r0
            r0 = r7
            if (r0 != 0) goto L59
            r0 = r8
            if (r0 >= 0) goto L59
            sun.net.dns.ResolverConfiguration r0 = sun.net.dns.ResolverConfiguration.open()
            java.util.List r0 = r0.nameservers()
            r1 = 0
            java.util.List r0 = filterNameServers(r0, r1)
            r9 = r0
            r0 = r9
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L59
            r0 = r5
            r1 = r9
            boolean r0 = r0.addAll(r1)
            goto L88
        L59:
            r0 = r7
            if (r0 != 0) goto L60
            java.lang.String r0 = "localhost"
            r7 = r0
        L60:
            r0 = r5
            r1 = r8
            if (r1 >= 0) goto L6a
            r1 = r7
            goto L82
        L6a:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r2 = r1
            r2.<init>()
            r2 = r7
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = ":"
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = r8
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
        L82:
            boolean r0 = r0.add(r1)
        L88:
            int r6 = r6 + 1
            goto L19
        L8e:
            r0 = r5
            r1 = r5
            int r1 = r1.size()
            java.lang.String[] r1 = new java.lang.String[r1]
            java.lang.Object[] r0 = r0.toArray(r1)
            java.lang.String[] r0 = (java.lang.String[]) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jndi.dns.DnsContextFactory.serversForUrls(com.sun.jndi.dns.DnsUrl[]):java.lang.String[]");
    }

    private static boolean platformServersUsed(DnsUrl[] dnsUrlArr) {
        if (!platformServersAvailable()) {
            return false;
        }
        for (int i2 = 0; i2 < dnsUrlArr.length; i2++) {
            if (dnsUrlArr[i2].getHost() == null && dnsUrlArr[i2].getPort() < 0) {
                return true;
            }
        }
        return false;
    }

    private static String constructProviderUrl(String str, String[] strArr) {
        String str2 = "";
        if (!str.equals(".")) {
            try {
                str2 = "/" + UrlUtil.encode(str, FTP.DEFAULT_CONTROL_ENCODING);
            } catch (UnsupportedEncodingException e2) {
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (i2 > 0) {
                stringBuffer.append(' ');
            }
            stringBuffer.append("dns://").append(strArr[i2]).append(str2);
        }
        return stringBuffer.toString();
    }

    private static String getInitCtxUrl(Hashtable<?, ?> hashtable) {
        String str = (String) hashtable.get(Context.PROVIDER_URL);
        return str != null ? str : DEFAULT_URL;
    }

    private static List<String> filterNameServers(List<String> list, boolean z2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null || list == null || list.isEmpty()) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            int iIndexOf = str.indexOf(58, str.indexOf(93) + 1);
            try {
                securityManager.checkConnect(iIndexOf < 0 ? str : str.substring(0, iIndexOf), iIndexOf < 0 ? 53 : Integer.parseInt(str.substring(iIndexOf + 1)));
                arrayList.add(str);
            } catch (SecurityException e2) {
            }
            if (z2) {
                return arrayList;
            }
        }
        return arrayList;
    }
}
