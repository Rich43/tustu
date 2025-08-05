package org.icepdf.core.pobjects.security;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.HashMap;
import java.util.List;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/security/EncryptionDictionary.class */
public class EncryptionDictionary extends Dictionary {
    public static final Name FILTER_KEY = new Name(PdfOps.F_NAME);
    public static final Name SUB_FILTER_KEY = new Name("SubFilter");
    public static final Name V_KEY = new Name("V");
    public static final Name LENGTH_KEY = new Name("Length");
    public static final Name R_KEY = new Name("R");
    public static final Name O_KEY = new Name("O");
    public static final Name U_KEY = new Name("U");
    public static final Name P_KEY = new Name(Constants._TAG_P);
    public static final Name CF_KEY = new Name("CF");
    public static final Name STMF_KEY = new Name("StmF");
    public static final Name STRF_KEY = new Name("StrF");
    public static final Name EEF_KEY = new Name("EEF");
    public static final Name OE_KEY = new Name("OE");
    public static final Name UE_KEY = new Name("UE");
    public static final Name PERMS_KEY = new Name("Perms");
    public static final Name ENCRYPT_METADATA_KEY = new Name("EncryptMetadata");
    private List fileID;
    private CryptFilter cryptFilter;
    private boolean isAuthenticatedUserPassword;
    private boolean isAuthenticatedOwnerPassword;

    public EncryptionDictionary(Library lib, HashMap<Object, Object> encryptionDictionary, List fileID) {
        super(lib, encryptionDictionary);
        this.fileID = null;
        this.entries = encryptionDictionary;
        this.fileID = fileID;
    }

    public List getFileID() {
        return this.fileID;
    }

    public Name getPreferredSecurityHandlerName() {
        return this.library.getName(this.entries, FILTER_KEY);
    }

    public Name getPreferredSecurityHandlerSubName() {
        return this.library.getName(this.entries, SUB_FILTER_KEY);
    }

    public int getVersion() {
        return this.library.getInt(this.entries, V_KEY);
    }

    public int getKeyLength() {
        int length = 40;
        int len = this.library.getInt(this.entries, LENGTH_KEY);
        if (len != 0) {
            length = len;
        }
        return length;
    }

    public int getRevisionNumber() {
        return this.library.getInt(this.entries, R_KEY);
    }

    public String getBigO() {
        Object tmp = this.library.getObject(this.entries, O_KEY);
        if (tmp instanceof StringObject) {
            return ((StringObject) tmp).getLiteralString();
        }
        return null;
    }

    public String getBigU() {
        Object tmp = this.library.getObject(this.entries, U_KEY);
        if (tmp instanceof StringObject) {
            return ((StringObject) tmp).getLiteralString();
        }
        return null;
    }

    public int getPermissions() {
        return this.library.getInt(this.entries, P_KEY);
    }

    public CryptFilter getCryptFilter() {
        HashMap tmp;
        if (this.cryptFilter == null && (tmp = (HashMap) this.library.getObject(this.entries, CF_KEY)) != null) {
            this.cryptFilter = new CryptFilter(this.library, tmp);
            return this.cryptFilter;
        }
        return this.cryptFilter;
    }

    public Name getStmF() {
        Object tmp = this.library.getObject(this.entries, STMF_KEY);
        if (tmp != null && (tmp instanceof Name)) {
            return (Name) tmp;
        }
        return null;
    }

    public Name getStrF() {
        Object tmp = this.library.getObject(this.entries, STRF_KEY);
        if (tmp != null && (tmp instanceof Name)) {
            return (Name) tmp;
        }
        return null;
    }

    public Name getEEF() {
        Object tmp = this.library.getObject(this.entries, EEF_KEY);
        if (tmp != null && (tmp instanceof Name)) {
            return (Name) tmp;
        }
        return null;
    }

    public String getBigOE() {
        Object tmp = this.library.getObject(this.entries, OE_KEY);
        if (tmp instanceof StringObject) {
            return ((StringObject) tmp).getLiteralString();
        }
        return null;
    }

    public String getBigUE() {
        Object tmp = this.library.getObject(this.entries, UE_KEY);
        if (tmp instanceof StringObject) {
            return ((StringObject) tmp).getLiteralString();
        }
        return null;
    }

    public String getPerms() {
        Object tmp = this.library.getObject(this.entries, PERMS_KEY);
        if (tmp instanceof StringObject) {
            return ((StringObject) tmp).getLiteralString();
        }
        return null;
    }

    public boolean isEncryptMetaData() {
        return this.library.getBoolean(this.entries, ENCRYPT_METADATA_KEY).booleanValue();
    }

    protected boolean isAuthenticatedUserPassword() {
        return this.isAuthenticatedUserPassword;
    }

    protected void setAuthenticatedUserPassword(boolean authenticatedUserPassword) {
        this.isAuthenticatedUserPassword = authenticatedUserPassword;
    }

    protected boolean isAuthenticatedOwnerPassword() {
        return this.isAuthenticatedOwnerPassword;
    }

    protected void setAuthenticatedOwnerPassword(boolean authenticatedOwnerPassword) {
        this.isAuthenticatedOwnerPassword = authenticatedOwnerPassword;
    }

    public Object getValue(Object key) {
        return this.entries.get(key);
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public String toString() {
        return "Encryption Dictionary:  \n  fileID: " + ((Object) getFileID()) + " \n  Filter: " + ((Object) getPreferredSecurityHandlerName()) + " \n  SubFilter: " + ((Object) getPreferredSecurityHandlerSubName()) + " \n  V: " + getVersion() + " \n  P: " + getPermissions() + " \n  Length:" + getKeyLength() + " \n  CF: " + ((Object) this.cryptFilter) + " \n  StmF: " + ((Object) getStmF()) + " \n  StrF: " + ((Object) getStrF()) + " \n  R: " + getRevisionNumber() + " \n  O: " + getBigO() + " \n  U: " + getBigU() + " \n UE: " + getBigUE() + " \n OE: " + getBigOE() + " \n  Recipients: not done yet \n" + sun.security.pkcs11.wrapper.Constants.INDENT;
    }
}
