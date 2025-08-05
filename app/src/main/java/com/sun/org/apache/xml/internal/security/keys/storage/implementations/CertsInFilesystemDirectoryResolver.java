package com.sun.org.apache.xml.internal.security.keys.storage.implementations;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SKI;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverException;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverSpi;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/storage/implementations/CertsInFilesystemDirectoryResolver.class */
public class CertsInFilesystemDirectoryResolver extends StorageResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(CertsInFilesystemDirectoryResolver.class);
    private String merlinsCertificatesDir;
    private List<X509Certificate> certs = new ArrayList();

    public CertsInFilesystemDirectoryResolver(String str) throws StorageResolverException {
        this.merlinsCertificatesDir = str;
        readCertsFromHarddrive();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readCertsFromHarddrive() throws StorageResolverException {
        InputStream inputStreamNewInputStream;
        Throwable th;
        File file = new File(this.merlinsCertificatesDir);
        ArrayList arrayList = new ArrayList();
        String[] list = file.list();
        if (list != null) {
            for (int i2 = 0; i2 < list.length; i2++) {
                if (list[i2].endsWith(".crt")) {
                    arrayList.add(list[i2]);
                }
            }
        }
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                String str = file.getAbsolutePath() + File.separator + ((String) arrayList.get(i3));
                boolean z2 = false;
                String name = null;
                try {
                    inputStreamNewInputStream = Files.newInputStream(Paths.get(str, new String[0]), new OpenOption[0]);
                    th = null;
                } catch (FileNotFoundException e2) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Could not add certificate from file " + str, e2);
                    }
                } catch (IOException e3) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Could not add certificate from file " + str, e3);
                    }
                } catch (CertificateExpiredException e4) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Could not add certificate from file " + str, e4);
                    }
                } catch (CertificateNotYetValidException e5) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Could not add certificate from file " + str, e5);
                    }
                } catch (CertificateException e6) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Could not add certificate from file " + str, e6);
                    }
                }
                try {
                    try {
                        X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(inputStreamNewInputStream);
                        x509Certificate.checkValidity();
                        this.certs.add(x509Certificate);
                        name = x509Certificate.getSubjectX500Principal().getName();
                        z2 = true;
                        if (inputStreamNewInputStream != null) {
                            if (0 != 0) {
                                try {
                                    inputStreamNewInputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                inputStreamNewInputStream.close();
                            }
                        }
                        if (z2) {
                            LOG.debug("Added certificate: {}", name);
                        }
                    } catch (Throwable th3) {
                        if (inputStreamNewInputStream != null) {
                            if (th != null) {
                                try {
                                    inputStreamNewInputStream.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                inputStreamNewInputStream.close();
                            }
                        }
                        throw th3;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    throw th5;
                }
            }
        } catch (CertificateException e7) {
            throw new StorageResolverException(e7);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverSpi
    public Iterator<Certificate> getIterator() {
        return new FilesystemIterator(this.certs);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/storage/implementations/CertsInFilesystemDirectoryResolver$FilesystemIterator.class */
    private static class FilesystemIterator implements Iterator<Certificate> {
        private List<X509Certificate> certs;

        /* renamed from: i, reason: collision with root package name */
        private int f12010i = 0;

        public FilesystemIterator(List<X509Certificate> list) {
            this.certs = list;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f12010i < this.certs.size();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Certificate next() {
            List<X509Certificate> list = this.certs;
            int i2 = this.f12010i;
            this.f12010i = i2 + 1;
            return list.get(i2);
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Can't remove keys from KeyStore");
        }
    }

    public static void main(String[] strArr) throws Exception {
        Iterator<Certificate> iterator = new CertsInFilesystemDirectoryResolver("data/ie/baltimore/merlin-examples/merlin-xmldsig-eighteen/certs").getIterator();
        while (iterator.hasNext()) {
            X509Certificate x509Certificate = (X509Certificate) iterator.next();
            byte[] sKIBytesFromCert = XMLX509SKI.getSKIBytesFromCert(x509Certificate);
            System.out.println();
            System.out.println("Base64(SKI())=                 \"" + XMLUtils.encodeToString(sKIBytesFromCert) + PdfOps.DOUBLE_QUOTE__TOKEN);
            System.out.println("cert.getSerialNumber()=        \"" + x509Certificate.getSerialNumber().toString() + PdfOps.DOUBLE_QUOTE__TOKEN);
            System.out.println("cert.getSubjectX500Principal().getName()= \"" + x509Certificate.getSubjectX500Principal().getName() + PdfOps.DOUBLE_QUOTE__TOKEN);
            System.out.println("cert.getIssuerX500Principal().getName()=  \"" + x509Certificate.getIssuerX500Principal().getName() + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
    }
}
