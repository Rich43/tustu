package sun.security.tools.policytool;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.CodeSource;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ListIterator;
import org.icepdf.core.util.PdfOps;
import sun.security.provider.PolicyParser;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/PolicyEntry.class */
class PolicyEntry {
    private CodeSource codesource;
    private PolicyTool tool;
    private PolicyParser.GrantEntry grantEntry;
    private boolean testing = false;

    PolicyEntry(PolicyTool policyTool, PolicyParser.GrantEntry grantEntry) throws IllegalAccessException, NoSuchMethodException, NoSuchAlgorithmException, UnrecoverableKeyException, InstantiationException, ClassNotFoundException, IOException, CertificateException, InvocationTargetException {
        this.tool = policyTool;
        URL url = grantEntry.codeBase != null ? new URL(grantEntry.codeBase) : null;
        this.codesource = new CodeSource(url, (Certificate[]) null);
        if (this.testing) {
            System.out.println("Adding Policy Entry:");
            System.out.println("    CodeBase = " + ((Object) url));
            System.out.println("    Signers = " + grantEntry.signedBy);
            System.out.println("    with " + grantEntry.principals.size() + " Principals");
        }
        this.grantEntry = grantEntry;
    }

    CodeSource getCodeSource() {
        return this.codesource;
    }

    PolicyParser.GrantEntry getGrantEntry() {
        return this.grantEntry;
    }

    String headerToString() {
        String strPrincipalsToString = principalsToString();
        if (strPrincipalsToString.length() == 0) {
            return codebaseToString();
        }
        return codebaseToString() + ", " + strPrincipalsToString;
    }

    String codebaseToString() {
        String strConcat;
        String str = new String();
        if (this.grantEntry.codeBase != null && !this.grantEntry.codeBase.equals("")) {
            str = str.concat("CodeBase \"" + this.grantEntry.codeBase + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        if (this.grantEntry.signedBy != null && !this.grantEntry.signedBy.equals("")) {
            if (str.length() > 0) {
                strConcat = str.concat(", SignedBy \"" + this.grantEntry.signedBy + PdfOps.DOUBLE_QUOTE__TOKEN);
            } else {
                strConcat = str.concat("SignedBy \"" + this.grantEntry.signedBy + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
            str = strConcat;
        }
        if (str.length() == 0) {
            return new String("CodeBase <ALL>");
        }
        return str;
    }

    String principalsToString() {
        String string = "";
        if (this.grantEntry.principals != null && !this.grantEntry.principals.isEmpty()) {
            StringBuffer stringBuffer = new StringBuffer(200);
            ListIterator<PolicyParser.PrincipalEntry> listIterator = this.grantEntry.principals.listIterator();
            while (listIterator.hasNext()) {
                PolicyParser.PrincipalEntry next = listIterator.next();
                stringBuffer.append(" Principal " + next.getDisplayClass() + " " + next.getDisplayName(true));
                if (listIterator.hasNext()) {
                    stringBuffer.append(", ");
                }
            }
            string = stringBuffer.toString();
        }
        return string;
    }

    PolicyParser.PermissionEntry toPermissionEntry(Permission permission) {
        String actions = null;
        if (permission.getActions() != null && permission.getActions().trim() != "") {
            actions = permission.getActions();
        }
        return new PolicyParser.PermissionEntry(permission.getClass().getName(), permission.getName(), actions);
    }
}
