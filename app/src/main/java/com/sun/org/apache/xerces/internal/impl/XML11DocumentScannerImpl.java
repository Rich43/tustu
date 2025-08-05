package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.impl.XMLScanner;
import com.sun.org.apache.xerces.internal.util.XML11Char;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XML11DocumentScannerImpl.class */
public class XML11DocumentScannerImpl extends XMLDocumentScannerImpl {
    private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
    private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
    private final XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl
    protected int scanContent(XMLStringBuffer content) throws IOException, XNIException {
        this.fTempString.length = 0;
        int c2 = this.fEntityScanner.scanContent(this.fTempString);
        content.append(this.fTempString);
        if (c2 == 13 || c2 == 133 || c2 == 8232) {
            this.fEntityScanner.scanChar(null);
            content.append((char) c2);
            c2 = -1;
        }
        if (c2 == 93) {
            content.append((char) this.fEntityScanner.scanChar(null));
            this.fInScanContent = true;
            if (this.fEntityScanner.skipChar(93, null)) {
                content.append(']');
                while (this.fEntityScanner.skipChar(93, null)) {
                    content.append(']');
                }
                if (this.fEntityScanner.skipChar(62, null)) {
                    reportFatalError("CDEndInContent", null);
                }
            }
            this.fInScanContent = false;
            c2 = -1;
        }
        return c2;
    }

    protected boolean scanAttributeValue(XMLString value, XMLString nonNormalizedValue, String atName, boolean checkEntities, String eleName, boolean isNSURI) throws IOException, XNIException {
        int quote = this.fEntityScanner.peekChar();
        if (quote != 39 && quote != 34) {
            reportFatalError("OpenQuoteExpected", new Object[]{eleName, atName});
        }
        this.fEntityScanner.scanChar(XMLScanner.NameType.ATTRIBUTE);
        int entityDepth = this.fEntityDepth;
        int c2 = this.fEntityScanner.scanLiteral(quote, value, isNSURI);
        int fromIndex = 0;
        if (c2 == quote) {
            int iIsUnchangedByNormalization = isUnchangedByNormalization(value);
            fromIndex = iIsUnchangedByNormalization;
            if (iIsUnchangedByNormalization == -1) {
                nonNormalizedValue.setValues(value);
                int cquote = this.fEntityScanner.scanChar(XMLScanner.NameType.ATTRIBUTE);
                if (cquote != quote) {
                    reportFatalError("CloseQuoteExpected", new Object[]{eleName, atName});
                    return true;
                }
                return true;
            }
        }
        this.fStringBuffer2.clear();
        this.fStringBuffer2.append(value);
        normalizeWhitespace(value, fromIndex);
        if (c2 != quote) {
            this.fScanningAttribute = true;
            this.fStringBuffer.clear();
            while (true) {
                this.fStringBuffer.append(value);
                if (c2 == 38) {
                    this.fEntityScanner.skipChar(38, XMLScanner.NameType.REFERENCE);
                    if (entityDepth == this.fEntityDepth) {
                        this.fStringBuffer2.append('&');
                    }
                    if (this.fEntityScanner.skipChar(35, XMLScanner.NameType.REFERENCE)) {
                        if (entityDepth == this.fEntityDepth) {
                            this.fStringBuffer2.append('#');
                        }
                        int ch = scanCharReferenceValue(this.fStringBuffer, this.fStringBuffer2);
                        if (ch != -1) {
                        }
                    } else {
                        String entityName = this.fEntityScanner.scanName(XMLScanner.NameType.REFERENCE);
                        if (entityName == null) {
                            reportFatalError("NameRequiredInReference", null);
                        } else if (entityDepth == this.fEntityDepth) {
                            this.fStringBuffer2.append(entityName);
                        }
                        if (!this.fEntityScanner.skipChar(59, XMLScanner.NameType.REFERENCE)) {
                            reportFatalError("SemicolonRequiredInReference", new Object[]{entityName});
                        } else if (entityDepth == this.fEntityDepth) {
                            this.fStringBuffer2.append(';');
                        }
                        if (resolveCharacter(entityName, this.fStringBuffer)) {
                            checkEntityLimit(false, this.fEntityScanner.fCurrentEntity.name, 1);
                        } else if (this.fEntityManager.isExternalEntity(entityName)) {
                            reportFatalError("ReferenceToExternalEntity", new Object[]{entityName});
                        } else {
                            if (!this.fEntityManager.isDeclaredEntity(entityName)) {
                                if (checkEntities) {
                                    if (this.fValidation) {
                                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[]{entityName}, (short) 1);
                                    }
                                } else {
                                    reportFatalError("EntityNotDeclared", new Object[]{entityName});
                                }
                            }
                            this.fEntityManager.startEntity(true, entityName, true);
                        }
                    }
                } else if (c2 == 60) {
                    reportFatalError("LessthanInAttValue", new Object[]{eleName, atName});
                    this.fEntityScanner.scanChar(null);
                    if (entityDepth == this.fEntityDepth) {
                        this.fStringBuffer2.append((char) c2);
                    }
                } else if (c2 == 37 || c2 == 93) {
                    this.fEntityScanner.scanChar(null);
                    this.fStringBuffer.append((char) c2);
                    if (entityDepth == this.fEntityDepth) {
                        this.fStringBuffer2.append((char) c2);
                    }
                } else if (c2 != -1 && XMLChar.isHighSurrogate(c2)) {
                    this.fStringBuffer3.clear();
                    if (scanSurrogates(this.fStringBuffer3)) {
                        this.fStringBuffer.append(this.fStringBuffer3);
                        if (entityDepth == this.fEntityDepth) {
                            this.fStringBuffer2.append(this.fStringBuffer3);
                        }
                    }
                } else if (c2 != -1 && isInvalidLiteral(c2)) {
                    reportFatalError("InvalidCharInAttValue", new Object[]{eleName, atName, Integer.toString(c2, 16)});
                    this.fEntityScanner.scanChar(null);
                    if (entityDepth == this.fEntityDepth) {
                        this.fStringBuffer2.append((char) c2);
                    }
                }
                c2 = this.fEntityScanner.scanLiteral(quote, value, isNSURI);
                if (entityDepth == this.fEntityDepth) {
                    this.fStringBuffer2.append(value);
                }
                normalizeWhitespace(value);
                if (c2 == quote && entityDepth == this.fEntityDepth) {
                    break;
                }
            }
            this.fStringBuffer.append(value);
            value.setValues(this.fStringBuffer);
            this.fScanningAttribute = false;
        }
        nonNormalizedValue.setValues(this.fStringBuffer2);
        int cquote2 = this.fEntityScanner.scanChar(null);
        if (cquote2 != quote) {
            reportFatalError("CloseQuoteExpected", new Object[]{eleName, atName});
        }
        return nonNormalizedValue.equals(value.ch, value.offset, value.length);
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
        return XML11Char.isXML11Invalid(value);
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
