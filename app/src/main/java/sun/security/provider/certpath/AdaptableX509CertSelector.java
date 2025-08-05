package sun.security.provider.certpath;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SKI;
import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.x509.AuthorityKeyIdentifierExtension;
import sun.security.x509.SerialNumber;

/* loaded from: rt.jar:sun/security/provider/certpath/AdaptableX509CertSelector.class */
class AdaptableX509CertSelector extends X509CertSelector {
    private static final Debug debug = Debug.getInstance("certpath");
    private Date startDate;
    private Date endDate;
    private byte[] ski;
    private BigInteger serial;

    AdaptableX509CertSelector() {
    }

    void setValidityPeriod(Date date, Date date2) {
        this.startDate = date;
        this.endDate = date2;
    }

    @Override // java.security.cert.X509CertSelector
    public void setSubjectKeyIdentifier(byte[] bArr) {
        throw new IllegalArgumentException();
    }

    @Override // java.security.cert.X509CertSelector
    public void setSerialNumber(BigInteger bigInteger) {
        throw new IllegalArgumentException();
    }

    void setSkiAndSerialNumber(AuthorityKeyIdentifierExtension authorityKeyIdentifierExtension) throws IOException {
        this.ski = null;
        this.serial = null;
        if (authorityKeyIdentifierExtension != null) {
            this.ski = authorityKeyIdentifierExtension.getEncodedKeyIdentifier();
            SerialNumber serialNumber = (SerialNumber) authorityKeyIdentifierExtension.get(AuthorityKeyIdentifierExtension.SERIAL_NUMBER);
            if (serialNumber != null) {
                this.serial = serialNumber.getNumber();
            }
        }
    }

    @Override // java.security.cert.X509CertSelector, java.security.cert.CertSelector
    public boolean match(Certificate certificate) {
        X509Certificate x509Certificate = (X509Certificate) certificate;
        if (!matchSubjectKeyID(x509Certificate)) {
            return false;
        }
        int version = x509Certificate.getVersion();
        if (this.serial != null && version > 2 && !this.serial.equals(x509Certificate.getSerialNumber())) {
            return false;
        }
        if (version < 3) {
            if (this.startDate != null) {
                try {
                    x509Certificate.checkValidity(this.startDate);
                } catch (CertificateException e2) {
                    return false;
                }
            }
            if (this.endDate != null) {
                try {
                    x509Certificate.checkValidity(this.endDate);
                } catch (CertificateException e3) {
                    return false;
                }
            }
        }
        if (!super.match(certificate)) {
            return false;
        }
        return true;
    }

    private boolean matchSubjectKeyID(X509Certificate x509Certificate) {
        if (this.ski == null) {
            return true;
        }
        try {
            byte[] extensionValue = x509Certificate.getExtensionValue(XMLX509SKI.SKI_OID);
            if (extensionValue == null) {
                if (debug != null) {
                    debug.println("AdaptableX509CertSelector.match: no subject key ID extension. Subject: " + ((Object) x509Certificate.getSubjectX500Principal()));
                    return true;
                }
                return true;
            }
            byte[] octetString = new DerInputStream(extensionValue).getOctetString();
            if (octetString == null || !Arrays.equals(this.ski, octetString)) {
                if (debug != null) {
                    debug.println("AdaptableX509CertSelector.match: subject key IDs don't match. Expected: " + Arrays.toString(this.ski) + " Cert's: " + Arrays.toString(octetString));
                    return false;
                }
                return false;
            }
            return true;
        } catch (IOException e2) {
            if (debug != null) {
                debug.println("AdaptableX509CertSelector.match: exception in subject key ID check");
                return false;
            }
            return false;
        }
    }

    @Override // java.security.cert.X509CertSelector, java.security.cert.CertSelector
    public Object clone() {
        AdaptableX509CertSelector adaptableX509CertSelector = (AdaptableX509CertSelector) super.clone();
        if (this.startDate != null) {
            adaptableX509CertSelector.startDate = (Date) this.startDate.clone();
        }
        if (this.endDate != null) {
            adaptableX509CertSelector.endDate = (Date) this.endDate.clone();
        }
        if (this.ski != null) {
            adaptableX509CertSelector.ski = (byte[]) this.ski.clone();
        }
        return adaptableX509CertSelector;
    }
}
