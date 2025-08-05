package sun.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.util.Enumeration;

/* loaded from: rt.jar:sun/security/x509/CertAttrSet.class */
public interface CertAttrSet<T> {
    String toString();

    void encode(OutputStream outputStream) throws IOException, CertificateException;

    void set(String str, Object obj) throws IOException, CertificateException;

    Object get(String str) throws IOException, CertificateException;

    void delete(String str) throws IOException, CertificateException;

    Enumeration<T> getElements();

    String getName();
}
