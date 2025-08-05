package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.impl.XMLScanner;
import com.sun.org.apache.xerces.internal.impl.io.ASCIIReader;
import com.sun.org.apache.xerces.internal.impl.io.UCSReader;
import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;
import com.sun.org.apache.xerces.internal.util.EncodingMap;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import com.sun.xml.internal.stream.Entity;
import com.sun.xml.internal.stream.XMLBufferListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.commons.net.ftp.FTP;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLEntityScanner.class */
public class XMLEntityScanner implements XMLLocator {
    protected Entity.ScannedEntity fCurrentEntity;
    protected int fBufferSize;
    protected XMLEntityManager fEntityManager;
    protected XMLSecurityManager fSecurityManager;
    protected XMLLimitAnalyzer fLimitAnalyzer;
    private static final boolean DEBUG_ENCODINGS = false;
    private ArrayList<XMLBufferListener> listeners;
    private static final boolean DEBUG_BUFFER = false;
    private static final boolean DEBUG_SKIP_STRING = false;
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    int[] whiteSpaceLookup;
    int whiteSpaceLen;
    boolean whiteSpaceInfoNeeded;
    protected boolean fAllowJavaEncodings;
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
    protected PropertyManager fPropertyManager;
    boolean isExternal;
    protected boolean xmlVersionSetExplicitly;
    boolean detectingVersion;
    int offset;
    int newlines;
    boolean counted;
    private static final boolean[] VALID_NAMES = new boolean[127];
    private static final EOFException END_OF_DOCUMENT_ENTITY = new EOFException() { // from class: com.sun.org.apache.xerces.internal.impl.XMLEntityScanner.1
        private static final long serialVersionUID = 980337771224675268L;

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            return this;
        }
    };

    static {
        for (int i2 = 65; i2 <= 90; i2++) {
            VALID_NAMES[i2] = true;
        }
        for (int i3 = 97; i3 <= 122; i3++) {
            VALID_NAMES[i3] = true;
        }
        for (int i4 = 48; i4 <= 57; i4++) {
            VALID_NAMES[i4] = true;
        }
        VALID_NAMES[45] = true;
        VALID_NAMES[46] = true;
        VALID_NAMES[58] = true;
        VALID_NAMES[95] = true;
    }

    public XMLEntityScanner() {
        this.fCurrentEntity = null;
        this.fBufferSize = 8192;
        this.fSecurityManager = null;
        this.fLimitAnalyzer = null;
        this.listeners = new ArrayList<>();
        this.fSymbolTable = null;
        this.fErrorReporter = null;
        this.whiteSpaceLookup = new int[100];
        this.whiteSpaceLen = 0;
        this.whiteSpaceInfoNeeded = true;
        this.fPropertyManager = null;
        this.isExternal = false;
        this.xmlVersionSetExplicitly = false;
        this.detectingVersion = false;
        this.offset = 0;
        this.newlines = 0;
        this.counted = false;
    }

    public XMLEntityScanner(PropertyManager propertyManager, XMLEntityManager entityManager) {
        this.fCurrentEntity = null;
        this.fBufferSize = 8192;
        this.fSecurityManager = null;
        this.fLimitAnalyzer = null;
        this.listeners = new ArrayList<>();
        this.fSymbolTable = null;
        this.fErrorReporter = null;
        this.whiteSpaceLookup = new int[100];
        this.whiteSpaceLen = 0;
        this.whiteSpaceInfoNeeded = true;
        this.fPropertyManager = null;
        this.isExternal = false;
        this.xmlVersionSetExplicitly = false;
        this.detectingVersion = false;
        this.offset = 0;
        this.newlines = 0;
        this.counted = false;
        this.fEntityManager = entityManager;
        reset(propertyManager);
    }

    public final void setBufferSize(int size) {
        this.fBufferSize = size;
    }

    public void reset(PropertyManager propertyManager) {
        this.fSymbolTable = (SymbolTable) propertyManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fErrorReporter = (XMLErrorReporter) propertyManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        resetCommon();
    }

    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        this.fAllowJavaEncodings = componentManager.getFeature(ALLOW_JAVA_ENCODINGS, false);
        this.fSymbolTable = (SymbolTable) componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fErrorReporter = (XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        resetCommon();
    }

    public final void reset(SymbolTable symbolTable, XMLEntityManager entityManager, XMLErrorReporter reporter) {
        this.fCurrentEntity = null;
        this.fSymbolTable = symbolTable;
        this.fEntityManager = entityManager;
        this.fErrorReporter = reporter;
        this.fLimitAnalyzer = this.fEntityManager.fLimitAnalyzer;
        this.fSecurityManager = this.fEntityManager.fSecurityManager;
    }

    private void resetCommon() {
        this.fCurrentEntity = null;
        this.whiteSpaceLen = 0;
        this.whiteSpaceInfoNeeded = true;
        this.listeners.clear();
        this.fLimitAnalyzer = this.fEntityManager.fLimitAnalyzer;
        this.fSecurityManager = this.fEntityManager.fSecurityManager;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public final String getXMLVersion() {
        if (this.fCurrentEntity != null) {
            return this.fCurrentEntity.xmlVersion;
        }
        return null;
    }

    public final void setXMLVersion(String xmlVersion) {
        this.xmlVersionSetExplicitly = true;
        this.fCurrentEntity.xmlVersion = xmlVersion;
    }

    public final void setCurrentEntity(Entity.ScannedEntity scannedEntity) {
        this.fCurrentEntity = scannedEntity;
        if (this.fCurrentEntity != null) {
            this.isExternal = this.fCurrentEntity.isExternal();
        }
    }

    public Entity.ScannedEntity getCurrentEntity() {
        return this.fCurrentEntity;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public final String getBaseSystemId() {
        if (this.fCurrentEntity == null || this.fCurrentEntity.entityLocation == null) {
            return null;
        }
        return this.fCurrentEntity.entityLocation.getExpandedSystemId();
    }

    public void setBaseSystemId(String systemId) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public final int getLineNumber() {
        if (this.fCurrentEntity != null) {
            return this.fCurrentEntity.lineNumber;
        }
        return -1;
    }

    public void setLineNumber(int line) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public final int getColumnNumber() {
        if (this.fCurrentEntity != null) {
            return this.fCurrentEntity.columnNumber;
        }
        return -1;
    }

    public void setColumnNumber(int col) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public final int getCharacterOffset() {
        if (this.fCurrentEntity != null) {
            return this.fCurrentEntity.fTotalCountTillLastLoad + this.fCurrentEntity.position;
        }
        return -1;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public final String getExpandedSystemId() {
        if (this.fCurrentEntity == null || this.fCurrentEntity.entityLocation == null) {
            return null;
        }
        return this.fCurrentEntity.entityLocation.getExpandedSystemId();
    }

    public void setExpandedSystemId(String systemId) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public final String getLiteralSystemId() {
        if (this.fCurrentEntity == null || this.fCurrentEntity.entityLocation == null) {
            return null;
        }
        return this.fCurrentEntity.entityLocation.getLiteralSystemId();
    }

    public void setLiteralSystemId(String systemId) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public final String getPublicId() {
        if (this.fCurrentEntity == null || this.fCurrentEntity.entityLocation == null) {
            return null;
        }
        return this.fCurrentEntity.entityLocation.getPublicId();
    }

    public void setPublicId(String publicId) {
    }

    public void setVersion(String version) {
        this.fCurrentEntity.version = version;
    }

    public String getVersion() {
        if (this.fCurrentEntity != null) {
            return this.fCurrentEntity.version;
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public final String getEncoding() {
        if (this.fCurrentEntity != null) {
            return this.fCurrentEntity.encoding;
        }
        return null;
    }

    public final void setEncoding(String encoding) throws IOException {
        if (this.fCurrentEntity.stream != null) {
            if (this.fCurrentEntity.encoding == null || !this.fCurrentEntity.encoding.equals(encoding)) {
                if (this.fCurrentEntity.encoding != null && this.fCurrentEntity.encoding.startsWith("UTF-16")) {
                    String ENCODING = encoding.toUpperCase(Locale.ENGLISH);
                    if (ENCODING.equals("UTF-16")) {
                        return;
                    }
                    if (ENCODING.equals("ISO-10646-UCS-4")) {
                        if (this.fCurrentEntity.encoding.equals(FastInfosetSerializer.UTF_16BE)) {
                            this.fCurrentEntity.reader = new UCSReader(this.fCurrentEntity.stream, (short) 8);
                            return;
                        } else {
                            this.fCurrentEntity.reader = new UCSReader(this.fCurrentEntity.stream, (short) 4);
                            return;
                        }
                    }
                    if (ENCODING.equals("ISO-10646-UCS-2")) {
                        if (this.fCurrentEntity.encoding.equals(FastInfosetSerializer.UTF_16BE)) {
                            this.fCurrentEntity.reader = new UCSReader(this.fCurrentEntity.stream, (short) 2);
                            return;
                        } else {
                            this.fCurrentEntity.reader = new UCSReader(this.fCurrentEntity.stream, (short) 1);
                            return;
                        }
                    }
                }
                this.fCurrentEntity.reader = createReader(this.fCurrentEntity.stream, encoding, null);
                this.fCurrentEntity.encoding = encoding;
            }
        }
    }

    public final boolean isExternal() {
        return this.fCurrentEntity.isExternal();
    }

    public int getChar(int relative) throws IOException {
        if (arrangeCapacity(relative + 1, false)) {
            return this.fCurrentEntity.ch[this.fCurrentEntity.position + relative];
        }
        return -1;
    }

    public int peekChar() throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        char c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        if (this.isExternal) {
            if (c2 != '\r') {
                return c2;
            }
            return 10;
        }
        return c2;
    }

    protected int scanChar(XMLScanner.NameType nt) throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        this.offset = this.fCurrentEntity.position;
        char[] cArr = this.fCurrentEntity.ch;
        Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
        int i2 = scannedEntity.position;
        scannedEntity.position = i2 + 1;
        int c2 = cArr[i2];
        if (c2 == 10 || (c2 == 13 && this.isExternal)) {
            this.fCurrentEntity.lineNumber++;
            this.fCurrentEntity.columnNumber = 1;
            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                invokeListeners(1);
                this.fCurrentEntity.ch[0] = (char) c2;
                load(1, true, false);
                this.offset = 0;
            }
            if (c2 == 13 && this.isExternal && this.fCurrentEntity.position < this.fCurrentEntity.count) {
                char[] cArr2 = this.fCurrentEntity.ch;
                Entity.ScannedEntity scannedEntity2 = this.fCurrentEntity;
                int i3 = scannedEntity2.position;
                scannedEntity2.position = i3 + 1;
                if (cArr2[i3] != '\n') {
                    this.fCurrentEntity.position--;
                }
                c2 = 10;
            }
        }
        this.fCurrentEntity.columnNumber++;
        if (!this.detectingVersion) {
            checkEntityLimit(nt, this.fCurrentEntity, this.offset, this.fCurrentEntity.position - this.offset);
        }
        return c2;
    }

    protected String scanNmtoken() throws IOException {
        boolean vc;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        this.offset = this.fCurrentEntity.position;
        while (true) {
            char c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
            if (c2 < 127) {
                vc = VALID_NAMES[c2];
            } else {
                vc = XMLChar.isName(c2);
            }
            if (!vc) {
                break;
            }
            Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
            int i2 = scannedEntity.position + 1;
            scannedEntity.position = i2;
            if (i2 == this.fCurrentEntity.count) {
                int length = this.fCurrentEntity.position - this.offset;
                invokeListeners(length);
                if (length == this.fCurrentEntity.fBufferSize) {
                    char[] tmp = new char[this.fCurrentEntity.fBufferSize * 2];
                    System.arraycopy(this.fCurrentEntity.ch, this.offset, tmp, 0, length);
                    this.fCurrentEntity.ch = tmp;
                    this.fCurrentEntity.fBufferSize *= 2;
                } else {
                    System.arraycopy(this.fCurrentEntity.ch, this.offset, this.fCurrentEntity.ch, 0, length);
                }
                this.offset = 0;
                if (load(length, false, false)) {
                    break;
                }
            }
        }
        int length2 = this.fCurrentEntity.position - this.offset;
        this.fCurrentEntity.columnNumber += length2;
        String symbol = null;
        if (length2 > 0) {
            symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, this.offset, length2);
        }
        return symbol;
    }

    protected String scanName(XMLScanner.NameType nt) throws IOException {
        String symbol;
        boolean vc;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        this.offset = this.fCurrentEntity.position;
        if (XMLChar.isNameStart(this.fCurrentEntity.ch[this.offset])) {
            Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
            int i2 = scannedEntity.position + 1;
            scannedEntity.position = i2;
            if (i2 == this.fCurrentEntity.count) {
                invokeListeners(1);
                this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[this.offset];
                this.offset = 0;
                if (load(1, false, false)) {
                    this.fCurrentEntity.columnNumber++;
                    String symbol2 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
                    return symbol2;
                }
            }
            while (true) {
                char c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
                if (c2 < 127) {
                    vc = VALID_NAMES[c2];
                } else {
                    vc = XMLChar.isName(c2);
                }
                if (!vc) {
                    break;
                }
                int length = checkBeforeLoad(this.fCurrentEntity, this.offset, this.offset);
                if (length > 0) {
                    this.offset = 0;
                    if (load(length, false, false)) {
                        break;
                    }
                }
            }
        }
        int length2 = this.fCurrentEntity.position - this.offset;
        this.fCurrentEntity.columnNumber += length2;
        if (length2 > 0) {
            checkLimit(XMLSecurityManager.Limit.MAX_NAME_LIMIT, this.fCurrentEntity, this.offset, length2);
            checkEntityLimit(nt, this.fCurrentEntity, this.offset, length2);
            symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, this.offset, length2);
        } else {
            symbol = null;
        }
        return symbol;
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x0120 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00af A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean scanQName(com.sun.org.apache.xerces.internal.xni.QName r9, com.sun.org.apache.xerces.internal.impl.XMLScanner.NameType r10) throws java.io.IOException, com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            Method dump skipped, instructions count: 578
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XMLEntityScanner.scanQName(com.sun.org.apache.xerces.internal.xni.QName, com.sun.org.apache.xerces.internal.impl.XMLScanner$NameType):boolean");
    }

    protected int checkBeforeLoad(Entity.ScannedEntity entity, int offset, int nameOffset) throws IOException, XNIException {
        int nameOffset2;
        int length = 0;
        int i2 = entity.position + 1;
        entity.position = i2;
        if (i2 == entity.count) {
            length = entity.position - offset;
            int nameLength = length;
            if (nameOffset != -1) {
                nameOffset2 = nameOffset - offset;
                nameLength = length - nameOffset2;
            } else {
                nameOffset2 = offset;
            }
            checkLimit(XMLSecurityManager.Limit.MAX_NAME_LIMIT, entity, nameOffset2, nameLength);
            invokeListeners(length);
            if (length == entity.ch.length) {
                char[] tmp = new char[entity.fBufferSize * 2];
                System.arraycopy(entity.ch, offset, tmp, 0, length);
                entity.ch = tmp;
                entity.fBufferSize *= 2;
            } else {
                System.arraycopy(entity.ch, offset, entity.ch, 0, length);
            }
        }
        return length;
    }

    protected void checkEntityLimit(XMLScanner.NameType nt, Entity.ScannedEntity entity, int offset, int length) throws XNIException {
        if (entity == null || !entity.isGE) {
            return;
        }
        if (nt != XMLScanner.NameType.REFERENCE) {
            checkLimit(XMLSecurityManager.Limit.GENERAL_ENTITY_SIZE_LIMIT, entity, offset, length);
        }
        if (nt == XMLScanner.NameType.ELEMENTSTART || nt == XMLScanner.NameType.ATTRIBUTENAME) {
            checkNodeCount(entity);
        }
    }

    protected void checkNodeCount(Entity.ScannedEntity entity) throws XNIException {
        if (entity != null && entity.isGE) {
            checkLimit(XMLSecurityManager.Limit.ENTITY_REPLACEMENT_LIMIT, entity, 0, 1);
        }
    }

    protected void checkLimit(XMLSecurityManager.Limit limit, Entity.ScannedEntity entity, int offset, int length) throws XNIException {
        this.fLimitAnalyzer.addValue(limit, entity.name, length);
        if (this.fSecurityManager.isOverLimit(limit, this.fLimitAnalyzer)) {
            this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
            Object[] e2 = limit == XMLSecurityManager.Limit.ENTITY_REPLACEMENT_LIMIT ? new Object[]{Integer.valueOf(this.fLimitAnalyzer.getValue(limit)), Integer.valueOf(this.fSecurityManager.getLimit(limit)), this.fSecurityManager.getStateLiteral(limit)} : new Object[]{entity.name, Integer.valueOf(this.fLimitAnalyzer.getValue(limit)), Integer.valueOf(this.fSecurityManager.getLimit(limit)), this.fSecurityManager.getStateLiteral(limit)};
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", limit.key(), e2, (short) 2);
        }
        if (this.fSecurityManager.isOverLimit(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT, this.fLimitAnalyzer)) {
            this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "TotalEntitySizeLimit", new Object[]{Integer.valueOf(this.fLimitAnalyzer.getTotalValue(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT)), Integer.valueOf(this.fSecurityManager.getLimit(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT)), this.fSecurityManager.getStateLiteral(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT)}, (short) 2);
        }
    }

    protected int scanContent(XMLString content) throws IOException, XNIException {
        int c2;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        } else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
            invokeListeners(1);
            this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[this.fCurrentEntity.count - 1];
            load(1, false, false);
            this.fCurrentEntity.position = 0;
        }
        if (normalizeNewlines((short) 1, content, false, false, null)) {
            return -1;
        }
        while (true) {
            if (this.fCurrentEntity.position >= this.fCurrentEntity.count) {
                break;
            }
            char[] cArr = this.fCurrentEntity.ch;
            Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
            int i2 = scannedEntity.position;
            scannedEntity.position = i2 + 1;
            if (!XMLChar.isContent(cArr[i2])) {
                this.fCurrentEntity.position--;
                break;
            }
        }
        int length = this.fCurrentEntity.position - this.offset;
        this.fCurrentEntity.columnNumber += length - this.newlines;
        if (!this.counted) {
            checkEntityLimit(null, this.fCurrentEntity, this.offset, length);
        }
        content.setValues(this.fCurrentEntity.ch, this.offset, length);
        if (this.fCurrentEntity.position != this.fCurrentEntity.count) {
            c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
            if (c2 == 13 && this.isExternal) {
                c2 = 10;
            }
        } else {
            c2 = -1;
        }
        return c2;
    }

    protected int scanLiteral(int quote, XMLString content, boolean isNSURI) throws IOException, XNIException {
        int c2;
        char c3;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        } else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
            invokeListeners(1);
            this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[this.fCurrentEntity.count - 1];
            load(1, false, false);
            this.fCurrentEntity.position = 0;
        }
        if (this.whiteSpaceInfoNeeded) {
            this.whiteSpaceLen = 0;
        }
        if (normalizeNewlines((short) 1, content, false, true, null)) {
            return -1;
        }
        while (this.fCurrentEntity.position < this.fCurrentEntity.count && (((c3 = this.fCurrentEntity.ch[this.fCurrentEntity.position]) != quote || (this.fCurrentEntity.literal && !this.isExternal)) && c3 != '%' && XMLChar.isContent(c3))) {
            if (this.whiteSpaceInfoNeeded && c3 == '\t') {
                storeWhiteSpace(this.fCurrentEntity.position);
            }
            this.fCurrentEntity.position++;
        }
        int length = this.fCurrentEntity.position - this.offset;
        this.fCurrentEntity.columnNumber += length - this.newlines;
        checkEntityLimit(null, this.fCurrentEntity, this.offset, length);
        if (isNSURI) {
            checkLimit(XMLSecurityManager.Limit.MAX_NAME_LIMIT, this.fCurrentEntity, this.offset, length);
        }
        content.setValues(this.fCurrentEntity.ch, this.offset, length);
        if (this.fCurrentEntity.position != this.fCurrentEntity.count) {
            c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
            if (c2 == quote && this.fCurrentEntity.literal) {
                c2 = -1;
            }
        } else {
            c2 = -1;
        }
        return c2;
    }

    void storeWhiteSpace(int whiteSpacePos) {
        if (this.whiteSpaceLen >= this.whiteSpaceLookup.length) {
            int[] tmp = new int[this.whiteSpaceLookup.length + 100];
            System.arraycopy(this.whiteSpaceLookup, 0, tmp, 0, this.whiteSpaceLookup.length);
            this.whiteSpaceLookup = tmp;
        }
        int[] iArr = this.whiteSpaceLookup;
        int i2 = this.whiteSpaceLen;
        this.whiteSpaceLen = i2 + 1;
        iArr[i2] = whiteSpacePos;
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x0214, code lost:
    
        r7.fCurrentEntity.position--;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x027e, code lost:
    
        r15 = r7.fCurrentEntity.position - r7.offset;
        r7.fCurrentEntity.columnNumber += r15 - r7.newlines;
        checkEntityLimit(com.sun.org.apache.xerces.internal.impl.XMLScanner.NameType.COMMENT, r7.fCurrentEntity, r7.offset, r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x02b1, code lost:
    
        if (r10 == false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x02b4, code lost:
    
        r15 = r15 - r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x02bb, code lost:
    
        r9.append(r7.fCurrentEntity.ch, r7.offset, r15);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean scanData(java.lang.String r8, com.sun.org.apache.xerces.internal.util.XMLStringBuffer r9) throws java.io.IOException, com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            Method dump skipped, instructions count: 730
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XMLEntityScanner.scanData(java.lang.String, com.sun.org.apache.xerces.internal.util.XMLStringBuffer):boolean");
    }

    protected boolean skipChar(int c2, XMLScanner.NameType nt) throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        this.offset = this.fCurrentEntity.position;
        if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == c2) {
            this.fCurrentEntity.position++;
            if (c2 == 10) {
                this.fCurrentEntity.lineNumber++;
                this.fCurrentEntity.columnNumber = 1;
            } else {
                this.fCurrentEntity.columnNumber++;
            }
            checkEntityLimit(nt, this.fCurrentEntity, this.offset, this.fCurrentEntity.position - this.offset);
            return true;
        }
        return false;
    }

    public boolean isSpace(char ch) {
        return ch == ' ' || ch == '\n' || ch == '\t' || ch == '\r';
    }

    protected boolean skipSpaces() throws IOException {
        char c2;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        if (this.fCurrentEntity == null) {
            return false;
        }
        int c3 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        this.offset = this.fCurrentEntity.position - 1;
        if (XMLChar.isSpace(c3)) {
            do {
                boolean entityChanged = false;
                if (c3 == 10 || (this.isExternal && c3 == 13)) {
                    this.fCurrentEntity.lineNumber++;
                    this.fCurrentEntity.columnNumber = 1;
                    if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                        invokeListeners(1);
                        this.fCurrentEntity.ch[0] = (char) c3;
                        entityChanged = load(1, true, false);
                        if (!entityChanged) {
                            this.fCurrentEntity.position = 0;
                        } else if (this.fCurrentEntity == null) {
                            return true;
                        }
                    }
                    if (c3 == 13 && this.isExternal) {
                        char[] cArr = this.fCurrentEntity.ch;
                        Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
                        int i2 = scannedEntity.position + 1;
                        scannedEntity.position = i2;
                        if (cArr[i2] != '\n') {
                            this.fCurrentEntity.position--;
                        }
                    }
                } else {
                    this.fCurrentEntity.columnNumber++;
                }
                checkEntityLimit(null, this.fCurrentEntity, this.offset, this.fCurrentEntity.position - this.offset);
                this.offset = this.fCurrentEntity.position;
                if (!entityChanged) {
                    this.fCurrentEntity.position++;
                }
                if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                    load(0, true, true);
                    if (this.fCurrentEntity == null) {
                        return true;
                    }
                }
                c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
                c3 = c2;
            } while (XMLChar.isSpace(c2));
            return true;
        }
        return false;
    }

    public boolean arrangeCapacity(int length) throws IOException {
        return arrangeCapacity(length, false);
    }

    public boolean arrangeCapacity(int length, boolean changeEntity) throws IOException, XNIException {
        if (this.fCurrentEntity.count - this.fCurrentEntity.position >= length) {
            return true;
        }
        while (this.fCurrentEntity.count - this.fCurrentEntity.position < length) {
            if (this.fCurrentEntity.ch.length - this.fCurrentEntity.position < length) {
                invokeListeners(0);
                System.arraycopy(this.fCurrentEntity.ch, this.fCurrentEntity.position, this.fCurrentEntity.ch, 0, this.fCurrentEntity.count - this.fCurrentEntity.position);
                this.fCurrentEntity.count -= this.fCurrentEntity.position;
                this.fCurrentEntity.position = 0;
            }
            if (this.fCurrentEntity.count - this.fCurrentEntity.position < length) {
                int pos = this.fCurrentEntity.position;
                invokeListeners(pos);
                boolean entityChanged = load(this.fCurrentEntity.count, changeEntity, false);
                this.fCurrentEntity.position = pos;
                if (entityChanged) {
                    break;
                }
            }
        }
        if (this.fCurrentEntity.count - this.fCurrentEntity.position >= length) {
            return true;
        }
        return false;
    }

    protected boolean skipString(String s2) throws IOException, XNIException {
        int i2;
        int length = s2.length();
        if (arrangeCapacity(length, false)) {
            int beforeSkip = this.fCurrentEntity.position;
            int afterSkip = (this.fCurrentEntity.position + length) - 1;
            int i3 = length - 1;
            do {
                int i4 = i3;
                i3--;
                if (s2.charAt(i4) == this.fCurrentEntity.ch[afterSkip]) {
                    i2 = afterSkip;
                    afterSkip--;
                } else {
                    return false;
                }
            } while (i2 != beforeSkip);
            this.fCurrentEntity.position += length;
            this.fCurrentEntity.columnNumber += length;
            if (!this.detectingVersion) {
                checkEntityLimit(null, this.fCurrentEntity, beforeSkip, length);
                return true;
            }
            return true;
        }
        return false;
    }

    protected boolean skipString(char[] s2) throws IOException, XNIException {
        int length = s2.length;
        if (arrangeCapacity(length, false)) {
            int beforeSkip = this.fCurrentEntity.position;
            for (char c2 : s2) {
                int i2 = beforeSkip;
                beforeSkip++;
                if (this.fCurrentEntity.ch[i2] != c2) {
                    return false;
                }
            }
            this.fCurrentEntity.position += length;
            this.fCurrentEntity.columnNumber += length;
            if (!this.detectingVersion) {
                checkEntityLimit(null, this.fCurrentEntity, beforeSkip, length);
                return true;
            }
            return true;
        }
        return false;
    }

    final boolean load(int offset, boolean changeEntity, boolean notify) throws IOException, XNIException {
        if (notify) {
            invokeListeners(offset);
        }
        this.fCurrentEntity.fTotalCountTillLastLoad += this.fCurrentEntity.fLastCount;
        int length = this.fCurrentEntity.ch.length - offset;
        if (!this.fCurrentEntity.mayReadChunks && length > 64) {
            length = 64;
        }
        int count = this.fCurrentEntity.reader.read(this.fCurrentEntity.ch, offset, length);
        boolean entityChanged = false;
        if (count != -1) {
            if (count != 0) {
                this.fCurrentEntity.fLastCount = count;
                this.fCurrentEntity.count = count + offset;
                this.fCurrentEntity.position = offset;
            }
        } else {
            this.fCurrentEntity.count = offset;
            this.fCurrentEntity.position = offset;
            entityChanged = true;
            if (changeEntity) {
                this.fEntityManager.endEntity();
                if (this.fCurrentEntity == null) {
                    throw END_OF_DOCUMENT_ENTITY;
                }
                if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                    load(0, true, false);
                }
            }
        }
        return entityChanged;
    }

    protected Reader createReader(InputStream inputStream, String encoding, Boolean isBigEndian) throws IOException, XNIException {
        if (encoding == null) {
            encoding = "UTF-8";
        }
        String ENCODING = encoding.toUpperCase(Locale.ENGLISH);
        if (ENCODING.equals("UTF-8")) {
            return new UTF8Reader(inputStream, this.fCurrentEntity.fBufferSize, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
        }
        if (ENCODING.equals("US-ASCII")) {
            return new ASCIIReader(inputStream, this.fCurrentEntity.fBufferSize, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
        }
        if (ENCODING.equals("ISO-10646-UCS-4")) {
            if (isBigEndian != null) {
                boolean isBE = isBigEndian.booleanValue();
                if (isBE) {
                    return new UCSReader(inputStream, (short) 8);
                }
                return new UCSReader(inputStream, (short) 4);
            }
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingByteOrderUnsupported", new Object[]{encoding}, (short) 2);
        }
        if (ENCODING.equals("ISO-10646-UCS-2")) {
            if (isBigEndian != null) {
                boolean isBE2 = isBigEndian.booleanValue();
                if (isBE2) {
                    return new UCSReader(inputStream, (short) 2);
                }
                return new UCSReader(inputStream, (short) 1);
            }
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingByteOrderUnsupported", new Object[]{encoding}, (short) 2);
        }
        boolean validIANA = XMLChar.isValidIANAEncoding(encoding);
        boolean validJava = XMLChar.isValidJavaEncoding(encoding);
        if (!validIANA || (this.fAllowJavaEncodings && !validJava)) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingDeclInvalid", new Object[]{encoding}, (short) 2);
            encoding = FTP.DEFAULT_CONTROL_ENCODING;
        }
        String javaEncoding = EncodingMap.getIANA2JavaMapping(ENCODING);
        if (javaEncoding == null) {
            if (this.fAllowJavaEncodings) {
                javaEncoding = encoding;
            } else {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingDeclInvalid", new Object[]{encoding}, (short) 2);
                javaEncoding = "ISO8859_1";
            }
        } else if (javaEncoding.equals("ASCII")) {
            return new ASCIIReader(inputStream, this.fBufferSize, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
        }
        return new InputStreamReader(inputStream, javaEncoding);
    }

    protected Object[] getEncodingName(byte[] b4, int count) {
        if (count < 2) {
            return new Object[]{"UTF-8", null};
        }
        int b0 = b4[0] & 255;
        int b1 = b4[1] & 255;
        if (b0 == 254 && b1 == 255) {
            return new Object[]{FastInfosetSerializer.UTF_16BE, new Boolean(true)};
        }
        if (b0 == 255 && b1 == 254) {
            return new Object[]{"UTF-16LE", new Boolean(false)};
        }
        if (count < 3) {
            return new Object[]{"UTF-8", null};
        }
        int b2 = b4[2] & 255;
        if (b0 == 239 && b1 == 187 && b2 == 191) {
            return new Object[]{"UTF-8", null};
        }
        if (count < 4) {
            return new Object[]{"UTF-8", null};
        }
        int b3 = b4[3] & 255;
        if (b0 == 0 && b1 == 0 && b2 == 0 && b3 == 60) {
            return new Object[]{"ISO-10646-UCS-4", new Boolean(true)};
        }
        if (b0 == 60 && b1 == 0 && b2 == 0 && b3 == 0) {
            return new Object[]{"ISO-10646-UCS-4", new Boolean(false)};
        }
        if (b0 == 0 && b1 == 0 && b2 == 60 && b3 == 0) {
            return new Object[]{"ISO-10646-UCS-4", null};
        }
        if (b0 == 0 && b1 == 60 && b2 == 0 && b3 == 0) {
            return new Object[]{"ISO-10646-UCS-4", null};
        }
        if (b0 == 0 && b1 == 60 && b2 == 0 && b3 == 63) {
            return new Object[]{FastInfosetSerializer.UTF_16BE, new Boolean(true)};
        }
        if (b0 == 60 && b1 == 0 && b2 == 63 && b3 == 0) {
            return new Object[]{"UTF-16LE", new Boolean(false)};
        }
        if (b0 == 76 && b1 == 111 && b2 == 167 && b3 == 148) {
            return new Object[]{"CP037", null};
        }
        return new Object[]{"UTF-8", null};
    }

    final void print() {
    }

    public void registerListener(XMLBufferListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void invokeListeners(int loadPos) {
        for (int i2 = 0; i2 < this.listeners.size(); i2++) {
            this.listeners.get(i2).refresh(loadPos);
        }
    }

    protected final boolean skipDeclSpaces() throws IOException, XNIException {
        char c2;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, false);
        }
        int c3 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        if (XMLChar.isSpace(c3)) {
            boolean external = this.fCurrentEntity.isExternal();
            do {
                boolean entityChanged = false;
                if (c3 == 10 || (external && c3 == 13)) {
                    this.fCurrentEntity.lineNumber++;
                    this.fCurrentEntity.columnNumber = 1;
                    if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                        this.fCurrentEntity.ch[0] = (char) c3;
                        entityChanged = load(1, true, false);
                        if (!entityChanged) {
                            this.fCurrentEntity.position = 0;
                        }
                    }
                    if (c3 == 13 && external) {
                        char[] cArr = this.fCurrentEntity.ch;
                        Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
                        int i2 = scannedEntity.position + 1;
                        scannedEntity.position = i2;
                        if (cArr[i2] != '\n') {
                            this.fCurrentEntity.position--;
                        }
                    }
                } else {
                    this.fCurrentEntity.columnNumber++;
                }
                if (!entityChanged) {
                    this.fCurrentEntity.position++;
                }
                if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                    load(0, true, false);
                }
                c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
                c3 = c2;
            } while (XMLChar.isSpace(c2));
            return true;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x0142, code lost:
    
        r6.fCurrentEntity.position--;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00ef  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean normalizeNewlines(short r7, com.sun.org.apache.xerces.internal.xni.XMLString r8, boolean r9, boolean r10, com.sun.org.apache.xerces.internal.impl.XMLScanner.NameType r11) throws java.io.IOException, com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            Method dump skipped, instructions count: 498
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XMLEntityScanner.normalizeNewlines(short, com.sun.org.apache.xerces.internal.xni.XMLString, boolean, boolean, com.sun.org.apache.xerces.internal.impl.XMLScanner$NameType):boolean");
    }
}
