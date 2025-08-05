package java.security;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

@Deprecated
/* loaded from: rt.jar:java/security/Identity.class */
public abstract class Identity implements Principal, Serializable {
    private static final long serialVersionUID = 3609922007826600659L;
    private String name;
    private PublicKey publicKey;
    String info;
    IdentityScope scope;
    Vector<Certificate> certificates;

    protected Identity() {
        this("restoring...");
    }

    public Identity(String str, IdentityScope identityScope) throws KeyManagementException {
        this(str);
        if (identityScope != null) {
            identityScope.addIdentity(this);
        }
        this.scope = identityScope;
    }

    public Identity(String str) {
        this.info = "No further information available.";
        this.name = str;
    }

    @Override // java.security.Principal
    public final String getName() {
        return this.name;
    }

    public final IdentityScope getScope() {
        return this.scope;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(PublicKey publicKey) throws KeyManagementException {
        check("setIdentityPublicKey");
        this.publicKey = publicKey;
        this.certificates = new Vector<>();
    }

    public void setInfo(String str) {
        check("setIdentityInfo");
        this.info = str;
    }

    public String getInfo() {
        return this.info;
    }

    public void addCertificate(Certificate certificate) throws KeyManagementException {
        check("addIdentityCertificate");
        if (this.certificates == null) {
            this.certificates = new Vector<>();
        }
        if (this.publicKey != null) {
            if (!keyEquals(this.publicKey, certificate.getPublicKey())) {
                throw new KeyManagementException("public key different from cert public key");
            }
        } else {
            this.publicKey = certificate.getPublicKey();
        }
        this.certificates.addElement(certificate);
    }

    private boolean keyEquals(PublicKey publicKey, PublicKey publicKey2) {
        String format = publicKey.getFormat();
        String format2 = publicKey2.getFormat();
        if ((format == null) ^ (format2 == null)) {
            return false;
        }
        if (format != null && format2 != null && !format.equalsIgnoreCase(format2)) {
            return false;
        }
        return Arrays.equals(publicKey.getEncoded(), publicKey2.getEncoded());
    }

    public void removeCertificate(Certificate certificate) throws KeyManagementException {
        check("removeIdentityCertificate");
        if (this.certificates != null) {
            this.certificates.removeElement(certificate);
        }
    }

    public Certificate[] certificates() {
        if (this.certificates == null) {
            return new Certificate[0];
        }
        Certificate[] certificateArr = new Certificate[this.certificates.size()];
        this.certificates.copyInto(certificateArr);
        return certificateArr;
    }

    @Override // java.security.Principal
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Identity) {
            Identity identity = (Identity) obj;
            if (fullName().equals(identity.fullName())) {
                return true;
            }
            return identityEquals(identity);
        }
        return false;
    }

    protected boolean identityEquals(Identity identity) {
        if (!this.name.equalsIgnoreCase(identity.name)) {
            return false;
        }
        if ((this.publicKey == null) ^ (identity.publicKey == null)) {
            return false;
        }
        if (this.publicKey != null && identity.publicKey != null && !this.publicKey.equals(identity.publicKey)) {
            return false;
        }
        return true;
    }

    String fullName() {
        String str = this.name;
        if (this.scope != null) {
            str = str + "." + this.scope.getName();
        }
        return str;
    }

    @Override // java.security.Principal
    public String toString() {
        check("printIdentity");
        String str = this.name;
        if (this.scope != null) {
            str = str + "[" + this.scope.getName() + "]";
        }
        return str;
    }

    public String toString(boolean z2) {
        String string = toString();
        if (z2) {
            String str = ((string + "\n") + printKeys()) + "\n" + printCertificates();
            if (this.info != null) {
                string = str + "\n\t" + this.info;
            } else {
                string = str + "\n\tno additional information available.";
            }
        }
        return string;
    }

    String printKeys() {
        String str;
        if (this.publicKey != null) {
            str = "\tpublic key initialized";
        } else {
            str = "\tno public key";
        }
        return str;
    }

    String printCertificates() {
        if (this.certificates != null) {
            String str = "\tcertificates: \n";
            int i2 = 1;
            Iterator<Certificate> it = this.certificates.iterator();
            while (it.hasNext()) {
                Certificate next = it.next();
                int i3 = i2;
                i2++;
                str = (str + "\tcertificate " + i3 + "\tfor  : " + ((Object) next.getPrincipal()) + "\n") + "\t\t\tfrom : " + ((Object) next.getGuarantor()) + "\n";
            }
            return str;
        }
        return "\tno certificates";
    }

    @Override // java.security.Principal
    public int hashCode() {
        return this.name.hashCode();
    }

    private static void check(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSecurityAccess(str);
        }
    }
}
