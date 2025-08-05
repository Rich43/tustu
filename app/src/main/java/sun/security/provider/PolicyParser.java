package sun.security.provider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;
import sun.net.www.ParseUtil;
import sun.security.pkcs11.wrapper.Constants;
import sun.security.tools.policytool.ToolDialog;
import sun.security.util.Debug;
import sun.security.util.PropertyExpander;
import sun.security.util.ResourcesMgr;

/* loaded from: rt.jar:sun/security/provider/PolicyParser.class */
public class PolicyParser {
    private static final String EXTDIRS_PROPERTY = "java.ext.dirs";
    private static final String OLD_EXTDIRS_EXPANSION = "${java.ext.dirs}";
    static final String EXTDIRS_EXPANSION = "${{java.ext.dirs}}";
    private Vector<GrantEntry> grantEntries;
    private Map<String, DomainEntry> domainEntries;
    private static final Debug debug = Debug.getInstance("parser", "\t[Policy Parser]");
    private StreamTokenizer st;
    private int lookahead;
    private boolean expandProp;
    private String keyStoreUrlString;
    private String keyStoreType;
    private String keyStoreProvider;
    private String storePassURL;

    private String expand(String str) throws PropertyExpander.ExpandException {
        return expand(str, false);
    }

    private String expand(String str, boolean z2) throws PropertyExpander.ExpandException {
        if (!this.expandProp) {
            return str;
        }
        return PropertyExpander.expand(str, z2);
    }

    public PolicyParser() {
        this.expandProp = false;
        this.keyStoreUrlString = null;
        this.keyStoreType = null;
        this.keyStoreProvider = null;
        this.storePassURL = null;
        this.grantEntries = new Vector<>();
    }

    public PolicyParser(boolean z2) {
        this();
        this.expandProp = z2;
    }

    public void read(Reader reader) throws ParsingException, IOException {
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
        this.lookahead = this.st.nextToken();
        GrantEntry grantEntry = null;
        while (this.lookahead != -1) {
            if (peek("grant")) {
                grantEntry = parseGrantEntry();
                if (grantEntry != null) {
                    add(grantEntry);
                }
            } else if (peek("keystore") && this.keyStoreUrlString == null) {
                parseKeyStoreEntry();
            } else if (peek("keystorePasswordURL") && this.storePassURL == null) {
                parseStorePassURL();
            } else if (grantEntry == null && this.keyStoreUrlString == null && this.storePassURL == null && peek("domain")) {
                if (this.domainEntries == null) {
                    this.domainEntries = new TreeMap();
                }
                DomainEntry domainEntry = parseDomainEntry();
                if (domainEntry != null) {
                    String name = domainEntry.getName();
                    if (!this.domainEntries.containsKey(name)) {
                        this.domainEntries.put(name, domainEntry);
                    } else {
                        throw new ParsingException(new MessageFormat(ResourcesMgr.getString("duplicate.keystore.domain.name")).format(new Object[]{name}));
                    }
                } else {
                    continue;
                }
            }
            match(";");
        }
        if (this.keyStoreUrlString == null && this.storePassURL != null) {
            throw new ParsingException(ResourcesMgr.getString("keystorePasswordURL.can.not.be.specified.without.also.specifying.keystore"));
        }
    }

    public void add(GrantEntry grantEntry) {
        this.grantEntries.addElement(grantEntry);
    }

    public void replace(GrantEntry grantEntry, GrantEntry grantEntry2) {
        this.grantEntries.setElementAt(grantEntry2, this.grantEntries.indexOf(grantEntry));
    }

    public boolean remove(GrantEntry grantEntry) {
        return this.grantEntries.removeElement(grantEntry);
    }

    public String getKeyStoreUrl() {
        try {
            if (this.keyStoreUrlString != null && this.keyStoreUrlString.length() != 0) {
                return expand(this.keyStoreUrlString, true).replace(File.separatorChar, '/');
            }
            return null;
        } catch (PropertyExpander.ExpandException e2) {
            if (debug != null) {
                debug.println(e2.toString());
                return null;
            }
            return null;
        }
    }

    public void setKeyStoreUrl(String str) {
        this.keyStoreUrlString = str;
    }

    public String getKeyStoreType() {
        return this.keyStoreType;
    }

    public void setKeyStoreType(String str) {
        this.keyStoreType = str;
    }

    public String getKeyStoreProvider() {
        return this.keyStoreProvider;
    }

    public void setKeyStoreProvider(String str) {
        this.keyStoreProvider = str;
    }

    public String getStorePassURL() {
        try {
            if (this.storePassURL != null && this.storePassURL.length() != 0) {
                return expand(this.storePassURL, true).replace(File.separatorChar, '/');
            }
            return null;
        } catch (PropertyExpander.ExpandException e2) {
            if (debug != null) {
                debug.println(e2.toString());
                return null;
            }
            return null;
        }
    }

    public void setStorePassURL(String str) {
        this.storePassURL = str;
    }

    public Enumeration<GrantEntry> grantElements() {
        return this.grantEntries.elements();
    }

    public Collection<DomainEntry> getDomainEntries() {
        return this.domainEntries.values();
    }

    public void write(Writer writer) {
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(writer));
        Enumeration<GrantEntry> enumerationGrantElements = grantElements();
        printWriter.println("/* AUTOMATICALLY GENERATED ON " + ((Object) new Date()) + "*/");
        printWriter.println("/* DO NOT EDIT */");
        printWriter.println();
        if (this.keyStoreUrlString != null) {
            writeKeyStoreEntry(printWriter);
        }
        if (this.storePassURL != null) {
            writeStorePassURL(printWriter);
        }
        while (enumerationGrantElements.hasMoreElements()) {
            enumerationGrantElements.nextElement2().write(printWriter);
            printWriter.println();
        }
        printWriter.flush();
    }

    private void parseKeyStoreEntry() throws ParsingException, IOException {
        match("keystore");
        this.keyStoreUrlString = match("quoted string");
        if (!peek(",")) {
            return;
        }
        match(",");
        if (peek(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            this.keyStoreType = match("quoted string");
            if (!peek(",")) {
                return;
            }
            match(",");
            if (peek(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                this.keyStoreProvider = match("quoted string");
                return;
            }
            throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("expected.keystore.provider"));
        }
        throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("expected.keystore.type"));
    }

    private void parseStorePassURL() throws ParsingException, IOException {
        match("keyStorePasswordURL");
        this.storePassURL = match("quoted string");
    }

    private void writeKeyStoreEntry(PrintWriter printWriter) {
        printWriter.print("keystore \"");
        printWriter.print(this.keyStoreUrlString);
        printWriter.print('\"');
        if (this.keyStoreType != null && this.keyStoreType.length() > 0) {
            printWriter.print(", \"" + this.keyStoreType + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        if (this.keyStoreProvider != null && this.keyStoreProvider.length() > 0) {
            printWriter.print(", \"" + this.keyStoreProvider + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        printWriter.println(";");
        printWriter.println();
    }

    private void writeStorePassURL(PrintWriter printWriter) {
        printWriter.print("keystorePasswordURL \"");
        printWriter.print(this.storePassURL);
        printWriter.print('\"');
        printWriter.println(";");
        printWriter.println();
    }

    /* JADX WARN: Failed to analyze thrown exceptions
    java.util.ConcurrentModificationException
    	at java.base/java.util.ArrayList$Itr.checkForComodification(Unknown Source)
    	at java.base/java.util.ArrayList$Itr.next(Unknown Source)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.processInstructions(MethodThrowsVisitor.java:131)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.visit(MethodThrowsVisitor.java:69)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.checkInsn(MethodThrowsVisitor.java:179)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.processInstructions(MethodThrowsVisitor.java:132)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.visit(MethodThrowsVisitor.java:69)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.checkInsn(MethodThrowsVisitor.java:179)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.processInstructions(MethodThrowsVisitor.java:132)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.visit(MethodThrowsVisitor.java:69)
     */
    private GrantEntry parseGrantEntry() throws ParsingException, IOException {
        String strMatch;
        String strMatch2;
        GrantEntry grantEntry = new GrantEntry();
        LinkedList<PrincipalEntry> linkedList = null;
        boolean z2 = false;
        match("grant");
        while (!peek(VectorFormat.DEFAULT_PREFIX)) {
            if (peekAndMatch("Codebase")) {
                if (grantEntry.codeBase != null) {
                    throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("multiple.Codebase.expressions"));
                }
                grantEntry.codeBase = match("quoted string");
                peekAndMatch(",");
            } else if (peekAndMatch("SignedBy")) {
                if (grantEntry.signedBy != null) {
                    throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("multiple.SignedBy.expressions"));
                }
                grantEntry.signedBy = match("quoted string");
                StringTokenizer stringTokenizer = new StringTokenizer(grantEntry.signedBy, ",", true);
                int i2 = 0;
                int i3 = 0;
                while (stringTokenizer.hasMoreTokens()) {
                    String strTrim = stringTokenizer.nextToken().trim();
                    if (strTrim.equals(",")) {
                        i3++;
                    } else if (strTrim.length() > 0) {
                        i2++;
                    }
                }
                if (i2 <= i3) {
                    throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("SignedBy.has.empty.alias"));
                }
                peekAndMatch(",");
            } else if (peekAndMatch("Principal")) {
                if (linkedList == null) {
                    linkedList = new LinkedList<>();
                }
                if (peek(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                    strMatch = PrincipalEntry.REPLACE_NAME;
                    strMatch2 = match("principal type");
                } else {
                    if (peek("*")) {
                        match("*");
                        strMatch = PrincipalEntry.WILDCARD_CLASS;
                    } else {
                        strMatch = match("principal type");
                    }
                    if (peek("*")) {
                        match("*");
                        strMatch2 = PrincipalEntry.WILDCARD_NAME;
                    } else {
                        strMatch2 = match("quoted string");
                    }
                    if (strMatch.equals(PrincipalEntry.WILDCARD_CLASS) && !strMatch2.equals(PrincipalEntry.WILDCARD_NAME)) {
                        if (debug != null) {
                            debug.println("disallowing principal that has WILDCARD class but no WILDCARD name");
                        }
                        throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("can.not.specify.Principal.with.a.wildcard.class.without.a.wildcard.name"));
                    }
                }
                try {
                    strMatch2 = expand(strMatch2);
                    if (strMatch.equals(ToolDialog.X500_PRIN_CLASS) && !strMatch2.equals(PrincipalEntry.WILDCARD_NAME)) {
                        strMatch2 = new X500Principal(new X500Principal(strMatch2).toString()).getName();
                    }
                    linkedList.add(new PrincipalEntry(strMatch, strMatch2));
                } catch (PropertyExpander.ExpandException e2) {
                    if (debug != null) {
                        debug.println("principal name expansion failed: " + strMatch2);
                    }
                    z2 = true;
                }
                peekAndMatch(",");
            } else {
                throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("expected.codeBase.or.SignedBy.or.Principal"));
            }
        }
        if (linkedList != null) {
            grantEntry.principals = linkedList;
        }
        match(VectorFormat.DEFAULT_PREFIX);
        while (!peek("}")) {
            if (peek("Permission")) {
                try {
                    grantEntry.add(parsePermissionEntry());
                } catch (PropertyExpander.ExpandException e3) {
                    if (debug != null) {
                        debug.println(e3.toString());
                    }
                    skipEntry();
                }
                match(";");
            } else {
                throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("expected.permission.entry"));
            }
        }
        match("}");
        try {
            if (grantEntry.signedBy != null) {
                grantEntry.signedBy = expand(grantEntry.signedBy);
            }
            if (grantEntry.codeBase != null) {
                if (grantEntry.codeBase.equals(OLD_EXTDIRS_EXPANSION)) {
                    grantEntry.codeBase = EXTDIRS_EXPANSION;
                }
                int iIndexOf = grantEntry.codeBase.indexOf(EXTDIRS_EXPANSION);
                if (iIndexOf < 0) {
                    grantEntry.codeBase = expand(grantEntry.codeBase, true).replace(File.separatorChar, '/');
                } else {
                    String[] extDirs = parseExtDirs(grantEntry.codeBase, iIndexOf);
                    if (extDirs != null && extDirs.length > 0) {
                        for (int i4 = 0; i4 < extDirs.length; i4++) {
                            GrantEntry grantEntry2 = (GrantEntry) grantEntry.clone();
                            grantEntry2.codeBase = extDirs[i4];
                            add(grantEntry2);
                            if (debug != null) {
                                debug.println("creating policy entry for expanded java.ext.dirs path:\n\t\t" + extDirs[i4]);
                            }
                        }
                    }
                    z2 = true;
                }
            }
            if (z2) {
                return null;
            }
            return grantEntry;
        } catch (PropertyExpander.ExpandException e4) {
            if (debug != null) {
                debug.println(e4.toString());
                return null;
            }
            return null;
        }
    }

    private PermissionEntry parsePermissionEntry() throws ParsingException, IOException, PropertyExpander.ExpandException {
        PermissionEntry permissionEntry = new PermissionEntry();
        match("Permission");
        permissionEntry.permission = match("permission type");
        if (peek(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            permissionEntry.name = expand(match("quoted string"));
        }
        if (!peek(",")) {
            return permissionEntry;
        }
        match(",");
        if (peek(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            permissionEntry.action = expand(match("quoted string"));
            if (!peek(",")) {
                return permissionEntry;
            }
            match(",");
        }
        if (peekAndMatch("SignedBy")) {
            permissionEntry.signedBy = expand(match("quoted string"));
        }
        return permissionEntry;
    }

    private DomainEntry parseDomainEntry() throws ParsingException, IOException {
        Map<String, String> map = new HashMap();
        match("domain");
        String strMatch = match("domain name");
        while (!peek(VectorFormat.DEFAULT_PREFIX)) {
            map = parseProperties(VectorFormat.DEFAULT_PREFIX);
        }
        match(VectorFormat.DEFAULT_PREFIX);
        DomainEntry domainEntry = new DomainEntry(strMatch, map);
        while (!peek("}")) {
            match("keystore");
            String strMatch2 = match("keystore name");
            if (!peek("}")) {
                map = parseProperties(";");
            }
            match(";");
            domainEntry.add(new KeyStoreEntry(strMatch2, map));
        }
        match("}");
        if (0 == 1) {
            return null;
        }
        return domainEntry;
    }

    private Map<String, String> parseProperties(String str) throws ParsingException, IOException {
        HashMap map = new HashMap();
        while (!peek(str)) {
            String strMatch = match("property name");
            match("=");
            try {
                map.put(strMatch.toLowerCase(Locale.ENGLISH), expand(match("quoted string")));
            } catch (PropertyExpander.ExpandException e2) {
                throw new IOException(e2.getLocalizedMessage());
            }
        }
        return map;
    }

    static String[] parseExtDirs(String str, int i2) {
        String str2;
        String property = System.getProperty(EXTDIRS_PROPERTY);
        String strSubstring = i2 > 0 ? str.substring(0, i2) : "file:";
        int length = i2 + EXTDIRS_EXPANSION.length();
        String strSubstring2 = length < str.length() ? str.substring(length) : (String) null;
        String[] strArr = null;
        if (property != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(property, File.pathSeparator);
            int iCountTokens = stringTokenizer.countTokens();
            strArr = new String[iCountTokens];
            for (int i3 = 0; i3 < iCountTokens; i3++) {
                strArr[i3] = ParseUtil.encodePath(new File(stringTokenizer.nextToken()).getAbsolutePath());
                if (!strArr[i3].startsWith("/")) {
                    strArr[i3] = "/" + strArr[i3];
                }
                if (strSubstring2 == null) {
                    str2 = strArr[i3].endsWith("/") ? "*" : "/*";
                } else {
                    str2 = strSubstring2;
                }
                strArr[i3] = strSubstring + strArr[i3] + str2;
            }
        }
        return strArr;
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
            case 34:
                if (str.equalsIgnoreCase(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                    z2 = true;
                    break;
                }
                break;
            case 42:
                if (str.equalsIgnoreCase("*")) {
                    z2 = true;
                    break;
                }
                break;
            case 44:
                if (str.equalsIgnoreCase(",")) {
                    z2 = true;
                    break;
                }
                break;
            case 59:
                if (str.equalsIgnoreCase(";")) {
                    z2 = true;
                    break;
                }
                break;
            case 123:
                if (str.equalsIgnoreCase(VectorFormat.DEFAULT_PREFIX)) {
                    z2 = true;
                    break;
                }
                break;
            case 125:
                if (str.equalsIgnoreCase("}")) {
                    z2 = true;
                    break;
                }
                break;
        }
        return z2;
    }

    private String match(String str) throws ParsingException, IOException {
        String str2 = null;
        switch (this.lookahead) {
            case -3:
                if (str.equalsIgnoreCase(this.st.sval)) {
                    this.lookahead = this.st.nextToken();
                    break;
                } else if (str.equalsIgnoreCase("permission type") || str.equalsIgnoreCase("principal type") || str.equalsIgnoreCase("domain name") || str.equalsIgnoreCase("keystore name") || str.equalsIgnoreCase("property name")) {
                    str2 = this.st.sval;
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, this.st.sval);
                }
            case -2:
                throw new ParsingException(this.st.lineno(), str, ResourcesMgr.getString("number.") + String.valueOf(this.st.nval));
            case -1:
                throw new ParsingException(new MessageFormat(ResourcesMgr.getString("expected.expect.read.end.of.file.")).format(new Object[]{str}));
            case 34:
                if (str.equalsIgnoreCase("quoted string") || str.equalsIgnoreCase("permission type") || str.equalsIgnoreCase("principal type")) {
                    str2 = this.st.sval;
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, this.st.sval);
                }
            case 42:
                if (str.equalsIgnoreCase("*")) {
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, "*");
                }
            case 44:
                if (str.equalsIgnoreCase(",")) {
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, ",");
                }
            case 59:
                if (str.equalsIgnoreCase(";")) {
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, ";");
                }
            case 61:
                if (str.equalsIgnoreCase("=")) {
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, "=");
                }
            case 123:
                if (str.equalsIgnoreCase(VectorFormat.DEFAULT_PREFIX)) {
                    this.lookahead = this.st.nextToken();
                    break;
                } else {
                    throw new ParsingException(this.st.lineno(), str, VectorFormat.DEFAULT_PREFIX);
                }
            case 125:
                if (str.equalsIgnoreCase("}")) {
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

    private void skipEntry() throws ParsingException, IOException {
        while (this.lookahead != 59) {
            switch (this.lookahead) {
                case -2:
                    throw new ParsingException(this.st.lineno(), ";", ResourcesMgr.getString("number.") + String.valueOf(this.st.nval));
                case -1:
                    throw new ParsingException(ResourcesMgr.getString("expected.read.end.of.file."));
                default:
                    this.lookahead = this.st.nextToken();
            }
        }
    }

    /* loaded from: rt.jar:sun/security/provider/PolicyParser$GrantEntry.class */
    public static class GrantEntry {
        public String signedBy;
        public String codeBase;
        public LinkedList<PrincipalEntry> principals = new LinkedList<>();
        public Vector<PermissionEntry> permissionEntries = new Vector<>();

        public GrantEntry() {
        }

        public GrantEntry(String str, String str2) {
            this.codeBase = str2;
            this.signedBy = str;
        }

        public void add(PermissionEntry permissionEntry) {
            this.permissionEntries.addElement(permissionEntry);
        }

        public boolean remove(PrincipalEntry principalEntry) {
            return this.principals.remove(principalEntry);
        }

        public boolean remove(PermissionEntry permissionEntry) {
            return this.permissionEntries.removeElement(permissionEntry);
        }

        public boolean contains(PrincipalEntry principalEntry) {
            return this.principals.contains(principalEntry);
        }

        public boolean contains(PermissionEntry permissionEntry) {
            return this.permissionEntries.contains(permissionEntry);
        }

        public Enumeration<PermissionEntry> permissionElements() {
            return this.permissionEntries.elements();
        }

        public void write(PrintWriter printWriter) {
            printWriter.print("grant");
            if (this.signedBy != null) {
                printWriter.print(" signedBy \"");
                printWriter.print(this.signedBy);
                printWriter.print('\"');
                if (this.codeBase != null) {
                    printWriter.print(", ");
                }
            }
            if (this.codeBase != null) {
                printWriter.print(" codeBase \"");
                printWriter.print(this.codeBase);
                printWriter.print('\"');
                if (this.principals != null && this.principals.size() > 0) {
                    printWriter.print(",\n");
                }
            }
            if (this.principals != null && this.principals.size() > 0) {
                Iterator<PrincipalEntry> it = this.principals.iterator();
                while (it.hasNext()) {
                    printWriter.print(GoToActionDialog.EMPTY_DESTINATION);
                    it.next().write(printWriter);
                    if (it.hasNext()) {
                        printWriter.print(",\n");
                    }
                }
            }
            printWriter.println(" {");
            Enumeration<PermissionEntry> enumerationElements = this.permissionEntries.elements();
            while (enumerationElements.hasMoreElements()) {
                PermissionEntry permissionEntryNextElement2 = enumerationElements.nextElement2();
                printWriter.write(Constants.INDENT);
                permissionEntryNextElement2.write(printWriter);
            }
            printWriter.println("};");
        }

        public Object clone() {
            GrantEntry grantEntry = new GrantEntry();
            grantEntry.codeBase = this.codeBase;
            grantEntry.signedBy = this.signedBy;
            grantEntry.principals = new LinkedList<>(this.principals);
            grantEntry.permissionEntries = new Vector<>(this.permissionEntries);
            return grantEntry;
        }
    }

    /* loaded from: rt.jar:sun/security/provider/PolicyParser$PrincipalEntry.class */
    public static class PrincipalEntry implements Principal {
        public static final String WILDCARD_CLASS = "WILDCARD_PRINCIPAL_CLASS";
        public static final String WILDCARD_NAME = "WILDCARD_PRINCIPAL_NAME";
        public static final String REPLACE_NAME = "PolicyParser.REPLACE_NAME";
        String principalClass;
        String principalName;

        public PrincipalEntry(String str, String str2) {
            if (str == null || str2 == null) {
                throw new NullPointerException(ResourcesMgr.getString("null.principalClass.or.principalName"));
            }
            this.principalClass = str;
            this.principalName = str2;
        }

        boolean isWildcardName() {
            return this.principalName.equals(WILDCARD_NAME);
        }

        boolean isWildcardClass() {
            return this.principalClass.equals(WILDCARD_CLASS);
        }

        boolean isReplaceName() {
            return this.principalClass.equals(REPLACE_NAME);
        }

        public String getPrincipalClass() {
            return this.principalClass;
        }

        public String getPrincipalName() {
            return this.principalName;
        }

        public String getDisplayClass() {
            if (isWildcardClass()) {
                return "*";
            }
            if (isReplaceName()) {
                return "";
            }
            return this.principalClass;
        }

        public String getDisplayName() {
            return getDisplayName(false);
        }

        public String getDisplayName(boolean z2) {
            if (isWildcardName()) {
                return "*";
            }
            return z2 ? PdfOps.DOUBLE_QUOTE__TOKEN + this.principalName + PdfOps.DOUBLE_QUOTE__TOKEN : this.principalName;
        }

        @Override // java.security.Principal
        public String getName() {
            return this.principalName;
        }

        @Override // java.security.Principal
        public String toString() {
            if (!isReplaceName()) {
                return getDisplayClass() + "/" + getDisplayName();
            }
            return getDisplayName();
        }

        @Override // java.security.Principal
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PrincipalEntry)) {
                return false;
            }
            PrincipalEntry principalEntry = (PrincipalEntry) obj;
            return this.principalClass.equals(principalEntry.principalClass) && this.principalName.equals(principalEntry.principalName);
        }

        @Override // java.security.Principal
        public int hashCode() {
            return this.principalClass.hashCode();
        }

        public void write(PrintWriter printWriter) {
            printWriter.print("principal " + getDisplayClass() + " " + getDisplayName(true));
        }
    }

    /* loaded from: rt.jar:sun/security/provider/PolicyParser$PermissionEntry.class */
    public static class PermissionEntry {
        public String permission;
        public String name;
        public String action;
        public String signedBy;

        public PermissionEntry() {
        }

        public PermissionEntry(String str, String str2, String str3) {
            this.permission = str;
            this.name = str2;
            this.action = str3;
        }

        public int hashCode() {
            int iHashCode = this.permission.hashCode();
            if (this.name != null) {
                iHashCode ^= this.name.hashCode();
            }
            if (this.action != null) {
                iHashCode ^= this.action.hashCode();
            }
            return iHashCode;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof PermissionEntry)) {
                return false;
            }
            PermissionEntry permissionEntry = (PermissionEntry) obj;
            if (this.permission == null) {
                if (permissionEntry.permission != null) {
                    return false;
                }
            } else if (!this.permission.equals(permissionEntry.permission)) {
                return false;
            }
            if (this.name == null) {
                if (permissionEntry.name != null) {
                    return false;
                }
            } else if (!this.name.equals(permissionEntry.name)) {
                return false;
            }
            if (this.action == null) {
                if (permissionEntry.action != null) {
                    return false;
                }
            } else if (!this.action.equals(permissionEntry.action)) {
                return false;
            }
            return this.signedBy == null ? permissionEntry.signedBy == null : this.signedBy.equals(permissionEntry.signedBy);
        }

        public void write(PrintWriter printWriter) {
            printWriter.print("permission ");
            printWriter.print(this.permission);
            if (this.name != null) {
                printWriter.print(" \"");
                printWriter.print(this.name.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\""));
                printWriter.print('\"');
            }
            if (this.action != null) {
                printWriter.print(", \"");
                printWriter.print(this.action);
                printWriter.print('\"');
            }
            if (this.signedBy != null) {
                printWriter.print(", signedBy \"");
                printWriter.print(this.signedBy);
                printWriter.print('\"');
            }
            printWriter.println(";");
        }
    }

    /* loaded from: rt.jar:sun/security/provider/PolicyParser$DomainEntry.class */
    static class DomainEntry {
        private final String name;
        private final Map<String, String> properties;
        private final Map<String, KeyStoreEntry> entries = new HashMap();

        DomainEntry(String str, Map<String, String> map) {
            this.name = str;
            this.properties = map;
        }

        String getName() {
            return this.name;
        }

        Map<String, String> getProperties() {
            return this.properties;
        }

        Collection<KeyStoreEntry> getEntries() {
            return this.entries.values();
        }

        void add(KeyStoreEntry keyStoreEntry) throws ParsingException {
            String name = keyStoreEntry.getName();
            if (!this.entries.containsKey(name)) {
                this.entries.put(name, keyStoreEntry);
                return;
            }
            throw new ParsingException(new MessageFormat(ResourcesMgr.getString("duplicate.keystore.name")).format(new Object[]{name}));
        }

        public String toString() {
            StringBuilder sbAppend = new StringBuilder("\ndomain ").append(this.name);
            if (this.properties != null) {
                for (Map.Entry<String, String> entry : this.properties.entrySet()) {
                    sbAppend.append("\n        ").append(entry.getKey()).append('=').append(entry.getValue());
                }
            }
            sbAppend.append(" {\n");
            if (this.entries != null) {
                Iterator<KeyStoreEntry> it = this.entries.values().iterator();
                while (it.hasNext()) {
                    sbAppend.append((Object) it.next()).append("\n");
                }
            }
            sbAppend.append("}");
            return sbAppend.toString();
        }
    }

    /* loaded from: rt.jar:sun/security/provider/PolicyParser$KeyStoreEntry.class */
    static class KeyStoreEntry {
        private final String name;
        private final Map<String, String> properties;

        KeyStoreEntry(String str, Map<String, String> map) {
            this.name = str;
            this.properties = map;
        }

        String getName() {
            return this.name;
        }

        Map<String, String> getProperties() {
            return this.properties;
        }

        public String toString() {
            StringBuilder sbAppend = new StringBuilder("\n    keystore ").append(this.name);
            if (this.properties != null) {
                for (Map.Entry<String, String> entry : this.properties.entrySet()) {
                    sbAppend.append("\n        ").append(entry.getKey()).append('=').append(entry.getValue());
                }
            }
            sbAppend.append(";");
            return sbAppend.toString();
        }
    }

    /* loaded from: rt.jar:sun/security/provider/PolicyParser$ParsingException.class */
    public static class ParsingException extends GeneralSecurityException {
        private static final long serialVersionUID = -4330692689482574072L;
        private String i18nMessage;

        public ParsingException(String str) {
            super(str);
            this.i18nMessage = str;
        }

        public ParsingException(int i2, String str) {
            super("line " + i2 + ": " + str);
            this.i18nMessage = new MessageFormat(ResourcesMgr.getString("line.number.msg")).format(new Object[]{new Integer(i2), str});
        }

        public ParsingException(int i2, String str, String str2) {
            super("line " + i2 + ": expected [" + str + "], found [" + str2 + "]");
            this.i18nMessage = new MessageFormat(ResourcesMgr.getString("line.number.expected.expect.found.actual.")).format(new Object[]{new Integer(i2), str, str2});
        }

        @Override // java.lang.Throwable
        public String getLocalizedMessage() {
            return this.i18nMessage;
        }
    }

    public static void main(String[] strArr) throws Exception {
        FileReader fileReader = new FileReader(strArr[0]);
        Throwable th = null;
        try {
            FileWriter fileWriter = new FileWriter(strArr[1]);
            Throwable th2 = null;
            try {
                try {
                    PolicyParser policyParser = new PolicyParser(true);
                    policyParser.read(fileReader);
                    policyParser.write(fileWriter);
                    if (fileWriter != null) {
                        if (0 != 0) {
                            try {
                                fileWriter.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            fileWriter.close();
                        }
                    }
                    if (fileReader != null) {
                        if (0 == 0) {
                            fileReader.close();
                            return;
                        }
                        try {
                            fileReader.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    }
                } catch (Throwable th5) {
                    if (fileWriter != null) {
                        if (th2 != null) {
                            try {
                                fileWriter.close();
                            } catch (Throwable th6) {
                                th2.addSuppressed(th6);
                            }
                        } else {
                            fileWriter.close();
                        }
                    }
                    throw th5;
                }
            } catch (Throwable th7) {
                th2 = th7;
                throw th7;
            }
        } catch (Throwable th8) {
            if (fileReader != null) {
                if (0 != 0) {
                    try {
                        fileReader.close();
                    } catch (Throwable th9) {
                        th.addSuppressed(th9);
                    }
                } else {
                    fileReader.close();
                }
            }
            throw th8;
        }
    }
}
