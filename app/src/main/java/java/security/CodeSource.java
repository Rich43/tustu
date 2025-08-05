package java.security;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.SocketPermission;
import java.net.URL;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Hashtable;
import sun.misc.IOUtils;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/security/CodeSource.class */
public class CodeSource implements Serializable {
    private static final long serialVersionUID = 4977541819976013951L;
    private URL location;
    private transient CodeSigner[] signers;
    private transient java.security.cert.Certificate[] certs;
    private transient SocketPermission sp;
    private transient CertificateFactory factory;

    public CodeSource(URL url, java.security.cert.Certificate[] certificateArr) {
        this.signers = null;
        this.certs = null;
        this.factory = null;
        this.location = url;
        if (certificateArr != null) {
            this.certs = (java.security.cert.Certificate[]) certificateArr.clone();
        }
    }

    public CodeSource(URL url, CodeSigner[] codeSignerArr) {
        this.signers = null;
        this.certs = null;
        this.factory = null;
        this.location = url;
        if (codeSignerArr != null) {
            this.signers = (CodeSigner[]) codeSignerArr.clone();
        }
    }

    public int hashCode() {
        if (this.location != null) {
            return this.location.hashCode();
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CodeSource)) {
            return false;
        }
        CodeSource codeSource = (CodeSource) obj;
        if (this.location == null) {
            if (codeSource.location != null) {
                return false;
            }
        } else if (!this.location.equals(codeSource.location)) {
            return false;
        }
        return matchCerts(codeSource, true);
    }

    public final URL getLocation() {
        return this.location;
    }

    public final java.security.cert.Certificate[] getCertificates() {
        if (this.certs != null) {
            return (java.security.cert.Certificate[]) this.certs.clone();
        }
        if (this.signers != null) {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < this.signers.length; i2++) {
                arrayList.addAll(this.signers[i2].getSignerCertPath().getCertificates());
            }
            this.certs = (java.security.cert.Certificate[]) arrayList.toArray(new java.security.cert.Certificate[arrayList.size()]);
            return (java.security.cert.Certificate[]) this.certs.clone();
        }
        return null;
    }

    public final CodeSigner[] getCodeSigners() {
        if (this.signers != null) {
            return (CodeSigner[]) this.signers.clone();
        }
        if (this.certs != null) {
            this.signers = convertCertArrayToSignerArray(this.certs);
            return (CodeSigner[]) this.signers.clone();
        }
        return null;
    }

    public boolean implies(CodeSource codeSource) {
        return codeSource != null && matchCerts(codeSource, false) && matchLocation(codeSource);
    }

    private boolean matchCerts(CodeSource codeSource, boolean z2) {
        if (this.certs == null && this.signers == null) {
            if (z2) {
                return codeSource.certs == null && codeSource.signers == null;
            }
            return true;
        }
        if (this.signers != null && codeSource.signers != null) {
            if (z2 && this.signers.length != codeSource.signers.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.signers.length; i2++) {
                boolean z3 = false;
                int i3 = 0;
                while (true) {
                    if (i3 >= codeSource.signers.length) {
                        break;
                    }
                    if (!this.signers[i2].equals(codeSource.signers[i3])) {
                        i3++;
                    } else {
                        z3 = true;
                        break;
                    }
                }
                if (!z3) {
                    return false;
                }
            }
            return true;
        }
        if (this.certs != null && codeSource.certs != null) {
            if (z2 && this.certs.length != codeSource.certs.length) {
                return false;
            }
            for (int i4 = 0; i4 < this.certs.length; i4++) {
                boolean z4 = false;
                int i5 = 0;
                while (true) {
                    if (i5 >= codeSource.certs.length) {
                        break;
                    }
                    if (!this.certs[i4].equals(codeSource.certs[i5])) {
                        i5++;
                    } else {
                        z4 = true;
                        break;
                    }
                }
                if (!z4) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean matchLocation(CodeSource codeSource) {
        if (this.location == null) {
            return true;
        }
        if (codeSource == null || codeSource.location == null) {
            return false;
        }
        if (this.location.equals(codeSource.location)) {
            return true;
        }
        if (!this.location.getProtocol().equalsIgnoreCase(codeSource.location.getProtocol())) {
            return false;
        }
        int port = this.location.getPort();
        if (port != -1) {
            int port2 = codeSource.location.getPort();
            if (port != (port2 != -1 ? port2 : codeSource.location.getDefaultPort())) {
                return false;
            }
        }
        if (this.location.getFile().endsWith("/-")) {
            if (!codeSource.location.getFile().startsWith(this.location.getFile().substring(0, this.location.getFile().length() - 1))) {
                return false;
            }
        } else if (this.location.getFile().endsWith("/*")) {
            int iLastIndexOf = codeSource.location.getFile().lastIndexOf(47);
            if (iLastIndexOf == -1) {
                return false;
            }
            if (!codeSource.location.getFile().substring(0, iLastIndexOf + 1).equals(this.location.getFile().substring(0, this.location.getFile().length() - 1))) {
                return false;
            }
        } else if (!codeSource.location.getFile().equals(this.location.getFile()) && !codeSource.location.getFile().equals(this.location.getFile() + "/")) {
            return false;
        }
        if (this.location.getRef() != null && !this.location.getRef().equals(codeSource.location.getRef())) {
            return false;
        }
        String host = this.location.getHost();
        String host2 = codeSource.location.getHost();
        if (host != null) {
            if (((!"".equals(host) && !"localhost".equals(host)) || (!"".equals(host2) && !"localhost".equals(host2))) && !host.equals(host2)) {
                if (host2 == null) {
                    return false;
                }
                if (this.sp == null) {
                    this.sp = new SocketPermission(host, SecurityConstants.SOCKET_RESOLVE_ACTION);
                }
                if (codeSource.sp == null) {
                    codeSource.sp = new SocketPermission(host2, SecurityConstants.SOCKET_RESOLVE_ACTION);
                }
                if (!this.sp.implies(codeSource.sp)) {
                    return false;
                }
                return true;
            }
            return true;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append((Object) this.location);
        if (this.certs != null && this.certs.length > 0) {
            for (int i2 = 0; i2 < this.certs.length; i2++) {
                sb.append(" " + ((Object) this.certs[i2]));
            }
        } else if (this.signers != null && this.signers.length > 0) {
            for (int i3 = 0; i3 < this.signers.length; i3++) {
                sb.append(" " + ((Object) this.signers[i3]));
            }
        } else {
            sb.append(" <no signer certificates>");
        }
        sb.append(")");
        return sb.toString();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (this.certs == null || this.certs.length == 0) {
            objectOutputStream.writeInt(0);
        } else {
            objectOutputStream.writeInt(this.certs.length);
            for (int i2 = 0; i2 < this.certs.length; i2++) {
                java.security.cert.Certificate certificate = this.certs[i2];
                try {
                    objectOutputStream.writeUTF(certificate.getType());
                    byte[] encoded = certificate.getEncoded();
                    objectOutputStream.writeInt(encoded.length);
                    objectOutputStream.write(encoded);
                } catch (CertificateEncodingException e2) {
                    throw new IOException(e2.getMessage());
                }
            }
        }
        if (this.signers != null && this.signers.length > 0) {
            objectOutputStream.writeObject(this.signers);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        CertificateFactory certificateFactory;
        Hashtable hashtable = null;
        ArrayList arrayList = null;
        objectInputStream.defaultReadObject();
        int i2 = objectInputStream.readInt();
        if (i2 > 0) {
            hashtable = new Hashtable(3);
            arrayList = new ArrayList(i2 > 20 ? 20 : i2);
        } else if (i2 < 0) {
            throw new IOException("size cannot be negative");
        }
        for (int i3 = 0; i3 < i2; i3++) {
            String utf = objectInputStream.readUTF();
            if (hashtable.containsKey(utf)) {
                certificateFactory = (CertificateFactory) hashtable.get(utf);
            } else {
                try {
                    certificateFactory = CertificateFactory.getInstance(utf);
                    hashtable.put(utf, certificateFactory);
                } catch (CertificateException e2) {
                    throw new ClassNotFoundException("Certificate factory for " + utf + " not found");
                }
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(IOUtils.readExactlyNBytes(objectInputStream, objectInputStream.readInt()));
            try {
                arrayList.add(certificateFactory.generateCertificate(byteArrayInputStream));
                byteArrayInputStream.close();
            } catch (CertificateException e3) {
                throw new IOException(e3.getMessage());
            }
        }
        if (arrayList != null) {
            this.certs = (java.security.cert.Certificate[]) arrayList.toArray(new java.security.cert.Certificate[i2]);
        }
        try {
            this.signers = (CodeSigner[]) ((CodeSigner[]) objectInputStream.readObject()).clone();
        } catch (IOException e4) {
        }
    }

    private CodeSigner[] convertCertArrayToSignerArray(java.security.cert.Certificate[] certificateArr) {
        if (certificateArr == null) {
            return null;
        }
        try {
            if (this.factory == null) {
                this.factory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
            }
            int i2 = 0;
            ArrayList arrayList = new ArrayList();
            while (i2 < certificateArr.length) {
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(certificateArr[i2]);
                int i3 = i2 + 1;
                while (i3 < certificateArr.length && (certificateArr[i3] instanceof X509Certificate) && ((X509Certificate) certificateArr[i3]).getBasicConstraints() != -1) {
                    arrayList2.add(certificateArr[i3]);
                    i3++;
                }
                i2 = i3;
                arrayList.add(new CodeSigner(this.factory.generateCertPath(arrayList2), null));
            }
            if (arrayList.isEmpty()) {
                return null;
            }
            return (CodeSigner[]) arrayList.toArray(new CodeSigner[arrayList.size()]);
        } catch (CertificateException e2) {
            return null;
        }
    }
}
