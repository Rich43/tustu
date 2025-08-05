package sun.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/CRLExtensions.class */
public class CRLExtensions {
    private Map<String, Extension> map = Collections.synchronizedMap(new TreeMap());
    private boolean unsupportedCritExt = false;
    private static final Class[] PARAMS = {Boolean.class, Object.class};

    public CRLExtensions() {
    }

    public CRLExtensions(DerInputStream derInputStream) throws CRLException {
        init(derInputStream);
    }

    private void init(DerInputStream derInputStream) throws CRLException {
        try {
            DerInputStream derInputStream2 = derInputStream;
            byte bPeekByte = (byte) derInputStream.peekByte();
            if ((bPeekByte & 192) == 128 && (bPeekByte & 31) == 0) {
                derInputStream2 = derInputStream2.getDerValue().data;
            }
            for (DerValue derValue : derInputStream2.getSequence(5)) {
                parseExtension(new Extension(derValue));
            }
        } catch (IOException e2) {
            throw new CRLException("Parsing error: " + e2.toString());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void parseExtension(Extension extension) throws CRLException {
        try {
            Class<?> cls = OIDMap.getClass(extension.getExtensionId());
            if (cls == null) {
                if (extension.isCritical()) {
                    this.unsupportedCritExt = true;
                }
                if (this.map.put(extension.getExtensionId().toString(), extension) != null) {
                    throw new CRLException("Duplicate extensions not allowed");
                }
                return;
            }
            CertAttrSet certAttrSet = (CertAttrSet) cls.getConstructor(PARAMS).newInstance(Boolean.valueOf(extension.isCritical()), extension.getExtensionValue());
            if (this.map.put(certAttrSet.getName(), (Extension) certAttrSet) != null) {
                throw new CRLException("Duplicate extensions not allowed");
            }
        } catch (InvocationTargetException e2) {
            throw new CRLException(e2.getTargetException().getMessage());
        } catch (Exception e3) {
            throw new CRLException(e3.toString());
        }
    }

    public void encode(OutputStream outputStream, boolean z2) throws CRLException {
        try {
            DerOutputStream derOutputStream = new DerOutputStream();
            Object[] array = this.map.values().toArray();
            for (int i2 = 0; i2 < array.length; i2++) {
                if (array[i2] instanceof CertAttrSet) {
                    ((CertAttrSet) array[i2]).encode(derOutputStream);
                } else if (array[i2] instanceof Extension) {
                    ((Extension) array[i2]).encode(derOutputStream);
                } else {
                    throw new CRLException("Illegal extension object");
                }
            }
            DerOutputStream derOutputStream2 = new DerOutputStream();
            derOutputStream2.write((byte) 48, derOutputStream);
            DerOutputStream derOutputStream3 = new DerOutputStream();
            if (z2) {
                derOutputStream3.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
            } else {
                derOutputStream3 = derOutputStream2;
            }
            outputStream.write(derOutputStream3.toByteArray());
        } catch (IOException e2) {
            throw new CRLException("Encoding error: " + e2.toString());
        } catch (CertificateException e3) {
            throw new CRLException("Encoding error: " + e3.toString());
        }
    }

    public Extension get(String str) {
        String strSubstring;
        if (new X509AttributeName(str).getPrefix().equalsIgnoreCase(X509CertImpl.NAME)) {
            strSubstring = str.substring(str.lastIndexOf(".") + 1);
        } else {
            strSubstring = str;
        }
        return this.map.get(strSubstring);
    }

    public void set(String str, Object obj) {
        this.map.put(str, (Extension) obj);
    }

    public void delete(String str) {
        this.map.remove(str);
    }

    public Enumeration<Extension> getElements() {
        return Collections.enumeration(this.map.values());
    }

    public Collection<Extension> getAllExtensions() {
        return this.map.values();
    }

    public boolean hasUnsupportedCriticalExtension() {
        return this.unsupportedCritExt;
    }

    public boolean equals(Object obj) {
        Object[] array;
        int length;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CRLExtensions) || (length = (array = ((CRLExtensions) obj).getAllExtensions().toArray()).length) != this.map.size()) {
            return false;
        }
        String string = null;
        for (int i2 = 0; i2 < length; i2++) {
            if (array[i2] instanceof CertAttrSet) {
                string = ((CertAttrSet) array[i2]).getName();
            }
            Extension extension = (Extension) array[i2];
            if (string == null) {
                string = extension.getExtensionId().toString();
            }
            Extension extension2 = this.map.get(string);
            if (extension2 == null || !extension2.equals(extension)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public String toString() {
        return this.map.toString();
    }
}
