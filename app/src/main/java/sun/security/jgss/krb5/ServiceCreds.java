package sun.security.jgss.krb5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosKey;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.kerberos.KeyTab;
import sun.security.krb5.Credentials;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;

/* loaded from: rt.jar:sun/security/jgss/krb5/ServiceCreds.class */
public final class ServiceCreds {
    private KerberosPrincipal kp;
    private Set<KerberosPrincipal> allPrincs;
    private List<KeyTab> ktabs;
    private List<KerberosKey> kk;
    private KerberosTicket tgt;
    private boolean destroyed;

    private ServiceCreds() {
    }

    public static ServiceCreds getInstance(Subject subject, String str) {
        ServiceCreds serviceCreds = new ServiceCreds();
        serviceCreds.allPrincs = subject.getPrincipals(KerberosPrincipal.class);
        Iterator it = SubjectComber.findMany(subject, str, null, KerberosKey.class).iterator();
        while (it.hasNext()) {
            serviceCreds.allPrincs.add(((KerberosKey) it.next()).getPrincipal());
        }
        if (str != null) {
            serviceCreds.kp = new KerberosPrincipal(str);
        } else if (serviceCreds.allPrincs.size() == 1) {
            boolean z2 = false;
            Iterator it2 = SubjectComber.findMany(subject, null, null, KeyTab.class).iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                if (!((KeyTab) it2.next()).isBound()) {
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                serviceCreds.kp = serviceCreds.allPrincs.iterator().next();
                str = serviceCreds.kp.getName();
            }
        }
        serviceCreds.ktabs = SubjectComber.findMany(subject, str, null, KeyTab.class);
        serviceCreds.kk = SubjectComber.findMany(subject, str, null, KerberosKey.class);
        serviceCreds.tgt = (KerberosTicket) SubjectComber.find(subject, null, str, KerberosTicket.class);
        if (serviceCreds.ktabs.isEmpty() && serviceCreds.kk.isEmpty() && serviceCreds.tgt == null) {
            return null;
        }
        serviceCreds.destroyed = false;
        return serviceCreds;
    }

    public String getName() {
        if (this.destroyed) {
            throw new IllegalStateException("This object is destroyed");
        }
        if (this.kp == null) {
            return null;
        }
        return this.kp.getName();
    }

    public KerberosKey[] getKKeys() {
        if (this.destroyed) {
            throw new IllegalStateException("This object is destroyed");
        }
        KerberosPrincipal kerberosPrincipal = this.kp;
        if (kerberosPrincipal == null && !this.allPrincs.isEmpty()) {
            kerberosPrincipal = this.allPrincs.iterator().next();
        }
        if (kerberosPrincipal == null) {
            Iterator<KeyTab> it = this.ktabs.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                PrincipalName oneName = Krb5Util.snapshotFromJavaxKeyTab(it.next()).getOneName();
                if (oneName != null) {
                    kerberosPrincipal = new KerberosPrincipal(oneName.getName());
                    break;
                }
            }
        }
        if (kerberosPrincipal != null) {
            return getKKeys(kerberosPrincipal);
        }
        return new KerberosKey[0];
    }

    public KerberosKey[] getKKeys(KerberosPrincipal kerberosPrincipal) {
        if (this.destroyed) {
            throw new IllegalStateException("This object is destroyed");
        }
        ArrayList arrayList = new ArrayList();
        if (this.kp != null && !kerberosPrincipal.equals(this.kp)) {
            return new KerberosKey[0];
        }
        for (KerberosKey kerberosKey : this.kk) {
            if (kerberosKey.getPrincipal().equals(kerberosPrincipal)) {
                arrayList.add(kerberosKey);
            }
        }
        for (KeyTab keyTab : this.ktabs) {
            if (keyTab.getPrincipal() != null || !keyTab.isBound() || this.allPrincs.contains(kerberosPrincipal)) {
                for (KerberosKey kerberosKey2 : keyTab.getKeys(kerberosPrincipal)) {
                    arrayList.add(kerberosKey2);
                }
            }
        }
        return (KerberosKey[]) arrayList.toArray(new KerberosKey[arrayList.size()]);
    }

    public EncryptionKey[] getEKeys(PrincipalName principalName) {
        if (this.destroyed) {
            throw new IllegalStateException("This object is destroyed");
        }
        KerberosKey[] kKeys = getKKeys(new KerberosPrincipal(principalName.getName()));
        if (kKeys.length == 0) {
            kKeys = getKKeys();
        }
        EncryptionKey[] encryptionKeyArr = new EncryptionKey[kKeys.length];
        for (int i2 = 0; i2 < encryptionKeyArr.length; i2++) {
            encryptionKeyArr[i2] = new EncryptionKey(kKeys[i2].getEncoded(), kKeys[i2].getKeyType(), new Integer(kKeys[i2].getVersionNumber()));
        }
        return encryptionKeyArr;
    }

    public Credentials getInitCred() {
        if (this.destroyed) {
            throw new IllegalStateException("This object is destroyed");
        }
        if (this.tgt == null) {
            return null;
        }
        try {
            return Krb5Util.ticketToCreds(this.tgt);
        } catch (IOException | KrbException e2) {
            return null;
        }
    }

    public void destroy() {
        this.destroyed = true;
        this.kp = null;
        this.ktabs.clear();
        this.kk.clear();
        this.tgt = null;
    }
}
