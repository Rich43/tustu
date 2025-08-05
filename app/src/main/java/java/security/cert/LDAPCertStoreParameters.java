package java.security.cert;

/* loaded from: rt.jar:java/security/cert/LDAPCertStoreParameters.class */
public class LDAPCertStoreParameters implements CertStoreParameters {
    private static final int LDAP_DEFAULT_PORT = 389;
    private int port;
    private String serverName;

    public LDAPCertStoreParameters(String str, int i2) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.serverName = str;
        this.port = i2;
    }

    public LDAPCertStoreParameters(String str) {
        this(str, 389);
    }

    public LDAPCertStoreParameters() {
        this("localhost", 389);
    }

    public String getServerName() {
        return this.serverName;
    }

    public int getPort() {
        return this.port;
    }

    @Override // java.security.cert.CertStoreParameters
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2.toString(), e2);
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("LDAPCertStoreParameters: [\n");
        stringBuffer.append("  serverName: " + this.serverName + "\n");
        stringBuffer.append("  port: " + this.port + "\n");
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
