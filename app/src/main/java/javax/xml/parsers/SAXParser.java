package javax.xml.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.validation.Schema;
import org.icepdf.core.util.PdfOps;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:javax/xml/parsers/SAXParser.class */
public abstract class SAXParser {
    public abstract Parser getParser() throws SAXException;

    public abstract XMLReader getXMLReader() throws SAXException;

    public abstract boolean isNamespaceAware();

    public abstract boolean isValidating();

    public abstract void setProperty(String str, Object obj) throws SAXNotRecognizedException, SAXNotSupportedException;

    public abstract Object getProperty(String str) throws SAXNotRecognizedException, SAXNotSupportedException;

    protected SAXParser() {
    }

    public void reset() {
        throw new UnsupportedOperationException("This SAXParser, \"" + getClass().getName() + "\", does not support the reset functionality.  Specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + PdfOps.DOUBLE_QUOTE__TOKEN);
    }

    public void parse(InputStream is, HandlerBase hb) throws SAXException, IOException {
        if (is == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        InputSource input = new InputSource(is);
        parse(input, hb);
    }

    public void parse(InputStream is, HandlerBase hb, String systemId) throws SAXException, IOException {
        if (is == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        InputSource input = new InputSource(is);
        input.setSystemId(systemId);
        parse(input, hb);
    }

    public void parse(InputStream is, DefaultHandler dh) throws SAXException, IOException {
        if (is == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        InputSource input = new InputSource(is);
        parse(input, dh);
    }

    public void parse(InputStream is, DefaultHandler dh, String systemId) throws SAXException, IOException {
        if (is == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        InputSource input = new InputSource(is);
        input.setSystemId(systemId);
        parse(input, dh);
    }

    public void parse(String uri, HandlerBase hb) throws SAXException, IOException {
        if (uri == null) {
            throw new IllegalArgumentException("uri cannot be null");
        }
        InputSource input = new InputSource(uri);
        parse(input, hb);
    }

    public void parse(String uri, DefaultHandler dh) throws SAXException, IOException {
        if (uri == null) {
            throw new IllegalArgumentException("uri cannot be null");
        }
        InputSource input = new InputSource(uri);
        parse(input, dh);
    }

    public void parse(File f2, HandlerBase hb) throws SAXException, IOException {
        if (f2 == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        InputSource input = new InputSource(f2.toURI().toASCIIString());
        parse(input, hb);
    }

    public void parse(File f2, DefaultHandler dh) throws SAXException, IOException {
        if (f2 == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        InputSource input = new InputSource(f2.toURI().toASCIIString());
        parse(input, dh);
    }

    public void parse(InputSource is, HandlerBase hb) throws SAXException, IOException {
        if (is == null) {
            throw new IllegalArgumentException("InputSource cannot be null");
        }
        Parser parser = getParser();
        if (hb != null) {
            parser.setDocumentHandler(hb);
            parser.setEntityResolver(hb);
            parser.setErrorHandler(hb);
            parser.setDTDHandler(hb);
        }
        parser.parse(is);
    }

    public void parse(InputSource is, DefaultHandler dh) throws SAXException, IOException {
        if (is == null) {
            throw new IllegalArgumentException("InputSource cannot be null");
        }
        XMLReader reader = getXMLReader();
        if (dh != null) {
            reader.setContentHandler(dh);
            reader.setEntityResolver(dh);
            reader.setErrorHandler(dh);
            reader.setDTDHandler(dh);
        }
        reader.parse(is);
    }

    public Schema getSchema() {
        throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + PdfOps.DOUBLE_QUOTE__TOKEN);
    }

    public boolean isXIncludeAware() {
        throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + PdfOps.DOUBLE_QUOTE__TOKEN);
    }
}
