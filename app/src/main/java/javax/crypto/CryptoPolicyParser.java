package javax.crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.pobjects.graphics.Separation;
import org.icepdf.core.util.PdfOps;

/* loaded from: jce.jar:javax/crypto/CryptoPolicyParser.class */
final class CryptoPolicyParser {
    private Vector<GrantEntry> grantEntries = new Vector<>();
    private StreamTokenizer st;
    private int lookahead;

    CryptoPolicyParser() {
    }

    void read(Reader reader) throws ParsingException, IOException {
        if (!(reader instanceof BufferedReader)) {
            reader = new BufferedReader(reader);
        }
        this.st = new StreamTokenizer(reader);
        this.st.resetSyntax();
        this.st.wordChars(97, 122);
        this.st.wordChars(65, 90);
        this.st.wordChars(46, 46);
        this.st.wordChars(48, 57);
        this.st.wordChars(95, 95);
        this.st.wordChars(36, 36);
        this.st.wordChars(160, 255);
        this.st.whitespaceChars(0, 32);
        this.st.commentChar(47);
        this.st.quoteChar(39);
        this.st.quoteChar(34);
        this.st.lowerCaseMode(false);
        this.st.ordinaryChar(47);
        this.st.slashSlashComments(true);
        this.st.slashStarComments(true);
        this.st.parseNumbers();
        this.lookahead = this.st.nextToken();
        while (this.lookahead != -1) {
            if (peek("grant")) {
                GrantEntry grantEntry = parseGrantEntry(null);
                if (grantEntry != null) {
                    this.grantEntries.addElement(grantEntry);
                }
                match(";");
            } else {
                throw new ParsingException(this.st.lineno(), "expected grant statement");
            }
        }
    }

    private GrantEntry parseGrantEntry(Hashtable<String, Vector<String>> hashtable) throws ParsingException, IOException {
        GrantEntry grantEntry = new GrantEntry();
        match("grant");
        match(VectorFormat.DEFAULT_PREFIX);
        while (!peek("}")) {
            if (peek("Permission")) {
                grantEntry.add(parsePermissionEntry(hashtable));
                match(";");
            } else {
                throw new ParsingException(this.st.lineno(), "expected permission entry");
            }
        }
        match("}");
        return grantEntry;
    }

    private CryptoPermissionEntry parsePermissionEntry(Hashtable<String, Vector<String>> hashtable) throws ParsingException, IOException {
        CryptoPermissionEntry cryptoPermissionEntry = new CryptoPermissionEntry();
        match("Permission");
        cryptoPermissionEntry.cryptoPermission = match("permission type");
        if (cryptoPermissionEntry.cryptoPermission.equals("javax.crypto.CryptoAllPermission")) {
            cryptoPermissionEntry.alg = "CryptoAllPermission";
            cryptoPermissionEntry.maxKeySize = Integer.MAX_VALUE;
            return cryptoPermissionEntry;
        }
        if (peek(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            cryptoPermissionEntry.alg = match("quoted string").toUpperCase(Locale.ENGLISH);
        } else if (peek("*")) {
            match("*");
            cryptoPermissionEntry.alg = "*";
        } else {
            throw new ParsingException(this.st.lineno(), "Missing the algorithm name");
        }
        peekAndMatch(",");
        if (peek(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            cryptoPermissionEntry.exemptionMechanism = match("quoted string").toUpperCase(Locale.ENGLISH);
        }
        peekAndMatch(",");
        if (!isConsistent(cryptoPermissionEntry.alg, cryptoPermissionEntry.exemptionMechanism, hashtable)) {
            throw new ParsingException(this.st.lineno(), "Inconsistent policy");
        }
        if (peek("number")) {
            cryptoPermissionEntry.maxKeySize = match();
        } else if (peek("*")) {
            match("*");
            cryptoPermissionEntry.maxKeySize = Integer.MAX_VALUE;
        } else {
            if (!peek(";")) {
                throw new ParsingException(this.st.lineno(), "Missing the maximum allowable key size");
            }
            cryptoPermissionEntry.maxKeySize = Integer.MAX_VALUE;
        }
        peekAndMatch(",");
        if (peek(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            String strMatch = match("quoted string");
            Vector vector = new Vector(1);
            while (peek(",")) {
                match(",");
                if (peek("number")) {
                    vector.addElement(new Integer(match()));
                } else if (peek("*")) {
                    match("*");
                    vector.addElement(new Integer(Integer.MAX_VALUE));
                } else {
                    throw new ParsingException(this.st.lineno(), "Expecting an integer");
                }
            }
            Integer[] numArr = new Integer[vector.size()];
            vector.copyInto(numArr);
            cryptoPermissionEntry.checkParam = true;
            cryptoPermissionEntry.algParamSpec = getInstance(strMatch, numArr);
        }
        return cryptoPermissionEntry;
    }

    private static final AlgorithmParameterSpec getInstance(String str, Integer[] numArr) throws ParsingException {
        try {
            Class<?> cls = Class.forName(str);
            Class<?>[] clsArr = new Class[numArr.length];
            for (int i2 = 0; i2 < numArr.length; i2++) {
                clsArr[i2] = Integer.TYPE;
            }
            return (AlgorithmParameterSpec) cls.getConstructor(clsArr).newInstance(numArr);
        } catch (Exception e2) {
            throw new ParsingException("Cannot call the constructor of " + str + ((Object) e2));
        }
    }

    private boolean peekAndMatch(String str) throws ParsingException, IOException {
        if (peek(str)) {
            match(str);
            return true;
        }
        return false;
    }

    private boolean peek(String str) {
        boolean z2 = false;
        switch (this.lookahead) {
            case -3:
                if (str.equalsIgnoreCase(this.st.sval)) {
                    z2 = true;
                    break;
                }
                break;
            case -2:
                if (str.equalsIgnoreCase("number")) {
                    z2 = true;
                    break;
                }
                break;
            case 34:
                if (str.equals(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                    z2 = true;
                    break;
                }
                break;
            case 42:
                if (str.equals("*")) {
                    z2 = true;
                    break;
                }
                break;
            case 44:
                if (str.equals(",")) {
                    z2 = true;
                    break;
                }
                break;
            case 59:
                if (str.equals(";")) {
                    z2 = true;
                    break;
                }
                break;
            case 123:
                if (str.equals(VectorFormat.DEFAULT_PREFIX)) {
                    z2 = true;
                    break;
                }
                break;
            case 125:
                if (str.equals("}")) {
                    z2 = true;
                    break;
                }
                break;
        }
        return z2;
    }

    private int match() throws ParsingException, IOException {
        int i2 = -1;
        int iLineno = this.st.lineno();
        String strValueOf = null;
        switch (this.lookahead) {
            case -2:
                i2 = (int) this.st.nval;
                if (i2 < 0) {
                    strValueOf = String.valueOf(this.st.nval);
                }
                this.lookahead = this.st.nextToken();
                break;
            default:
                strValueOf = this.st.sval;
                break;
        }
        if (i2 <= 0) {
            throw new ParsingException(iLineno, "a non-negative number", strValueOf);
        }
        return i2;
    }

    private String match(String str) throws ParsingException, IOException {
        String str2 = null;
        switch (this.lookahead) {
            case -3:
                if (str.equalsIgnoreCase(this.st.sval)) {
                    this.lookahead = this.st.nextToken();
                    break;
                } else if (str.equalsIgnoreCase("permission type")) {
                    str2 = this.st.sval;
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, this.st.sval);
                }
            case -2:
                throw new ParsingException(this.st.lineno(), str, "number " + String.valueOf(this.st.nval));
            case -1:
                throw new ParsingException("expected " + str + ", read end of file");
            case 34:
                if (str.equalsIgnoreCase("quoted string") || str.equalsIgnoreCase("permission type")) {
                    str2 = this.st.sval;
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, this.st.sval);
                }
                break;
            case 42:
                if (str.equals("*")) {
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, "*");
                }
            case 44:
                if (str.equals(",")) {
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, ",");
                }
            case 59:
                if (str.equals(";")) {
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, ";");
                }
            case 123:
                if (str.equals(VectorFormat.DEFAULT_PREFIX)) {
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, VectorFormat.DEFAULT_PREFIX);
                }
            case 125:
                if (str.equals("}")) {
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, "}");
                }
            default:
                throw new ParsingException(this.st.lineno(), str, new String(new char[]{(char) this.lookahead}));
        }
        return str2;
    }

    CryptoPermission[] getPermissions() {
        Vector vector = new Vector();
        Enumeration<GrantEntry> enumerationElements = this.grantEntries.elements();
        while (enumerationElements.hasMoreElements()) {
            Enumeration<CryptoPermissionEntry> enumerationPermissionElements = enumerationElements.nextElement2().permissionElements();
            while (enumerationPermissionElements.hasMoreElements()) {
                CryptoPermissionEntry cryptoPermissionEntryNextElement2 = enumerationPermissionElements.nextElement2();
                if (cryptoPermissionEntryNextElement2.cryptoPermission.equals("javax.crypto.CryptoAllPermission")) {
                    vector.addElement(CryptoAllPermission.INSTANCE);
                } else if (cryptoPermissionEntryNextElement2.checkParam) {
                    vector.addElement(new CryptoPermission(cryptoPermissionEntryNextElement2.alg, cryptoPermissionEntryNextElement2.maxKeySize, cryptoPermissionEntryNextElement2.algParamSpec, cryptoPermissionEntryNextElement2.exemptionMechanism));
                } else {
                    vector.addElement(new CryptoPermission(cryptoPermissionEntryNextElement2.alg, cryptoPermissionEntryNextElement2.maxKeySize, cryptoPermissionEntryNextElement2.exemptionMechanism));
                }
            }
        }
        CryptoPermission[] cryptoPermissionArr = new CryptoPermission[vector.size()];
        vector.copyInto(cryptoPermissionArr);
        return cryptoPermissionArr;
    }

    private boolean isConsistent(String str, String str2, Hashtable<String, Vector<String>> hashtable) {
        Vector<String> vector;
        String str3 = str2 == null ? Separation.COLORANT_NONE : str2;
        if (hashtable == null) {
            Hashtable hashtable2 = new Hashtable();
            Vector vector2 = new Vector(1);
            vector2.addElement(str3);
            hashtable2.put(str, vector2);
            return true;
        }
        if (hashtable.containsKey("CryptoAllPermission")) {
            return false;
        }
        if (hashtable.containsKey(str)) {
            vector = hashtable.get(str);
            if (vector.contains(str3)) {
                return false;
            }
        } else {
            vector = new Vector<>(1);
        }
        vector.addElement(str3);
        hashtable.put(str, vector);
        return true;
    }

    /* loaded from: jce.jar:javax/crypto/CryptoPolicyParser$GrantEntry.class */
    private static class GrantEntry {
        private Vector<CryptoPermissionEntry> permissionEntries = new Vector<>();

        GrantEntry() {
        }

        void add(CryptoPermissionEntry cryptoPermissionEntry) {
            this.permissionEntries.addElement(cryptoPermissionEntry);
        }

        boolean remove(CryptoPermissionEntry cryptoPermissionEntry) {
            return this.permissionEntries.removeElement(cryptoPermissionEntry);
        }

        boolean contains(CryptoPermissionEntry cryptoPermissionEntry) {
            return this.permissionEntries.contains(cryptoPermissionEntry);
        }

        Enumeration<CryptoPermissionEntry> permissionElements() {
            return this.permissionEntries.elements();
        }
    }

    /* loaded from: jce.jar:javax/crypto/CryptoPolicyParser$CryptoPermissionEntry.class */
    private static class CryptoPermissionEntry {
        String cryptoPermission;
        int maxKeySize = 0;
        String alg = null;
        String exemptionMechanism = null;
        boolean checkParam = false;
        AlgorithmParameterSpec algParamSpec = null;

        CryptoPermissionEntry() {
        }

        public int hashCode() {
            int iHashCode = this.cryptoPermission.hashCode();
            if (this.alg != null) {
                iHashCode ^= this.alg.hashCode();
            }
            if (this.exemptionMechanism != null) {
                iHashCode ^= this.exemptionMechanism.hashCode();
            }
            int iHashCode2 = iHashCode ^ this.maxKeySize;
            if (this.checkParam) {
                iHashCode2 ^= 100;
            }
            if (this.algParamSpec != null) {
                iHashCode2 ^= this.algParamSpec.hashCode();
            }
            return iHashCode2;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof CryptoPermissionEntry)) {
                return false;
            }
            CryptoPermissionEntry cryptoPermissionEntry = (CryptoPermissionEntry) obj;
            if (this.cryptoPermission == null) {
                if (cryptoPermissionEntry.cryptoPermission != null) {
                    return false;
                }
            } else if (!this.cryptoPermission.equals(cryptoPermissionEntry.cryptoPermission)) {
                return false;
            }
            if (this.alg == null) {
                if (cryptoPermissionEntry.alg != null) {
                    return false;
                }
            } else if (!this.alg.equalsIgnoreCase(cryptoPermissionEntry.alg)) {
                return false;
            }
            if (this.maxKeySize != cryptoPermissionEntry.maxKeySize || this.checkParam != cryptoPermissionEntry.checkParam) {
                return false;
            }
            if (this.algParamSpec == null) {
                return cryptoPermissionEntry.algParamSpec == null;
            }
            if (!this.algParamSpec.equals(cryptoPermissionEntry.algParamSpec)) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: jce.jar:javax/crypto/CryptoPolicyParser$ParsingException.class */
    static final class ParsingException extends GeneralSecurityException {
        private static final long serialVersionUID = 7147241245566588374L;

        ParsingException(String str) {
            super(str);
        }

        ParsingException(int i2, String str) {
            super("line " + i2 + ": " + str);
        }

        ParsingException(int i2, String str, String str2) {
            super("line " + i2 + ": expected '" + str + "', found '" + str2 + PdfOps.SINGLE_QUOTE_TOKEN);
        }
    }
}
