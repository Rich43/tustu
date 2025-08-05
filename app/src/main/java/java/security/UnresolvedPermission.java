package java.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Hashtable;
import sun.misc.IOUtils;
import sun.security.util.Debug;

/* loaded from: rt.jar:java/security/UnresolvedPermission.class */
public final class UnresolvedPermission extends Permission implements Serializable {
    private static final long serialVersionUID = -4821973115467008846L;
    private String type;
    private String name;
    private String actions;
    private transient java.security.cert.Certificate[] certs;
    private static final Debug debug = Debug.getInstance("policy,access", "UnresolvedPermission");
    private static final Class[] PARAMS0 = new Class[0];
    private static final Class[] PARAMS1 = {String.class};
    private static final Class[] PARAMS2 = {String.class, String.class};

    public UnresolvedPermission(String str, String str2, String str3, java.security.cert.Certificate[] certificateArr) {
        super(str);
        if (str == null) {
            throw new NullPointerException("type can't be null");
        }
        certificateArr = certificateArr != null ? (java.security.cert.Certificate[]) certificateArr.clone() : certificateArr;
        this.type = str;
        this.name = str2;
        this.actions = str3;
        if (certificateArr != null) {
            for (java.security.cert.Certificate certificate : certificateArr) {
                if (!(certificate instanceof X509Certificate)) {
                    this.certs = certificateArr;
                    return;
                }
            }
            int i2 = 0;
            int i3 = 0;
            while (i2 < certificateArr.length) {
                i3++;
                while (i2 + 1 < certificateArr.length && ((X509Certificate) certificateArr[i2]).getIssuerDN().equals(((X509Certificate) certificateArr[i2 + 1]).getSubjectDN())) {
                    i2++;
                }
                i2++;
            }
            if (i3 == certificateArr.length) {
                this.certs = certificateArr;
                return;
            }
            ArrayList arrayList = new ArrayList();
            int i4 = 0;
            while (i4 < certificateArr.length) {
                arrayList.add(certificateArr[i4]);
                while (i4 + 1 < certificateArr.length && ((X509Certificate) certificateArr[i4]).getIssuerDN().equals(((X509Certificate) certificateArr[i4 + 1]).getSubjectDN())) {
                    i4++;
                }
                i4++;
            }
            this.certs = new java.security.cert.Certificate[arrayList.size()];
            arrayList.toArray(this.certs);
        }
    }

    Permission resolve(Permission permission, java.security.cert.Certificate[] certificateArr) {
        if (this.certs != null) {
            if (certificateArr == null) {
                return null;
            }
            for (int i2 = 0; i2 < this.certs.length; i2++) {
                boolean z2 = false;
                int i3 = 0;
                while (true) {
                    if (i3 >= certificateArr.length) {
                        break;
                    }
                    if (!this.certs[i2].equals(certificateArr[i3])) {
                        i3++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
                if (!z2) {
                    return null;
                }
            }
        }
        try {
            Class<?> cls = permission.getClass();
            if (this.name == null && this.actions == null) {
                try {
                    return (Permission) cls.getConstructor(PARAMS0).newInstance(new Object[0]);
                } catch (NoSuchMethodException e2) {
                    try {
                        return (Permission) cls.getConstructor(PARAMS1).newInstance(this.name);
                    } catch (NoSuchMethodException e3) {
                        return (Permission) cls.getConstructor(PARAMS2).newInstance(this.name, this.actions);
                    }
                }
            }
            if (this.name != null && this.actions == null) {
                try {
                    return (Permission) cls.getConstructor(PARAMS1).newInstance(this.name);
                } catch (NoSuchMethodException e4) {
                    return (Permission) cls.getConstructor(PARAMS2).newInstance(this.name, this.actions);
                }
            }
            return (Permission) cls.getConstructor(PARAMS2).newInstance(this.name, this.actions);
        } catch (NoSuchMethodException e5) {
            if (debug != null) {
                debug.println("NoSuchMethodException:\n  could not find proper constructor for " + this.type);
                e5.printStackTrace();
                return null;
            }
            return null;
        } catch (Exception e6) {
            if (debug != null) {
                debug.println("unable to instantiate " + this.name);
                e6.printStackTrace();
                return null;
            }
            return null;
        }
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        return false;
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UnresolvedPermission)) {
            return false;
        }
        UnresolvedPermission unresolvedPermission = (UnresolvedPermission) obj;
        if (!this.type.equals(unresolvedPermission.type)) {
            return false;
        }
        if (this.name == null) {
            if (unresolvedPermission.name != null) {
                return false;
            }
        } else if (!this.name.equals(unresolvedPermission.name)) {
            return false;
        }
        if (this.actions == null) {
            if (unresolvedPermission.actions != null) {
                return false;
            }
        } else if (!this.actions.equals(unresolvedPermission.actions)) {
            return false;
        }
        if (this.certs == null && unresolvedPermission.certs != null) {
            return false;
        }
        if (this.certs != null && unresolvedPermission.certs == null) {
            return false;
        }
        if (this.certs != null && unresolvedPermission.certs != null && this.certs.length != unresolvedPermission.certs.length) {
            return false;
        }
        for (int i2 = 0; this.certs != null && i2 < this.certs.length; i2++) {
            boolean z2 = false;
            int i3 = 0;
            while (true) {
                if (i3 >= unresolvedPermission.certs.length) {
                    break;
                }
                if (!this.certs[i2].equals(unresolvedPermission.certs[i3])) {
                    i3++;
                } else {
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                return false;
            }
        }
        for (int i4 = 0; unresolvedPermission.certs != null && i4 < unresolvedPermission.certs.length; i4++) {
            boolean z3 = false;
            int i5 = 0;
            while (true) {
                if (i5 >= this.certs.length) {
                    break;
                }
                if (!unresolvedPermission.certs[i4].equals(this.certs[i5])) {
                    i5++;
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

    @Override // java.security.Permission
    public int hashCode() {
        int iHashCode = this.type.hashCode();
        if (this.name != null) {
            iHashCode ^= this.name.hashCode();
        }
        if (this.actions != null) {
            iHashCode ^= this.actions.hashCode();
        }
        return iHashCode;
    }

    @Override // java.security.Permission
    public String getActions() {
        return "";
    }

    public String getUnresolvedType() {
        return this.type;
    }

    public String getUnresolvedName() {
        return this.name;
    }

    public String getUnresolvedActions() {
        return this.actions;
    }

    public java.security.cert.Certificate[] getUnresolvedCerts() {
        if (this.certs == null) {
            return null;
        }
        return (java.security.cert.Certificate[]) this.certs.clone();
    }

    @Override // java.security.Permission
    public String toString() {
        return "(unresolved " + this.type + " " + this.name + " " + this.actions + ")";
    }

    @Override // java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new UnresolvedPermissionCollection();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (this.certs == null || this.certs.length == 0) {
            objectOutputStream.writeInt(0);
            return;
        }
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

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        CertificateFactory certificateFactory;
        Hashtable hashtable = null;
        ArrayList arrayList = null;
        objectInputStream.defaultReadObject();
        if (this.type == null) {
            throw new NullPointerException("type can't be null");
        }
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
    }
}
