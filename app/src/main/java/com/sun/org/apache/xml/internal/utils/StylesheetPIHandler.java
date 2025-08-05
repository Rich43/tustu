package com.sun.org.apache.xml.internal.utils;

import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/StylesheetPIHandler.class */
public class StylesheetPIHandler extends DefaultHandler {
    String m_baseID;
    String m_media;
    String m_title;
    String m_charset;
    Vector m_stylesheets = new Vector();
    URIResolver m_uriResolver;

    public void setURIResolver(URIResolver resolver) {
        this.m_uriResolver = resolver;
    }

    public URIResolver getURIResolver() {
        return this.m_uriResolver;
    }

    public StylesheetPIHandler(String baseID, String media, String title, String charset) {
        this.m_baseID = baseID;
        this.m_media = media;
        this.m_title = title;
        this.m_charset = charset;
    }

    public Source getAssociatedStylesheet() {
        int sz = this.m_stylesheets.size();
        if (sz > 0) {
            Source source = (Source) this.m_stylesheets.elementAt(sz - 1);
            return source;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v78, types: [javax.xml.transform.Source] */
    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        if (target.equals("xml-stylesheet")) {
            String href = null;
            String type = null;
            String title = null;
            String media = null;
            String charset = null;
            StringTokenizer tokenizer = new StringTokenizer(data, " \t=\n", true);
            boolean lookedAhead = false;
            SAXSource sAXSource = null;
            String token = "";
            while (tokenizer.hasMoreTokens()) {
                if (!lookedAhead) {
                    token = tokenizer.nextToken();
                } else {
                    lookedAhead = false;
                }
                if (!tokenizer.hasMoreTokens() || (!token.equals(" ") && !token.equals("\t") && !token.equals("="))) {
                    String name = token;
                    if (name.equals("type")) {
                        String strNextToken = tokenizer.nextToken();
                        while (true) {
                            token = strNextToken;
                            if (!tokenizer.hasMoreTokens() || (!token.equals(" ") && !token.equals("\t") && !token.equals("="))) {
                                break;
                            } else {
                                strNextToken = tokenizer.nextToken();
                            }
                        }
                        type = token.substring(1, token.length() - 1);
                    } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_HREF)) {
                        String strNextToken2 = tokenizer.nextToken();
                        while (true) {
                            token = strNextToken2;
                            if (!tokenizer.hasMoreTokens() || (!token.equals(" ") && !token.equals("\t") && !token.equals("="))) {
                                break;
                            } else {
                                strNextToken2 = tokenizer.nextToken();
                            }
                        }
                        String href2 = token;
                        if (tokenizer.hasMoreTokens()) {
                            token = tokenizer.nextToken();
                            while (token.equals("=") && tokenizer.hasMoreTokens()) {
                                href2 = href2 + token + tokenizer.nextToken();
                                if (!tokenizer.hasMoreTokens()) {
                                    break;
                                }
                                token = tokenizer.nextToken();
                                lookedAhead = true;
                            }
                        }
                        href = href2.substring(1, href2.length() - 1);
                        try {
                            if (this.m_uriResolver != null) {
                                sAXSource = this.m_uriResolver.resolve(href, this.m_baseID);
                            } else {
                                href = SystemIDResolver.getAbsoluteURI(href, this.m_baseID);
                                sAXSource = new SAXSource(new InputSource(href));
                            }
                        } catch (TransformerException te) {
                            throw new SAXException(te);
                        }
                    } else if (name.equals("title")) {
                        String strNextToken3 = tokenizer.nextToken();
                        while (true) {
                            token = strNextToken3;
                            if (!tokenizer.hasMoreTokens() || (!token.equals(" ") && !token.equals("\t") && !token.equals("="))) {
                                break;
                            } else {
                                strNextToken3 = tokenizer.nextToken();
                            }
                        }
                        title = token.substring(1, token.length() - 1);
                    } else if (name.equals("media")) {
                        String strNextToken4 = tokenizer.nextToken();
                        while (true) {
                            token = strNextToken4;
                            if (!tokenizer.hasMoreTokens() || (!token.equals(" ") && !token.equals("\t") && !token.equals("="))) {
                                break;
                            } else {
                                strNextToken4 = tokenizer.nextToken();
                            }
                        }
                        media = token.substring(1, token.length() - 1);
                    } else if (name.equals("charset")) {
                        String strNextToken5 = tokenizer.nextToken();
                        while (true) {
                            token = strNextToken5;
                            if (!tokenizer.hasMoreTokens() || (!token.equals(" ") && !token.equals("\t") && !token.equals("="))) {
                                break;
                            } else {
                                strNextToken5 = tokenizer.nextToken();
                            }
                        }
                        charset = token.substring(1, token.length() - 1);
                    } else if (name.equals("alternate")) {
                        String strNextToken6 = tokenizer.nextToken();
                        while (true) {
                            token = strNextToken6;
                            if (!tokenizer.hasMoreTokens() || (!token.equals(" ") && !token.equals("\t") && !token.equals("="))) {
                                break;
                            } else {
                                strNextToken6 = tokenizer.nextToken();
                            }
                        }
                        token.substring(1, token.length() - 1).equals("yes");
                    }
                }
            }
            if (null != type) {
                if ((type.equals("text/xsl") || type.equals("text/xml") || type.equals("application/xml+xslt")) && null != href) {
                    if (null != this.m_media && (null == media || !media.equals(this.m_media))) {
                        return;
                    }
                    if (null != this.m_charset && (null == charset || !charset.equals(this.m_charset))) {
                        return;
                    }
                    if (null != this.m_title && (null == title || !title.equals(this.m_title))) {
                        return;
                    }
                    this.m_stylesheets.addElement(sAXSource);
                }
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        throw new StopParseException();
    }

    public void setBaseId(String baseId) {
        this.m_baseID = baseId;
    }

    public String getBaseId() {
        return this.m_baseID;
    }
}
