package com.sun.xml.internal.fastinfoset.tools;

import com.sun.xml.internal.fastinfoset.Decoder;
import com.sun.xml.internal.fastinfoset.dom.DOMDocumentParser;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import org.w3c.dom.Document;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/tools/FI_DOM_Or_XML_DOM_SAX_SAXEvent.class */
public class FI_DOM_Or_XML_DOM_SAX_SAXEvent extends TransformInputOutput {
    @Override // com.sun.xml.internal.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream document, OutputStream events, String workingDirectory) throws Exception {
        Document d2;
        if (!document.markSupported()) {
            document = new BufferedInputStream(document);
        }
        document.mark(4);
        boolean isFastInfosetDocument = Decoder.isFastInfosetDocument(document);
        document.reset();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        if (isFastInfosetDocument) {
            d2 = db.newDocument();
            DOMDocumentParser ddp = new DOMDocumentParser();
            ddp.parse(d2, document);
        } else {
            if (workingDirectory != null) {
                db.setEntityResolver(createRelativePathResolver(workingDirectory));
            }
            d2 = db.parse(document);
        }
        SAXEventSerializer ses = new SAXEventSerializer(events);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t2 = tf.newTransformer();
        t2.transform(new DOMSource(d2), new SAXResult(ses));
    }

    @Override // com.sun.xml.internal.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream document, OutputStream events) throws Exception {
        parse(document, events, null);
    }

    public static void main(String[] args) throws Exception {
        FI_DOM_Or_XML_DOM_SAX_SAXEvent p2 = new FI_DOM_Or_XML_DOM_SAX_SAXEvent();
        p2.parse(args);
    }
}
