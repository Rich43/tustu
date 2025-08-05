package sun.net.spi.nameservice.dns;

import java.lang.ref.SoftReference;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.spi.NamingManager;
import sun.net.dns.ResolverConfiguration;
import sun.net.spi.nameservice.NameService;
import sun.net.util.IPAddressUtil;
import sun.security.action.GetPropertyAction;

/* loaded from: dnsns.jar:sun/net/spi/nameservice/dns/DNSNameService.class */
public final class DNSNameService implements NameService {
    private LinkedList<String> domainList;
    private String nameProviderUrl;
    private static ThreadLocal<SoftReference<ThreadContext>> contextRef;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DNSNameService.class.desiredAssertionStatus();
        contextRef = new ThreadLocal<>();
    }

    /* loaded from: dnsns.jar:sun/net/spi/nameservice/dns/DNSNameService$ThreadContext.class */
    private static class ThreadContext {
        private DirContext dirCtxt;
        private List<String> nsList;

        public ThreadContext(DirContext dirContext, List<String> list) {
            this.dirCtxt = dirContext;
            this.nsList = list;
        }

        public DirContext dirContext() {
            return this.dirCtxt;
        }

        public List<String> nameservers() {
            return this.nsList;
        }
    }

    private DirContext getTemporaryContext() throws NamingException {
        SoftReference<ThreadContext> softReference = contextRef.get();
        ThreadContext threadContext = null;
        List<String> listNameservers = null;
        if (this.nameProviderUrl == null) {
            listNameservers = ResolverConfiguration.open().nameservers();
        }
        if (softReference != null) {
            ThreadContext threadContext2 = softReference.get();
            threadContext = threadContext2;
            if (threadContext2 != null && this.nameProviderUrl == null && !threadContext.nameservers().equals(listNameservers)) {
                threadContext = null;
            }
        }
        if (threadContext == null) {
            final Hashtable hashtable = new Hashtable();
            hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
            String strCreateProviderURL = this.nameProviderUrl;
            if (strCreateProviderURL == null) {
                strCreateProviderURL = createProviderURL(listNameservers);
                if (strCreateProviderURL.length() == 0) {
                    throw new RuntimeException("bad nameserver configuration");
                }
            }
            hashtable.put(Context.PROVIDER_URL, strCreateProviderURL);
            try {
                threadContext = new ThreadContext((DirContext) AccessController.doPrivileged(new PrivilegedExceptionAction<DirContext>() { // from class: sun.net.spi.nameservice.dns.DNSNameService.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public DirContext run() throws NamingException {
                        Context initialContext = NamingManager.getInitialContext(hashtable);
                        if (!(initialContext instanceof DirContext)) {
                            return null;
                        }
                        return (DirContext) initialContext;
                    }
                }), listNameservers);
                contextRef.set(new SoftReference<>(threadContext));
            } catch (PrivilegedActionException e2) {
                throw ((NamingException) e2.getException());
            }
        }
        return threadContext.dirContext();
    }

    private ArrayList<String> resolve(final DirContext dirContext, final String str, final String[] strArr, int i2) throws UnknownHostException {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            NamingEnumeration<? extends Attribute> all = ((Attributes) AccessController.doPrivileged(new PrivilegedExceptionAction<Attributes>() { // from class: sun.net.spi.nameservice.dns.DNSNameService.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Attributes run() throws NamingException {
                    return dirContext.getAttributes(str, strArr);
                }
            })).getAll();
            if (!all.hasMoreElements()) {
                throw new UnknownHostException("DNS record not found");
            }
            UnknownHostException unknownHostException = null;
            while (all.hasMoreElements()) {
                try {
                    Attribute next = all.next();
                    String id = next.getID();
                    NamingEnumeration<?> all2 = next.getAll();
                    while (all2.hasMoreElements()) {
                        String str2 = (String) all2.next();
                        if (id.equals("CNAME")) {
                            if (i2 > 4) {
                                throw new UnknownHostException(str + ": possible CNAME loop");
                            }
                            try {
                                arrayList.addAll(resolve(dirContext, str2, strArr, i2 + 1));
                            } catch (UnknownHostException e2) {
                                if (unknownHostException == null) {
                                    unknownHostException = e2;
                                }
                            }
                        } else {
                            arrayList.add(str2);
                        }
                    }
                } catch (NamingException e3) {
                    throw new UnknownHostException(e3.getMessage());
                }
            }
            if (arrayList.isEmpty() && unknownHostException != null) {
                throw unknownHostException;
            }
            return arrayList;
        } catch (PrivilegedActionException e4) {
            throw new UnknownHostException(e4.getException().getMessage());
        }
    }

    public DNSNameService() throws Exception {
        this.domainList = null;
        this.nameProviderUrl = null;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.net.spi.nameservice.domain"));
        if (str != null && str.length() > 0) {
            this.domainList = new LinkedList<>();
            this.domainList.add(str);
        }
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("sun.net.spi.nameservice.nameservers"));
        if (str2 != null && str2.length() > 0) {
            this.nameProviderUrl = createProviderURL(str2);
            if (this.nameProviderUrl.length() == 0) {
                throw new RuntimeException("malformed nameservers property");
            }
            return;
        }
        List<String> listNameservers = ResolverConfiguration.open().nameservers();
        if (listNameservers.isEmpty()) {
            throw new RuntimeException("no nameservers provided");
        }
        boolean z2 = false;
        for (String str3 : listNameservers) {
            if (IPAddressUtil.isIPv4LiteralAddress(str3) || IPAddressUtil.isIPv6LiteralAddress(str3)) {
                z2 = true;
                break;
            }
        }
        if (!z2) {
            throw new RuntimeException("bad nameserver configuration");
        }
    }

    @Override // sun.net.spi.nameservice.NameService
    public InetAddress[] lookupAllHostAddr(String str) throws UnknownHostException {
        Iterator<String> it;
        String[] strArr = {"A", "AAAA", "CNAME"};
        try {
            DirContext temporaryContext = getTemporaryContext();
            ArrayList<String> arrayListResolve = null;
            UnknownHostException unknownHostException = null;
            if (str.indexOf(46) >= 0) {
                try {
                    arrayListResolve = resolve(temporaryContext, str, strArr, 0);
                } catch (UnknownHostException e2) {
                    unknownHostException = e2;
                }
            }
            if (arrayListResolve == null) {
                boolean z2 = false;
                if (this.domainList != null) {
                    it = this.domainList.iterator();
                } else {
                    List<String> listSearchlist = ResolverConfiguration.open().searchlist();
                    if (listSearchlist.size() > 1) {
                        z2 = true;
                    }
                    it = listSearchlist.iterator();
                }
                while (it.hasNext()) {
                    String next = it.next();
                    while (true) {
                        int iIndexOf = next.indexOf(".");
                        if (iIndexOf == -1 || iIndexOf >= next.length() - 1) {
                            break;
                        }
                        try {
                            arrayListResolve = resolve(temporaryContext, str + "." + next, strArr, 0);
                            break;
                        } catch (UnknownHostException e3) {
                            unknownHostException = e3;
                            if (z2) {
                                break;
                            }
                            next = next.substring(iIndexOf + 1);
                        }
                    }
                    if (arrayListResolve != null) {
                        break;
                    }
                }
            }
            if (arrayListResolve == null && str.indexOf(46) < 0) {
                arrayListResolve = resolve(temporaryContext, str, strArr, 0);
            }
            if (arrayListResolve == null) {
                if ($assertionsDisabled || unknownHostException != null) {
                    throw unknownHostException;
                }
                throw new AssertionError();
            }
            if (!$assertionsDisabled && arrayListResolve.size() <= 0) {
                throw new AssertionError();
            }
            InetAddress[] inetAddressArr = new InetAddress[arrayListResolve.size()];
            int i2 = 0;
            for (int i3 = 0; i3 < arrayListResolve.size(); i3++) {
                String str2 = arrayListResolve.get(i3);
                byte[] bArrTextToNumericFormatV6 = null;
                try {
                    bArrTextToNumericFormatV6 = IPAddressUtil.validateNumericFormatV4(str2);
                } catch (IllegalArgumentException e4) {
                }
                if (bArrTextToNumericFormatV6 == null) {
                    bArrTextToNumericFormatV6 = IPAddressUtil.textToNumericFormatV6(str2);
                }
                if (bArrTextToNumericFormatV6 != null) {
                    int i4 = i2;
                    i2++;
                    inetAddressArr[i4] = InetAddress.getByAddress(str, bArrTextToNumericFormatV6);
                }
            }
            if (i2 == 0) {
                throw new UnknownHostException(str + ": no valid DNS records");
            }
            if (i2 < arrayListResolve.size()) {
                InetAddress[] inetAddressArr2 = new InetAddress[i2];
                for (int i5 = 0; i5 < i2; i5++) {
                    inetAddressArr2[i5] = inetAddressArr[i5];
                }
                inetAddressArr = inetAddressArr2;
            }
            return inetAddressArr;
        } catch (NamingException e5) {
            throw new Error(e5);
        }
    }

    @Override // sun.net.spi.nameservice.NameService
    public String getHostByAddr(byte[] bArr) throws UnknownHostException {
        String strSubstring = null;
        try {
            String str = "";
            String[] strArr = {"PTR"};
            try {
                DirContext temporaryContext = getTemporaryContext();
                if (bArr.length == 4) {
                    for (int length = bArr.length - 1; length >= 0; length--) {
                        str = str + (bArr[length] & 255) + ".";
                    }
                    strSubstring = resolve(temporaryContext, str + "IN-ADDR.ARPA.", strArr, 0).get(0);
                } else if (bArr.length == 16) {
                    for (int length2 = bArr.length - 1; length2 >= 0; length2--) {
                        str = str + Integer.toHexString(bArr[length2] & 15) + "." + Integer.toHexString((bArr[length2] & 240) >> 4) + ".";
                    }
                    try {
                        strSubstring = resolve(temporaryContext, str + "IP6.ARPA.", strArr, 0).get(0);
                    } catch (UnknownHostException e2) {
                        strSubstring = null;
                    }
                    if (strSubstring == null) {
                        strSubstring = resolve(temporaryContext, str + "IP6.INT.", strArr, 0).get(0);
                    }
                }
                if (strSubstring == null) {
                    throw new UnknownHostException();
                }
                if (strSubstring.endsWith(".")) {
                    strSubstring = strSubstring.substring(0, strSubstring.length() - 1);
                }
                return strSubstring;
            } catch (NamingException e3) {
                throw new Error(e3);
            }
        } catch (Exception e4) {
            throw new UnknownHostException(e4.getMessage());
        }
    }

    private static void appendIfLiteralAddress(String str, StringBuffer stringBuffer) {
        if (IPAddressUtil.isIPv4LiteralAddress(str)) {
            stringBuffer.append("dns://" + str + " ");
        } else if (IPAddressUtil.isIPv6LiteralAddress(str)) {
            stringBuffer.append("dns://[" + str + "] ");
        }
    }

    private static String createProviderURL(List<String> list) {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            appendIfLiteralAddress(it.next(), stringBuffer);
        }
        return stringBuffer.toString();
    }

    private static String createProviderURL(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        while (stringTokenizer.hasMoreTokens()) {
            appendIfLiteralAddress(stringTokenizer.nextToken(), stringBuffer);
        }
        return stringBuffer.toString();
    }
}
