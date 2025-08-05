package com.sun.org.apache.xml.internal.serialize;

import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XML11Char;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.MissingResourceException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/XML11Serializer.class */
public class XML11Serializer extends XMLSerializer {
    protected static final boolean DEBUG = false;
    protected NamespaceSupport fNSBinder;
    protected NamespaceSupport fLocalNSBinder;
    protected SymbolTable fSymbolTable;
    protected boolean fDOML1;
    protected int fNamespaceCounter;
    protected static final String PREFIX = "NS";
    protected boolean fNamespaces;

    public XML11Serializer() {
        this.fDOML1 = false;
        this.fNamespaceCounter = 1;
        this.fNamespaces = false;
        this._format.setVersion(SerializerConstants.XMLVERSION11);
    }

    public XML11Serializer(OutputFormat format) {
        super(format);
        this.fDOML1 = false;
        this.fNamespaceCounter = 1;
        this.fNamespaces = false;
        this._format.setVersion(SerializerConstants.XMLVERSION11);
    }

    public XML11Serializer(Writer writer, OutputFormat format) {
        super(writer, format);
        this.fDOML1 = false;
        this.fNamespaceCounter = 1;
        this.fNamespaces = false;
        this._format.setVersion(SerializerConstants.XMLVERSION11);
    }

    public XML11Serializer(OutputStream output, OutputFormat format) {
        super(output, format != null ? format : new OutputFormat("xml", (String) null, false));
        this.fDOML1 = false;
        this.fNamespaceCounter = 1;
        this.fNamespaces = false;
        this._format.setVersion(SerializerConstants.XMLVERSION11);
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer, org.xml.sax.ContentHandler
    public void characters(char[] chars, int start, int length) throws SAXException {
        try {
            ElementState state = content();
            if (state.inCData || state.doCData) {
                if (!state.inCData) {
                    this._printer.printText("<![CDATA[");
                    state.inCData = true;
                }
                int saveIndent = this._printer.getNextIndent();
                this._printer.setNextIndent(0);
                int end = start + length;
                int index = start;
                while (index < end) {
                    char ch = chars[index];
                    if (ch == ']' && index + 2 < end && chars[index + 1] == ']' && chars[index + 2] == '>') {
                        this._printer.printText(SerializerConstants.CDATA_CONTINUE);
                        index += 2;
                    } else if (!XML11Char.isXML11Valid(ch)) {
                        index++;
                        if (index < end) {
                            surrogates(ch, chars[index], true);
                        } else {
                            fatalError("The character '" + ch + "' is an invalid XML character");
                        }
                    } else if (this._encodingInfo.isPrintable(ch) && XML11Char.isXML11ValidLiteral(ch)) {
                        this._printer.printText(ch);
                    } else {
                        this._printer.printText("]]>&#x");
                        this._printer.printText(Integer.toHexString(ch));
                        this._printer.printText(";<![CDATA[");
                    }
                    index++;
                }
                this._printer.setNextIndent(saveIndent);
            } else if (state.preserveSpace) {
                int saveIndent2 = this._printer.getNextIndent();
                this._printer.setNextIndent(0);
                printText(chars, start, length, true, state.unescaped);
                this._printer.setNextIndent(saveIndent2);
            } else {
                printText(chars, start, length, false, state.unescaped);
            }
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.XMLSerializer, com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected void printEscaped(String source) throws IOException {
        int length = source.length();
        int i2 = 0;
        while (i2 < length) {
            int ch = source.charAt(i2);
            if (!XML11Char.isXML11Valid(ch)) {
                i2++;
                if (i2 < length) {
                    surrogates(ch, source.charAt(i2), false);
                } else {
                    fatalError("The character '" + ((char) ch) + "' is an invalid XML character");
                }
            } else if (ch == 10 || ch == 13 || ch == 9 || ch == 133 || ch == 8232) {
                printHex(ch);
            } else if (ch == 60) {
                this._printer.printText(SerializerConstants.ENTITY_LT);
            } else if (ch == 38) {
                this._printer.printText(SerializerConstants.ENTITY_AMP);
            } else if (ch == 34) {
                this._printer.printText(SerializerConstants.ENTITY_QUOT);
            } else if (ch >= 32 && this._encodingInfo.isPrintable((char) ch)) {
                this._printer.printText((char) ch);
            } else {
                printHex(ch);
            }
            i2++;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected final void printCDATAText(String text) throws MissingResourceException, IOException {
        int length = text.length();
        int index = 0;
        while (index < length) {
            char ch = text.charAt(index);
            if (ch == ']' && index + 2 < length && text.charAt(index + 1) == ']' && text.charAt(index + 2) == '>') {
                if (this.fDOMErrorHandler != null) {
                    if ((this.features & 16) == 0 && (this.features & 2) == 0) {
                        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "EndingCDATA", null);
                        modifyDOMError(msg, (short) 3, null, this.fCurrentNode);
                        boolean continueProcess = this.fDOMErrorHandler.handleError(this.fDOMError);
                        if (!continueProcess) {
                            throw new IOException();
                        }
                    } else {
                        String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "SplittingCDATA", null);
                        modifyDOMError(msg2, (short) 1, null, this.fCurrentNode);
                        this.fDOMErrorHandler.handleError(this.fDOMError);
                    }
                }
                this._printer.printText(SerializerConstants.CDATA_CONTINUE);
                index += 2;
            } else if (!XML11Char.isXML11Valid(ch)) {
                index++;
                if (index < length) {
                    surrogates(ch, text.charAt(index), true);
                } else {
                    fatalError("The character '" + ch + "' is an invalid XML character");
                }
            } else if (this._encodingInfo.isPrintable(ch) && XML11Char.isXML11ValidLiteral(ch)) {
                this._printer.printText(ch);
            } else {
                this._printer.printText("]]>&#x");
                this._printer.printText(Integer.toHexString(ch));
                this._printer.printText(";<![CDATA[");
            }
            index++;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.XMLSerializer
    protected final void printXMLChar(int ch) throws IOException {
        if (ch == 13 || ch == 133 || ch == 8232) {
            printHex(ch);
            return;
        }
        if (ch == 60) {
            this._printer.printText(SerializerConstants.ENTITY_LT);
            return;
        }
        if (ch == 38) {
            this._printer.printText(SerializerConstants.ENTITY_AMP);
            return;
        }
        if (ch == 62) {
            this._printer.printText(SerializerConstants.ENTITY_GT);
        } else if (this._encodingInfo.isPrintable((char) ch) && XML11Char.isXML11ValidLiteral(ch)) {
            this._printer.printText((char) ch);
        } else {
            printHex(ch);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected final void surrogates(int high, int low, boolean inContent) throws IOException {
        if (XMLChar.isHighSurrogate(high)) {
            if (!XMLChar.isLowSurrogate(low)) {
                fatalError("The character '" + ((char) low) + "' is an invalid XML character");
                return;
            }
            int supplemental = XMLChar.supplemental((char) high, (char) low);
            if (!XML11Char.isXML11Valid(supplemental)) {
                fatalError("The character '" + ((char) supplemental) + "' is an invalid XML character");
                return;
            }
            if (inContent && content().inCData) {
                this._printer.printText("]]>&#x");
                this._printer.printText(Integer.toHexString(supplemental));
                this._printer.printText(";<![CDATA[");
                return;
            }
            printHex(supplemental);
            return;
        }
        fatalError("The character '" + ((char) high) + "' is an invalid XML character");
    }

    @Override // com.sun.org.apache.xml.internal.serialize.XMLSerializer, com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected void printText(String text, boolean preserveSpace, boolean unescaped) throws IOException {
        int length = text.length();
        if (preserveSpace) {
            int index = 0;
            while (index < length) {
                char ch = text.charAt(index);
                if (!XML11Char.isXML11Valid(ch)) {
                    index++;
                    if (index < length) {
                        surrogates(ch, text.charAt(index), true);
                    } else {
                        fatalError("The character '" + ch + "' is an invalid XML character");
                    }
                } else if (unescaped && XML11Char.isXML11ValidLiteral(ch)) {
                    this._printer.printText(ch);
                } else {
                    printXMLChar(ch);
                }
                index++;
            }
            return;
        }
        int index2 = 0;
        while (index2 < length) {
            char ch2 = text.charAt(index2);
            if (!XML11Char.isXML11Valid(ch2)) {
                index2++;
                if (index2 < length) {
                    surrogates(ch2, text.charAt(index2), true);
                } else {
                    fatalError("The character '" + ch2 + "' is an invalid XML character");
                }
            } else if (unescaped && XML11Char.isXML11ValidLiteral(ch2)) {
                this._printer.printText(ch2);
            } else {
                printXMLChar(ch2);
            }
            index2++;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.XMLSerializer, com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected void printText(char[] chars, int start, int length, boolean preserveSpace, boolean unescaped) throws IOException {
        if (preserveSpace) {
            while (true) {
                int i2 = length;
                length--;
                if (i2 > 0) {
                    int i3 = start;
                    start++;
                    char ch = chars[i3];
                    if (!XML11Char.isXML11Valid(ch)) {
                        length--;
                        if (length > 0) {
                            start++;
                            surrogates(ch, chars[start], true);
                        } else {
                            fatalError("The character '" + ch + "' is an invalid XML character");
                        }
                    } else if (unescaped && XML11Char.isXML11ValidLiteral(ch)) {
                        this._printer.printText(ch);
                    } else {
                        printXMLChar(ch);
                    }
                } else {
                    return;
                }
            }
        } else {
            while (true) {
                int i4 = length;
                length--;
                if (i4 > 0) {
                    int i5 = start;
                    start++;
                    char ch2 = chars[i5];
                    if (!XML11Char.isXML11Valid(ch2)) {
                        length--;
                        if (length > 0) {
                            start++;
                            surrogates(ch2, chars[start], true);
                        } else {
                            fatalError("The character '" + ch2 + "' is an invalid XML character");
                        }
                    } else if (unescaped && XML11Char.isXML11ValidLiteral(ch2)) {
                        this._printer.printText(ch2);
                    } else {
                        printXMLChar(ch2);
                    }
                } else {
                    return;
                }
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.XMLSerializer, com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public boolean reset() throws MissingResourceException {
        super.reset();
        return true;
    }
}
