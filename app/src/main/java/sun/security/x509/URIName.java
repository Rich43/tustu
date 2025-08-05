package sun.security.x509;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/URIName.class */
public class URIName implements GeneralNameInterface {
    private URI uri;
    private String host;
    private DNSName hostDNS;
    private IPAddressName hostIP;

    public URIName(DerValue derValue) throws IOException {
        this(derValue.getIA5String());
    }

    public URIName(String str) throws IOException {
        try {
            this.uri = new URI(str);
            if (this.uri.getScheme() == null) {
                throw new IOException("URI name must include scheme:" + str);
            }
            this.host = this.uri.getHost();
            if (this.host != null) {
                if (this.host.charAt(0) == '[') {
                    try {
                        this.hostIP = new IPAddressName(this.host.substring(1, this.host.length() - 1));
                    } catch (IOException e2) {
                        throw new IOException("invalid URI name (host portion is not a valid IPv6 address):" + str);
                    }
                } else {
                    try {
                        this.hostDNS = new DNSName(this.host);
                    } catch (IOException e3) {
                        try {
                            this.hostIP = new IPAddressName(this.host);
                        } catch (Exception e4) {
                            throw new IOException("invalid URI name (host portion is not a valid DNSName, IPv4 address, or IPv6 address):" + str);
                        }
                    }
                }
            }
        } catch (URISyntaxException e5) {
            throw new IOException("invalid URI name:" + str, e5);
        }
    }

    public static URIName nameConstraint(DerValue derValue) throws IOException {
        DNSName dNSName;
        String iA5String = derValue.getIA5String();
        try {
            URI uri = new URI(iA5String);
            if (uri.getScheme() == null) {
                String schemeSpecificPart = uri.getSchemeSpecificPart();
                try {
                    if (schemeSpecificPart.startsWith(".")) {
                        dNSName = new DNSName(schemeSpecificPart.substring(1));
                    } else {
                        dNSName = new DNSName(schemeSpecificPart);
                    }
                    return new URIName(uri, schemeSpecificPart, dNSName);
                } catch (IOException e2) {
                    throw new IOException("invalid URI name constraint:" + iA5String, e2);
                }
            }
            throw new IOException("invalid URI name constraint (should not include scheme):" + iA5String);
        } catch (URISyntaxException e3) {
            throw new IOException("invalid URI name constraint:" + iA5String, e3);
        }
    }

    URIName(URI uri, String str, DNSName dNSName) {
        this.uri = uri;
        this.host = str;
        this.hostDNS = dNSName;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int getType() {
        return 6;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putIA5String(this.uri.toASCIIString());
    }

    public String toString() {
        return "URIName: " + this.uri.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof URIName)) {
            return false;
        }
        return this.uri.equals(((URIName) obj).getURI());
    }

    public URI getURI() {
        return this.uri;
    }

    public String getName() {
        return this.uri.toString();
    }

    public String getScheme() {
        return this.uri.getScheme();
    }

    public String getHost() {
        return this.host;
    }

    public Object getHostObject() {
        if (this.hostIP != null) {
            return this.hostIP;
        }
        return this.hostDNS;
    }

    public int hashCode() {
        return this.uri.hashCode();
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        int iConstrains;
        if (generalNameInterface == null || generalNameInterface.getType() != 6) {
            iConstrains = -1;
        } else {
            String host = ((URIName) generalNameInterface).getHost();
            if (host.equalsIgnoreCase(this.host)) {
                iConstrains = 0;
            } else {
                Object hostObject = ((URIName) generalNameInterface).getHostObject();
                if (this.hostDNS == null || !(hostObject instanceof DNSName)) {
                    iConstrains = 3;
                } else {
                    boolean z2 = this.host.charAt(0) == '.';
                    boolean z3 = host.charAt(0) == '.';
                    iConstrains = this.hostDNS.constrains((DNSName) hostObject);
                    if (!z2 && !z3 && (iConstrains == 2 || iConstrains == 1)) {
                        iConstrains = 3;
                    }
                    if (z2 != z3 && iConstrains == 0) {
                        if (z2) {
                            iConstrains = 2;
                        } else {
                            iConstrains = 1;
                        }
                    }
                }
            }
        }
        return iConstrains;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int subtreeDepth() throws UnsupportedOperationException {
        try {
            return new DNSName(this.host).subtreeDepth();
        } catch (IOException e2) {
            throw new UnsupportedOperationException(e2.getMessage());
        }
    }
}
