package java.security.cert;

import java.io.ByteArrayInputStream;
import java.io.NotSerializableException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:java/security/cert/CertPath.class */
public abstract class CertPath implements Serializable {
    private static final long serialVersionUID = 6068470306649138683L;
    private String type;

    public abstract Iterator<String> getEncodings();

    public abstract byte[] getEncoded() throws CertificateEncodingException;

    public abstract byte[] getEncoded(String str) throws CertificateEncodingException;

    public abstract List<? extends Certificate> getCertificates();

    protected CertPath(String str) {
        this.type = str;
    }

    public String getType() {
        return this.type;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CertPath)) {
            return false;
        }
        CertPath certPath = (CertPath) obj;
        if (!certPath.getType().equals(this.type)) {
            return false;
        }
        return getCertificates().equals(certPath.getCertificates());
    }

    public int hashCode() {
        return (31 * this.type.hashCode()) + getCertificates().hashCode();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator<? extends Certificate> it = getCertificates().iterator();
        stringBuffer.append("\n" + this.type + " Cert Path: length = " + getCertificates().size() + ".\n");
        stringBuffer.append("[\n");
        int i2 = 1;
        while (it.hasNext()) {
            stringBuffer.append("=========================================================Certificate " + i2 + " start.\n");
            stringBuffer.append(it.next().toString());
            stringBuffer.append("\n=========================================================Certificate " + i2 + " end.\n\n\n");
            i2++;
        }
        stringBuffer.append("\n]");
        return stringBuffer.toString();
    }

    protected Object writeReplace() throws ObjectStreamException {
        try {
            return new CertPathRep(this.type, getEncoded());
        } catch (CertificateException e2) {
            NotSerializableException notSerializableException = new NotSerializableException("java.security.cert.CertPath: " + this.type);
            notSerializableException.initCause(e2);
            throw notSerializableException;
        }
    }

    /* loaded from: rt.jar:java/security/cert/CertPath$CertPathRep.class */
    protected static class CertPathRep implements Serializable {
        private static final long serialVersionUID = 3015633072427920915L;
        private String type;
        private byte[] data;

        protected CertPathRep(String str, byte[] bArr) {
            this.type = str;
            this.data = bArr;
        }

        protected Object readResolve() throws ObjectStreamException {
            try {
                return CertificateFactory.getInstance(this.type).generateCertPath(new ByteArrayInputStream(this.data));
            } catch (CertificateException e2) {
                NotSerializableException notSerializableException = new NotSerializableException("java.security.cert.CertPath: " + this.type);
                notSerializableException.initCause(e2);
                throw notSerializableException;
            }
        }
    }
}
