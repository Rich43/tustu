package com.sun.org.apache.xerces.internal.xpointer;

import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeNamespaceSupport;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingResourceException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xpointer/XPointerHandler.class */
public final class XPointerHandler extends XIncludeHandler implements XPointerProcessor {
    protected ArrayList<XPointerPart> fXPointerParts;
    protected XMLErrorReporter fXPointerErrorReporter;
    protected XMLErrorHandler fErrorHandler;
    protected SymbolTable fSymbolTable;
    protected XPointerPart fXPointerPart = null;
    protected boolean fFoundMatchingPtrPart = false;
    private final String ELEMENT_SCHEME_NAME = "element";
    protected boolean fIsXPointerResolved = false;
    protected boolean fFixupBase = false;
    protected boolean fFixupLang = false;

    public XPointerHandler() {
        this.fXPointerParts = null;
        this.fSymbolTable = null;
        this.fXPointerParts = new ArrayList<>();
        this.fSymbolTable = new SymbolTable();
    }

    public XPointerHandler(SymbolTable symbolTable, XMLErrorHandler errorHandler, XMLErrorReporter errorReporter) {
        this.fXPointerParts = null;
        this.fSymbolTable = null;
        this.fXPointerParts = new ArrayList<>();
        this.fSymbolTable = symbolTable;
        this.fErrorHandler = errorHandler;
        this.fXPointerErrorReporter = errorReporter;
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler, com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public void setDocumentHandler(XMLDocumentHandler handler) {
        this.fDocumentHandler = handler;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0174  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x018c  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x01a9  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0221 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01d9 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01a2 A[SYNTHETIC] */
    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerProcessor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void parseXPointer(java.lang.String r10) throws com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            Method dump skipped, instructions count: 581
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.xpointer.XPointerHandler.parseXPointer(java.lang.String):void");
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerProcessor
    public boolean resolveXPointer(QName element, XMLAttributes attributes, Augmentations augs, int event) throws XNIException {
        boolean resolved = false;
        if (!this.fFoundMatchingPtrPart) {
            for (int i2 = 0; i2 < this.fXPointerParts.size(); i2++) {
                this.fXPointerPart = this.fXPointerParts.get(i2);
                if (this.fXPointerPart.resolveXPointer(element, attributes, augs, event)) {
                    this.fFoundMatchingPtrPart = true;
                    resolved = true;
                }
            }
        } else if (this.fXPointerPart.resolveXPointer(element, attributes, augs, event)) {
            resolved = true;
        }
        if (!this.fIsXPointerResolved) {
            this.fIsXPointerResolved = resolved;
        }
        return resolved;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerProcessor
    public boolean isFragmentResolved() throws XNIException {
        boolean resolved = this.fXPointerPart != null ? this.fXPointerPart.isFragmentResolved() : false;
        if (!this.fIsXPointerResolved) {
            this.fIsXPointerResolved = resolved;
        }
        return resolved;
    }

    public boolean isChildFragmentResolved() throws XNIException {
        boolean resolved = this.fXPointerPart != null ? this.fXPointerPart.isChildFragmentResolved() : false;
        return resolved;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerProcessor
    public boolean isXPointerResolved() throws XNIException {
        return this.fIsXPointerResolved;
    }

    public XPointerPart getXPointerPart() {
        return this.fXPointerPart;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportError(String key, Object[] arguments) throws XNIException {
        throw new XNIException(this.fErrorReporter.getMessageFormatter(XPointerMessageFormatter.XPOINTER_DOMAIN).formatMessage(this.fErrorReporter.getLocale(), key, arguments));
    }

    private void reportWarning(String key, Object[] arguments) throws XNIException {
        this.fXPointerErrorReporter.reportError(XPointerMessageFormatter.XPOINTER_DOMAIN, key, arguments, (short) 0);
    }

    protected void initErrorReporter() {
        if (this.fXPointerErrorReporter == null) {
            this.fXPointerErrorReporter = new XMLErrorReporter();
        }
        if (this.fErrorHandler == null) {
            this.fErrorHandler = new XPointerErrorHandler();
        }
        this.fXPointerErrorReporter.putMessageFormatter(XPointerMessageFormatter.XPOINTER_DOMAIN, new XPointerMessageFormatter());
    }

    protected void init() {
        this.fXPointerParts.clear();
        this.fXPointerPart = null;
        this.fFoundMatchingPtrPart = false;
        this.fIsXPointerResolved = false;
        initErrorReporter();
    }

    public ArrayList<XPointerPart> getPointerParts() {
        return this.fXPointerParts;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens.class */
    private final class Tokens {
        private static final int XPTRTOKEN_OPEN_PAREN = 0;
        private static final int XPTRTOKEN_CLOSE_PAREN = 1;
        private static final int XPTRTOKEN_SHORTHAND = 2;
        private static final int XPTRTOKEN_SCHEMENAME = 3;
        private static final int XPTRTOKEN_SCHEMEDATA = 4;
        private final String[] fgTokenNames;
        private static final int INITIAL_TOKEN_COUNT = 256;
        private int[] fTokens;
        private int fTokenCount;
        private int fCurrentTokenIndex;
        private SymbolTable fSymbolTable;
        private HashMap<Integer, String> fTokenNames;

        private Tokens(SymbolTable symbolTable) {
            this.fgTokenNames = new String[]{"XPTRTOKEN_OPEN_PAREN", "XPTRTOKEN_CLOSE_PAREN", "XPTRTOKEN_SHORTHAND", "XPTRTOKEN_SCHEMENAME", "XPTRTOKEN_SCHEMEDATA"};
            this.fTokens = new int[256];
            this.fTokenCount = 0;
            this.fTokenNames = new HashMap<>();
            this.fSymbolTable = symbolTable;
            this.fTokenNames.put(new Integer(0), "XPTRTOKEN_OPEN_PAREN");
            this.fTokenNames.put(new Integer(1), "XPTRTOKEN_CLOSE_PAREN");
            this.fTokenNames.put(new Integer(2), "XPTRTOKEN_SHORTHAND");
            this.fTokenNames.put(new Integer(3), "XPTRTOKEN_SCHEMENAME");
            this.fTokenNames.put(new Integer(4), "XPTRTOKEN_SCHEMEDATA");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getTokenString(int token) {
            return this.fTokenNames.get(new Integer(token));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addToken(String tokenStr) {
            String str = this.fTokenNames.get(tokenStr);
            Integer tokenInt = str == null ? null : Integer.valueOf(Integer.parseInt(str));
            if (tokenInt == null) {
                tokenInt = new Integer(this.fTokenNames.size());
                this.fTokenNames.put(tokenInt, tokenStr);
            }
            addToken(tokenInt.intValue());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addToken(int token) {
            try {
                this.fTokens[this.fTokenCount] = token;
            } catch (ArrayIndexOutOfBoundsException e2) {
                int[] oldList = this.fTokens;
                this.fTokens = new int[this.fTokenCount << 1];
                System.arraycopy(oldList, 0, this.fTokens, 0, this.fTokenCount);
                this.fTokens[this.fTokenCount] = token;
            }
            this.fTokenCount++;
        }

        private void rewind() {
            this.fCurrentTokenIndex = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasMore() {
            return this.fCurrentTokenIndex < this.fTokenCount;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int nextToken() throws XNIException {
            if (this.fCurrentTokenIndex == this.fTokenCount) {
                XPointerHandler.this.reportError("XPointerProcessingError", null);
            }
            int[] iArr = this.fTokens;
            int i2 = this.fCurrentTokenIndex;
            this.fCurrentTokenIndex = i2 + 1;
            return iArr[i2];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int peekToken() throws XNIException {
            if (this.fCurrentTokenIndex == this.fTokenCount) {
                XPointerHandler.this.reportError("XPointerProcessingError", null);
            }
            return this.fTokens[this.fCurrentTokenIndex];
        }

        private String nextTokenAsString() throws XNIException {
            String tokenStrint = getTokenString(nextToken());
            if (tokenStrint == null) {
                XPointerHandler.this.reportError("XPointerProcessingError", null);
            }
            return tokenStrint;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Scanner.class */
    private class Scanner {
        private static final byte CHARTYPE_INVALID = 0;
        private static final byte CHARTYPE_OTHER = 1;
        private static final byte CHARTYPE_WHITESPACE = 2;
        private static final byte CHARTYPE_CARRET = 3;
        private static final byte CHARTYPE_OPEN_PAREN = 4;
        private static final byte CHARTYPE_CLOSE_PAREN = 5;
        private static final byte CHARTYPE_MINUS = 6;
        private static final byte CHARTYPE_PERIOD = 7;
        private static final byte CHARTYPE_SLASH = 8;
        private static final byte CHARTYPE_DIGIT = 9;
        private static final byte CHARTYPE_COLON = 10;
        private static final byte CHARTYPE_EQUAL = 11;
        private static final byte CHARTYPE_LETTER = 12;
        private static final byte CHARTYPE_UNDERSCORE = 13;
        private static final byte CHARTYPE_NONASCII = 14;
        private final byte[] fASCIICharMap;
        private SymbolTable fSymbolTable;

        private Scanner(SymbolTable symbolTable) {
            this.fASCIICharMap = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 4, 5, 1, 1, 1, 6, 7, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10, 1, 1, 11, 1, 1, 1, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 1, 1, 1, 3, 13, 1, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 1, 1, 1, 1, 1};
            this.fSymbolTable = symbolTable;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean scanExpr(SymbolTable symbolTable, Tokens tokens, String data, int currentOffset, int endOffset) throws XNIException {
            int ch;
            int ch2;
            int iCharAt;
            int openParen = 0;
            int closeParen = 0;
            String name = null;
            StringBuffer schemeDataBuff = new StringBuffer();
            while (currentOffset != endOffset) {
                int iCharAt2 = data.charAt(currentOffset);
                while (true) {
                    ch = iCharAt2;
                    if (ch == 32 || ch == 10 || ch == 9 || ch == 13) {
                        currentOffset++;
                        if (currentOffset != endOffset) {
                            iCharAt2 = data.charAt(currentOffset);
                        }
                    }
                }
                if (currentOffset != endOffset) {
                    byte chartype = ch >= 128 ? (byte) 14 : this.fASCIICharMap[ch];
                    switch (chartype) {
                        case 1:
                        case 2:
                        case 3:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                            if (openParen == 0) {
                                int nameOffset = currentOffset;
                                currentOffset = scanNCName(data, endOffset, currentOffset);
                                if (currentOffset == nameOffset) {
                                    XPointerHandler.this.reportError("InvalidShortHandPointer", new Object[]{data});
                                    return false;
                                }
                                if (currentOffset < endOffset) {
                                    ch2 = data.charAt(currentOffset);
                                } else {
                                    ch2 = -1;
                                }
                                name = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
                                String prefix = XMLSymbols.EMPTY_STRING;
                                if (ch2 == 58) {
                                    int currentOffset2 = currentOffset + 1;
                                    if (currentOffset2 == endOffset) {
                                        return false;
                                    }
                                    data.charAt(currentOffset2);
                                    prefix = name;
                                    currentOffset = scanNCName(data, endOffset, currentOffset2);
                                    if (currentOffset == currentOffset2) {
                                        return false;
                                    }
                                    if (currentOffset < endOffset) {
                                        data.charAt(currentOffset);
                                    }
                                    name = symbolTable.addSymbol(data.substring(currentOffset2, currentOffset));
                                }
                                if (currentOffset != endOffset) {
                                    addToken(tokens, 3);
                                    tokens.addToken(prefix);
                                    tokens.addToken(name);
                                } else if (currentOffset == endOffset) {
                                    addToken(tokens, 2);
                                    tokens.addToken(name);
                                }
                                closeParen = 0;
                                break;
                            } else if (openParen > 0 && closeParen == 0 && name != null) {
                                int dataOffset = currentOffset;
                                currentOffset = scanData(data, schemeDataBuff, endOffset, currentOffset);
                                if (currentOffset == dataOffset) {
                                    XPointerHandler.this.reportError("InvalidSchemeDataInXPointer", new Object[]{data});
                                    return false;
                                }
                                if (currentOffset < endOffset) {
                                    iCharAt = data.charAt(currentOffset);
                                } else {
                                    iCharAt = -1;
                                }
                                String schemeData = symbolTable.addSymbol(schemeDataBuff.toString());
                                addToken(tokens, 4);
                                tokens.addToken(schemeData);
                                openParen = 0;
                                schemeDataBuff.delete(0, schemeDataBuff.length());
                                break;
                            } else {
                                return false;
                            }
                            break;
                        case 4:
                            addToken(tokens, 0);
                            openParen++;
                            currentOffset++;
                            break;
                        case 5:
                            addToken(tokens, 1);
                            closeParen++;
                            currentOffset++;
                            break;
                    }
                } else {
                    return true;
                }
            }
            return true;
        }

        private int scanNCName(String data, int endOffset, int currentOffset) {
            int ch = data.charAt(currentOffset);
            if (ch >= 128) {
                if (!XMLChar.isNameStart(ch)) {
                    return currentOffset;
                }
            } else {
                byte chartype = this.fASCIICharMap[ch];
                if (chartype != 12 && chartype != 13) {
                    return currentOffset;
                }
            }
            while (true) {
                currentOffset++;
                if (currentOffset < endOffset) {
                    int ch2 = data.charAt(currentOffset);
                    if (ch2 >= 128) {
                        if (!XMLChar.isName(ch2)) {
                            break;
                        }
                    } else {
                        byte chartype2 = this.fASCIICharMap[ch2];
                        if (chartype2 != 12 && chartype2 != 9 && chartype2 != 7 && chartype2 != 6 && chartype2 != 13) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            return currentOffset;
        }

        private int scanData(String data, StringBuffer schemeData, int endOffset, int currentOffset) {
            while (currentOffset != endOffset) {
                int ch = data.charAt(currentOffset);
                byte chartype = ch >= 128 ? (byte) 14 : this.fASCIICharMap[ch];
                if (chartype == 4) {
                    schemeData.append(ch);
                    int currentOffset2 = scanData(data, schemeData, endOffset, currentOffset + 1);
                    if (currentOffset2 == endOffset) {
                        return currentOffset2;
                    }
                    int ch2 = data.charAt(currentOffset2);
                    if ((ch2 >= 128 ? (byte) 14 : this.fASCIICharMap[ch2]) != 5) {
                        return endOffset;
                    }
                    schemeData.append((char) ch2);
                    currentOffset = currentOffset2 + 1;
                } else {
                    if (chartype == 5) {
                        return currentOffset;
                    }
                    if (chartype == 3) {
                        currentOffset++;
                        int ch3 = data.charAt(currentOffset);
                        byte chartype2 = ch3 >= 128 ? (byte) 14 : this.fASCIICharMap[ch3];
                        if (chartype2 != 3 && chartype2 != 4 && chartype2 != 5) {
                            break;
                        }
                        schemeData.append((char) ch3);
                        currentOffset++;
                    } else {
                        schemeData.append((char) ch);
                        currentOffset++;
                    }
                }
            }
            return currentOffset;
        }

        protected void addToken(Tokens tokens, int token) throws XNIException {
            tokens.addToken(token);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void comment(XMLString text, Augmentations augs) throws XNIException {
        if (!isChildFragmentResolved()) {
            return;
        }
        super.comment(text, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
        if (!isChildFragmentResolved()) {
            return;
        }
        super.processingInstruction(target, data, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws MissingResourceException, XNIException {
        if (!resolveXPointer(element, attributes, augs, 0)) {
            if (this.fFixupBase) {
                processXMLBaseAttributes(attributes);
            }
            if (this.fFixupLang) {
                processXMLLangAttributes(attributes);
            }
            this.fNamespaceContext.setContextInvalid();
            return;
        }
        super.startElement(element, attributes, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws MissingResourceException, XNIException {
        if (!resolveXPointer(element, attributes, augs, 2)) {
            if (this.fFixupBase) {
                processXMLBaseAttributes(attributes);
            }
            if (this.fFixupLang) {
                processXMLLangAttributes(attributes);
            }
            this.fNamespaceContext.setContextInvalid();
            return;
        }
        super.emptyElement(element, attributes, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void characters(XMLString text, Augmentations augs) throws XNIException {
        if (!isChildFragmentResolved()) {
            return;
        }
        super.characters(text, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        if (!isChildFragmentResolved()) {
            return;
        }
        super.ignorableWhitespace(text, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName element, Augmentations augs) throws XNIException {
        if (!resolveXPointer(element, null, augs, 1)) {
            return;
        }
        super.endElement(element, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startCDATA(Augmentations augs) throws XNIException {
        if (!isChildFragmentResolved()) {
            return;
        }
        super.startCDATA(augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endCDATA(Augmentations augs) throws XNIException {
        if (!isChildFragmentResolved()) {
            return;
        }
        super.endCDATA(augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        if (propertyId == "http://apache.org/xml/properties/internal/error-reporter") {
            if (value != null) {
                this.fXPointerErrorReporter = (XMLErrorReporter) value;
            } else {
                this.fXPointerErrorReporter = null;
            }
        }
        if (propertyId == "http://apache.org/xml/properties/internal/error-handler") {
            if (value != null) {
                this.fErrorHandler = (XMLErrorHandler) value;
            } else {
                this.fErrorHandler = null;
            }
        }
        if (propertyId == "http://apache.org/xml/features/xinclude/fixup-language") {
            if (value != null) {
                this.fFixupLang = ((Boolean) value).booleanValue();
            } else {
                this.fFixupLang = false;
            }
        }
        if (propertyId == "http://apache.org/xml/features/xinclude/fixup-base-uris") {
            if (value != null) {
                this.fFixupBase = ((Boolean) value).booleanValue();
            } else {
                this.fFixupBase = false;
            }
        }
        if (propertyId == "http://apache.org/xml/properties/internal/namespace-context") {
            this.fNamespaceContext = (XIncludeNamespaceSupport) value;
        }
        super.setProperty(propertyId, value);
    }
}
