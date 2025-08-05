package javax.management.remote;

import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.BitSet;
import java.util.StringTokenizer;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/management/remote/JMXServiceURL.class */
public class JMXServiceURL implements Serializable {
    private static final long serialVersionUID = 8173364409860779292L;
    private static final String INVALID_INSTANCE_MSG = "Trying to deserialize an invalid instance of JMXServiceURL";
    private static final Exception randomException = new Exception();
    private static final BitSet alphaBitSet = new BitSet(128);
    private static final BitSet numericBitSet = new BitSet(128);
    private static final BitSet alphaNumericBitSet = new BitSet(128);
    private static final BitSet protocolBitSet = new BitSet(128);
    private static final BitSet hostNameBitSet = new BitSet(128);
    private String protocol;
    private String host;
    private int port;
    private String urlPath;
    private transient String toString;
    private static final ClassLogger logger;

    public JMXServiceURL(String str) throws Exception {
        int iIndexOfFirstNotInSet;
        int iIndexOfFirstNotInSet2;
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt < ' ' || cCharAt >= 127) {
                throw new MalformedURLException("Service URL contains non-ASCII character 0x" + Integer.toHexString(cCharAt));
            }
        }
        int length2 = "service:jmx:".length();
        if (!str.regionMatches(true, 0, "service:jmx:", 0, length2)) {
            throw new MalformedURLException("Service URL must start with service:jmx:");
        }
        int iIndexOf = indexOf(str, ':', length2);
        this.protocol = str.substring(length2, iIndexOf).toLowerCase();
        if (!str.regionMatches(iIndexOf, "://", 0, 3)) {
            throw new MalformedURLException("Missing \"://\" after protocol name");
        }
        int i3 = iIndexOf + 3;
        if (i3 < length && str.charAt(i3) == '[') {
            iIndexOfFirstNotInSet = str.indexOf(93, i3) + 1;
            if (iIndexOfFirstNotInSet == 0) {
                throw new MalformedURLException("Bad host name: [ without ]");
            }
            this.host = str.substring(i3 + 1, iIndexOfFirstNotInSet - 1);
            if (!isNumericIPv6Address(this.host)) {
                throw new MalformedURLException("Address inside [...] must be numeric IPv6 address");
            }
        } else {
            iIndexOfFirstNotInSet = indexOfFirstNotInSet(str, hostNameBitSet, i3);
            this.host = str.substring(i3, iIndexOfFirstNotInSet);
        }
        if (iIndexOfFirstNotInSet < length && str.charAt(iIndexOfFirstNotInSet) == ':') {
            if (this.host.length() == 0) {
                throw new MalformedURLException("Cannot give port number without host name");
            }
            int i4 = iIndexOfFirstNotInSet + 1;
            iIndexOfFirstNotInSet2 = indexOfFirstNotInSet(str, numericBitSet, i4);
            String strSubstring = str.substring(i4, iIndexOfFirstNotInSet2);
            try {
                this.port = Integer.parseInt(strSubstring);
            } catch (NumberFormatException e2) {
                throw new MalformedURLException("Bad port number: \"" + strSubstring + "\": " + ((Object) e2));
            }
        } else {
            iIndexOfFirstNotInSet2 = iIndexOfFirstNotInSet;
            this.port = 0;
        }
        int i5 = iIndexOfFirstNotInSet2;
        if (i5 < length) {
            this.urlPath = str.substring(i5);
        } else {
            this.urlPath = "";
        }
        validate();
    }

    public JMXServiceURL(String str, String str2, int i2) throws MalformedURLException {
        this(str, str2, i2, null);
    }

    public JMXServiceURL(String str, String str2, int i2, String str3) throws Exception {
        str = str == null ? "jmxmp" : str;
        if (str2 == null) {
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                str2 = localHost.getHostName();
                try {
                    validateHost(str2, i2);
                } catch (MalformedURLException e2) {
                    if (logger.fineOn()) {
                        logger.fine("JMXServiceURL", "Replacing illegal local host name " + str2 + " with numeric IP address (see RFC 1034)", e2);
                    }
                    str2 = localHost.getHostAddress();
                }
            } catch (UnknownHostException e3) {
                throw new MalformedURLException("Local host name unknown: " + ((Object) e3));
            }
        }
        if (str2.startsWith("[")) {
            if (!str2.endsWith("]")) {
                throw new MalformedURLException("Host starts with [ but does not end with ]");
            }
            str2 = str2.substring(1, str2.length() - 1);
            if (!isNumericIPv6Address(str2)) {
                throw new MalformedURLException("Address inside [...] must be numeric IPv6 address");
            }
            if (str2.startsWith("[")) {
                throw new MalformedURLException("More than one [[...]]");
            }
        }
        this.protocol = str.toLowerCase();
        this.host = str2;
        this.port = i2;
        this.urlPath = str3 == null ? "" : str3;
        validate();
    }

    private void readObject(ObjectInputStream objectInputStream) throws Exception {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        String str = (String) fields.get("host", (Object) null);
        int i2 = fields.get(DeploymentDescriptorParser.ATTR_PORT, -1);
        String str2 = (String) fields.get("protocol", (Object) null);
        String str3 = (String) fields.get("urlPath", (Object) null);
        if (str2 == null || str3 == null || str == null) {
            StringBuilder sbAppend = new StringBuilder(INVALID_INSTANCE_MSG).append('[');
            boolean z2 = true;
            if (str2 == null) {
                sbAppend.append("protocol=null");
                z2 = false;
            }
            if (str == null) {
                sbAppend.append(z2 ? "" : ",").append("host=null");
                z2 = false;
            }
            if (str3 == null) {
                sbAppend.append(z2 ? "" : ",").append("urlPath=null");
            }
            sbAppend.append(']');
            throw new InvalidObjectException(sbAppend.toString());
        }
        if (str.contains("[") || str.contains("]")) {
            throw new InvalidObjectException("Invalid host name: " + str);
        }
        try {
            validate(str2, str, i2, str3);
            this.protocol = str2;
            this.host = str;
            this.port = i2;
            this.urlPath = str3;
        } catch (MalformedURLException e2) {
            throw new InvalidObjectException("Trying to deserialize an invalid instance of JMXServiceURL: " + e2.getMessage());
        }
    }

    private void validate(String str, String str2, int i2, String str3) throws Exception {
        int iIndexOfFirstNotInSet = indexOfFirstNotInSet(str, protocolBitSet, 0);
        if (iIndexOfFirstNotInSet == 0 || iIndexOfFirstNotInSet < str.length() || !alphaBitSet.get(str.charAt(0))) {
            throw new MalformedURLException("Missing or invalid protocol name: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        validateHost(str2, i2);
        if (i2 < 0) {
            throw new MalformedURLException("Bad port: " + i2);
        }
        if (str3.length() > 0 && !str3.startsWith("/") && !str3.startsWith(";")) {
            throw new MalformedURLException("Bad URL path: " + str3);
        }
    }

    private void validate() throws Exception {
        validate(this.protocol, this.host, this.port, this.urlPath);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v17 */
    /* JADX WARN: Type inference failed for: r0v45 */
    /* JADX WARN: Type inference failed for: r0v51 */
    /* JADX WARN: Type inference failed for: r0v52 */
    /* JADX WARN: Type inference failed for: r0v53 */
    /* JADX WARN: Type inference failed for: r0v54 */
    /* JADX WARN: Type inference failed for: r0v55 */
    /* JADX WARN: Type inference failed for: r0v56 */
    private static void validateHost(String str, int i2) throws Exception {
        ?? r0;
        if (str.length() == 0) {
            if (i2 != 0) {
                throw new MalformedURLException("Cannot give port number without host name");
            }
            return;
        }
        if (isNumericIPv6Address(str)) {
            try {
                InetAddress.getByName(str);
                return;
            } catch (Exception e2) {
                MalformedURLException malformedURLException = new MalformedURLException("Bad IPv6 address: " + str);
                EnvHelp.initCause(malformedURLException, e2);
                throw malformedURLException;
            }
        }
        int length = str.length();
        boolean z2 = 46;
        boolean z3 = false;
        char c2 = 0;
        int i3 = 0;
        while (true) {
            if (i3 >= length) {
                break;
            }
            char cCharAt = str.charAt(i3);
            boolean z4 = alphaNumericBitSet.get(cCharAt);
            if (z2 == 46) {
                c2 = cCharAt;
            }
            if (z4) {
                r0 = 97;
            } else if (cCharAt == '-') {
                if (z2 == 46) {
                    break;
                } else {
                    r0 = 45;
                }
            } else if (cCharAt == '.') {
                z3 = true;
                if (z2 != 97) {
                    break;
                } else {
                    r0 = 46;
                }
            } else {
                z2 = 46;
                break;
            }
            z2 = r0;
            i3++;
        }
        try {
            if (z2 != 97) {
                throw randomException;
            }
            if (z3 && !alphaBitSet.get(c2)) {
                StringTokenizer stringTokenizer = new StringTokenizer(str, ".", true);
                for (int i4 = 0; i4 < 4; i4++) {
                    int i5 = Integer.parseInt(stringTokenizer.nextToken());
                    if (i5 < 0 || i5 > 255) {
                        throw randomException;
                    }
                    if (i4 < 3 && !stringTokenizer.nextToken().equals(".")) {
                        throw randomException;
                    }
                }
                if (stringTokenizer.hasMoreTokens()) {
                    throw randomException;
                }
            }
        } catch (Exception e3) {
            throw new MalformedURLException("Bad host: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
    }

    static {
        char c2 = '0';
        while (true) {
            char c3 = c2;
            if (c3 > '9') {
                break;
            }
            numericBitSet.set(c3);
            c2 = (char) (c3 + 1);
        }
        char c4 = 'A';
        while (true) {
            char c5 = c4;
            if (c5 > 'Z') {
                break;
            }
            alphaBitSet.set(c5);
            c4 = (char) (c5 + 1);
        }
        char c6 = 'a';
        while (true) {
            char c7 = c6;
            if (c7 <= 'z') {
                alphaBitSet.set(c7);
                c6 = (char) (c7 + 1);
            } else {
                alphaNumericBitSet.or(alphaBitSet);
                alphaNumericBitSet.or(numericBitSet);
                protocolBitSet.or(alphaNumericBitSet);
                protocolBitSet.set(43);
                protocolBitSet.set(45);
                hostNameBitSet.or(alphaNumericBitSet);
                hostNameBitSet.set(45);
                hostNameBitSet.set(46);
                logger = new ClassLogger("javax.management.remote.misc", "JMXServiceURL");
                return;
            }
        }
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getURLPath() {
        return this.urlPath;
    }

    public String toString() {
        if (this.toString != null) {
            return this.toString;
        }
        StringBuilder sb = new StringBuilder("service:jmx:");
        sb.append(getProtocol()).append("://");
        String host = getHost();
        if (isNumericIPv6Address(host)) {
            sb.append('[').append(host).append(']');
        } else {
            sb.append(host);
        }
        int port = getPort();
        if (port != 0) {
            sb.append(':').append(port);
        }
        sb.append(getURLPath());
        this.toString = sb.toString();
        return this.toString;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof JMXServiceURL)) {
            return false;
        }
        JMXServiceURL jMXServiceURL = (JMXServiceURL) obj;
        return jMXServiceURL.getProtocol().equalsIgnoreCase(getProtocol()) && jMXServiceURL.getHost().equalsIgnoreCase(getHost()) && jMXServiceURL.getPort() == getPort() && jMXServiceURL.getURLPath().equals(getURLPath());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    private static boolean isNumericIPv6Address(String str) {
        return str.indexOf(58) >= 0;
    }

    private static int indexOf(String str, char c2, int i2) {
        int iIndexOf = str.indexOf(c2, i2);
        if (iIndexOf < 0) {
            return str.length();
        }
        return iIndexOf;
    }

    private static int indexOfFirstNotInSet(String str, BitSet bitSet, int i2) {
        char cCharAt;
        int length = str.length();
        int i3 = i2;
        while (i3 < length && (cCharAt = str.charAt(i3)) < 128 && bitSet.get(cCharAt)) {
            i3++;
        }
        return i3;
    }
}
