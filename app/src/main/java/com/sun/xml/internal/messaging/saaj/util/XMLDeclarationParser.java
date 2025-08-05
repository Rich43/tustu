package com.sun.xml.internal.messaging.saaj.util;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Writer;
import java.util.StringTokenizer;
import javax.xml.transform.TransformerException;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/util/XMLDeclarationParser.class */
public class XMLDeclarationParser {
    private PushbackReader m_pushbackReader;
    static String gt16;
    static String utf16Decl;
    private String xmlDecl = null;
    private String m_encoding = "utf-8";
    private boolean m_hasHeader = false;

    static {
        gt16 = null;
        utf16Decl = null;
        try {
            gt16 = new String(">".getBytes("utf-16"));
            utf16Decl = new String("<?xml".getBytes("utf-16"));
        } catch (Exception e2) {
        }
    }

    public XMLDeclarationParser(PushbackReader pr) {
        this.m_pushbackReader = pr;
    }

    public String getEncoding() {
        return this.m_encoding;
    }

    public String getXmlDeclaration() {
        return this.xmlDecl;
    }

    public void parse() throws TransformerException, IOException {
        int c2;
        int index = 0;
        char[] aChar = new char[65535];
        StringBuffer xmlDeclStr = new StringBuffer();
        do {
            c2 = this.m_pushbackReader.read();
            if (c2 == -1) {
                break;
            }
            aChar[index] = (char) c2;
            xmlDeclStr.append((char) c2);
            index++;
        } while (c2 != 62);
        int len = index;
        String decl = xmlDeclStr.toString();
        boolean utf16 = false;
        boolean utf8 = false;
        int xmlIndex = decl.indexOf(utf16Decl);
        if (xmlIndex > -1) {
            utf16 = true;
        } else {
            xmlIndex = decl.indexOf("<?xml");
            if (xmlIndex > -1) {
                utf8 = true;
            }
        }
        if (!utf16 && !utf8) {
            this.m_pushbackReader.unread(aChar, 0, len);
            return;
        }
        this.m_hasHeader = true;
        if (utf16) {
            this.xmlDecl = new String(decl.getBytes(), "utf-16");
            this.xmlDecl = this.xmlDecl.substring(this.xmlDecl.indexOf("<"));
        } else {
            this.xmlDecl = decl;
        }
        if (xmlIndex != 0) {
            throw new IOException("Unexpected characters before XML declaration");
        }
        int versionIndex = this.xmlDecl.indexOf("version");
        if (versionIndex == -1) {
            throw new IOException("Mandatory 'version' attribute Missing in XML declaration");
        }
        int encodingIndex = this.xmlDecl.indexOf("encoding");
        if (encodingIndex == -1) {
            return;
        }
        if (versionIndex > encodingIndex) {
            throw new IOException("The 'version' attribute should preceed the 'encoding' attribute in an XML Declaration");
        }
        int stdAloneIndex = this.xmlDecl.indexOf("standalone");
        if (stdAloneIndex > -1 && (stdAloneIndex < versionIndex || stdAloneIndex < encodingIndex)) {
            throw new IOException("The 'standalone' attribute should be the last attribute in an XML Declaration");
        }
        int eqIndex = this.xmlDecl.indexOf("=", encodingIndex);
        if (eqIndex == -1) {
            throw new IOException("Missing '=' character after 'encoding' in XML declaration");
        }
        this.m_encoding = parseEncoding(this.xmlDecl, eqIndex);
        if (this.m_encoding.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            this.m_encoding = this.m_encoding.substring(this.m_encoding.indexOf(PdfOps.DOUBLE_QUOTE__TOKEN) + 1, this.m_encoding.lastIndexOf(PdfOps.DOUBLE_QUOTE__TOKEN));
        } else if (this.m_encoding.startsWith(PdfOps.SINGLE_QUOTE_TOKEN)) {
            this.m_encoding = this.m_encoding.substring(this.m_encoding.indexOf(PdfOps.SINGLE_QUOTE_TOKEN) + 1, this.m_encoding.lastIndexOf(PdfOps.SINGLE_QUOTE_TOKEN));
        }
    }

    public void writeTo(Writer wr) throws IOException {
        if (this.m_hasHeader) {
            wr.write(this.xmlDecl.toString());
        }
    }

    private String parseEncoding(String xmlDeclFinal, int eqIndex) throws IOException {
        StringTokenizer strTok = new StringTokenizer(xmlDeclFinal.substring(eqIndex + 1));
        if (strTok.hasMoreTokens()) {
            String encodingTok = strTok.nextToken();
            int indexofQ = encodingTok.indexOf("?");
            if (indexofQ > -1) {
                return encodingTok.substring(0, indexofQ);
            }
            return encodingTok;
        }
        throw new IOException("Error parsing 'encoding' attribute in XML declaration");
    }
}
