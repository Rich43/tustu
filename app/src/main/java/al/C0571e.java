package al;

import am.AbstractC0573a;
import am.h;
import am.k;
import bH.C;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* renamed from: al.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:al/e.class */
public class C0571e {

    /* renamed from: a, reason: collision with root package name */
    private final XMLInputFactory f4934a = XMLInputFactory.newInstance();

    /* renamed from: b, reason: collision with root package name */
    private final DateFormat f4935b = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public Properties a(h hVar) {
        Properties properties = new Properties();
        AbstractC0573a abstractC0573aF = hVar.f();
        if (abstractC0573aF instanceof k) {
            a(properties, ((k) abstractC0573aF).e());
        }
        return properties;
    }

    public void a(Properties properties, String str) throws IOException {
        XMLStreamReader xMLStreamReaderCreateXMLStreamReader = null;
        try {
            try {
                xMLStreamReaderCreateXMLStreamReader = this.f4934a.createXMLStreamReader(new StringReader(str));
                while (xMLStreamReaderCreateXMLStreamReader.hasNext()) {
                    xMLStreamReaderCreateXMLStreamReader.next();
                    if (xMLStreamReaderCreateXMLStreamReader.isStartElement() && xMLStreamReaderCreateXMLStreamReader.getLocalName().equals("TX")) {
                        properties.setProperty("desc", xMLStreamReaderCreateXMLStreamReader.getElementText());
                    } else if (xMLStreamReaderCreateXMLStreamReader.isStartElement() && xMLStreamReaderCreateXMLStreamReader.getLocalName().equals("time_source")) {
                        properties.setProperty("time_source", xMLStreamReaderCreateXMLStreamReader.getElementText());
                    } else if (xMLStreamReaderCreateXMLStreamReader.isStartElement() && xMLStreamReaderCreateXMLStreamReader.getLocalName().equals("constants")) {
                        C.b("'constants' in XML content 'HDcomment' is not yet supported!");
                    } else if (xMLStreamReaderCreateXMLStreamReader.isStartElement() && xMLStreamReaderCreateXMLStreamReader.getLocalName().equals("UNITSPEC")) {
                        C.b("UNITSPEC in XML content 'HDcomment' is not yet supported!");
                    } else if (xMLStreamReaderCreateXMLStreamReader.isStartElement() && xMLStreamReaderCreateXMLStreamReader.getLocalName().equals("common_properties")) {
                        a(properties, xMLStreamReaderCreateXMLStreamReader);
                    }
                }
                if (xMLStreamReaderCreateXMLStreamReader != null) {
                    try {
                        xMLStreamReaderCreateXMLStreamReader.close();
                    } catch (XMLStreamException e2) {
                        C.a(e2.getMessage(), e2);
                        throw new IOException(e2.getMessage(), e2);
                    }
                }
            } catch (XMLStreamException e3) {
                C.a(e3.getMessage(), e3);
                throw new IOException(e3.getMessage(), e3);
            }
        } catch (Throwable th) {
            if (xMLStreamReaderCreateXMLStreamReader != null) {
                try {
                    xMLStreamReaderCreateXMLStreamReader.close();
                } catch (XMLStreamException e4) {
                    C.a(e4.getMessage(), e4);
                    throw new IOException(e4.getMessage(), e4);
                }
            }
            throw th;
        }
    }

    private void a(Properties properties, XMLStreamReader xMLStreamReader) throws XMLStreamException {
        xMLStreamReader.nextTag();
        while (true) {
            if (xMLStreamReader.isEndElement() && xMLStreamReader.getLocalName().equals("common_properties")) {
                return;
            }
            if (xMLStreamReader.isStartElement() && xMLStreamReader.getLocalName().equals("e")) {
                String attributeValue = xMLStreamReader.getAttributeValue(null, "name");
                String attributeValue2 = xMLStreamReader.getAttributeValue(null, "type");
                String elementText = xMLStreamReader.getElementText();
                if (attributeValue2 == null || attributeValue2.length() < 1 || attributeValue2.equalsIgnoreCase("string")) {
                    properties.setProperty(attributeValue, elementText);
                } else if (attributeValue2.equalsIgnoreCase(SchemaSymbols.ATTVAL_DECIMAL)) {
                    properties.setProperty(attributeValue, elementText);
                } else if (attributeValue2.equalsIgnoreCase(SchemaSymbols.ATTVAL_INTEGER)) {
                    properties.setProperty(attributeValue, elementText);
                } else if (attributeValue2.equalsIgnoreCase(SchemaSymbols.ATTVAL_FLOAT)) {
                    properties.setProperty(attributeValue, elementText);
                } else if (attributeValue2.equalsIgnoreCase("boolean")) {
                    properties.setProperty(attributeValue, elementText);
                } else if (attributeValue2.equalsIgnoreCase("datetime")) {
                    try {
                        properties.setProperty(attributeValue, this.f4935b.parse(elementText).toString());
                    } catch (ParseException e2) {
                        C.b(e2.getMessage());
                        properties.setProperty(attributeValue, elementText);
                    }
                } else {
                    properties.setProperty(attributeValue, elementText);
                }
            } else if (xMLStreamReader.isStartElement() && xMLStreamReader.getLocalName().equals("tree")) {
                C.b("'tree' in XML content 'common_properties' is not yet supported!");
            } else if (xMLStreamReader.isStartElement() && xMLStreamReader.getLocalName().equals(SchemaSymbols.ATTVAL_LIST)) {
                C.b("'list' in XML content 'common_properties' is not yet supported!");
            } else if (xMLStreamReader.isStartElement() && xMLStreamReader.getLocalName().equals("elist")) {
                C.b("'elist' in XML content 'common_properties' is not yet supported!");
            }
            xMLStreamReader.next();
        }
    }
}
