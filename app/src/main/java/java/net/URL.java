package java.net;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.net.Proxy;
import java.security.AccessController;
import java.util.Hashtable;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.misc.VM;
import sun.net.ApplicationProxy;
import sun.net.util.IPAddressUtil;
import sun.net.www.protocol.jar.Handler;
import sun.security.action.GetPropertyAction;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/net/URL.class */
public final class URL implements Serializable {
    static final String BUILTIN_HANDLERS_PREFIX = "sun.net.www.protocol";
    static final long serialVersionUID = -7627629688361524110L;
    private static final String protocolPathProp = "java.protocol.handler.pkgs";
    private String protocol;
    private String host;
    private int port;
    private String file;
    private transient String query;
    private String authority;
    private transient String path;
    private transient String userInfo;
    private String ref;
    private transient InetAddress hostAddress;
    transient URLStreamHandler handler;
    private int hashCode;
    private transient UrlDeserializedState tempState;
    static URLStreamHandlerFactory factory;
    static Hashtable<String, URLStreamHandler> handlers = new Hashtable<>();
    private static Object streamHandlerLock = new Object();
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("protocol", String.class), new ObjectStreamField("host", String.class), new ObjectStreamField(DeploymentDescriptorParser.ATTR_PORT, Integer.TYPE), new ObjectStreamField("authority", String.class), new ObjectStreamField(DeploymentDescriptorParser.ATTR_FILE, String.class), new ObjectStreamField("ref", String.class), new ObjectStreamField("hashCode", Integer.TYPE)};

    public URL(String str, String str2, int i2, String str3) throws MalformedURLException {
        this(str, str2, i2, str3, null);
    }

    public URL(String str, String str2, String str3) throws MalformedURLException {
        this(str, str2, -1, str3);
    }

    public URL(String str, String str2, int i2, String str3, URLStreamHandler uRLStreamHandler) throws MalformedURLException {
        String strCheckNestedProtocol;
        String strCheckExternalForm;
        SecurityManager securityManager;
        this.port = -1;
        this.hashCode = -1;
        if (uRLStreamHandler != null && (securityManager = System.getSecurityManager()) != null) {
            checkSpecifyHandler(securityManager);
        }
        String lowerCase = str.toLowerCase();
        this.protocol = lowerCase;
        if (str2 != null) {
            if (str2.indexOf(58) >= 0 && !str2.startsWith("[")) {
                str2 = "[" + str2 + "]";
            }
            this.host = str2;
            if (i2 < -1) {
                throw new MalformedURLException("Invalid port number :" + i2);
            }
            this.port = i2;
            this.authority = i2 == -1 ? str2 : str2 + CallSiteDescriptor.TOKEN_DELIMITER + i2;
        }
        Parts parts = new Parts(str3);
        this.path = parts.getPath();
        this.query = parts.getQuery();
        if (this.query != null) {
            this.file = this.path + "?" + this.query;
        } else {
            this.file = this.path;
        }
        this.ref = parts.getRef();
        if (uRLStreamHandler == null) {
            URLStreamHandler uRLStreamHandler2 = getURLStreamHandler(lowerCase);
            uRLStreamHandler = uRLStreamHandler2;
            if (uRLStreamHandler2 == null) {
                throw new MalformedURLException("unknown protocol: " + lowerCase);
            }
        }
        this.handler = uRLStreamHandler;
        if (str2 != null && isBuiltinStreamHandler(uRLStreamHandler) && (strCheckExternalForm = IPAddressUtil.checkExternalForm(this)) != null) {
            throw new MalformedURLException(strCheckExternalForm);
        }
        if ("jar".equalsIgnoreCase(lowerCase) && (uRLStreamHandler instanceof Handler) && (strCheckNestedProtocol = ((Handler) uRLStreamHandler).checkNestedProtocol(str3)) != null) {
            throw new MalformedURLException(strCheckNestedProtocol);
        }
    }

    public URL(String str) throws MalformedURLException {
        this(null, str);
    }

    public URL(URL url, String str) throws MalformedURLException {
        this(url, str, (URLStreamHandler) null);
    }

    public URL(URL url, String str, URLStreamHandler uRLStreamHandler) throws MalformedURLException {
        char cCharAt;
        SecurityManager securityManager;
        this.port = -1;
        this.hashCode = -1;
        int i2 = 0;
        String str2 = null;
        boolean z2 = false;
        boolean z3 = false;
        if (uRLStreamHandler != null && (securityManager = System.getSecurityManager()) != null) {
            checkSpecifyHandler(securityManager);
        }
        try {
            int length = str.length();
            while (length > 0 && str.charAt(length - 1) <= ' ') {
                length--;
            }
            while (i2 < length && str.charAt(i2) <= ' ') {
                i2++;
            }
            i2 = str.regionMatches(true, i2, "url:", 0, 4) ? i2 + 4 : i2;
            if (i2 < str.length() && str.charAt(i2) == '#') {
                z2 = true;
            }
            int i3 = i2;
            while (true) {
                if (z2 || i3 >= length || (cCharAt = str.charAt(i3)) == '/') {
                    break;
                }
                if (cCharAt != ':') {
                    i3++;
                } else {
                    String lowerCase = str.substring(i2, i3).toLowerCase();
                    if (isValidProtocol(lowerCase)) {
                        str2 = lowerCase;
                        i2 = i3 + 1;
                    }
                }
            }
            this.protocol = str2;
            if (url != null && (str2 == null || str2.equalsIgnoreCase(url.protocol))) {
                uRLStreamHandler = uRLStreamHandler == null ? url.handler : uRLStreamHandler;
                if (url.path != null && url.path.startsWith("/")) {
                    str2 = null;
                }
                if (str2 == null) {
                    this.protocol = url.protocol;
                    this.authority = url.authority;
                    this.userInfo = url.userInfo;
                    this.host = url.host;
                    this.port = url.port;
                    this.file = url.file;
                    this.path = url.path;
                    z3 = true;
                }
            }
            if (this.protocol == null) {
                throw new MalformedURLException("no protocol: " + str);
            }
            if (uRLStreamHandler == null) {
                URLStreamHandler uRLStreamHandler2 = getURLStreamHandler(this.protocol);
                uRLStreamHandler = uRLStreamHandler2;
                if (uRLStreamHandler2 == null) {
                    throw new MalformedURLException("unknown protocol: " + this.protocol);
                }
            }
            this.handler = uRLStreamHandler;
            int iIndexOf = str.indexOf(35, i2);
            if (iIndexOf >= 0) {
                this.ref = str.substring(iIndexOf + 1, length);
                length = iIndexOf;
            }
            if (z3 && i2 == length) {
                this.query = url.query;
                if (this.ref == null) {
                    this.ref = url.ref;
                }
            }
            uRLStreamHandler.parseURL(this, str, i2, length);
        } catch (MalformedURLException e2) {
            throw e2;
        } catch (Exception e3) {
            MalformedURLException malformedURLException = new MalformedURLException(e3.getMessage());
            malformedURLException.initCause(e3);
            throw malformedURLException;
        }
    }

    private boolean isValidProtocol(String str) {
        int length = str.length();
        if (length < 1 || !Character.isLetter(str.charAt(0))) {
            return false;
        }
        for (int i2 = 1; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (!Character.isLetterOrDigit(cCharAt) && cCharAt != '.' && cCharAt != '+' && cCharAt != '-') {
                return false;
            }
        }
        return true;
    }

    private void checkSpecifyHandler(SecurityManager securityManager) {
        securityManager.checkPermission(SecurityConstants.SPECIFY_HANDLER_PERMISSION);
    }

    void set(String str, String str2, int i2, String str3, String str4) {
        synchronized (this) {
            this.protocol = str;
            this.host = str2;
            this.authority = i2 == -1 ? str2 : str2 + CallSiteDescriptor.TOKEN_DELIMITER + i2;
            this.port = i2;
            this.file = str3;
            this.ref = str4;
            this.hashCode = -1;
            this.hostAddress = null;
            int iLastIndexOf = str3.lastIndexOf(63);
            if (iLastIndexOf != -1) {
                this.query = str3.substring(iLastIndexOf + 1);
                this.path = str3.substring(0, iLastIndexOf);
            } else {
                this.path = str3;
            }
        }
    }

    void set(String str, String str2, int i2, String str3, String str4, String str5, String str6, String str7) {
        synchronized (this) {
            this.protocol = str;
            this.host = str2;
            this.port = i2;
            this.file = str6 == null ? str5 : str5 + "?" + str6;
            this.userInfo = str4;
            this.path = str5;
            this.ref = str7;
            this.hashCode = -1;
            this.hostAddress = null;
            this.query = str6;
            this.authority = str3;
        }
    }

    synchronized InetAddress getHostAddress() {
        if (this.hostAddress != null) {
            return this.hostAddress;
        }
        if (this.host == null || this.host.isEmpty()) {
            return null;
        }
        try {
            this.hostAddress = InetAddress.getByName(this.host);
            return this.hostAddress;
        } catch (SecurityException | UnknownHostException e2) {
            return null;
        }
    }

    public String getQuery() {
        return this.query;
    }

    public String getPath() {
        return this.path;
    }

    public String getUserInfo() {
        return this.userInfo;
    }

    public String getAuthority() {
        return this.authority;
    }

    public int getPort() {
        return this.port;
    }

    public int getDefaultPort() {
        return this.handler.getDefaultPort();
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String getHost() {
        return this.host;
    }

    public String getFile() {
        return this.file;
    }

    public String getRef() {
        return this.ref;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof URL)) {
            return false;
        }
        return this.handler.equals(this, (URL) obj);
    }

    public synchronized int hashCode() {
        if (this.hashCode != -1) {
            return this.hashCode;
        }
        this.hashCode = this.handler.hashCode(this);
        return this.hashCode;
    }

    public boolean sameFile(URL url) {
        return this.handler.sameFile(this, url);
    }

    public String toString() {
        return toExternalForm();
    }

    public String toExternalForm() {
        return this.handler.toExternalForm(this);
    }

    public URI toURI() throws URISyntaxException {
        String strCheckAuthority;
        URI uri = new URI(toString());
        if (this.authority == null || !isBuiltinStreamHandler(this.handler) || (strCheckAuthority = IPAddressUtil.checkAuthority(this)) == null) {
            return uri;
        }
        throw new URISyntaxException(this.authority, strCheckAuthority);
    }

    public URLConnection openConnection() throws IOException {
        return this.handler.openConnection(this);
    }

    public URLConnection openConnection(Proxy proxy) throws IOException {
        if (proxy == null) {
            throw new IllegalArgumentException("proxy can not be null");
        }
        Proxy proxyCreate = proxy == Proxy.NO_PROXY ? Proxy.NO_PROXY : ApplicationProxy.create(proxy);
        SecurityManager securityManager = System.getSecurityManager();
        if (proxyCreate.type() != Proxy.Type.DIRECT && securityManager != null) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) proxyCreate.address();
            if (inetSocketAddress.isUnresolved()) {
                securityManager.checkConnect(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
            } else {
                securityManager.checkConnect(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
            }
        }
        return this.handler.openConnection(this, proxyCreate);
    }

    public final InputStream openStream() throws IOException {
        return openConnection().getInputStream();
    }

    public final Object getContent() throws IOException {
        return openConnection().getContent();
    }

    public final Object getContent(Class[] clsArr) throws IOException {
        return openConnection().getContent(clsArr);
    }

    public static void setURLStreamHandlerFactory(URLStreamHandlerFactory uRLStreamHandlerFactory) {
        synchronized (streamHandlerLock) {
            if (factory != null) {
                throw new Error("factory already defined");
            }
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkSetFactory();
            }
            handlers.clear();
            factory = uRLStreamHandlerFactory;
        }
    }

    static URLStreamHandler getURLStreamHandler(String str) {
        URLStreamHandler uRLStreamHandlerCreateURLStreamHandler = handlers.get(str);
        if (uRLStreamHandlerCreateURLStreamHandler == null) {
            boolean z2 = false;
            if (factory != null) {
                uRLStreamHandlerCreateURLStreamHandler = factory.createURLStreamHandler(str);
                z2 = true;
            }
            if (uRLStreamHandlerCreateURLStreamHandler == null) {
                String str2 = (String) AccessController.doPrivileged(new GetPropertyAction(protocolPathProp, ""));
                if (str2 != "") {
                    str2 = str2 + CallSiteDescriptor.OPERATOR_DELIMITER;
                }
                StringTokenizer stringTokenizer = new StringTokenizer(str2 + BUILTIN_HANDLERS_PREFIX, CallSiteDescriptor.OPERATOR_DELIMITER);
                while (uRLStreamHandlerCreateURLStreamHandler == null && stringTokenizer.hasMoreTokens()) {
                    try {
                        String str3 = stringTokenizer.nextToken().trim() + "." + str + ".Handler";
                        Class<?> clsLoadClass = null;
                        try {
                            clsLoadClass = Class.forName(str3);
                        } catch (ClassNotFoundException e2) {
                            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                            if (systemClassLoader != null) {
                                clsLoadClass = systemClassLoader.loadClass(str3);
                            }
                        }
                        if (clsLoadClass != null) {
                            uRLStreamHandlerCreateURLStreamHandler = (URLStreamHandler) clsLoadClass.newInstance();
                        }
                    } catch (Exception e3) {
                    }
                }
            }
            synchronized (streamHandlerLock) {
                URLStreamHandler uRLStreamHandlerCreateURLStreamHandler2 = handlers.get(str);
                if (uRLStreamHandlerCreateURLStreamHandler2 != null) {
                    return uRLStreamHandlerCreateURLStreamHandler2;
                }
                if (!z2 && factory != null) {
                    uRLStreamHandlerCreateURLStreamHandler2 = factory.createURLStreamHandler(str);
                }
                if (uRLStreamHandlerCreateURLStreamHandler2 != null) {
                    uRLStreamHandlerCreateURLStreamHandler = uRLStreamHandlerCreateURLStreamHandler2;
                }
                if (uRLStreamHandlerCreateURLStreamHandler != null) {
                    handlers.put(str, uRLStreamHandlerCreateURLStreamHandler);
                }
            }
        }
        return uRLStreamHandlerCreateURLStreamHandler;
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        String str = (String) fields.get("protocol", (Object) null);
        if (getURLStreamHandler(str) == null) {
            throw new IOException("unknown protocol: " + str);
        }
        String str2 = (String) fields.get("host", (Object) null);
        int i2 = fields.get(DeploymentDescriptorParser.ATTR_PORT, -1);
        String str3 = (String) fields.get("authority", (Object) null);
        String str4 = (String) fields.get(DeploymentDescriptorParser.ATTR_FILE, (Object) null);
        String str5 = (String) fields.get("ref", (Object) null);
        int i3 = fields.get("hashCode", -1);
        if (str3 == null && ((str2 != null && str2.length() > 0) || i2 != -1)) {
            if (str2 == null) {
                str2 = "";
            }
            str3 = i2 == -1 ? str2 : str2 + CallSiteDescriptor.TOKEN_DELIMITER + i2;
        }
        this.tempState = new UrlDeserializedState(str, str2, i2, str3, str4, str5, i3);
    }

    private Object readResolve() throws ObjectStreamException {
        URL deserializedFields;
        URLStreamHandler uRLStreamHandler = getURLStreamHandler(this.tempState.getProtocol());
        if (isBuiltinStreamHandler(uRLStreamHandler.getClass().getName())) {
            deserializedFields = fabricateNewURL();
        } else {
            deserializedFields = setDeserializedFields(uRLStreamHandler);
        }
        return deserializedFields;
    }

    private URL setDeserializedFields(URLStreamHandler uRLStreamHandler) {
        int iIndexOf;
        String strSubstring = null;
        String protocol = this.tempState.getProtocol();
        String host = this.tempState.getHost();
        int port = this.tempState.getPort();
        String authority = this.tempState.getAuthority();
        String file = this.tempState.getFile();
        String ref = this.tempState.getRef();
        int hashCode = this.tempState.getHashCode();
        if (authority == null && ((host != null && host.length() > 0) || port != -1)) {
            if (host == null) {
                host = "";
            }
            authority = port == -1 ? host : host + CallSiteDescriptor.TOKEN_DELIMITER + port;
            int iLastIndexOf = host.lastIndexOf(64);
            if (iLastIndexOf != -1) {
                strSubstring = host.substring(0, iLastIndexOf);
                host = host.substring(iLastIndexOf + 1);
            }
        } else if (authority != null && (iIndexOf = authority.indexOf(64)) != -1) {
            strSubstring = authority.substring(0, iIndexOf);
        }
        String strSubstring2 = null;
        String strSubstring3 = null;
        if (file != null) {
            int iLastIndexOf2 = file.lastIndexOf(63);
            if (iLastIndexOf2 != -1) {
                strSubstring3 = file.substring(iLastIndexOf2 + 1);
                strSubstring2 = file.substring(0, iLastIndexOf2);
            } else {
                strSubstring2 = file;
            }
        }
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.file = file;
        this.authority = authority;
        this.ref = ref;
        this.hashCode = hashCode;
        this.handler = uRLStreamHandler;
        this.query = strSubstring3;
        this.path = strSubstring2;
        this.userInfo = strSubstring;
        return this;
    }

    private URL fabricateNewURL() throws InvalidObjectException {
        String strReconstituteUrlString = this.tempState.reconstituteUrlString();
        try {
            URL url = new URL(strReconstituteUrlString);
            url.setSerializedHashCode(this.tempState.getHashCode());
            resetState();
            return url;
        } catch (MalformedURLException e2) {
            resetState();
            InvalidObjectException invalidObjectException = new InvalidObjectException("Malformed URL: " + strReconstituteUrlString);
            invalidObjectException.initCause(e2);
            throw invalidObjectException;
        }
    }

    boolean isBuiltinStreamHandler(URLStreamHandler uRLStreamHandler) {
        Class<?> cls = uRLStreamHandler.getClass();
        return isBuiltinStreamHandler(cls.getName()) || VM.isSystemDomainLoader(cls.getClassLoader());
    }

    private boolean isBuiltinStreamHandler(String str) {
        return str.startsWith(BUILTIN_HANDLERS_PREFIX);
    }

    private void resetState() {
        this.protocol = null;
        this.host = null;
        this.port = -1;
        this.file = null;
        this.authority = null;
        this.ref = null;
        this.hashCode = -1;
        this.handler = null;
        this.query = null;
        this.path = null;
        this.userInfo = null;
        this.tempState = null;
    }

    private void setSerializedHashCode(int i2) {
        this.hashCode = i2;
    }
}
