package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XML11Char;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XML11DTDScannerImpl.class */
public class XML11DTDScannerImpl extends XMLDTDScannerImpl {
    private XMLStringBuffer fStringBuffer;

    public XML11DTDScannerImpl() {
        this.fStringBuffer = new XMLStringBuffer();
    }

    public XML11DTDScannerImpl(SymbolTable symbolTable, XMLErrorReporter errorReporter, XMLEntityManager entityManager) {
        super(symbolTable, errorReporter, entityManager);
        this.fStringBuffer = new XMLStringBuffer();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    protected boolean scanPubidLiteral(XMLString literal) throws IOException, XNIException {
        int quote = this.fEntityScanner.scanChar(null);
        if (quote != 39 && quote != 34) {
            reportFatalError("QuoteRequiredInPublicID", null);
            return false;
        }
        this.fStringBuffer.clear();
        boolean skipSpace = true;
        boolean dataok = true;
        while (true) {
            int c2 = this.fEntityScanner.scanChar(null);
            if (c2 == 32 || c2 == 10 || c2 == 13 || c2 == 133 || c2 == 8232) {
                if (!skipSpace) {
                    this.fStringBuffer.append(' ');
                    skipSpace = true;
                }
            } else {
                if (c2 == quote) {
                    if (skipSpace) {
                        this.fStringBuffer.length--;
                    }
                    literal.setValues(this.fStringBuffer);
                    return dataok;
                }
                if (XMLChar.isPubid(c2)) {
                    this.fStringBuffer.append((char) c2);
                    skipSpace = false;
                } else {
                    if (c2 == -1) {
                        reportFatalError("PublicIDUnterminated", null);
                        return false;
                    }
                    dataok = false;
                    reportFatalError("InvalidCharInPublicID", new Object[]{Integer.toHexString(c2)});
                }
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    protected void normalizeWhitespace(XMLString value) {
        int end = value.offset + value.length;
        for (int i2 = value.offset; i2 < end; i2++) {
            if (XMLChar.isSpace(value.ch[i2])) {
                value.ch[i2] = ' ';
            }
        }
    }

    protected void normalizeWhitespace(XMLString value, int fromIndex) {
        int end = value.offset + value.length;
        for (int i2 = value.offset + fromIndex; i2 < end; i2++) {
            if (XMLChar.isSpace(value.ch[i2])) {
                value.ch[i2] = ' ';
            }
        }
    }

    protected int isUnchangedByNormalization(XMLString value) {
        int end = value.offset + value.length;
        for (int i2 = value.offset; i2 < end; i2++) {
            if (XMLChar.isSpace(value.ch[i2])) {
                return i2 - value.offset;
            }
        }
        return -1;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    protected boolean isInvalid(int value) {
        return !XML11Char.isXML11Valid(value);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    protected boolean isInvalidLiteral(int value) {
        return !XML11Char.isXML11ValidLiteral(value);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    protected boolean isValidNameChar(int value) {
        return XML11Char.isXML11Name(value);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    protected boolean isValidNameStartChar(int value) {
        return XML11Char.isXML11NameStart(value);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    protected boolean isValidNCName(int value) {
        return XML11Char.isXML11NCName(value);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    protected boolean isValidNameStartHighSurrogate(int value) {
        return XML11Char.isXML11NameHighSurrogate(value);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    protected boolean versionSupported(String version) {
        return version.equals(SerializerConstants.XMLVERSION11) || version.equals("1.0");
    }

    protected String getVersionNotSupportedKey() {
        return "VersionNotSupported11";
    }
}
