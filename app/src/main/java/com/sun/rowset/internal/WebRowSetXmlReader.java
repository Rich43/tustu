package com.sun.rowset.internal;

import com.sun.org.apache.xerces.internal.jaxp.JAXPConstants;
import com.sun.rowset.JdbcRowSetResourceBundle;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.MessageFormat;
import javax.sql.RowSetInternal;
import javax.sql.rowset.WebRowSet;
import javax.sql.rowset.spi.XmlReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/rowset/internal/WebRowSetXmlReader.class */
public class WebRowSetXmlReader implements XmlReader, Serializable {
    private JdbcRowSetResourceBundle resBundle;
    static final long serialVersionUID = -9127058392819008014L;

    public WebRowSetXmlReader() {
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // javax.sql.rowset.spi.XmlReader
    public void readXML(WebRowSet webRowSet, Reader reader) throws SQLException {
        try {
            InputSource inputSource = new InputSource(reader);
            XmlErrorHandler xmlErrorHandler = new XmlErrorHandler();
            XmlReaderContentHandler xmlReaderContentHandler = new XmlReaderContentHandler(webRowSet);
            SAXParserFactory sAXParserFactoryNewInstance = SAXParserFactory.newInstance();
            sAXParserFactoryNewInstance.setNamespaceAware(true);
            sAXParserFactoryNewInstance.setValidating(true);
            SAXParser sAXParserNewSAXParser = sAXParserFactoryNewInstance.newSAXParser();
            sAXParserNewSAXParser.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, "http://www.w3.org/2001/XMLSchema");
            XMLReader xMLReader = sAXParserNewSAXParser.getXMLReader();
            xMLReader.setEntityResolver(new XmlResolver());
            xMLReader.setContentHandler(xmlReaderContentHandler);
            xMLReader.setErrorHandler(xmlErrorHandler);
            xMLReader.parse(inputSource);
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new SQLException(this.resBundle.handleGetObject("wrsxmlreader.invalidcp").toString());
        } catch (SAXParseException e3) {
            System.out.println(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlreader.parseerr").toString(), e3.getMessage(), Integer.valueOf(e3.getLineNumber()), e3.getSystemId()));
            e3.printStackTrace();
            throw new SQLException(e3.getMessage());
        } catch (SAXException e4) {
            SAXException exception = e4;
            if (e4.getException() != null) {
                exception = e4.getException();
            }
            exception.printStackTrace();
            throw new SQLException(exception.getMessage());
        } catch (Throwable th) {
            throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlreader.readxml").toString(), th.getMessage()));
        }
    }

    public void readXML(WebRowSet webRowSet, InputStream inputStream) throws SQLException {
        try {
            InputSource inputSource = new InputSource(inputStream);
            XmlErrorHandler xmlErrorHandler = new XmlErrorHandler();
            XmlReaderContentHandler xmlReaderContentHandler = new XmlReaderContentHandler(webRowSet);
            SAXParserFactory sAXParserFactoryNewInstance = SAXParserFactory.newInstance();
            sAXParserFactoryNewInstance.setNamespaceAware(true);
            sAXParserFactoryNewInstance.setValidating(true);
            SAXParser sAXParserNewSAXParser = sAXParserFactoryNewInstance.newSAXParser();
            sAXParserNewSAXParser.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, "http://www.w3.org/2001/XMLSchema");
            XMLReader xMLReader = sAXParserNewSAXParser.getXMLReader();
            xMLReader.setEntityResolver(new XmlResolver());
            xMLReader.setContentHandler(xmlReaderContentHandler);
            xMLReader.setErrorHandler(xmlErrorHandler);
            xMLReader.parse(inputSource);
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new SQLException(this.resBundle.handleGetObject("wrsxmlreader.invalidcp").toString());
        } catch (SAXParseException e3) {
            System.out.println(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlreader.parseerr").toString(), Integer.valueOf(e3.getLineNumber()), e3.getSystemId()));
            System.out.println("   " + e3.getMessage());
            e3.printStackTrace();
            throw new SQLException(e3.getMessage());
        } catch (SAXException e4) {
            SAXException exception = e4;
            if (e4.getException() != null) {
                exception = e4.getException();
            }
            exception.printStackTrace();
            throw new SQLException(exception.getMessage());
        } catch (Throwable th) {
            throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlreader.readxml").toString(), th.getMessage()));
        }
    }

    @Override // javax.sql.RowSetReader
    public void readData(RowSetInternal rowSetInternal) {
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }
}
