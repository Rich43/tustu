package com.sun.xml.internal.fastinfoset.tools;

import com.sun.xml.internal.fastinfoset.Decoder;
import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/tools/FI_StAX_SAX_Or_XML_SAX_SAXEvent.class */
public class FI_StAX_SAX_Or_XML_SAX_SAXEvent extends TransformInputOutput {
    @Override // com.sun.xml.internal.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream document, OutputStream events) throws Exception {
        if (!document.markSupported()) {
            document = new BufferedInputStream(document);
        }
        document.mark(4);
        boolean isFastInfosetDocument = Decoder.isFastInfosetDocument(document);
        document.reset();
        if (isFastInfosetDocument) {
            StAXDocumentParser parser = new StAXDocumentParser();
            parser.setInputStream(document);
            SAXEventSerializer ses = new SAXEventSerializer(events);
            StAX2SAXReader reader = new StAX2SAXReader(parser, ses);
            reader.setLexicalHandler(ses);
            reader.adapt();
            return;
        }
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        SAXParser parser2 = parserFactory.newSAXParser();
        SAXEventSerializer ses2 = new SAXEventSerializer(events);
        parser2.setProperty("http://xml.org/sax/properties/lexical-handler", ses2);
        parser2.parse(document, ses2);
    }

    public static void main(String[] args) throws Exception {
        FI_StAX_SAX_Or_XML_SAX_SAXEvent p2 = new FI_StAX_SAX_Or_XML_SAX_SAXEvent();
        p2.parse(args);
    }
}
