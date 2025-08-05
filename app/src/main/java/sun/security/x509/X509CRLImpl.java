package sun.security.x509;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.security.auth.x500.X500Principal;
import sun.misc.HexDumpEncoder;
import sun.security.provider.X509Factory;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.util.SignatureUtil;

/* loaded from: rt.jar:sun/security/x509/X509CRLImpl.class */
public class X509CRLImpl extends X509CRL implements DerEncoder {
    private byte[] signedCRL;
    private byte[] signature;
    private byte[] tbsCertList;
    private AlgorithmId sigAlgId;
    private int version;
    private AlgorithmId infoSigAlgId;
    private X500Name issuer;
    private X500Principal issuerPrincipal;
    private Date thisUpdate;
    private Date nextUpdate;
    private Map<X509IssuerSerial, X509CRLEntry> revokedMap;
    private List<X509CRLEntry> revokedList;
    private CRLExtensions extensions;
    private static final boolean isExplicit = true;
    private boolean readOnly;
    private PublicKey verifiedPublicKey;
    private String verifiedProvider;

    private X509CRLImpl() {
        this.signedCRL = null;
        this.signature = null;
        this.tbsCertList = null;
        this.sigAlgId = null;
        this.issuer = null;
        this.issuerPrincipal = null;
        this.thisUpdate = null;
        this.nextUpdate = null;
        this.revokedMap = new TreeMap();
        this.revokedList = new LinkedList();
        this.extensions = null;
        this.readOnly = false;
    }

    public X509CRLImpl(byte[] bArr) throws CRLException {
        this.signedCRL = null;
        this.signature = null;
        this.tbsCertList = null;
        this.sigAlgId = null;
        this.issuer = null;
        this.issuerPrincipal = null;
        this.thisUpdate = null;
        this.nextUpdate = null;
        this.revokedMap = new TreeMap();
        this.revokedList = new LinkedList();
        this.extensions = null;
        this.readOnly = false;
        try {
            parse(new DerValue(bArr));
        } catch (IOException e2) {
            this.signedCRL = null;
            throw new CRLException("Parsing error: " + e2.getMessage());
        }
    }

    public X509CRLImpl(DerValue derValue) throws CRLException {
        this.signedCRL = null;
        this.signature = null;
        this.tbsCertList = null;
        this.sigAlgId = null;
        this.issuer = null;
        this.issuerPrincipal = null;
        this.thisUpdate = null;
        this.nextUpdate = null;
        this.revokedMap = new TreeMap();
        this.revokedList = new LinkedList();
        this.extensions = null;
        this.readOnly = false;
        try {
            parse(derValue);
        } catch (IOException e2) {
            this.signedCRL = null;
            throw new CRLException("Parsing error: " + e2.getMessage());
        }
    }

    public X509CRLImpl(InputStream inputStream) throws CRLException {
        this.signedCRL = null;
        this.signature = null;
        this.tbsCertList = null;
        this.sigAlgId = null;
        this.issuer = null;
        this.issuerPrincipal = null;
        this.thisUpdate = null;
        this.nextUpdate = null;
        this.revokedMap = new TreeMap();
        this.revokedList = new LinkedList();
        this.extensions = null;
        this.readOnly = false;
        try {
            parse(new DerValue(inputStream));
        } catch (IOException e2) {
            this.signedCRL = null;
            throw new CRLException("Parsing error: " + e2.getMessage());
        }
    }

    public X509CRLImpl(X500Name x500Name, Date date, Date date2) {
        this.signedCRL = null;
        this.signature = null;
        this.tbsCertList = null;
        this.sigAlgId = null;
        this.issuer = null;
        this.issuerPrincipal = null;
        this.thisUpdate = null;
        this.nextUpdate = null;
        this.revokedMap = new TreeMap();
        this.revokedList = new LinkedList();
        this.extensions = null;
        this.readOnly = false;
        this.issuer = x500Name;
        this.thisUpdate = date;
        this.nextUpdate = date2;
    }

    public X509CRLImpl(X500Name x500Name, Date date, Date date2, X509CRLEntry[] x509CRLEntryArr) throws CRLException {
        this.signedCRL = null;
        this.signature = null;
        this.tbsCertList = null;
        this.sigAlgId = null;
        this.issuer = null;
        this.issuerPrincipal = null;
        this.thisUpdate = null;
        this.nextUpdate = null;
        this.revokedMap = new TreeMap();
        this.revokedList = new LinkedList();
        this.extensions = null;
        this.readOnly = false;
        this.issuer = x500Name;
        this.thisUpdate = date;
        this.nextUpdate = date2;
        if (x509CRLEntryArr != null) {
            X500Principal issuerX500Principal = getIssuerX500Principal();
            X500Principal certIssuer = issuerX500Principal;
            for (X509CRLEntry x509CRLEntry : x509CRLEntryArr) {
                X509CRLEntryImpl x509CRLEntryImpl = (X509CRLEntryImpl) x509CRLEntry;
                try {
                    certIssuer = getCertIssuer(x509CRLEntryImpl, certIssuer);
                    x509CRLEntryImpl.setCertificateIssuer(issuerX500Principal, certIssuer);
                    this.revokedMap.put(new X509IssuerSerial(certIssuer, x509CRLEntryImpl.getSerialNumber()), x509CRLEntryImpl);
                    this.revokedList.add(x509CRLEntryImpl);
                    if (x509CRLEntryImpl.hasExtensions()) {
                        this.version = 1;
                    }
                } catch (IOException e2) {
                    throw new CRLException(e2);
                }
            }
        }
    }

    public X509CRLImpl(X500Name x500Name, Date date, Date date2, X509CRLEntry[] x509CRLEntryArr, CRLExtensions cRLExtensions) throws CRLException {
        this(x500Name, date, date2, x509CRLEntryArr);
        if (cRLExtensions != null) {
            this.extensions = cRLExtensions;
            this.version = 1;
        }
    }

    public byte[] getEncodedInternal() throws CRLException {
        if (this.signedCRL == null) {
            throw new CRLException("Null CRL to encode");
        }
        return this.signedCRL;
    }

    @Override // java.security.cert.X509CRL
    public byte[] getEncoded() throws CRLException {
        return (byte[]) getEncodedInternal().clone();
    }

    public void encodeInfo(OutputStream outputStream) throws CRLException {
        try {
            DerOutputStream derOutputStream = new DerOutputStream();
            DerOutputStream derOutputStream2 = new DerOutputStream();
            DerOutputStream derOutputStream3 = new DerOutputStream();
            if (this.version != 0) {
                derOutputStream.putInteger(this.version);
            }
            this.infoSigAlgId.encode(derOutputStream);
            if (this.version == 0 && this.issuer.toString() == null) {
                throw new CRLException("Null Issuer DN not allowed in v1 CRL");
            }
            this.issuer.encode(derOutputStream);
            if (this.thisUpdate.getTime() < 2524608000000L) {
                derOutputStream.putUTCTime(this.thisUpdate);
            } else {
                derOutputStream.putGeneralizedTime(this.thisUpdate);
            }
            if (this.nextUpdate != null) {
                if (this.nextUpdate.getTime() < 2524608000000L) {
                    derOutputStream.putUTCTime(this.nextUpdate);
                } else {
                    derOutputStream.putGeneralizedTime(this.nextUpdate);
                }
            }
            if (!this.revokedList.isEmpty()) {
                Iterator<X509CRLEntry> it = this.revokedList.iterator();
                while (it.hasNext()) {
                    ((X509CRLEntryImpl) it.next()).encode(derOutputStream2);
                }
                derOutputStream.write((byte) 48, derOutputStream2);
            }
            if (this.extensions != null) {
                this.extensions.encode(derOutputStream, true);
            }
            derOutputStream3.write((byte) 48, derOutputStream);
            this.tbsCertList = derOutputStream3.toByteArray();
            outputStream.write(this.tbsCertList);
        } catch (IOException e2) {
            throw new CRLException("Encoding error: " + e2.getMessage());
        }
    }

    @Override // java.security.cert.X509CRL
    public void verify(PublicKey publicKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CRLException, NoSuchProviderException {
        verify(publicKey, "");
    }

    @Override // java.security.cert.X509CRL
    public synchronized void verify(PublicKey publicKey, String str) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CRLException, NoSuchProviderException {
        Signature signature;
        if (str == null) {
            str = "";
        }
        if (this.verifiedPublicKey != null && this.verifiedPublicKey.equals(publicKey) && str.equals(this.verifiedProvider)) {
            return;
        }
        if (this.signedCRL == null) {
            throw new CRLException("Uninitialized CRL");
        }
        String name = this.sigAlgId.getName();
        if (str.length() == 0) {
            signature = Signature.getInstance(name);
        } else {
            signature = Signature.getInstance(name, str);
        }
        try {
            SignatureUtil.initVerifyWithParam(signature, publicKey, SignatureUtil.getParamSpec(name, getSigAlgParams()));
            if (this.tbsCertList == null) {
                throw new CRLException("Uninitialized CRL");
            }
            signature.update(this.tbsCertList, 0, this.tbsCertList.length);
            if (!signature.verify(this.signature)) {
                throw new SignatureException("Signature does not match.");
            }
            this.verifiedPublicKey = publicKey;
            this.verifiedProvider = str;
        } catch (InvalidAlgorithmParameterException e2) {
            throw new CRLException(e2);
        } catch (ProviderException e3) {
            throw new CRLException(e3.getMessage(), e3.getCause());
        }
    }

    @Override // java.security.cert.X509CRL
    public synchronized void verify(PublicKey publicKey, Provider provider) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CRLException {
        Signature signature;
        if (this.signedCRL == null) {
            throw new CRLException("Uninitialized CRL");
        }
        String name = this.sigAlgId.getName();
        if (provider == null) {
            signature = Signature.getInstance(name);
        } else {
            signature = Signature.getInstance(name, provider);
        }
        try {
            SignatureUtil.initVerifyWithParam(signature, publicKey, SignatureUtil.getParamSpec(name, getSigAlgParams()));
            if (this.tbsCertList == null) {
                throw new CRLException("Uninitialized CRL");
            }
            signature.update(this.tbsCertList, 0, this.tbsCertList.length);
            if (!signature.verify(this.signature)) {
                throw new SignatureException("Signature does not match.");
            }
            this.verifiedPublicKey = publicKey;
        } catch (InvalidAlgorithmParameterException e2) {
            throw new CRLException(e2);
        } catch (ProviderException e3) {
            throw new CRLException(e3.getMessage(), e3.getCause());
        }
    }

    public void sign(PrivateKey privateKey, String str) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CRLException, NoSuchProviderException {
        sign(privateKey, str, null);
    }

    public void sign(PrivateKey privateKey, String str, String str2) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CRLException, NoSuchProviderException {
        Signature signature;
        try {
            if (this.readOnly) {
                throw new CRLException("cannot over-write existing CRL");
            }
            if (str2 == null || str2.length() == 0) {
                signature = Signature.getInstance(str);
            } else {
                signature = Signature.getInstance(str, str2);
            }
            signature.initSign(privateKey);
            this.sigAlgId = AlgorithmId.get(signature.getAlgorithm());
            this.infoSigAlgId = this.sigAlgId;
            DerOutputStream derOutputStream = new DerOutputStream();
            DerOutputStream derOutputStream2 = new DerOutputStream();
            encodeInfo(derOutputStream2);
            this.sigAlgId.encode(derOutputStream2);
            signature.update(this.tbsCertList, 0, this.tbsCertList.length);
            this.signature = signature.sign();
            derOutputStream2.putBitString(this.signature);
            derOutputStream.write((byte) 48, derOutputStream2);
            this.signedCRL = derOutputStream.toByteArray();
            this.readOnly = true;
        } catch (IOException e2) {
            throw new CRLException("Error while encoding data: " + e2.getMessage());
        }
    }

    @Override // java.security.cert.CRL
    public String toString() {
        return toStringWithAlgName("" + ((Object) this.sigAlgId));
    }

    public String toStringWithAlgName(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("X.509 CRL v" + (this.version + 1) + "\n");
        if (this.sigAlgId != null) {
            stringBuffer.append("Signature Algorithm: " + str.toString() + ", OID=" + this.sigAlgId.getOID().toString() + "\n");
        }
        if (this.issuer != null) {
            stringBuffer.append("Issuer: " + this.issuer.toString() + "\n");
        }
        if (this.thisUpdate != null) {
            stringBuffer.append("\nThis Update: " + this.thisUpdate.toString() + "\n");
        }
        if (this.nextUpdate != null) {
            stringBuffer.append("Next Update: " + this.nextUpdate.toString() + "\n");
        }
        if (this.revokedList.isEmpty()) {
            stringBuffer.append("\nNO certificates have been revoked\n");
        } else {
            stringBuffer.append("\nRevoked Certificates: " + this.revokedList.size());
            int i2 = 1;
            Iterator<X509CRLEntry> it = this.revokedList.iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                stringBuffer.append("\n[" + i3 + "] " + it.next().toString());
            }
        }
        if (this.extensions != null) {
            Object[] array = this.extensions.getAllExtensions().toArray();
            stringBuffer.append("\nCRL Extensions: " + array.length);
            for (int i4 = 0; i4 < array.length; i4++) {
                stringBuffer.append("\n[" + (i4 + 1) + "]: ");
                Extension extension = (Extension) array[i4];
                try {
                    if (OIDMap.getClass(extension.getExtensionId()) == null) {
                        stringBuffer.append(extension.toString());
                        byte[] extensionValue = extension.getExtensionValue();
                        if (extensionValue != null) {
                            DerOutputStream derOutputStream = new DerOutputStream();
                            derOutputStream.putOctetString(extensionValue);
                            stringBuffer.append("Extension unknown: DER encoded OCTET string =\n" + new HexDumpEncoder().encodeBuffer(derOutputStream.toByteArray()) + "\n");
                        }
                    } else {
                        stringBuffer.append(extension.toString());
                    }
                } catch (Exception e2) {
                    stringBuffer.append(", Error parsing this extension");
                }
            }
        }
        if (this.signature != null) {
            stringBuffer.append("\nSignature:\n" + new HexDumpEncoder().encodeBuffer(this.signature) + "\n");
        } else {
            stringBuffer.append("NOT signed yet\n");
        }
        return stringBuffer.toString();
    }

    @Override // java.security.cert.CRL
    public boolean isRevoked(Certificate certificate) {
        if (this.revokedMap.isEmpty() || !(certificate instanceof X509Certificate)) {
            return false;
        }
        return this.revokedMap.containsKey(new X509IssuerSerial((X509Certificate) certificate));
    }

    @Override // java.security.cert.X509CRL
    public int getVersion() {
        return this.version + 1;
    }

    @Override // java.security.cert.X509CRL
    public Principal getIssuerDN() {
        return this.issuer;
    }

    @Override // java.security.cert.X509CRL
    public X500Principal getIssuerX500Principal() {
        if (this.issuerPrincipal == null) {
            this.issuerPrincipal = this.issuer.asX500Principal();
        }
        return this.issuerPrincipal;
    }

    @Override // java.security.cert.X509CRL
    public Date getThisUpdate() {
        return new Date(this.thisUpdate.getTime());
    }

    @Override // java.security.cert.X509CRL
    public Date getNextUpdate() {
        if (this.nextUpdate == null) {
            return null;
        }
        return new Date(this.nextUpdate.getTime());
    }

    @Override // java.security.cert.X509CRL
    public X509CRLEntry getRevokedCertificate(BigInteger bigInteger) {
        if (this.revokedMap.isEmpty()) {
            return null;
        }
        return this.revokedMap.get(new X509IssuerSerial(getIssuerX500Principal(), bigInteger));
    }

    @Override // java.security.cert.X509CRL
    public X509CRLEntry getRevokedCertificate(X509Certificate x509Certificate) {
        if (this.revokedMap.isEmpty()) {
            return null;
        }
        return this.revokedMap.get(new X509IssuerSerial(x509Certificate));
    }

    @Override // java.security.cert.X509CRL
    public Set<X509CRLEntry> getRevokedCertificates() {
        if (this.revokedList.isEmpty()) {
            return null;
        }
        return new TreeSet(this.revokedList);
    }

    @Override // java.security.cert.X509CRL
    public byte[] getTBSCertList() throws CRLException {
        if (this.tbsCertList == null) {
            throw new CRLException("Uninitialized CRL");
        }
        return (byte[]) this.tbsCertList.clone();
    }

    @Override // java.security.cert.X509CRL
    public byte[] getSignature() {
        if (this.signature == null) {
            return null;
        }
        return (byte[]) this.signature.clone();
    }

    @Override // java.security.cert.X509CRL
    public String getSigAlgName() {
        if (this.sigAlgId == null) {
            return null;
        }
        return this.sigAlgId.getName();
    }

    @Override // java.security.cert.X509CRL
    public String getSigAlgOID() {
        if (this.sigAlgId == null) {
            return null;
        }
        return this.sigAlgId.getOID().toString();
    }

    @Override // java.security.cert.X509CRL
    public byte[] getSigAlgParams() {
        if (this.sigAlgId == null) {
            return null;
        }
        try {
            return this.sigAlgId.getEncodedParams();
        } catch (IOException e2) {
            return null;
        }
    }

    public AlgorithmId getSigAlgId() {
        return this.sigAlgId;
    }

    public KeyIdentifier getAuthKeyId() throws IOException {
        AuthorityKeyIdentifierExtension authKeyIdExtension = getAuthKeyIdExtension();
        if (authKeyIdExtension != null) {
            return (KeyIdentifier) authKeyIdExtension.get("key_id");
        }
        return null;
    }

    public AuthorityKeyIdentifierExtension getAuthKeyIdExtension() throws IOException {
        return (AuthorityKeyIdentifierExtension) getExtension(PKIXExtensions.AuthorityKey_Id);
    }

    public CRLNumberExtension getCRLNumberExtension() throws IOException {
        return (CRLNumberExtension) getExtension(PKIXExtensions.CRLNumber_Id);
    }

    public BigInteger getCRLNumber() throws IOException {
        CRLNumberExtension cRLNumberExtension = getCRLNumberExtension();
        if (cRLNumberExtension != null) {
            return cRLNumberExtension.get("value");
        }
        return null;
    }

    public DeltaCRLIndicatorExtension getDeltaCRLIndicatorExtension() throws IOException {
        return (DeltaCRLIndicatorExtension) getExtension(PKIXExtensions.DeltaCRLIndicator_Id);
    }

    public BigInteger getBaseCRLNumber() throws IOException {
        DeltaCRLIndicatorExtension deltaCRLIndicatorExtension = getDeltaCRLIndicatorExtension();
        if (deltaCRLIndicatorExtension != null) {
            return deltaCRLIndicatorExtension.get("value");
        }
        return null;
    }

    public IssuerAlternativeNameExtension getIssuerAltNameExtension() throws IOException {
        return (IssuerAlternativeNameExtension) getExtension(PKIXExtensions.IssuerAlternativeName_Id);
    }

    public IssuingDistributionPointExtension getIssuingDistributionPointExtension() throws IOException {
        return (IssuingDistributionPointExtension) getExtension(PKIXExtensions.IssuingDistributionPoint_Id);
    }

    @Override // java.security.cert.X509Extension
    public boolean hasUnsupportedCriticalExtension() {
        if (this.extensions == null) {
            return false;
        }
        return this.extensions.hasUnsupportedCriticalExtension();
    }

    @Override // java.security.cert.X509Extension
    public Set<String> getCriticalExtensionOIDs() {
        if (this.extensions == null) {
            return null;
        }
        TreeSet treeSet = new TreeSet();
        for (Extension extension : this.extensions.getAllExtensions()) {
            if (extension.isCritical()) {
                treeSet.add(extension.getExtensionId().toString());
            }
        }
        return treeSet;
    }

    @Override // java.security.cert.X509Extension
    public Set<String> getNonCriticalExtensionOIDs() {
        if (this.extensions == null) {
            return null;
        }
        TreeSet treeSet = new TreeSet();
        for (Extension extension : this.extensions.getAllExtensions()) {
            if (!extension.isCritical()) {
                treeSet.add(extension.getExtensionId().toString());
            }
        }
        return treeSet;
    }

    @Override // java.security.cert.X509Extension
    public byte[] getExtensionValue(String str) {
        byte[] extensionValue;
        if (this.extensions == null) {
            return null;
        }
        try {
            String name = OIDMap.getName(new ObjectIdentifier(str));
            Extension extension = null;
            if (name == null) {
                ObjectIdentifier objectIdentifier = new ObjectIdentifier(str);
                Enumeration<Extension> elements = this.extensions.getElements();
                while (true) {
                    if (!elements.hasMoreElements()) {
                        break;
                    }
                    Extension extensionNextElement2 = elements.nextElement2();
                    if (extensionNextElement2.getExtensionId().equals((Object) objectIdentifier)) {
                        extension = extensionNextElement2;
                        break;
                    }
                }
            } else {
                extension = this.extensions.get(name);
            }
            if (extension == null || (extensionValue = extension.getExtensionValue()) == null) {
                return null;
            }
            DerOutputStream derOutputStream = new DerOutputStream();
            derOutputStream.putOctetString(extensionValue);
            return derOutputStream.toByteArray();
        } catch (Exception e2) {
            return null;
        }
    }

    public Object getExtension(ObjectIdentifier objectIdentifier) {
        if (this.extensions == null) {
            return null;
        }
        return this.extensions.get(OIDMap.getName(objectIdentifier));
    }

    private void parse(DerValue derValue) throws IOException, CRLException {
        if (this.readOnly) {
            throw new CRLException("cannot over-write existing CRL");
        }
        if (derValue.getData() == null || derValue.tag != 48) {
            throw new CRLException("Invalid DER-encoded CRL data");
        }
        this.signedCRL = derValue.toByteArray();
        DerValue[] derValueArr = {derValue.data.getDerValue(), derValue.data.getDerValue(), derValue.data.getDerValue()};
        if (derValue.data.available() != 0) {
            throw new CRLException("signed overrun, bytes = " + derValue.data.available());
        }
        if (derValueArr[0].tag != 48) {
            throw new CRLException("signed CRL fields invalid");
        }
        this.sigAlgId = AlgorithmId.parse(derValueArr[1]);
        this.signature = derValueArr[2].getBitString();
        if (derValueArr[1].data.available() != 0) {
            throw new CRLException("AlgorithmId field overrun");
        }
        if (derValueArr[2].data.available() != 0) {
            throw new CRLException("Signature field overrun");
        }
        this.tbsCertList = derValueArr[0].toByteArray();
        DerInputStream derInputStream = derValueArr[0].data;
        this.version = 0;
        if (((byte) derInputStream.peekByte()) == 2) {
            this.version = derInputStream.getInteger();
            if (this.version != 1) {
                throw new CRLException("Invalid version");
            }
        }
        AlgorithmId algorithmId = AlgorithmId.parse(derInputStream.getDerValue());
        if (!algorithmId.equals(this.sigAlgId)) {
            throw new CRLException("Signature algorithm mismatch");
        }
        this.infoSigAlgId = algorithmId;
        this.issuer = new X500Name(derInputStream);
        if (this.issuer.isEmpty()) {
            throw new CRLException("Empty issuer DN not allowed in X509CRLs");
        }
        byte bPeekByte = (byte) derInputStream.peekByte();
        if (bPeekByte == 23) {
            this.thisUpdate = derInputStream.getUTCTime();
        } else if (bPeekByte == 24) {
            this.thisUpdate = derInputStream.getGeneralizedTime();
        } else {
            throw new CRLException("Invalid encoding for thisUpdate (tag=" + ((int) bPeekByte) + ")");
        }
        if (derInputStream.available() == 0) {
            return;
        }
        byte bPeekByte2 = (byte) derInputStream.peekByte();
        if (bPeekByte2 == 23) {
            this.nextUpdate = derInputStream.getUTCTime();
        } else if (bPeekByte2 == 24) {
            this.nextUpdate = derInputStream.getGeneralizedTime();
        }
        if (derInputStream.available() == 0) {
            return;
        }
        byte bPeekByte3 = (byte) derInputStream.peekByte();
        if (bPeekByte3 == 48 && (bPeekByte3 & 192) != 128) {
            DerValue[] sequence = derInputStream.getSequence(4);
            X500Principal issuerX500Principal = getIssuerX500Principal();
            X500Principal certIssuer = issuerX500Principal;
            for (DerValue derValue2 : sequence) {
                X509CRLEntryImpl x509CRLEntryImpl = new X509CRLEntryImpl(derValue2);
                certIssuer = getCertIssuer(x509CRLEntryImpl, certIssuer);
                x509CRLEntryImpl.setCertificateIssuer(issuerX500Principal, certIssuer);
                this.revokedMap.put(new X509IssuerSerial(certIssuer, x509CRLEntryImpl.getSerialNumber()), x509CRLEntryImpl);
                this.revokedList.add(x509CRLEntryImpl);
            }
        }
        if (derInputStream.available() == 0) {
            return;
        }
        DerValue derValue3 = derInputStream.getDerValue();
        if (derValue3.isConstructed() && derValue3.isContextSpecific((byte) 0)) {
            this.extensions = new CRLExtensions(derValue3.data);
        }
        this.readOnly = true;
    }

    public static X500Principal getIssuerX500Principal(X509CRL x509crl) {
        try {
            DerInputStream derInputStream = new DerInputStream(x509crl.getEncoded()).getSequence(3)[0].data;
            if (((byte) derInputStream.peekByte()) == 2) {
                derInputStream.getDerValue();
            }
            derInputStream.getDerValue();
            return new X500Principal(derInputStream.getDerValue().toByteArray());
        } catch (Exception e2) {
            throw new RuntimeException("Could not parse issuer", e2);
        }
    }

    public static byte[] getEncodedInternal(X509CRL x509crl) throws CRLException {
        if (x509crl instanceof X509CRLImpl) {
            return ((X509CRLImpl) x509crl).getEncodedInternal();
        }
        return x509crl.getEncoded();
    }

    public static X509CRLImpl toImpl(X509CRL x509crl) throws CRLException {
        if (x509crl instanceof X509CRLImpl) {
            return (X509CRLImpl) x509crl;
        }
        return X509Factory.intern(x509crl);
    }

    private X500Principal getCertIssuer(X509CRLEntryImpl x509CRLEntryImpl, X500Principal x500Principal) throws IOException {
        CertificateIssuerExtension certificateIssuerExtension = x509CRLEntryImpl.getCertificateIssuerExtension();
        if (certificateIssuerExtension != null) {
            return ((X500Name) certificateIssuerExtension.get("issuer").get(0).getName()).asX500Principal();
        }
        return x500Principal;
    }

    @Override // sun.security.util.DerEncoder
    public void derEncode(OutputStream outputStream) throws IOException {
        if (this.signedCRL == null) {
            throw new IOException("Null CRL to encode");
        }
        outputStream.write((byte[]) this.signedCRL.clone());
    }

    /* loaded from: rt.jar:sun/security/x509/X509CRLImpl$X509IssuerSerial.class */
    private static final class X509IssuerSerial implements Comparable<X509IssuerSerial> {
        final X500Principal issuer;
        final BigInteger serial;
        volatile int hashcode;

        X509IssuerSerial(X500Principal x500Principal, BigInteger bigInteger) {
            this.hashcode = 0;
            this.issuer = x500Principal;
            this.serial = bigInteger;
        }

        X509IssuerSerial(X509Certificate x509Certificate) {
            this(x509Certificate.getIssuerX500Principal(), x509Certificate.getSerialNumber());
        }

        X500Principal getIssuer() {
            return this.issuer;
        }

        BigInteger getSerial() {
            return this.serial;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof X509IssuerSerial)) {
                return false;
            }
            X509IssuerSerial x509IssuerSerial = (X509IssuerSerial) obj;
            if (this.serial.equals(x509IssuerSerial.getSerial()) && this.issuer.equals(x509IssuerSerial.getIssuer())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            if (this.hashcode == 0) {
                this.hashcode = (37 * ((37 * 17) + this.issuer.hashCode())) + this.serial.hashCode();
            }
            return this.hashcode;
        }

        @Override // java.lang.Comparable
        public int compareTo(X509IssuerSerial x509IssuerSerial) {
            int iCompareTo = this.issuer.toString().compareTo(x509IssuerSerial.issuer.toString());
            return iCompareTo != 0 ? iCompareTo : this.serial.compareTo(x509IssuerSerial.serial);
        }
    }
}
