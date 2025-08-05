package sun.security.jgss.krb5;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Provider;
import java.util.Locale;
import javax.security.auth.kerberos.ServicePermission;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/jgss/krb5/Krb5NameElement.class */
public class Krb5NameElement implements GSSNameSpi {
    private PrincipalName krb5PrincipalName;
    private String gssNameStr;
    private Oid gssNameType;
    private static String CHAR_ENCODING = "UTF-8";

    private Krb5NameElement(PrincipalName principalName, String str, Oid oid) {
        this.gssNameStr = null;
        this.gssNameType = null;
        this.krb5PrincipalName = principalName;
        this.gssNameStr = str;
        this.gssNameType = oid;
    }

    static Krb5NameElement getInstance(String str, Oid oid) throws GSSException {
        PrincipalName principalName;
        SecurityManager securityManager;
        if (oid == null) {
            oid = Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL;
        } else if (!oid.equals(GSSName.NT_USER_NAME) && !oid.equals(GSSName.NT_HOSTBASED_SERVICE) && !oid.equals(Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL) && !oid.equals(GSSName.NT_EXPORT_NAME)) {
            throw new GSSException(4, -1, oid.toString() + " is an unsupported nametype");
        }
        try {
            if (oid.equals(GSSName.NT_EXPORT_NAME) || oid.equals(Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL)) {
                principalName = new PrincipalName(str, 1);
            } else {
                String[] components = getComponents(str);
                if (oid.equals(GSSName.NT_USER_NAME)) {
                    principalName = new PrincipalName(str, 1);
                } else {
                    String str2 = null;
                    String str3 = components[0];
                    if (components.length >= 2) {
                        str2 = components[1];
                    }
                    principalName = new PrincipalName(getHostBasedInstance(str3, str2), 3);
                }
            }
            if (principalName.isRealmDeduced() && !Realm.AUTODEDUCEREALM && (securityManager = System.getSecurityManager()) != null) {
                try {
                    securityManager.checkPermission(new ServicePermission("@" + principalName.getRealmAsString(), LanguageTag.SEP));
                } catch (SecurityException e2) {
                    throw new GSSException(11);
                }
            }
            return new Krb5NameElement(principalName, str, oid);
        } catch (KrbException e3) {
            throw new GSSException(3, -1, e3.getMessage());
        }
    }

    public static Krb5NameElement getInstance(PrincipalName principalName) {
        return new Krb5NameElement(principalName, principalName.getName(), Krb5MechFactory.NT_GSS_KRB5_PRINCIPAL);
    }

    private static String[] getComponents(String str) throws GSSException {
        String[] strArr;
        int iLastIndexOf = str.lastIndexOf(64, str.length());
        if (iLastIndexOf > 0 && str.charAt(iLastIndexOf - 1) == '\\' && (iLastIndexOf - 2 < 0 || str.charAt(iLastIndexOf - 2) != '\\')) {
            iLastIndexOf = -1;
        }
        if (iLastIndexOf > 0) {
            strArr = new String[]{str.substring(0, iLastIndexOf), str.substring(iLastIndexOf + 1)};
        } else {
            strArr = new String[]{str};
        }
        return strArr;
    }

    private static String getHostBasedInstance(String str, String str2) throws GSSException {
        StringBuffer stringBuffer = new StringBuffer(str);
        if (str2 == null) {
            try {
                str2 = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e2) {
            }
        }
        return stringBuffer.append('/').append(str2.toLowerCase(Locale.ENGLISH)).toString();
    }

    public final PrincipalName getKrb5PrincipalName() {
        return this.krb5PrincipalName;
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public boolean equals(GSSNameSpi gSSNameSpi) throws GSSException {
        if (gSSNameSpi == this) {
            return true;
        }
        if (gSSNameSpi instanceof Krb5NameElement) {
            return this.krb5PrincipalName.getName().equals(((Krb5NameElement) gSSNameSpi).krb5PrincipalName.getName());
        }
        return false;
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        try {
            if (obj instanceof Krb5NameElement) {
                return equals((GSSNameSpi) obj);
            }
            return false;
        } catch (GSSException e2) {
            return false;
        }
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public int hashCode() {
        return 629 + this.krb5PrincipalName.getName().hashCode();
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public byte[] export() throws GSSException {
        byte[] bytes = null;
        try {
            bytes = this.krb5PrincipalName.getName().getBytes(CHAR_ENCODING);
        } catch (UnsupportedEncodingException e2) {
        }
        return bytes;
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public Oid getMechanism() {
        return Krb5MechFactory.GSS_KRB5_MECH_OID;
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public String toString() {
        return this.gssNameStr;
    }

    public Oid getGSSNameType() {
        return this.gssNameType;
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public Oid getStringNameType() {
        return this.gssNameType;
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public boolean isAnonymousName() {
        return this.gssNameType.equals(GSSName.NT_ANONYMOUS);
    }

    @Override // sun.security.jgss.spi.GSSNameSpi
    public Provider getProvider() {
        return Krb5MechFactory.PROVIDER;
    }
}
