package sun.security.provider.certpath;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.security.provider.certpath.PKIX;
import sun.security.provider.certpath.URICertStore;
import sun.security.util.Debug;
import sun.security.validator.Validator;
import sun.security.x509.AuthorityKeyIdentifierExtension;
import sun.security.x509.CRLDistributionPointsExtension;
import sun.security.x509.DistributionPoint;
import sun.security.x509.DistributionPointName;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.GeneralNames;
import sun.security.x509.IssuingDistributionPointExtension;
import sun.security.x509.KeyIdentifier;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.RDN;
import sun.security.x509.ReasonFlags;
import sun.security.x509.SerialNumber;
import sun.security.x509.URIName;
import sun.security.x509.X500Name;
import sun.security.x509.X509CRLImpl;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/provider/certpath/DistributionPointFetcher.class */
public class DistributionPointFetcher {
    private static final Debug debug = Debug.getInstance("certpath");
    private static final boolean[] ALL_REASONS = {true, true, true, true, true, true, true, true, true};

    private DistributionPointFetcher() {
    }

    public static Collection<X509CRL> getCRLs(X509CRLSelector x509CRLSelector, boolean z2, PublicKey publicKey, String str, List<CertStore> list, boolean[] zArr, Set<TrustAnchor> set, Date date, String str2) throws CertStoreException {
        return getCRLs(x509CRLSelector, z2, publicKey, null, str, list, zArr, set, date, str2, null);
    }

    public static Collection<X509CRL> getCRLs(X509CRLSelector x509CRLSelector, boolean z2, PublicKey publicKey, String str, List<CertStore> list, boolean[] zArr, Set<TrustAnchor> set, Date date) throws CertStoreException {
        if (set.isEmpty()) {
            throw new CertStoreException("at least one TrustAnchor must be specified");
        }
        return getCRLs(x509CRLSelector, z2, publicKey, null, str, list, zArr, set, date, Validator.VAR_PLUGIN_CODE_SIGNING, set.iterator().next());
    }

    public static Collection<X509CRL> getCRLs(X509CRLSelector x509CRLSelector, boolean z2, PublicKey publicKey, X509Certificate x509Certificate, String str, List<CertStore> list, boolean[] zArr, Set<TrustAnchor> set, Date date, String str2, TrustAnchor trustAnchor) throws CertStoreException {
        X509Certificate certificateChecking = x509CRLSelector.getCertificateChecking();
        if (certificateChecking == null) {
            return Collections.emptySet();
        }
        try {
            X509CertImpl impl = X509CertImpl.toImpl(certificateChecking);
            if (debug != null) {
                debug.println("DistributionPointFetcher.getCRLs: Checking CRLDPs for " + ((Object) impl.getSubjectX500Principal()));
            }
            CRLDistributionPointsExtension cRLDistributionPointsExtension = impl.getCRLDistributionPointsExtension();
            if (cRLDistributionPointsExtension == null) {
                if (debug != null) {
                    debug.println("No CRLDP ext");
                }
                return Collections.emptySet();
            }
            List<DistributionPoint> list2 = cRLDistributionPointsExtension.get(CRLDistributionPointsExtension.POINTS);
            HashSet hashSet = new HashSet();
            Iterator<DistributionPoint> it = list2.iterator();
            while (it.hasNext() && !Arrays.equals(zArr, ALL_REASONS)) {
                hashSet.addAll(getCRLs(x509CRLSelector, impl, it.next(), zArr, z2, publicKey, x509Certificate, str, list, set, date, str2, trustAnchor));
            }
            if (debug != null) {
                debug.println("Returning " + hashSet.size() + " CRLs");
            }
            return hashSet;
        } catch (IOException | CertificateException e2) {
            return Collections.emptySet();
        }
    }

    private static Collection<X509CRL> getCRLs(X509CRLSelector x509CRLSelector, X509CertImpl x509CertImpl, DistributionPoint distributionPoint, boolean[] zArr, boolean z2, PublicKey publicKey, X509Certificate x509Certificate, String str, List<CertStore> list, Set<TrustAnchor> set, Date date, String str2, TrustAnchor trustAnchor) throws CertStoreException {
        X509CRL crl;
        GeneralNames fullName = distributionPoint.getFullName();
        if (fullName == null) {
            RDN relativeName = distributionPoint.getRelativeName();
            if (relativeName == null) {
                return Collections.emptySet();
            }
            try {
                GeneralNames cRLIssuer = distributionPoint.getCRLIssuer();
                if (cRLIssuer == null) {
                    fullName = getFullNames((X500Name) x509CertImpl.getIssuerDN(), relativeName);
                } else {
                    if (cRLIssuer.size() != 1) {
                        return Collections.emptySet();
                    }
                    fullName = getFullNames((X500Name) cRLIssuer.get(0).getName(), relativeName);
                }
            } catch (IOException e2) {
                return Collections.emptySet();
            }
        }
        ArrayList<X509CRL> arrayList = new ArrayList();
        CertStoreException certStoreException = null;
        Iterator<GeneralName> it = fullName.iterator();
        while (it.hasNext()) {
            try {
                GeneralName next = it.next();
                if (next.getType() == 4) {
                    arrayList.addAll(getCRLs((X500Name) next.getName(), x509CertImpl.getIssuerX500Principal(), list));
                } else if (next.getType() == 6 && (crl = getCRL((URIName) next.getName())) != null) {
                    arrayList.add(crl);
                }
            } catch (CertStoreException e3) {
                certStoreException = e3;
            }
        }
        if (arrayList.isEmpty() && certStoreException != null) {
            throw certStoreException;
        }
        ArrayList arrayList2 = new ArrayList(2);
        for (X509CRL x509crl : arrayList) {
            try {
                x509CRLSelector.setIssuerNames(null);
                if (x509CRLSelector.match(x509crl) && verifyCRL(x509CertImpl, distributionPoint, x509crl, zArr, z2, publicKey, x509Certificate, str, set, list, date, str2, trustAnchor)) {
                    arrayList2.add(x509crl);
                }
            } catch (IOException | CRLException e4) {
                if (debug != null) {
                    debug.println("Exception verifying CRL: " + e4.getMessage());
                    e4.printStackTrace();
                }
            }
        }
        return arrayList2;
    }

    private static X509CRL getCRL(URIName uRIName) throws CertStoreException {
        URI uri = uRIName.getURI();
        if (debug != null) {
            debug.println("Trying to fetch CRL from DP " + ((Object) uri));
        }
        try {
            Collection<? extends CRL> cRLs = URICertStore.getInstance(new URICertStore.URICertStoreParameters(uri)).getCRLs(null);
            if (cRLs.isEmpty()) {
                return null;
            }
            return (X509CRL) cRLs.iterator().next();
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e2) {
            if (debug != null) {
                debug.println("Can't create URICertStore: " + e2.getMessage());
                return null;
            }
            return null;
        }
    }

    private static Collection<X509CRL> getCRLs(X500Name x500Name, X500Principal x500Principal, List<CertStore> list) throws CertStoreException {
        if (debug != null) {
            debug.println("Trying to fetch CRL from DP " + ((Object) x500Name));
        }
        X509CRLSelector x509CRLSelector = new X509CRLSelector();
        x509CRLSelector.addIssuer(x500Name.asX500Principal());
        x509CRLSelector.addIssuer(x500Principal);
        ArrayList arrayList = new ArrayList();
        PKIX.CertStoreTypeException certStoreTypeException = null;
        for (CertStore certStore : list) {
            try {
                Iterator<? extends CRL> it = certStore.getCRLs(x509CRLSelector).iterator();
                while (it.hasNext()) {
                    arrayList.add((X509CRL) it.next());
                }
            } catch (CertStoreException e2) {
                if (debug != null) {
                    debug.println("Exception while retrieving CRLs: " + ((Object) e2));
                    e2.printStackTrace();
                }
                certStoreTypeException = new PKIX.CertStoreTypeException(certStore.getType(), e2);
            }
        }
        if (arrayList.isEmpty() && certStoreTypeException != null) {
            throw certStoreTypeException;
        }
        return arrayList;
    }

    static boolean verifyCRL(X509CertImpl x509CertImpl, DistributionPoint distributionPoint, X509CRL x509crl, boolean[] zArr, boolean z2, PublicKey publicKey, X509Certificate x509Certificate, String str, Set<TrustAnchor> set, List<CertStore> list, Date date, String str2, TrustAnchor trustAnchor) throws IOException, CRLException {
        TrustAnchor trustAnchor2;
        if (debug != null) {
            debug.println("DistributionPointFetcher.verifyCRL: checking revocation status for\n  SN: " + Debug.toHexString(x509CertImpl.getSerialNumber()) + "\n  Subject: " + ((Object) x509CertImpl.getSubjectX500Principal()) + "\n  Issuer: " + ((Object) x509CertImpl.getIssuerX500Principal()));
        }
        boolean z3 = false;
        X509CRLImpl impl = X509CRLImpl.toImpl(x509crl);
        IssuingDistributionPointExtension issuingDistributionPointExtension = impl.getIssuingDistributionPointExtension();
        X500Name x500Name = (X500Name) x509CertImpl.getIssuerDN();
        X500Name x500Name2 = (X500Name) impl.getIssuerDN();
        GeneralNames cRLIssuer = distributionPoint.getCRLIssuer();
        X500Name x500Name3 = null;
        if (cRLIssuer != null) {
            if (issuingDistributionPointExtension == null || ((Boolean) issuingDistributionPointExtension.get(IssuingDistributionPointExtension.INDIRECT_CRL)).equals(Boolean.FALSE)) {
                return false;
            }
            boolean z4 = false;
            Iterator<GeneralName> it = cRLIssuer.iterator();
            while (!z4 && it.hasNext()) {
                GeneralNameInterface name = it.next().getName();
                if (x500Name2.equals(name)) {
                    x500Name3 = (X500Name) name;
                    z4 = true;
                }
            }
            if (!z4) {
                return false;
            }
            if (issues(x509CertImpl, impl, str)) {
                publicKey = x509CertImpl.getPublicKey();
            } else {
                z3 = true;
            }
        } else {
            if (!x500Name2.equals(x500Name)) {
                if (debug != null) {
                    debug.println("crl issuer does not equal cert issuer.\ncrl issuer: " + ((Object) x500Name2) + "\ncert issuer: " + ((Object) x500Name));
                    return false;
                }
                return false;
            }
            KeyIdentifier authKeyId = x509CertImpl.getAuthKeyId();
            KeyIdentifier authKeyId2 = impl.getAuthKeyId();
            if (authKeyId == null || authKeyId2 == null) {
                if (issues(x509CertImpl, impl, str)) {
                    publicKey = x509CertImpl.getPublicKey();
                }
            } else if (!authKeyId.equals(authKeyId2)) {
                if (issues(x509CertImpl, impl, str)) {
                    publicKey = x509CertImpl.getPublicKey();
                } else {
                    z3 = true;
                }
            }
        }
        if (!z3 && !z2) {
            return false;
        }
        if (issuingDistributionPointExtension != null) {
            DistributionPointName distributionPointName = (DistributionPointName) issuingDistributionPointExtension.get(IssuingDistributionPointExtension.POINT);
            if (distributionPointName != null) {
                GeneralNames fullName = distributionPointName.getFullName();
                if (fullName == null) {
                    RDN relativeName = distributionPointName.getRelativeName();
                    if (relativeName == null) {
                        if (debug != null) {
                            debug.println("IDP must be relative or full DN");
                            return false;
                        }
                        return false;
                    }
                    if (debug != null) {
                        debug.println("IDP relativeName:" + ((Object) relativeName));
                    }
                    fullName = getFullNames(x500Name2, relativeName);
                }
                if (distributionPoint.getFullName() != null || distributionPoint.getRelativeName() != null) {
                    GeneralNames fullName2 = distributionPoint.getFullName();
                    if (fullName2 == null) {
                        RDN relativeName2 = distributionPoint.getRelativeName();
                        if (relativeName2 == null) {
                            if (debug != null) {
                                debug.println("DP must be relative or full DN");
                                return false;
                            }
                            return false;
                        }
                        if (debug != null) {
                            debug.println("DP relativeName:" + ((Object) relativeName2));
                        }
                        if (z3) {
                            if (cRLIssuer.size() != 1) {
                                if (debug != null) {
                                    debug.println("must only be one CRL issuer when relative name present");
                                    return false;
                                }
                                return false;
                            }
                            fullName2 = getFullNames(x500Name3, relativeName2);
                        } else {
                            fullName2 = getFullNames(x500Name, relativeName2);
                        }
                    }
                    boolean zEquals = false;
                    Iterator<GeneralName> it2 = fullName.iterator();
                    while (!zEquals && it2.hasNext()) {
                        GeneralNameInterface name2 = it2.next().getName();
                        if (debug != null) {
                            debug.println("idpName: " + ((Object) name2));
                        }
                        Iterator<GeneralName> it3 = fullName2.iterator();
                        while (!zEquals && it3.hasNext()) {
                            GeneralNameInterface name3 = it3.next().getName();
                            if (debug != null) {
                                debug.println("pointName: " + ((Object) name3));
                            }
                            zEquals = name2.equals(name3);
                        }
                    }
                    if (!zEquals) {
                        if (debug != null) {
                            debug.println("IDP name does not match DP name");
                            return false;
                        }
                        return false;
                    }
                } else {
                    boolean zEquals2 = false;
                    Iterator<GeneralName> it4 = cRLIssuer.iterator();
                    while (!zEquals2 && it4.hasNext()) {
                        GeneralNameInterface name4 = it4.next().getName();
                        Iterator<GeneralName> it5 = fullName.iterator();
                        while (!zEquals2 && it5.hasNext()) {
                            zEquals2 = name4.equals(it5.next().getName());
                        }
                    }
                    if (!zEquals2) {
                        return false;
                    }
                }
            }
            if (((Boolean) issuingDistributionPointExtension.get(IssuingDistributionPointExtension.ONLY_USER_CERTS)).equals(Boolean.TRUE) && x509CertImpl.getBasicConstraints() != -1) {
                if (debug != null) {
                    debug.println("cert must be a EE cert");
                    return false;
                }
                return false;
            }
            if (((Boolean) issuingDistributionPointExtension.get(IssuingDistributionPointExtension.ONLY_CA_CERTS)).equals(Boolean.TRUE) && x509CertImpl.getBasicConstraints() == -1) {
                if (debug != null) {
                    debug.println("cert must be a CA cert");
                    return false;
                }
                return false;
            }
            if (((Boolean) issuingDistributionPointExtension.get(IssuingDistributionPointExtension.ONLY_ATTRIBUTE_CERTS)).equals(Boolean.TRUE)) {
                if (debug != null) {
                    debug.println("cert must not be an AA cert");
                    return false;
                }
                return false;
            }
        }
        boolean[] zArr2 = new boolean[9];
        ReasonFlags reasonFlags = null;
        if (issuingDistributionPointExtension != null) {
            reasonFlags = (ReasonFlags) issuingDistributionPointExtension.get(IssuingDistributionPointExtension.REASONS);
        }
        boolean[] reasonFlags2 = distributionPoint.getReasonFlags();
        if (reasonFlags != null) {
            if (reasonFlags2 != null) {
                boolean[] flags = reasonFlags.getFlags();
                int i2 = 0;
                while (i2 < zArr2.length) {
                    zArr2[i2] = i2 < flags.length && flags[i2] && i2 < reasonFlags2.length && reasonFlags2[i2];
                    i2++;
                }
            } else {
                zArr2 = (boolean[]) reasonFlags.getFlags().clone();
            }
        } else if (issuingDistributionPointExtension == null || reasonFlags == null) {
            if (reasonFlags2 != null) {
                zArr2 = (boolean[]) reasonFlags2.clone();
            } else {
                Arrays.fill(zArr2, true);
            }
        }
        boolean z5 = false;
        for (int i3 = 0; i3 < zArr2.length && !z5; i3++) {
            if (zArr2[i3] && (i3 >= zArr.length || !zArr[i3])) {
                z5 = true;
            }
        }
        if (!z5) {
            return false;
        }
        if (z3) {
            X509CertSelector x509CertSelector = new X509CertSelector();
            x509CertSelector.setSubject(x500Name2.asX500Principal());
            x509CertSelector.setKeyUsage(new boolean[]{false, false, false, false, false, false, true});
            AuthorityKeyIdentifierExtension authKeyIdExtension = impl.getAuthKeyIdExtension();
            if (authKeyIdExtension != null) {
                byte[] encodedKeyIdentifier = authKeyIdExtension.getEncodedKeyIdentifier();
                if (encodedKeyIdentifier != null) {
                    x509CertSelector.setSubjectKeyIdentifier(encodedKeyIdentifier);
                }
                SerialNumber serialNumber = (SerialNumber) authKeyIdExtension.get(AuthorityKeyIdentifierExtension.SERIAL_NUMBER);
                if (serialNumber != null) {
                    x509CertSelector.setSerialNumber(serialNumber.getNumber());
                }
            }
            HashSet hashSet = new HashSet(set);
            if (publicKey != null) {
                if (x509Certificate != null) {
                    trustAnchor2 = new TrustAnchor(x509Certificate, null);
                } else {
                    trustAnchor2 = new TrustAnchor(x509CertImpl.getIssuerX500Principal(), publicKey, (byte[]) null);
                }
                hashSet.add(trustAnchor2);
            }
            try {
                PKIXBuilderParameters pKIXBuilderParameters = new PKIXBuilderParameters(hashSet, x509CertSelector);
                pKIXBuilderParameters.setCertStores(list);
                pKIXBuilderParameters.setSigProvider(str);
                pKIXBuilderParameters.setDate(date);
                try {
                    publicKey = ((PKIXCertPathBuilderResult) CertPathBuilder.getInstance(Validator.TYPE_PKIX).build(pKIXBuilderParameters)).getPublicKey();
                } catch (GeneralSecurityException e2) {
                    throw new CRLException(e2);
                }
            } catch (InvalidAlgorithmParameterException e3) {
                throw new CRLException(e3);
            }
        }
        try {
            AlgorithmChecker.check(publicKey, impl.getSigAlgId(), str2, trustAnchor);
            try {
                x509crl.verify(publicKey, str);
                Set<String> criticalExtensionOIDs = x509crl.getCriticalExtensionOIDs();
                if (criticalExtensionOIDs != null) {
                    criticalExtensionOIDs.remove(PKIXExtensions.IssuingDistributionPoint_Id.toString());
                    if (!criticalExtensionOIDs.isEmpty()) {
                        if (debug != null) {
                            debug.println("Unrecognized critical extension(s) in CRL: " + ((Object) criticalExtensionOIDs));
                            Iterator<String> it6 = criticalExtensionOIDs.iterator();
                            while (it6.hasNext()) {
                                debug.println(it6.next());
                            }
                            return false;
                        }
                        return false;
                    }
                }
                int i4 = 0;
                while (i4 < zArr.length) {
                    zArr[i4] = zArr[i4] || (i4 < zArr2.length && zArr2[i4]);
                    i4++;
                }
                return true;
            } catch (GeneralSecurityException e4) {
                if (debug != null) {
                    debug.println("CRL signature failed to verify");
                    return false;
                }
                return false;
            }
        } catch (CertPathValidatorException e5) {
            if (debug != null) {
                debug.println("CRL signature algorithm check failed: " + ((Object) e5));
                return false;
            }
            return false;
        }
    }

    private static GeneralNames getFullNames(X500Name x500Name, RDN rdn) throws IOException {
        ArrayList arrayList = new ArrayList(x500Name.rdns());
        arrayList.add(rdn);
        X500Name x500Name2 = new X500Name((RDN[]) arrayList.toArray(new RDN[0]));
        GeneralNames generalNames = new GeneralNames();
        generalNames.add(new GeneralName(x500Name2));
        return generalNames;
    }

    private static boolean issues(X509CertImpl x509CertImpl, X509CRLImpl x509CRLImpl, String str) throws IOException {
        AdaptableX509CertSelector adaptableX509CertSelector = new AdaptableX509CertSelector();
        boolean[] keyUsage = x509CertImpl.getKeyUsage();
        if (keyUsage != null) {
            keyUsage[6] = true;
            adaptableX509CertSelector.setKeyUsage(keyUsage);
        }
        adaptableX509CertSelector.setSubject(x509CRLImpl.getIssuerX500Principal());
        AuthorityKeyIdentifierExtension authKeyIdExtension = x509CRLImpl.getAuthKeyIdExtension();
        adaptableX509CertSelector.setSkiAndSerialNumber(authKeyIdExtension);
        boolean zMatch = adaptableX509CertSelector.match(x509CertImpl);
        if (zMatch && (authKeyIdExtension == null || x509CertImpl.getAuthorityKeyIdentifierExtension() == null)) {
            try {
                x509CRLImpl.verify(x509CertImpl.getPublicKey(), str);
                zMatch = true;
            } catch (GeneralSecurityException e2) {
                zMatch = false;
            }
        }
        return zMatch;
    }
}
