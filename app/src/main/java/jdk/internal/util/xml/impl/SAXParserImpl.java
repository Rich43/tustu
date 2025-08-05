package jdk.internal.util.xml.impl;

import java.io.IOException;
import java.io.InputStream;
import jdk.internal.org.xml.sax.InputSource;
import jdk.internal.org.xml.sax.SAXException;
import jdk.internal.org.xml.sax.XMLReader;
import jdk.internal.org.xml.sax.helpers.DefaultHandler;
import jdk.internal.util.xml.SAXParser;

/* loaded from: rt.jar:jdk/internal/util/xml/impl/SAXParserImpl.class */
public class SAXParserImpl extends SAXParser {
    private ParserSAX parser = new ParserSAX();

    @Override // jdk.internal.util.xml.SAXParser
    public XMLReader getXMLReader() throws SAXException {
        return this.parser;
    }

    @Override // jdk.internal.util.xml.SAXParser
    public boolean isNamespaceAware() {
        return this.parser.mIsNSAware;
    }

    @Override // jdk.internal.util.xml.SAXParser
    public boolean isValidating() {
        return false;
    }

    @Override // jdk.internal.util.xml.SAXParser
    public void parse(InputStream inputStream, DefaultHandler defaultHandler) throws IOException, SAXException {
        this.parser.parse(inputStream, defaultHandler);
    }

    @Override // jdk.internal.util.xml.SAXParser
    public void parse(InputSource inputSource, DefaultHandler defaultHandler) throws IOException, SAXException {
        this.parser.parse(inputSource, defaultHandler);
    }
}
