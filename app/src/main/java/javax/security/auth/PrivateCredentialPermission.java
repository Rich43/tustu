package javax.security.auth;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;
import org.icepdf.core.util.PdfOps;
import sun.security.util.ResourcesMgr;

/* loaded from: rt.jar:javax/security/auth/PrivateCredentialPermission.class */
public final class PrivateCredentialPermission extends Permission {
    private static final long serialVersionUID = 5284372143517237068L;
    private static final CredOwner[] EMPTY_PRINCIPALS = new CredOwner[0];
    private String credentialClass;
    private Set<Principal> principals;
    private transient CredOwner[] credOwners;
    private boolean testing;

    PrivateCredentialPermission(String str, Set<Principal> set) {
        super(str);
        this.testing = false;
        this.credentialClass = str;
        synchronized (set) {
            if (set.size() == 0) {
                this.credOwners = EMPTY_PRINCIPALS;
            } else {
                this.credOwners = new CredOwner[set.size()];
                int i2 = 0;
                for (Principal principal : set) {
                    int i3 = i2;
                    i2++;
                    this.credOwners[i3] = new CredOwner(principal.getClass().getName(), principal.getName());
                }
            }
        }
    }

    public PrivateCredentialPermission(String str, String str2) {
        super(str);
        this.testing = false;
        if (!"read".equalsIgnoreCase(str2)) {
            throw new IllegalArgumentException(ResourcesMgr.getString("actions.can.only.be.read."));
        }
        init(str);
    }

    public String getCredentialClass() {
        return this.credentialClass;
    }

    public String[][] getPrincipals() {
        if (this.credOwners == null || this.credOwners.length == 0) {
            return new String[0][0];
        }
        String[][] strArr = new String[this.credOwners.length][2];
        for (int i2 = 0; i2 < this.credOwners.length; i2++) {
            strArr[i2][0] = this.credOwners[i2].principalClass;
            strArr[i2][1] = this.credOwners[i2].principalName;
        }
        return strArr;
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (permission == null || !(permission instanceof PrivateCredentialPermission)) {
            return false;
        }
        PrivateCredentialPermission privateCredentialPermission = (PrivateCredentialPermission) permission;
        if (!impliesCredentialClass(this.credentialClass, privateCredentialPermission.credentialClass)) {
            return false;
        }
        return impliesPrincipalSet(this.credOwners, privateCredentialPermission.credOwners);
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PrivateCredentialPermission)) {
            return false;
        }
        PrivateCredentialPermission privateCredentialPermission = (PrivateCredentialPermission) obj;
        return implies(privateCredentialPermission) && privateCredentialPermission.implies(this);
    }

    @Override // java.security.Permission
    public int hashCode() {
        return this.credentialClass.hashCode();
    }

    @Override // java.security.Permission
    public String getActions() {
        return "read";
    }

    @Override // java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return null;
    }

    private void init(String str) {
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("invalid empty name");
        }
        ArrayList arrayList = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(str, " ", true);
        if (this.testing) {
            System.out.println("whole name = " + str);
        }
        this.credentialClass = stringTokenizer.nextToken();
        if (this.testing) {
            System.out.println("Credential Class = " + this.credentialClass);
        }
        if (!stringTokenizer.hasMoreTokens()) {
            throw new IllegalArgumentException(new MessageFormat(ResourcesMgr.getString("permission.name.name.syntax.invalid.")).format(new Object[]{str}) + ResourcesMgr.getString("Credential.Class.not.followed.by.a.Principal.Class.and.Name"));
        }
        while (stringTokenizer.hasMoreTokens()) {
            stringTokenizer.nextToken();
            String strNextToken = stringTokenizer.nextToken();
            if (this.testing) {
                System.out.println("    Principal Class = " + strNextToken);
            }
            if (!stringTokenizer.hasMoreTokens()) {
                throw new IllegalArgumentException(new MessageFormat(ResourcesMgr.getString("permission.name.name.syntax.invalid.")).format(new Object[]{str}) + ResourcesMgr.getString("Principal.Class.not.followed.by.a.Principal.Name"));
            }
            stringTokenizer.nextToken();
            String strNextToken2 = stringTokenizer.nextToken();
            if (!strNextToken2.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                throw new IllegalArgumentException(new MessageFormat(ResourcesMgr.getString("permission.name.name.syntax.invalid.")).format(new Object[]{str}) + ResourcesMgr.getString("Principal.Name.must.be.surrounded.by.quotes"));
            }
            if (!strNextToken2.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                while (stringTokenizer.hasMoreTokens()) {
                    strNextToken2 = strNextToken2 + stringTokenizer.nextToken();
                    if (strNextToken2.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                        break;
                    }
                }
                if (!strNextToken2.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                    throw new IllegalArgumentException(new MessageFormat(ResourcesMgr.getString("permission.name.name.syntax.invalid.")).format(new Object[]{str}) + ResourcesMgr.getString("Principal.Name.missing.end.quote"));
                }
            }
            if (this.testing) {
                System.out.println("\tprincipalName = '" + strNextToken2 + PdfOps.SINGLE_QUOTE_TOKEN);
            }
            String strSubstring = strNextToken2.substring(1, strNextToken2.length() - 1);
            if (strNextToken.equals("*") && !strSubstring.equals("*")) {
                throw new IllegalArgumentException(ResourcesMgr.getString("PrivateCredentialPermission.Principal.Class.can.not.be.a.wildcard.value.if.Principal.Name.is.not.a.wildcard.value"));
            }
            if (this.testing) {
                System.out.println("\tprincipalName = '" + strSubstring + PdfOps.SINGLE_QUOTE_TOKEN);
            }
            arrayList.add(new CredOwner(strNextToken, strSubstring));
        }
        this.credOwners = new CredOwner[arrayList.size()];
        arrayList.toArray(this.credOwners);
    }

    private boolean impliesCredentialClass(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        if (this.testing) {
            System.out.println("credential class comparison: " + str + "/" + str2);
        }
        if (str.equals("*")) {
            return true;
        }
        return str.equals(str2);
    }

    private boolean impliesPrincipalSet(CredOwner[] credOwnerArr, CredOwner[] credOwnerArr2) {
        if (credOwnerArr == null || credOwnerArr2 == null) {
            return false;
        }
        if (credOwnerArr2.length == 0) {
            return true;
        }
        if (credOwnerArr.length == 0) {
            return false;
        }
        for (CredOwner credOwner : credOwnerArr) {
            boolean z2 = false;
            int i2 = 0;
            while (true) {
                if (i2 >= credOwnerArr2.length) {
                    break;
                }
                if (!credOwner.implies(credOwnerArr2[i2])) {
                    i2++;
                } else {
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                return false;
            }
        }
        return true;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (getName().indexOf(" ") == -1 && getName().indexOf(PdfOps.DOUBLE_QUOTE__TOKEN) == -1) {
            this.credentialClass = getName();
            this.credOwners = EMPTY_PRINCIPALS;
        } else {
            init(getName());
        }
    }

    /* loaded from: rt.jar:javax/security/auth/PrivateCredentialPermission$CredOwner.class */
    static class CredOwner implements Serializable {
        private static final long serialVersionUID = -5607449830436408266L;
        String principalClass;
        String principalName;

        CredOwner(String str, String str2) {
            this.principalClass = str;
            this.principalName = str2;
        }

        public boolean implies(Object obj) {
            if (obj == null || !(obj instanceof CredOwner)) {
                return false;
            }
            CredOwner credOwner = (CredOwner) obj;
            if (this.principalClass.equals("*") || this.principalClass.equals(credOwner.principalClass)) {
                if (this.principalName.equals("*") || this.principalName.equals(credOwner.principalName)) {
                    return true;
                }
                return false;
            }
            return false;
        }

        public String toString() {
            return new MessageFormat(ResourcesMgr.getString("CredOwner.Principal.Class.class.Principal.Name.name")).format(new Object[]{this.principalClass, this.principalName});
        }
    }
}
