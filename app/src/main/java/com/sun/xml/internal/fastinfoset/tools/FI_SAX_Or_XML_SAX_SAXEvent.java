package com.sun.xml.internal.fastinfoset.tools;

import com.sun.xml.internal.fastinfoset.Decoder;
import com.sun.xml.internal.fastinfoset.sax.SAXDocumentParser;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/tools/FI_SAX_Or_XML_SAX_SAXEvent.class */
public class FI_SAX_Or_XML_SAX_SAXEvent extends TransformInputOutput {
    @Override // com.sun.xml.internal.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream document, OutputStream events, String workingDirectory) throws Exception {
        if (!document.markSupported()) {
            document = new BufferedInputStream(document);
        }
        document.mark(4);
        boolean isFastInfosetDocument = Decoder.isFastInfosetDocument(document);
        document.reset();
        if (isFastInfosetDocument) {
            SAXDocumentParser parser = new SAXDocumentParser();
            SAXEventSerializer ses = new SAXEventSerializer(events);
            parser.setContentHandler(ses);
            parser.setProperty("http://xml.org/sax/properties/lexical-handler", ses);
            parser.parse(document);
            return;
        }
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        SAXParser parser2 = parserFactory.newSAXParser();
        SAXEventSerializer ses2 = new SAXEventSerializer(events);
        XMLReader reader = parser2.getXMLReader();
        reader.setProperty("http://xml.org/sax/properties/lexical-handler", ses2);
        reader.setContentHandler(ses2);
        if (workingDirectory != null) {
            reader.setEntityResolver(createRelativePathResolver(workingDirectory));
        }
        reader.parse(new InputSource(document));
    }

    @Override // com.sun.xml.internal.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream document, OutputStream events) throws Exception {
        parse(document, events, null);
    }

    public static void main(String[] args) throws Exception {
        FI_SAX_Or_XML_SAX_SAXEvent p2 = new FI_SAX_Or_XML_SAX_SAXEvent();
        p2.parse(args);
    }
}
