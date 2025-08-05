package sun.security.provider.certpath;

import java.io.IOException;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRLReason;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.Extension;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.misc.HexDumpEncoder;
import sun.security.action.GetIntegerAction;
import sun.security.provider.certpath.OCSP;
import sun.security.provider.certpath.ResponderId;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;
import sun.security.x509.KeyIdentifier;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/provider/certpath/OCSPResponse.class */
public final class OCSPResponse {
    private static final ResponseStatus[] rsvalues = ResponseStatus.values();
    private static final Debug debug = Debug.getInstance("certpath");
    private static final boolean dump;
    private static final ObjectIdentifier OCSP_BASIC_RESPONSE_OID;
    private static final int CERT_STATUS_GOOD = 0;
    private static final int CERT_STATUS_REVOKED = 1;
    private static final int CERT_STATUS_UNKNOWN = 2;
    private static final int NAME_TAG = 1;
    private static final int KEY_TAG = 2;
    private static final String KP_OCSP_SIGNING_OID = "1.3.6.1.5.5.7.3.9";
    private static final int DEFAULT_MAX_CLOCK_SKEW = 900000;
    private static final int MAX_CLOCK_SKEW;
    private static final CRLReason[] values;
    private final ResponseStatus responseStatus;
    private final Map<CertId, SingleResponse> singleResponseMap;
    private final AlgorithmId sigAlgId;
    private final byte[] signature;
    private final byte[] tbsResponseData;
    private final byte[] responseNonce;
    private List<X509CertImpl> certs;
    private X509CertImpl signerCert = null;
    private final ResponderId respId;
    private Date producedAtDate;
    private final Map<String, Extension> responseExtensions;

    /* loaded from: rt.jar:sun/security/provider/certpath/OCSPResponse$ResponseStatus.class */
    public enum ResponseStatus {
        SUCCESSFUL,
        MALFORMED_REQUEST,
        INTERNAL_ERROR,
        TRY_LATER,
        UNUSED,
        SIG_REQUIRED,
        UNAUTHORIZED
    }

    static {
        dump = debug != null && Debug.isOn("ocsp");
        OCSP_BASIC_RESPONSE_OID = ObjectIdentifier.newInternal(new int[]{1, 3, 6, 1, 5, 5, 7, 48, 1, 1});
        MAX_CLOCK_SKEW = initializeClockSkew();
        values = CRLReason.values();
    }

    private static int initializeClockSkew() {
        Integer num = (Integer) AccessController.doPrivileged(new GetIntegerAction("com.sun.security.ocsp.clockSkew"));
        if (num == null || num.intValue() < 0) {
            return DEFAULT_MAX_CLOCK_SKEW;
        }
        return num.intValue() * 1000;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public OCSPResponse(byte[] bArr) throws IOException {
        this.producedAtDate = null;
        if (dump) {
            debug.println("OCSPResponse bytes...\n\n" + new HexDumpEncoder().encode(bArr) + "\n");
        }
        DerValue derValue = new DerValue(bArr);
        if (derValue.tag != 48) {
            throw new IOException("Bad encoding in OCSP response: expected ASN.1 SEQUENCE tag.");
        }
        DerInputStream data = derValue.getData();
        int enumerated = data.getEnumerated();
        if (enumerated >= 0 && enumerated < rsvalues.length) {
            this.responseStatus = rsvalues[enumerated];
            if (debug != null) {
                debug.println("OCSP response status: " + ((Object) this.responseStatus));
            }
            if (this.responseStatus != ResponseStatus.SUCCESSFUL) {
                this.singleResponseMap = Collections.emptyMap();
                this.certs = new ArrayList();
                this.sigAlgId = null;
                this.signature = null;
                this.tbsResponseData = null;
                this.responseNonce = null;
                this.responseExtensions = Collections.emptyMap();
                this.respId = null;
                return;
            }
            DerValue derValue2 = data.getDerValue();
            if (!derValue2.isContextSpecific((byte) 0)) {
                throw new IOException("Bad encoding in responseBytes element of OCSP response: expected ASN.1 context specific tag 0.");
            }
            DerValue derValue3 = derValue2.data.getDerValue();
            if (derValue3.tag != 48) {
                throw new IOException("Bad encoding in responseBytes element of OCSP response: expected ASN.1 SEQUENCE tag.");
            }
            DerInputStream derInputStream = derValue3.data;
            ObjectIdentifier oid = derInputStream.getOID();
            if (oid.equals((Object) OCSP_BASIC_RESPONSE_OID)) {
                if (debug != null) {
                    debug.println("OCSP response type: basic");
                }
                DerValue[] sequence = new DerInputStream(derInputStream.getOctetString()).getSequence(3);
                if (sequence.length < 3) {
                    throw new IOException("Unexpected BasicOCSPResponse value");
                }
                DerValue derValue4 = sequence[0];
                this.tbsResponseData = sequence[0].toByteArray();
                if (derValue4.tag != 48) {
                    throw new IOException("Bad encoding in tbsResponseData element of OCSP response: expected ASN.1 SEQUENCE tag.");
                }
                DerInputStream derInputStream2 = derValue4.data;
                DerValue derValue5 = derInputStream2.getDerValue();
                if (derValue5.isContextSpecific((byte) 0) && derValue5.isConstructed() && derValue5.isContextSpecific()) {
                    DerValue derValue6 = derValue5.data.getDerValue();
                    derValue6.getInteger();
                    if (derValue6.data.available() != 0) {
                        throw new IOException("Bad encoding in version  element of OCSP response: bad format");
                    }
                    derValue5 = derInputStream2.getDerValue();
                }
                this.respId = new ResponderId(derValue5.toByteArray());
                if (debug != null) {
                    debug.println("Responder ID: " + ((Object) this.respId));
                }
                this.producedAtDate = derInputStream2.getDerValue().getGeneralizedTime();
                if (debug != null) {
                    debug.println("OCSP response produced at: " + ((Object) this.producedAtDate));
                }
                DerValue[] sequence2 = derInputStream2.getSequence(1);
                this.singleResponseMap = new HashMap(sequence2.length);
                if (debug != null) {
                    debug.println("OCSP number of SingleResponses: " + sequence2.length);
                }
                for (DerValue derValue7 : sequence2) {
                    SingleResponse singleResponse = new SingleResponse(derValue7);
                    this.singleResponseMap.put(singleResponse.getCertId(), singleResponse);
                }
                Map map = new HashMap();
                if (derInputStream2.available() > 0) {
                    DerValue derValue8 = derInputStream2.getDerValue();
                    if (derValue8.isContextSpecific((byte) 1)) {
                        map = parseExtensions(derValue8);
                    }
                }
                this.responseExtensions = map;
                sun.security.x509.Extension extension = (sun.security.x509.Extension) map.get(PKIXExtensions.OCSPNonce_Id.toString());
                this.responseNonce = extension != null ? extension.getExtensionValue() : null;
                if (debug != null && this.responseNonce != null) {
                    debug.println("Response nonce: " + Arrays.toString(this.responseNonce));
                }
                this.sigAlgId = AlgorithmId.parse(sequence[1]);
                this.signature = sequence[2].getBitString();
                if (sequence.length > 3) {
                    DerValue derValue9 = sequence[3];
                    if (!derValue9.isContextSpecific((byte) 0)) {
                        throw new IOException("Bad encoding in certs element of OCSP response: expected ASN.1 context specific tag 0.");
                    }
                    DerValue[] sequence3 = derValue9.getData().getSequence(3);
                    this.certs = new ArrayList(sequence3.length);
                    for (int i2 = 0; i2 < sequence3.length; i2++) {
                        try {
                            X509CertImpl x509CertImpl = new X509CertImpl(sequence3[i2].toByteArray());
                            this.certs.add(x509CertImpl);
                            if (debug != null) {
                                debug.println("OCSP response cert #" + (i2 + 1) + ": " + ((Object) x509CertImpl.getSubjectX500Principal()));
                            }
                        } catch (CertificateException e2) {
                            throw new IOException("Bad encoding in X509 Certificate", e2);
                        }
                    }
                    return;
                }
                this.certs = new ArrayList();
                return;
            }
            if (debug != null) {
                debug.println("OCSP response type: " + ((Object) oid));
            }
            throw new IOException("Unsupported OCSP response type: " + ((Object) oid));
        }
        throw new IOException("Unknown OCSPResponse status: " + enumerated);
    }

    void verify(List<CertId> list, IssuerInfo issuerInfo, X509Certificate x509Certificate, Date date, byte[] bArr, String str) throws CertPathValidatorException {
        switch (this.responseStatus) {
            case SUCCESSFUL:
                for (CertId certId : list) {
                    SingleResponse singleResponse = getSingleResponse(certId);
                    if (singleResponse == null) {
                        if (debug != null) {
                            debug.println("No response found for CertId: " + ((Object) certId));
                        }
                        throw new CertPathValidatorException("OCSP response does not include a response for a certificate supplied in the OCSP request");
                    }
                    if (debug != null) {
                        debug.println("Status of certificate (with serial number " + ((Object) certId.getSerialNumber()) + ") is: " + ((Object) singleResponse.getCertStatus()));
                    }
                }
                if (this.signerCert == null) {
                    try {
                        if (issuerInfo.getCertificate() != null) {
                            this.certs.add(X509CertImpl.toImpl(issuerInfo.getCertificate()));
                        }
                        if (x509Certificate != null) {
                            this.certs.add(X509CertImpl.toImpl(x509Certificate));
                        }
                        if (this.respId.getType() == ResponderId.Type.BY_NAME) {
                            X500Principal responderName = this.respId.getResponderName();
                            Iterator<X509CertImpl> it = this.certs.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    X509CertImpl next = it.next();
                                    if (next.getSubjectX500Principal().equals(responderName)) {
                                        this.signerCert = next;
                                    }
                                }
                            }
                        } else if (this.respId.getType() == ResponderId.Type.BY_KEY) {
                            KeyIdentifier keyIdentifier = this.respId.getKeyIdentifier();
                            Iterator<X509CertImpl> it2 = this.certs.iterator();
                            while (true) {
                                if (it2.hasNext()) {
                                    X509CertImpl next2 = it2.next();
                                    KeyIdentifier subjectKeyId = next2.getSubjectKeyId();
                                    if (subjectKeyId != null && keyIdentifier.equals(subjectKeyId)) {
                                        this.signerCert = next2;
                                    } else {
                                        try {
                                            subjectKeyId = new KeyIdentifier(next2.getPublicKey());
                                        } catch (IOException e2) {
                                        }
                                        if (keyIdentifier.equals(subjectKeyId)) {
                                            this.signerCert = next2;
                                        }
                                    }
                                }
                            }
                        }
                    } catch (CertificateException e3) {
                        throw new CertPathValidatorException("Invalid issuer or trusted responder certificate", e3);
                    }
                }
                boolean z2 = false;
                if (this.signerCert != null) {
                    if (this.signerCert.getSubjectX500Principal().equals(issuerInfo.getName()) && this.signerCert.getPublicKey().equals(issuerInfo.getPublicKey())) {
                        if (debug != null) {
                            debug.println("OCSP response is signed by the target's Issuing CA");
                        }
                    } else if (this.signerCert.equals(x509Certificate)) {
                        z2 = true;
                        if (debug != null) {
                            debug.println("OCSP response is signed by a Trusted Responder");
                        }
                    } else if (this.signerCert.getIssuerX500Principal().equals(issuerInfo.getName())) {
                        try {
                            List<String> extendedKeyUsage = this.signerCert.getExtendedKeyUsage();
                            if (extendedKeyUsage == null || !extendedKeyUsage.contains(KP_OCSP_SIGNING_OID)) {
                                throw new CertPathValidatorException("Responder's certificate not valid for signing OCSP responses");
                            }
                            AlgorithmChecker algorithmChecker = new AlgorithmChecker(issuerInfo.getAnchor(), date, str);
                            algorithmChecker.init(false);
                            algorithmChecker.check(this.signerCert, Collections.emptySet());
                            try {
                                if (date == null) {
                                    this.signerCert.checkValidity();
                                } else {
                                    this.signerCert.checkValidity(date);
                                }
                                if (this.signerCert.getExtension(PKIXExtensions.OCSPNoCheck_Id) != null && debug != null) {
                                    debug.println("Responder's certificate includes the extension id-pkix-ocsp-nocheck.");
                                }
                                try {
                                    this.signerCert.verify(issuerInfo.getPublicKey());
                                    if (debug != null) {
                                        debug.println("OCSP response is signed by an Authorized Responder");
                                    }
                                } catch (GeneralSecurityException e4) {
                                    this.signerCert = null;
                                }
                            } catch (CertificateException e5) {
                                throw new CertPathValidatorException("Responder's certificate not within the validity period", e5);
                            }
                        } catch (CertificateParsingException e6) {
                            throw new CertPathValidatorException("Responder's certificate not valid for signing OCSP responses", e6);
                        }
                    } else {
                        throw new CertPathValidatorException("Responder's certificate is not authorized to sign OCSP responses");
                    }
                }
                if (this.signerCert != null) {
                    AlgorithmChecker.check(this.signerCert.getPublicKey(), this.sigAlgId, str, z2 ? new TrustAnchor(x509Certificate, null) : issuerInfo.getAnchor());
                    if (!verifySignature(this.signerCert)) {
                        throw new CertPathValidatorException("Error verifying OCSP Response's signature");
                    }
                    if (bArr != null && this.responseNonce != null && !Arrays.equals(bArr, this.responseNonce)) {
                        throw new CertPathValidatorException("Nonces don't match");
                    }
                    long jCurrentTimeMillis = date == null ? System.currentTimeMillis() : date.getTime();
                    Date date2 = new Date(jCurrentTimeMillis + MAX_CLOCK_SKEW);
                    Date date3 = new Date(jCurrentTimeMillis - MAX_CLOCK_SKEW);
                    for (SingleResponse singleResponse2 : this.singleResponseMap.values()) {
                        if (debug != null) {
                            String str2 = "";
                            if (singleResponse2.nextUpdate != null) {
                                str2 = " until " + ((Object) singleResponse2.nextUpdate);
                            }
                            debug.println("OCSP response validity interval is from " + ((Object) singleResponse2.thisUpdate) + str2);
                            debug.println("Checking validity of OCSP response on: " + ((Object) new Date(jCurrentTimeMillis)));
                        }
                        if (!date2.before(singleResponse2.thisUpdate)) {
                            if (date3.after(singleResponse2.nextUpdate != null ? singleResponse2.nextUpdate : singleResponse2.thisUpdate)) {
                            }
                        }
                        throw new CertPathValidatorException("Response is unreliable: its validity interval is out-of-date");
                        break;
                    }
                    return;
                }
                throw new CertPathValidatorException("Unable to verify OCSP Response's signature");
            case TRY_LATER:
            case INTERNAL_ERROR:
                throw new CertPathValidatorException("OCSP response error: " + ((Object) this.responseStatus), null, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
            case UNAUTHORIZED:
            default:
                throw new CertPathValidatorException("OCSP response error: " + ((Object) this.responseStatus));
        }
    }

    public ResponseStatus getResponseStatus() {
        return this.responseStatus;
    }

    private boolean verifySignature(X509Certificate x509Certificate) throws CertPathValidatorException {
        try {
            Signature signature = Signature.getInstance(this.sigAlgId.getName());
            signature.initVerify(x509Certificate.getPublicKey());
            signature.update(this.tbsResponseData);
            if (signature.verify(this.signature)) {
                if (debug != null) {
                    debug.println("Verified signature of OCSP Response");
                    return true;
                }
                return true;
            }
            if (debug != null) {
                debug.println("Error verifying signature of OCSP Response");
                return false;
            }
            return false;
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e2) {
            throw new CertPathValidatorException(e2);
        }
    }

    public SingleResponse getSingleResponse(CertId certId) {
        return this.singleResponseMap.get(certId);
    }

    public Set<CertId> getCertIds() {
        return Collections.unmodifiableSet(this.singleResponseMap.keySet());
    }

    X509Certificate getSignerCertificate() {
        return this.signerCert;
    }

    public ResponderId getResponderId() {
        return this.respId;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OCSP Response:\n");
        sb.append("Response Status: ").append((Object) this.responseStatus).append("\n");
        sb.append("Responder ID: ").append((Object) this.respId).append("\n");
        sb.append("Produced at: ").append((Object) this.producedAtDate).append("\n");
        int size = this.singleResponseMap.size();
        sb.append(size).append(size == 1 ? " response:\n" : " responses:\n");
        Iterator<SingleResponse> it = this.singleResponseMap.values().iterator();
        while (it.hasNext()) {
            sb.append((Object) it.next()).append("\n");
        }
        if (this.responseExtensions != null && this.responseExtensions.size() > 0) {
            int size2 = this.responseExtensions.size();
            sb.append(size2).append(size2 == 1 ? " extension:\n" : " extensions:\n");
            Iterator<String> it2 = this.responseExtensions.keySet().iterator();
            while (it2.hasNext()) {
                sb.append((Object) this.responseExtensions.get(it2.next())).append("\n");
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Map<String, Extension> parseExtensions(DerValue derValue) throws IOException {
        DerValue[] sequence = derValue.data.getSequence(3);
        HashMap map = new HashMap(sequence.length);
        for (DerValue derValue2 : sequence) {
            sun.security.x509.Extension extension = new sun.security.x509.Extension(derValue2);
            if (debug != null) {
                debug.println("Extension: " + ((Object) extension));
            }
            if (extension.isCritical()) {
                throw new IOException("Unsupported OCSP critical extension: " + ((Object) extension.getExtensionId()));
            }
            map.put(extension.getId(), extension);
        }
        return map;
    }

    /* loaded from: rt.jar:sun/security/provider/certpath/OCSPResponse$SingleResponse.class */
    public static final class SingleResponse implements OCSP.RevocationStatus {
        private final CertId certId;
        private final OCSP.RevocationStatus.CertStatus certStatus;
        private final Date thisUpdate;
        private final Date nextUpdate;
        private final Date revocationTime;
        private final CRLReason revocationReason;
        private final Map<String, Extension> singleExtensions;

        private SingleResponse(DerValue derValue) throws IOException {
            int enumerated;
            if (derValue.tag != 48) {
                throw new IOException("Bad ASN.1 encoding in SingleResponse");
            }
            DerInputStream derInputStream = derValue.data;
            this.certId = new CertId(derInputStream.getDerValue().data);
            DerValue derValue2 = derInputStream.getDerValue();
            short s2 = (byte) (derValue2.tag & 31);
            if (s2 == 1) {
                this.certStatus = OCSP.RevocationStatus.CertStatus.REVOKED;
                this.revocationTime = derValue2.data.getGeneralizedTime();
                if (derValue2.data.available() != 0) {
                    DerValue derValue3 = derValue2.data.getDerValue();
                    if (((byte) (derValue3.tag & 31)) == 0 && (enumerated = derValue3.data.getEnumerated()) >= 0 && enumerated < OCSPResponse.values.length) {
                        this.revocationReason = OCSPResponse.values[enumerated];
                    } else {
                        this.revocationReason = CRLReason.UNSPECIFIED;
                    }
                } else {
                    this.revocationReason = CRLReason.UNSPECIFIED;
                }
                if (OCSPResponse.debug != null) {
                    OCSPResponse.debug.println("Revocation time: " + ((Object) this.revocationTime));
                    OCSPResponse.debug.println("Revocation reason: " + ((Object) this.revocationReason));
                }
            } else {
                this.revocationTime = null;
                this.revocationReason = null;
                if (s2 == 0) {
                    this.certStatus = OCSP.RevocationStatus.CertStatus.GOOD;
                } else if (s2 == 2) {
                    this.certStatus = OCSP.RevocationStatus.CertStatus.UNKNOWN;
                } else {
                    throw new IOException("Invalid certificate status");
                }
            }
            this.thisUpdate = derInputStream.getGeneralizedTime();
            if (OCSPResponse.debug != null) {
                OCSPResponse.debug.println("thisUpdate: " + ((Object) this.thisUpdate));
            }
            Date generalizedTime = null;
            Map<String, Extension> extensions = null;
            if (derInputStream.available() > 0) {
                DerValue derValue4 = derInputStream.getDerValue();
                if (derValue4.isContextSpecific((byte) 0)) {
                    generalizedTime = derValue4.data.getGeneralizedTime();
                    if (OCSPResponse.debug != null) {
                        OCSPResponse.debug.println("nextUpdate: " + ((Object) generalizedTime));
                    }
                    derValue4 = derInputStream.available() > 0 ? derInputStream.getDerValue() : null;
                }
                if (derValue4 != null) {
                    if (derValue4.isContextSpecific((byte) 1)) {
                        extensions = OCSPResponse.parseExtensions(derValue4);
                        if (derInputStream.available() > 0) {
                            throw new IOException(derInputStream.available() + " bytes of additional data in singleResponse");
                        }
                    } else {
                        throw new IOException("Unsupported singleResponse item, tag = " + String.format("%02X", Byte.valueOf(derValue4.tag)));
                    }
                }
            }
            this.nextUpdate = generalizedTime;
            this.singleExtensions = extensions != null ? extensions : Collections.emptyMap();
            if (OCSPResponse.debug != null) {
                Iterator<Extension> it = this.singleExtensions.values().iterator();
                while (it.hasNext()) {
                    OCSPResponse.debug.println("singleExtension: " + ((Object) it.next()));
                }
            }
        }

        @Override // sun.security.provider.certpath.OCSP.RevocationStatus
        public OCSP.RevocationStatus.CertStatus getCertStatus() {
            return this.certStatus;
        }

        public CertId getCertId() {
            return this.certId;
        }

        public Date getThisUpdate() {
            if (this.thisUpdate != null) {
                return (Date) this.thisUpdate.clone();
            }
            return null;
        }

        public Date getNextUpdate() {
            if (this.nextUpdate != null) {
                return (Date) this.nextUpdate.clone();
            }
            return null;
        }

        @Override // sun.security.provider.certpath.OCSP.RevocationStatus
        public Date getRevocationTime() {
            if (this.revocationTime != null) {
                return (Date) this.revocationTime.clone();
            }
            return null;
        }

        @Override // sun.security.provider.certpath.OCSP.RevocationStatus
        public CRLReason getRevocationReason() {
            return this.revocationReason;
        }

        @Override // sun.security.provider.certpath.OCSP.RevocationStatus
        public Map<String, Extension> getSingleExtensions() {
            return Collections.unmodifiableMap(this.singleExtensions);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("SingleResponse:\n");
            sb.append((Object) this.certId);
            sb.append("\nCertStatus: ").append((Object) this.certStatus).append("\n");
            if (this.certStatus == OCSP.RevocationStatus.CertStatus.REVOKED) {
                sb.append("revocationTime is ");
                sb.append((Object) this.revocationTime).append("\n");
                sb.append("revocationReason is ");
                sb.append((Object) this.revocationReason).append("\n");
            }
            sb.append("thisUpdate is ").append((Object) this.thisUpdate).append("\n");
            if (this.nextUpdate != null) {
                sb.append("nextUpdate is ").append((Object) this.nextUpdate).append("\n");
            }
            for (Extension extension : this.singleExtensions.values()) {
                sb.append("singleExtension: ");
                sb.append(extension.toString()).append("\n");
            }
            return sb.toString();
        }
    }

    /* loaded from: rt.jar:sun/security/provider/certpath/OCSPResponse$IssuerInfo.class */
    static final class IssuerInfo {
        private final TrustAnchor anchor;
        private final X509Certificate certificate;
        private final X500Principal name;
        private final PublicKey pubKey;

        IssuerInfo(TrustAnchor trustAnchor) {
            this(trustAnchor, trustAnchor != null ? trustAnchor.getTrustedCert() : null);
        }

        IssuerInfo(X509Certificate x509Certificate) {
            this(null, x509Certificate);
        }

        IssuerInfo(TrustAnchor trustAnchor, X509Certificate x509Certificate) {
            if (trustAnchor == null && x509Certificate == null) {
                throw new NullPointerException("TrustAnchor and issuerCert cannot be null");
            }
            this.anchor = trustAnchor;
            if (x509Certificate != null) {
                this.name = x509Certificate.getSubjectX500Principal();
                this.pubKey = x509Certificate.getPublicKey();
                this.certificate = x509Certificate;
            } else {
                this.name = trustAnchor.getCA();
                this.pubKey = trustAnchor.getCAPublicKey();
                this.certificate = trustAnchor.getTrustedCert();
            }
        }

        X509Certificate getCertificate() {
            return this.certificate;
        }

        X500Principal getName() {
            return this.name;
        }

        PublicKey getPublicKey() {
            return this.pubKey;
        }

        TrustAnchor getAnchor() {
            return this.anchor;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Issuer Info:\n");
            sb.append("Name: ").append(this.name.toString()).append("\n");
            sb.append("Public Key:\n").append(this.pubKey.toString()).append("\n");
            return sb.toString();
        }
    }
}
