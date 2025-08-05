package com.sun.xml.internal.fastinfoset.tools;

import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetResult;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Document;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/tools/XML_DOM_SAX_FI.class */
public class XML_DOM_SAX_FI extends TransformInputOutput {
    @Override // com.sun.xml.internal.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream document, OutputStream finf, String workingDirectory) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        if (workingDirectory != null) {
            db.setEntityResolver(createRelativePathResolver(workingDirectory));
        }
        Document d2 = db.parse(document);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t2 = tf.newTransformer();
        t2.transform(new DOMSource(d2), new FastInfosetResult(finf));
    }

    @Override // com.sun.xml.internal.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream document, OutputStream finf) throws Exception {
        parse(document, finf, null);
    }

    public static void main(String[] args) throws Exception {
        XML_DOM_SAX_FI p2 = new XML_DOM_SAX_FI();
        p2.parse(args);
    }
}
