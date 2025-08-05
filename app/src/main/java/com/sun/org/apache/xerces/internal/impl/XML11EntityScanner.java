package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.impl.XMLScanner;
import com.sun.org.apache.xerces.internal.util.XML11Char;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.xml.internal.stream.Entity;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XML11EntityScanner.class */
public class XML11EntityScanner extends XMLEntityScanner {
    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    public int peekChar() throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        char c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        if (this.fCurrentEntity.isExternal()) {
            if (c2 == '\r' || c2 == 133 || c2 == 8232) {
                return 10;
            }
            return c2;
        }
        return c2;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0062 A[PHI: r11
  0x0062: PHI (r11v1 'external' boolean) = (r11v0 'external' boolean), (r11v2 'external' boolean) binds: [B:6:0x003e, B:14:0x005f] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected int scanChar(com.sun.org.apache.xerces.internal.impl.XMLScanner.NameType r8) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 286
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanChar(com.sun.org.apache.xerces.internal.impl.XMLScanner$NameType):int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x017c, code lost:
    
        r6.fCurrentEntity.position--;
     */
    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected java.lang.String scanNmtoken() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 580
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanNmtoken():java.lang.String");
    }

    /* JADX WARN: Code restructure failed: missing block: B:55:0x0216, code lost:
    
        r6.fCurrentEntity.position--;
     */
    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected java.lang.String scanName(com.sun.org.apache.xerces.internal.impl.XMLScanner.NameType r7) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 666
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanName(com.sun.org.apache.xerces.internal.impl.XMLScanner$NameType):java.lang.String");
    }

    protected String scanNCName() throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        int offset = this.fCurrentEntity.position;
        char ch = this.fCurrentEntity.ch[offset];
        if (XML11Char.isXML11NCNameStart(ch)) {
            Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
            int i2 = scannedEntity.position + 1;
            scannedEntity.position = i2;
            if (i2 == this.fCurrentEntity.count) {
                invokeListeners(1);
                this.fCurrentEntity.ch[0] = ch;
                offset = 0;
                if (load(1, false, false)) {
                    this.fCurrentEntity.columnNumber++;
                    String symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
                    return symbol;
                }
            }
        } else if (XML11Char.isXML11NameHighSurrogate(ch)) {
            Entity.ScannedEntity scannedEntity2 = this.fCurrentEntity;
            int i3 = scannedEntity2.position + 1;
            scannedEntity2.position = i3;
            if (i3 == this.fCurrentEntity.count) {
                invokeListeners(1);
                this.fCurrentEntity.ch[0] = ch;
                offset = 0;
                if (load(1, false, false)) {
                    this.fCurrentEntity.position--;
                    this.fCurrentEntity.startPosition--;
                    return null;
                }
            }
            char ch2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
            if (!XMLChar.isLowSurrogate(ch2) || !XML11Char.isXML11NCNameStart(XMLChar.supplemental(ch, ch2))) {
                this.fCurrentEntity.position--;
                return null;
            }
            Entity.ScannedEntity scannedEntity3 = this.fCurrentEntity;
            int i4 = scannedEntity3.position + 1;
            scannedEntity3.position = i4;
            if (i4 == this.fCurrentEntity.count) {
                invokeListeners(2);
                this.fCurrentEntity.ch[0] = ch;
                this.fCurrentEntity.ch[1] = ch2;
                offset = 0;
                if (load(2, false, false)) {
                    this.fCurrentEntity.columnNumber += 2;
                    String symbol2 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 2);
                    return symbol2;
                }
            }
        } else {
            return null;
        }
        while (true) {
            char ch3 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
            if (XML11Char.isXML11NCName(ch3)) {
                Entity.ScannedEntity scannedEntity4 = this.fCurrentEntity;
                int i5 = scannedEntity4.position + 1;
                scannedEntity4.position = i5;
                if (i5 == this.fCurrentEntity.count) {
                    int length = this.fCurrentEntity.position - offset;
                    invokeListeners(length);
                    if (length == this.fCurrentEntity.ch.length) {
                        char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
                        System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
                        this.fCurrentEntity.ch = tmp;
                    } else {
                        System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
                    }
                    offset = 0;
                    if (load(length, false, false)) {
                        break;
                    }
                } else {
                    continue;
                }
            } else {
                if (!XML11Char.isXML11NameHighSurrogate(ch3)) {
                    break;
                }
                Entity.ScannedEntity scannedEntity5 = this.fCurrentEntity;
                int i6 = scannedEntity5.position + 1;
                scannedEntity5.position = i6;
                if (i6 == this.fCurrentEntity.count) {
                    int length2 = this.fCurrentEntity.position - offset;
                    invokeListeners(length2);
                    if (length2 == this.fCurrentEntity.ch.length) {
                        char[] tmp2 = new char[this.fCurrentEntity.ch.length << 1];
                        System.arraycopy(this.fCurrentEntity.ch, offset, tmp2, 0, length2);
                        this.fCurrentEntity.ch = tmp2;
                    } else {
                        System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length2);
                    }
                    offset = 0;
                    if (load(length2, false, false)) {
                        this.fCurrentEntity.startPosition--;
                        this.fCurrentEntity.position--;
                        break;
                    }
                }
                char ch22 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
                if (!XMLChar.isLowSurrogate(ch22) || !XML11Char.isXML11NCName(XMLChar.supplemental(ch3, ch22))) {
                    break;
                }
                Entity.ScannedEntity scannedEntity6 = this.fCurrentEntity;
                int i7 = scannedEntity6.position + 1;
                scannedEntity6.position = i7;
                if (i7 == this.fCurrentEntity.count) {
                    int length3 = this.fCurrentEntity.position - offset;
                    invokeListeners(length3);
                    if (length3 == this.fCurrentEntity.ch.length) {
                        char[] tmp3 = new char[this.fCurrentEntity.ch.length << 1];
                        System.arraycopy(this.fCurrentEntity.ch, offset, tmp3, 0, length3);
                        this.fCurrentEntity.ch = tmp3;
                    } else {
                        System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length3);
                    }
                    offset = 0;
                    if (load(length3, false, false)) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        this.fCurrentEntity.position--;
        int length4 = this.fCurrentEntity.position - offset;
        this.fCurrentEntity.columnNumber += length4;
        String symbol3 = null;
        if (length4 > 0) {
            symbol3 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, offset, length4);
        }
        return symbol3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:67:0x0295, code lost:
    
        r15 = true;
        r8.fCurrentEntity.position--;
     */
    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean scanQName(com.sun.org.apache.xerces.internal.xni.QName r9, com.sun.org.apache.xerces.internal.impl.XMLScanner.NameType r10) throws java.io.IOException, com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            Method dump skipped, instructions count: 989
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanQName(com.sun.org.apache.xerces.internal.xni.QName, com.sun.org.apache.xerces.internal.impl.XMLScanner$NameType):boolean");
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    protected int scanContent(XMLString content) throws IOException {
        int c2;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        } else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
            invokeListeners(1);
            this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[this.fCurrentEntity.count - 1];
            load(1, false, false);
            this.fCurrentEntity.position = 0;
            this.fCurrentEntity.startPosition = 0;
        }
        if (normalizeNewlines((short) 2, content, false, false, null)) {
            return -1;
        }
        boolean external = this.fCurrentEntity.isExternal();
        if (!external) {
            while (true) {
                if (this.fCurrentEntity.position >= this.fCurrentEntity.count) {
                    break;
                }
                char[] cArr = this.fCurrentEntity.ch;
                Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
                int i2 = scannedEntity.position;
                scannedEntity.position = i2 + 1;
                if (!XML11Char.isXML11InternalEntityContent(cArr[i2])) {
                    this.fCurrentEntity.position--;
                    break;
                }
            }
        } else {
            while (this.fCurrentEntity.position < this.fCurrentEntity.count) {
                char[] cArr2 = this.fCurrentEntity.ch;
                Entity.ScannedEntity scannedEntity2 = this.fCurrentEntity;
                int i3 = scannedEntity2.position;
                scannedEntity2.position = i3 + 1;
                char c3 = cArr2[i3];
                if (!XML11Char.isXML11Content(c3) || c3 == 133 || c3 == 8232) {
                    this.fCurrentEntity.position--;
                    break;
                }
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
            if ((c2 == 13 || c2 == 133 || c2 == 8232) && external) {
                c2 = 10;
            }
        } else {
            c2 = -1;
        }
        return c2;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    protected int scanLiteral(int quote, XMLString content, boolean isNSURI) throws IOException {
        int c2;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        } else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
            invokeListeners(1);
            this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[this.fCurrentEntity.count - 1];
            load(1, false, false);
            this.fCurrentEntity.startPosition = 0;
            this.fCurrentEntity.position = 0;
        }
        if (normalizeNewlines((short) 2, content, false, true, null)) {
            return -1;
        }
        boolean external = this.fCurrentEntity.isExternal();
        if (external) {
            while (this.fCurrentEntity.position < this.fCurrentEntity.count) {
                char[] cArr = this.fCurrentEntity.ch;
                Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
                int i2 = scannedEntity.position;
                scannedEntity.position = i2 + 1;
                char c3 = cArr[i2];
                if (c3 == quote || c3 == '%' || !XML11Char.isXML11Content(c3) || c3 == 133 || c3 == 8232) {
                    this.fCurrentEntity.position--;
                    break;
                }
            }
        } else {
            while (this.fCurrentEntity.position < this.fCurrentEntity.count) {
                char[] cArr2 = this.fCurrentEntity.ch;
                Entity.ScannedEntity scannedEntity2 = this.fCurrentEntity;
                int i3 = scannedEntity2.position;
                scannedEntity2.position = i3 + 1;
                char c4 = cArr2[i3];
                if ((c4 == quote && !this.fCurrentEntity.literal) || c4 == '%' || !XML11Char.isXML11InternalEntityContent(c4)) {
                    this.fCurrentEntity.position--;
                    break;
                }
            }
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

    /* JADX WARN: Code restructure failed: missing block: B:37:0x0201, code lost:
    
        if (r7.fCurrentEntity.position != (r0 + r0)) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0204, code lost:
    
        r10 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x022a, code lost:
    
        r7.fCurrentEntity.position--;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0340, code lost:
    
        if (r7.fCurrentEntity.position != (r0 + r0)) goto L118;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0343, code lost:
    
        r10 = true;
     */
    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean scanData(java.lang.String r8, com.sun.org.apache.xerces.internal.util.XMLStringBuffer r9) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1048
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanData(java.lang.String, com.sun.org.apache.xerces.internal.util.XMLStringBuffer):boolean");
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    protected boolean skipChar(int c2, XMLScanner.NameType nt) throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        int offset = this.fCurrentEntity.position;
        char c3 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        if (c3 == c2) {
            this.fCurrentEntity.position++;
            if (c2 == 10) {
                this.fCurrentEntity.lineNumber++;
                this.fCurrentEntity.columnNumber = 1;
            } else {
                this.fCurrentEntity.columnNumber++;
            }
            checkEntityLimit(nt, this.fCurrentEntity, offset, this.fCurrentEntity.position - offset);
            return true;
        }
        if (c2 != 10) {
            return false;
        }
        if ((c3 == 8232 || c3 == 133) && this.fCurrentEntity.isExternal()) {
            this.fCurrentEntity.position++;
            this.fCurrentEntity.lineNumber++;
            this.fCurrentEntity.columnNumber = 1;
            checkEntityLimit(nt, this.fCurrentEntity, offset, this.fCurrentEntity.position - offset);
            return true;
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    protected boolean skipSpaces() throws IOException {
        char c2;
        char c3;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        if (this.fCurrentEntity == null) {
            return false;
        }
        int c4 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        int offset = this.fCurrentEntity.position - 1;
        if (this.fCurrentEntity.isExternal()) {
            if (XML11Char.isXML11Space(c4)) {
                do {
                    boolean entityChanged = false;
                    if (c4 == 10 || c4 == 13 || c4 == 133 || c4 == 8232) {
                        this.fCurrentEntity.lineNumber++;
                        this.fCurrentEntity.columnNumber = 1;
                        if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                            invokeListeners(1);
                            this.fCurrentEntity.ch[0] = (char) c4;
                            entityChanged = load(1, true, false);
                            if (!entityChanged) {
                                this.fCurrentEntity.startPosition = 0;
                                this.fCurrentEntity.position = 0;
                            } else if (this.fCurrentEntity == null) {
                                return true;
                            }
                        }
                        if (c4 == 13) {
                            char[] cArr = this.fCurrentEntity.ch;
                            Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
                            int i2 = scannedEntity.position + 1;
                            scannedEntity.position = i2;
                            char c5 = cArr[i2];
                            if (c5 != '\n' && c5 != 133) {
                                this.fCurrentEntity.position--;
                            }
                        }
                    } else {
                        this.fCurrentEntity.columnNumber++;
                    }
                    checkEntityLimit(null, this.fCurrentEntity, offset, this.fCurrentEntity.position - offset);
                    offset = this.fCurrentEntity.position;
                    if (!entityChanged) {
                        this.fCurrentEntity.position++;
                    }
                    if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                        load(0, true, true);
                        if (this.fCurrentEntity == null) {
                            return true;
                        }
                    }
                    c3 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
                    c4 = c3;
                } while (XML11Char.isXML11Space(c3));
                return true;
            }
            return false;
        }
        if (XMLChar.isSpace(c4)) {
            do {
                boolean entityChanged2 = false;
                if (c4 == 10) {
                    this.fCurrentEntity.lineNumber++;
                    this.fCurrentEntity.columnNumber = 1;
                    if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                        invokeListeners(1);
                        this.fCurrentEntity.ch[0] = (char) c4;
                        entityChanged2 = load(1, true, false);
                        if (!entityChanged2) {
                            this.fCurrentEntity.startPosition = 0;
                            this.fCurrentEntity.position = 0;
                        } else if (this.fCurrentEntity == null) {
                            return true;
                        }
                    }
                } else {
                    this.fCurrentEntity.columnNumber++;
                }
                checkEntityLimit(null, this.fCurrentEntity, offset, this.fCurrentEntity.position - offset);
                offset = this.fCurrentEntity.position;
                if (!entityChanged2) {
                    this.fCurrentEntity.position++;
                }
                if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                    load(0, true, true);
                    if (this.fCurrentEntity == null) {
                        return true;
                    }
                }
                c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
                c4 = c2;
            } while (XMLChar.isSpace(c2));
            return true;
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    protected boolean skipString(String s2) throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        int length = s2.length();
        int beforeSkip = this.fCurrentEntity.position;
        for (int i2 = 0; i2 < length; i2++) {
            char[] cArr = this.fCurrentEntity.ch;
            Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
            int i3 = scannedEntity.position;
            scannedEntity.position = i3 + 1;
            char c2 = cArr[i3];
            if (c2 != s2.charAt(i2)) {
                this.fCurrentEntity.position -= i2 + 1;
                return false;
            }
            if (i2 < length - 1 && this.fCurrentEntity.position == this.fCurrentEntity.count) {
                invokeListeners(0);
                System.arraycopy(this.fCurrentEntity.ch, (this.fCurrentEntity.count - i2) - 1, this.fCurrentEntity.ch, 0, i2 + 1);
                if (load(i2 + 1, false, false)) {
                    this.fCurrentEntity.startPosition -= i2 + 1;
                    this.fCurrentEntity.position -= i2 + 1;
                    return false;
                }
            }
        }
        this.fCurrentEntity.columnNumber += length;
        if (!this.detectingVersion) {
            checkEntityLimit(null, this.fCurrentEntity, beforeSkip, length);
            return true;
        }
        return true;
    }
}
