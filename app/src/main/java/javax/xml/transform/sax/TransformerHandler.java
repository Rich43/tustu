package javax.xml.transform.sax;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:javax/xml/transform/sax/TransformerHandler.class */
public interface TransformerHandler extends ContentHandler, LexicalHandler, DTDHandler {
    void setResult(Result result) throws IllegalArgumentException;

    void setSystemId(String str);

    String getSystemId();

    Transformer getTransformer();
}
