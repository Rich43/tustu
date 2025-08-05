package com.sun.xml.internal.fastinfoset.tools;

import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSource;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/tools/FI_SAX_XML.class */
public class FI_SAX_XML extends TransformInputOutput {
    @Override // com.sun.xml.internal.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream finf, OutputStream xml) throws Exception {
        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.transform(new FastInfosetSource(finf), new StreamResult(xml));
    }

    public static void main(String[] args) throws Exception {
        FI_SAX_XML p2 = new FI_SAX_XML();
        p2.parse(args);
    }
}
