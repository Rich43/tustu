package jdk.internal.util.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import jdk.internal.org.xml.sax.InputSource;
import jdk.internal.org.xml.sax.SAXException;
import jdk.internal.org.xml.sax.XMLReader;
import jdk.internal.org.xml.sax.helpers.DefaultHandler;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:jdk/internal/util/xml/SAXParser.class */
public abstract class SAXParser {
    public abstract XMLReader getXMLReader() throws SAXException;

    public abstract boolean isNamespaceAware();

    public abstract boolean isValidating();

    protected SAXParser() {
    }

    public void parse(InputStream inputStream, DefaultHandler defaultHandler) throws IOException, SAXException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        parse(new InputSource(inputStream), defaultHandler);
    }

    public void parse(String str, DefaultHandler defaultHandler) throws IOException, SAXException {
        if (str == null) {
            throw new IllegalArgumentException("uri cannot be null");
        }
        parse(new InputSource(str), defaultHandler);
    }

    public void parse(File file, DefaultHandler defaultHandler) throws IOException, SAXException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        parse(new InputSource(file.toURI().toASCIIString()), defaultHandler);
    }

    public void parse(InputSource inputSource, DefaultHandler defaultHandler) throws IOException, SAXException {
        if (inputSource == null) {
            throw new IllegalArgumentException("InputSource cannot be null");
        }
        XMLReader xMLReader = getXMLReader();
        if (defaultHandler != null) {
            xMLReader.setContentHandler(defaultHandler);
            xMLReader.setEntityResolver(defaultHandler);
            xMLReader.setErrorHandler(defaultHandler);
            xMLReader.setDTDHandler(defaultHandler);
        }
        xMLReader.parse(inputSource);
    }

    public boolean isXIncludeAware() {
        throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + PdfOps.DOUBLE_QUOTE__TOKEN);
    }
}
