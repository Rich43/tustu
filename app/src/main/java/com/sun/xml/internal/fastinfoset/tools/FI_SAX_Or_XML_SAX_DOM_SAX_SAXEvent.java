package com.sun.xml.internal.fastinfoset.tools;

import com.sun.xml.internal.fastinfoset.Decoder;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSource;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/tools/FI_SAX_Or_XML_SAX_DOM_SAX_SAXEvent.class */
public class FI_SAX_Or_XML_SAX_DOM_SAX_SAXEvent extends TransformInputOutput {
    @Override // com.sun.xml.internal.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream document, OutputStream events, String workingDirectory) throws Exception {
        if (!document.markSupported()) {
            document = new BufferedInputStream(document);
        }
        document.mark(4);
        boolean isFastInfosetDocument = Decoder.isFastInfosetDocument(document);
        document.reset();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t2 = tf.newTransformer();
        DOMResult dr = new DOMResult();
        if (isFastInfosetDocument) {
            t2.transform(new FastInfosetSource(document), dr);
        } else if (workingDirectory != null) {
            SAXParser parser = getParser();
            XMLReader reader = parser.getXMLReader();
            reader.setEntityResolver(createRelativePathResolver(workingDirectory));
            SAXSource source = new SAXSource(reader, new InputSource(document));
            t2.transform(source, dr);
        } else {
            t2.transform(new StreamSource(document), dr);
        }
        SAXEventSerializer ses = new SAXEventSerializer(events);
        t2.transform(new DOMSource(dr.getNode()), new SAXResult(ses));
    }

    @Override // com.sun.xml.internal.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream document, OutputStream events) throws Exception {
        parse(document, events, null);
    }

    private SAXParser getParser() {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        try {
            return saxParserFactory.newSAXParser();
        } catch (Exception e2) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        FI_SAX_Or_XML_SAX_DOM_SAX_SAXEvent p2 = new FI_SAX_Or_XML_SAX_DOM_SAX_SAXEvent();
        p2.parse(args);
    }
}
