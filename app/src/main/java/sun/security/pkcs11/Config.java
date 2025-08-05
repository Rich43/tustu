package sun.security.pkcs11;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.ProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.fxml.FXMLLoader;
import org.apache.commons.math3.geometry.VectorFormat;
import sun.security.action.GetPropertyAction;
import sun.security.pkcs11.Secmod;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.Functions;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.util.PropertyExpander;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/Config.class */
final class Config {
    static final int ERR_HALT = 1;
    static final int ERR_IGNORE_ALL = 2;
    static final int ERR_IGNORE_LIB = 3;
    private static final boolean staticAllowSingleThreadedModules;
    private static final Map<String, Config> configMap;
    private static final boolean DEBUG = false;
    private Reader reader;
    private StreamTokenizer st;
    private Set<String> parsedKeywords;
    private String name;
    private String library;
    private String description;
    private Set<Long> enabledMechanisms;
    private Set<Long> disabledMechanisms;
    private TemplateManager templateManager;
    private boolean nssUseSecmod;
    private String nssLibraryDirectory;
    private String nssSecmodDirectory;
    private String nssModule;
    private String nssArgs;
    private static final CK_ATTRIBUTE[] CK_A0;
    private int slotID = -1;
    private int slotListIndex = -1;
    private boolean showInfo = false;
    private int handleStartupErrors = 1;
    private boolean keyStoreCompatibilityMode = true;
    private boolean explicitCancel = true;
    private int insertionCheckInterval = 2000;
    private boolean omitInitialize = false;
    private boolean allowSingleThreadedModules = true;
    private String functionList = "C_GetFunctionList";
    private Secmod.DbMode nssDbMode = Secmod.DbMode.READ_WRITE;
    private boolean nssNetscapeDbWorkaround = true;
    private boolean nssUseSecmodTrust = false;
    private boolean useEcX963Encoding = false;
    private boolean nssOptimizeSpace = false;

    static {
        if ("false".equalsIgnoreCase((String) AccessController.doPrivileged(new GetPropertyAction("sun.security.pkcs11.allowSingleThreadedModules")))) {
            staticAllowSingleThreadedModules = false;
        } else {
            staticAllowSingleThreadedModules = true;
        }
        configMap = new HashMap();
        CK_A0 = new CK_ATTRIBUTE[0];
    }

    static Config getConfig(String str, InputStream inputStream) {
        Config config = configMap.get(str);
        if (config != null) {
            return config;
        }
        try {
            Config config2 = new Config(str, inputStream);
            configMap.put(str, config2);
            return config2;
        } catch (Exception e2) {
            throw new ProviderException("Error parsing configuration", e2);
        }
    }

    static Config removeConfig(String str) {
        return configMap.remove(str);
    }

    private static void debug(Object obj) {
    }

    private Config(String str, InputStream inputStream) throws IOException {
        if (inputStream == null) {
            if (str.startsWith("--")) {
                this.reader = new StringReader(str.substring(2).replace("\\n", "\n"));
            } else {
                inputStream = new FileInputStream(expand(str));
            }
        }
        if (this.reader == null) {
            this.reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));
        }
        this.parsedKeywords = new HashSet();
        this.st = new StreamTokenizer(this.reader);
        setupTokenizer();
        parse();
    }

    String getName() {
        return this.name;
    }

    String getLibrary() {
        return this.library;
    }

    String getDescription() {
        if (this.description != null) {
            return this.description;
        }
        return "SunPKCS11-" + this.name + " using library " + this.library;
    }

    int getSlotID() {
        return this.slotID;
    }

    int getSlotListIndex() {
        if (this.slotID == -1 && this.slotListIndex == -1) {
            return 0;
        }
        return this.slotListIndex;
    }

    boolean getShowInfo() {
        return SunPKCS11.debug != null || this.showInfo;
    }

    TemplateManager getTemplateManager() {
        if (this.templateManager == null) {
            this.templateManager = new TemplateManager();
        }
        return this.templateManager;
    }

    boolean isEnabled(long j2) {
        if (this.enabledMechanisms != null) {
            return this.enabledMechanisms.contains(Long.valueOf(j2));
        }
        return this.disabledMechanisms == null || !this.disabledMechanisms.contains(Long.valueOf(j2));
    }

    int getHandleStartupErrors() {
        return this.handleStartupErrors;
    }

    boolean getKeyStoreCompatibilityMode() {
        return this.keyStoreCompatibilityMode;
    }

    boolean getExplicitCancel() {
        return this.explicitCancel;
    }

    int getInsertionCheckInterval() {
        return this.insertionCheckInterval;
    }

    boolean getOmitInitialize() {
        return this.omitInitialize;
    }

    boolean getAllowSingleThreadedModules() {
        return staticAllowSingleThreadedModules && this.allowSingleThreadedModules;
    }

    String getFunctionList() {
        return this.functionList;
    }

    boolean getNssUseSecmod() {
        return this.nssUseSecmod;
    }

    String getNssLibraryDirectory() {
        return this.nssLibraryDirectory;
    }

    String getNssSecmodDirectory() {
        return this.nssSecmodDirectory;
    }

    String getNssModule() {
        return this.nssModule;
    }

    Secmod.DbMode getNssDbMode() {
        return this.nssDbMode;
    }

    public boolean getNssNetscapeDbWorkaround() {
        return this.nssUseSecmod && this.nssNetscapeDbWorkaround;
    }

    String getNssArgs() {
        return this.nssArgs;
    }

    boolean getNssUseSecmodTrust() {
        return this.nssUseSecmodTrust;
    }

    boolean getUseEcX963Encoding() {
        return this.useEcX963Encoding;
    }

    boolean getNssOptimizeSpace() {
        return this.nssOptimizeSpace;
    }

    private static String expand(String str) throws IOException {
        try {
            return PropertyExpander.expand(str);
        } catch (Exception e2) {
            throw new RuntimeException(e2.getMessage());
        }
    }

    private void setupTokenizer() {
        this.st.resetSyntax();
        this.st.wordChars(97, 122);
        this.st.wordChars(65, 90);
        this.st.wordChars(48, 57);
        this.st.wordChars(58, 58);
        this.st.wordChars(46, 46);
        this.st.wordChars(95, 95);
        this.st.wordChars(45, 45);
        this.st.wordChars(47, 47);
        this.st.wordChars(92, 92);
        this.st.wordChars(36, 36);
        this.st.wordChars(123, 123);
        this.st.wordChars(125, 125);
        this.st.wordChars(42, 42);
        this.st.wordChars(43, 43);
        this.st.wordChars(126, 126);
        this.st.whitespaceChars(0, 32);
        this.st.commentChar(35);
        this.st.eolIsSignificant(true);
        this.st.quoteChar(34);
    }

    private ConfigurationException excToken(String str) {
        return new ConfigurationException(str + " " + ((Object) this.st));
    }

    private ConfigurationException excLine(String str) {
        return new ConfigurationException(str + ", line " + this.st.lineno());
    }

    private void parse() throws IOException {
        while (true) {
            int iNextToken = nextToken();
            if (iNextToken != -1) {
                if (iNextToken != 10) {
                    if (iNextToken != -3) {
                        throw excToken("Unexpected token:");
                    }
                    String str = this.st.sval;
                    if (str.equals("name")) {
                        this.name = parseStringEntry(str);
                    } else if (str.equals("library")) {
                        this.library = parseLibrary(str);
                    } else if (str.equals("description")) {
                        parseDescription(str);
                    } else if (str.equals("slot")) {
                        parseSlotID(str);
                    } else if (str.equals("slotListIndex")) {
                        parseSlotListIndex(str);
                    } else if (str.equals("enabledMechanisms")) {
                        parseEnabledMechanisms(str);
                    } else if (str.equals("disabledMechanisms")) {
                        parseDisabledMechanisms(str);
                    } else if (str.equals("attributes")) {
                        parseAttributes(str);
                    } else if (str.equals("handleStartupErrors")) {
                        parseHandleStartupErrors(str);
                    } else if (str.endsWith("insertionCheckInterval")) {
                        this.insertionCheckInterval = parseIntegerEntry(str);
                        if (this.insertionCheckInterval < 100) {
                            throw excLine(str + " must be at least 100 ms");
                        }
                    } else if (str.equals("showInfo")) {
                        this.showInfo = parseBooleanEntry(str);
                    } else if (str.equals("keyStoreCompatibilityMode")) {
                        this.keyStoreCompatibilityMode = parseBooleanEntry(str);
                    } else if (str.equals("explicitCancel")) {
                        this.explicitCancel = parseBooleanEntry(str);
                    } else if (str.equals("omitInitialize")) {
                        this.omitInitialize = parseBooleanEntry(str);
                    } else if (str.equals("allowSingleThreadedModules")) {
                        this.allowSingleThreadedModules = parseBooleanEntry(str);
                    } else if (str.equals("functionList")) {
                        this.functionList = parseStringEntry(str);
                    } else if (str.equals("nssUseSecmod")) {
                        this.nssUseSecmod = parseBooleanEntry(str);
                    } else if (str.equals("nssLibraryDirectory")) {
                        this.nssLibraryDirectory = parseLibrary(str);
                        this.nssUseSecmod = true;
                    } else if (str.equals("nssSecmodDirectory")) {
                        this.nssSecmodDirectory = expand(parseStringEntry(str));
                        this.nssUseSecmod = true;
                    } else if (str.equals("nssModule")) {
                        this.nssModule = parseStringEntry(str);
                        this.nssUseSecmod = true;
                    } else if (str.equals("nssDbMode")) {
                        String stringEntry = parseStringEntry(str);
                        if (stringEntry.equals("readWrite")) {
                            this.nssDbMode = Secmod.DbMode.READ_WRITE;
                        } else if (stringEntry.equals("readOnly")) {
                            this.nssDbMode = Secmod.DbMode.READ_ONLY;
                        } else if (stringEntry.equals("noDb")) {
                            this.nssDbMode = Secmod.DbMode.NO_DB;
                        } else {
                            throw excToken("nssDbMode must be one of readWrite, readOnly, and noDb:");
                        }
                        this.nssUseSecmod = true;
                    } else if (str.equals("nssNetscapeDbWorkaround")) {
                        this.nssNetscapeDbWorkaround = parseBooleanEntry(str);
                        this.nssUseSecmod = true;
                    } else if (str.equals("nssArgs")) {
                        parseNSSArgs(str);
                    } else if (str.equals("nssUseSecmodTrust")) {
                        this.nssUseSecmodTrust = parseBooleanEntry(str);
                    } else if (str.equals("useEcX963Encoding")) {
                        this.useEcX963Encoding = parseBooleanEntry(str);
                    } else if (str.equals("nssOptimizeSpace")) {
                        this.nssOptimizeSpace = parseBooleanEntry(str);
                    } else {
                        throw new ConfigurationException("Unknown keyword '" + str + "', line " + this.st.lineno());
                    }
                    this.parsedKeywords.add(str);
                }
            } else {
                this.reader.close();
                this.reader = null;
                this.st = null;
                this.parsedKeywords = null;
                if (this.name == null) {
                    throw new ConfigurationException("name must be specified");
                }
                if (!this.nssUseSecmod) {
                    if (this.library == null) {
                        throw new ConfigurationException("library must be specified");
                    }
                    return;
                } else {
                    if (this.library != null) {
                        throw new ConfigurationException("library must not be specified in NSS mode");
                    }
                    if (this.slotID != -1 || this.slotListIndex != -1) {
                        throw new ConfigurationException("slot and slotListIndex must not be specified in NSS mode");
                    }
                    if (this.nssArgs != null) {
                        throw new ConfigurationException("nssArgs must not be specified in NSS mode");
                    }
                    if (this.nssUseSecmodTrust) {
                        throw new ConfigurationException("nssUseSecmodTrust is an internal option and must not be specified in NSS mode");
                    }
                    return;
                }
            }
        }
    }

    private int nextToken() throws IOException {
        int iNextToken = this.st.nextToken();
        debug(this.st);
        return iNextToken;
    }

    private void parseEquals() throws IOException {
        if (nextToken() != 61) {
            throw excToken("Expected '=', read");
        }
    }

    private void parseOpenBraces() throws IOException {
        int iNextToken;
        do {
            iNextToken = nextToken();
        } while (iNextToken == 10);
        if (iNextToken == -3 && this.st.sval.equals(VectorFormat.DEFAULT_PREFIX)) {
        } else {
            throw excToken("Expected '{', read");
        }
    }

    private boolean isCloseBraces(int i2) {
        return i2 == -3 && this.st.sval.equals("}");
    }

    private String parseWord() throws IOException {
        if (nextToken() != -3) {
            throw excToken("Unexpected value:");
        }
        return this.st.sval;
    }

    private String parseStringEntry(String str) throws IOException {
        checkDup(str);
        parseEquals();
        int iNextToken = nextToken();
        if (iNextToken != -3 && iNextToken != 34) {
            throw excToken("Unexpected value:");
        }
        String str2 = this.st.sval;
        debug(str + ": " + str2);
        return str2;
    }

    private boolean parseBooleanEntry(String str) throws IOException {
        checkDup(str);
        parseEquals();
        boolean z2 = parseBoolean();
        debug(str + ": " + z2);
        return z2;
    }

    private int parseIntegerEntry(String str) throws IOException {
        checkDup(str);
        parseEquals();
        int iDecodeNumber = decodeNumber(parseWord());
        debug(str + ": " + iDecodeNumber);
        return iDecodeNumber;
    }

    private boolean parseBoolean() throws IOException {
        switch (parseWord()) {
            case "true":
                return true;
            case "false":
                return false;
            default:
                throw excToken("Expected boolean value, read:");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0059, code lost:
    
        if (r4 != null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0062, code lost:
    
        throw excToken("Unexpected empty line");
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0064, code lost:
    
        return r4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String parseLine() throws java.io.IOException {
        /*
            r3 = this;
            r0 = 0
            r4 = r0
        L2:
            r0 = r3
            int r0 = r0.nextToken()
            r5 = r0
            r0 = r5
            r1 = 10
            if (r0 == r1) goto L58
            r0 = r5
            r1 = -1
            if (r0 != r1) goto L15
            goto L58
        L15:
            r0 = r5
            r1 = -3
            if (r0 == r1) goto L28
            r0 = r5
            r1 = 34
            if (r0 == r1) goto L28
            r0 = r3
            java.lang.String r1 = "Unexpected value"
            sun.security.pkcs11.ConfigurationException r0 = r0.excToken(r1)
            throw r0
        L28:
            r0 = r4
            if (r0 != 0) goto L37
            r0 = r3
            java.io.StreamTokenizer r0 = r0.st
            java.lang.String r0 = r0.sval
            r4 = r0
            goto L55
        L37:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r1 = r4
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = " "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r3
            java.io.StreamTokenizer r1 = r1.st
            java.lang.String r1 = r1.sval
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r4 = r0
        L55:
            goto L2
        L58:
            r0 = r4
            if (r0 != 0) goto L63
            r0 = r3
            java.lang.String r1 = "Unexpected empty line"
            sun.security.pkcs11.ConfigurationException r0 = r0.excToken(r1)
            throw r0
        L63:
            r0 = r4
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.pkcs11.Config.parseLine():java.lang.String");
    }

    private int decodeNumber(String str) throws IOException {
        try {
            if (str.startsWith("0x") || str.startsWith("0X")) {
                return Integer.parseInt(str.substring(2), 16);
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException e2) {
            throw excToken("Expected number, read");
        }
    }

    private static boolean isNumber(String str) {
        char cCharAt;
        return str.length() != 0 && (cCharAt = str.charAt(0)) >= '0' && cCharAt <= '9';
    }

    private void parseComma() throws IOException {
        if (nextToken() != 44) {
            throw excToken("Expected ',', read");
        }
    }

    private static boolean isByteArray(String str) {
        return str.startsWith("0h");
    }

    private byte[] decodeByteArray(String str) throws IOException {
        if (!str.startsWith("0h")) {
            throw excToken("Expected byte array value, read");
        }
        try {
            return new BigInteger(str.substring(2), 16).toByteArray();
        } catch (NumberFormatException e2) {
            throw excToken("Expected byte array value, read");
        }
    }

    private void checkDup(String str) throws IOException {
        if (this.parsedKeywords.contains(str)) {
            throw excLine(str + " must only be specified once");
        }
    }

    private String parseLibrary(String str) throws IOException {
        checkDup(str);
        parseEquals();
        String strExpand = expand(parseLine());
        int iIndexOf = strExpand.indexOf("/$ISA/");
        if (iIndexOf != -1) {
            String property = System.getProperty("os.name", "");
            String property2 = System.getProperty("os.arch", "");
            String strSubstring = strExpand.substring(0, iIndexOf);
            String strSubstring2 = strExpand.substring(iIndexOf + 5);
            if (property.equals("SunOS") && property2.equals("sparcv9")) {
                strExpand = strSubstring + "/sparcv9" + strSubstring2;
            } else if (property.equals("SunOS") && property2.equals("amd64")) {
                strExpand = strSubstring + "/amd64" + strSubstring2;
            } else {
                strExpand = strSubstring + strSubstring2;
            }
        }
        debug(str + ": " + strExpand);
        if (!new File(strExpand).isAbsolute()) {
            throw new ConfigurationException("Absolute path required for library value: " + strExpand);
        }
        return strExpand;
    }

    private void parseDescription(String str) throws IOException {
        checkDup(str);
        parseEquals();
        this.description = parseLine();
        debug("description: " + this.description);
    }

    private void parseSlotID(String str) throws IOException {
        if (this.slotID >= 0) {
            throw excLine("Duplicate slot definition");
        }
        if (this.slotListIndex >= 0) {
            throw excLine("Only one of slot and slotListIndex must be specified");
        }
        parseEquals();
        this.slotID = decodeNumber(parseWord());
        debug("slot: " + this.slotID);
    }

    private void parseSlotListIndex(String str) throws IOException {
        if (this.slotListIndex >= 0) {
            throw excLine("Duplicate slotListIndex definition");
        }
        if (this.slotID >= 0) {
            throw excLine("Only one of slot and slotListIndex must be specified");
        }
        parseEquals();
        this.slotListIndex = decodeNumber(parseWord());
        debug("slotListIndex: " + this.slotListIndex);
    }

    private void parseEnabledMechanisms(String str) throws IOException {
        this.enabledMechanisms = parseMechanisms(str);
    }

    private void parseDisabledMechanisms(String str) throws IOException {
        this.disabledMechanisms = parseMechanisms(str);
    }

    private Set<Long> parseMechanisms(String str) throws IOException {
        checkDup(str);
        HashSet hashSet = new HashSet();
        parseEquals();
        parseOpenBraces();
        while (true) {
            int iNextToken = nextToken();
            if (!isCloseBraces(iNextToken)) {
                if (iNextToken != 10) {
                    if (iNextToken != -3) {
                        throw excToken("Expected mechanism, read");
                    }
                    hashSet.add(Long.valueOf(parseMechanism(this.st.sval)));
                }
            } else {
                return hashSet;
            }
        }
    }

    private long parseMechanism(String str) throws IOException {
        if (isNumber(str)) {
            return decodeNumber(str);
        }
        try {
            return Functions.getMechanismId(str);
        } catch (IllegalArgumentException e2) {
            throw excLine("Unknown mechanism: " + str);
        }
    }

    private void parseAttributes(String str) throws IOException {
        if (this.templateManager == null) {
            this.templateManager = new TemplateManager();
        }
        int iNextToken = nextToken();
        if (iNextToken == 61) {
            String word = parseWord();
            if (!word.equals("compatibility")) {
                throw excLine("Expected 'compatibility', read " + word);
            }
            setCompatibilityAttributes();
            return;
        }
        if (iNextToken != 40) {
            throw excToken("Expected '(' or '=', read");
        }
        String operation = parseOperation();
        parseComma();
        long objectClass = parseObjectClass();
        parseComma();
        long keyAlgorithm = parseKeyAlgorithm();
        if (nextToken() != 41) {
            throw excToken("Expected ')', read");
        }
        parseEquals();
        parseOpenBraces();
        ArrayList arrayList = new ArrayList();
        while (true) {
            int iNextToken2 = nextToken();
            if (!isCloseBraces(iNextToken2)) {
                if (iNextToken2 != 10) {
                    if (iNextToken2 != -3) {
                        throw excToken("Expected mechanism, read");
                    }
                    long jDecodeAttributeName = decodeAttributeName(this.st.sval);
                    parseEquals();
                    arrayList.add(decodeAttributeValue(jDecodeAttributeName, parseWord()));
                }
            } else {
                this.templateManager.addTemplate(operation, objectClass, keyAlgorithm, (CK_ATTRIBUTE[]) arrayList.toArray(CK_A0));
                return;
            }
        }
    }

    private void setCompatibilityAttributes() {
        this.templateManager.addTemplate("*", 4L, PKCS11Constants.PCKK_ANY, new CK_ATTRIBUTE[]{CK_ATTRIBUTE.TOKEN_FALSE, CK_ATTRIBUTE.SENSITIVE_FALSE, CK_ATTRIBUTE.EXTRACTABLE_TRUE, CK_ATTRIBUTE.ENCRYPT_TRUE, CK_ATTRIBUTE.DECRYPT_TRUE, CK_ATTRIBUTE.WRAP_TRUE, CK_ATTRIBUTE.UNWRAP_TRUE});
        this.templateManager.addTemplate("*", 4L, 16L, new CK_ATTRIBUTE[]{CK_ATTRIBUTE.SIGN_TRUE, CK_ATTRIBUTE.VERIFY_TRUE, CK_ATTRIBUTE.ENCRYPT_NULL, CK_ATTRIBUTE.DECRYPT_NULL, CK_ATTRIBUTE.WRAP_NULL, CK_ATTRIBUTE.UNWRAP_NULL, CK_ATTRIBUTE.DERIVE_TRUE});
        this.templateManager.addTemplate("*", 3L, PKCS11Constants.PCKK_ANY, new CK_ATTRIBUTE[]{CK_ATTRIBUTE.TOKEN_FALSE, CK_ATTRIBUTE.SENSITIVE_FALSE, CK_ATTRIBUTE.EXTRACTABLE_TRUE});
        this.templateManager.addTemplate("*", 2L, PKCS11Constants.PCKK_ANY, new CK_ATTRIBUTE[]{CK_ATTRIBUTE.TOKEN_FALSE});
        this.templateManager.addTemplate("*", 3L, 0L, new CK_ATTRIBUTE[]{CK_ATTRIBUTE.DECRYPT_TRUE, CK_ATTRIBUTE.SIGN_TRUE, CK_ATTRIBUTE.SIGN_RECOVER_TRUE, CK_ATTRIBUTE.UNWRAP_TRUE});
        this.templateManager.addTemplate("*", 2L, 0L, new CK_ATTRIBUTE[]{CK_ATTRIBUTE.ENCRYPT_TRUE, CK_ATTRIBUTE.VERIFY_TRUE, CK_ATTRIBUTE.VERIFY_RECOVER_TRUE, CK_ATTRIBUTE.WRAP_TRUE});
        this.templateManager.addTemplate("*", 3L, 1L, new CK_ATTRIBUTE[]{CK_ATTRIBUTE.SIGN_TRUE});
        this.templateManager.addTemplate("*", 2L, 1L, new CK_ATTRIBUTE[]{CK_ATTRIBUTE.VERIFY_TRUE});
        this.templateManager.addTemplate("*", 3L, 2L, new CK_ATTRIBUTE[]{CK_ATTRIBUTE.DERIVE_TRUE});
        this.templateManager.addTemplate("*", 3L, 3L, new CK_ATTRIBUTE[]{CK_ATTRIBUTE.SIGN_TRUE, CK_ATTRIBUTE.DERIVE_TRUE});
        this.templateManager.addTemplate("*", 2L, 3L, new CK_ATTRIBUTE[]{CK_ATTRIBUTE.VERIFY_TRUE});
    }

    private String parseOperation() throws IOException {
        String word;
        word = parseWord();
        switch (word) {
            case "*":
                return "*";
            case "generate":
                return "generate";
            case "import":
                return "import";
            default:
                throw excLine("Unknown operation " + word);
        }
    }

    private long parseObjectClass() throws IOException {
        String word = parseWord();
        try {
            return Functions.getObjectClassId(word);
        } catch (IllegalArgumentException e2) {
            throw excLine("Unknown object class " + word);
        }
    }

    private long parseKeyAlgorithm() throws IOException {
        String word = parseWord();
        if (isNumber(word)) {
            return decodeNumber(word);
        }
        try {
            return Functions.getKeyId(word);
        } catch (IllegalArgumentException e2) {
            throw excLine("Unknown key algorithm " + word);
        }
    }

    private long decodeAttributeName(String str) throws IOException {
        if (isNumber(str)) {
            return decodeNumber(str);
        }
        try {
            return Functions.getAttributeId(str);
        } catch (IllegalArgumentException e2) {
            throw excLine("Unknown attribute name " + str);
        }
    }

    private CK_ATTRIBUTE decodeAttributeValue(long j2, String str) throws IOException {
        if (str.equals(FXMLLoader.NULL_KEYWORD)) {
            return new CK_ATTRIBUTE(j2);
        }
        if (str.equals("true")) {
            return new CK_ATTRIBUTE(j2, true);
        }
        if (str.equals("false")) {
            return new CK_ATTRIBUTE(j2, false);
        }
        if (isByteArray(str)) {
            return new CK_ATTRIBUTE(j2, decodeByteArray(str));
        }
        if (isNumber(str)) {
            return new CK_ATTRIBUTE(j2, Integer.valueOf(decodeNumber(str)));
        }
        throw excLine("Unknown attribute value " + str);
    }

    private void parseNSSArgs(String str) throws IOException {
        checkDup(str);
        parseEquals();
        if (nextToken() != 34) {
            throw excToken("Expected quoted string");
        }
        this.nssArgs = expand(this.st.sval);
        debug("nssArgs: " + this.nssArgs);
    }

    private void parseHandleStartupErrors(String str) throws IOException {
        checkDup(str);
        parseEquals();
        String word = parseWord();
        if (word.equals("ignoreAll")) {
            this.handleStartupErrors = 2;
        } else if (word.equals("ignoreMissingLibrary")) {
            this.handleStartupErrors = 3;
        } else if (word.equals("halt")) {
            this.handleStartupErrors = 1;
        } else {
            throw excToken("Invalid value for handleStartupErrors:");
        }
        debug("handleStartupErrors: " + this.handleStartupErrors);
    }
}
