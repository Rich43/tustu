package javax.xml.crypto.dsig;

import java.util.List;
import javax.xml.crypto.XMLStructure;

/* loaded from: rt.jar:javax/xml/crypto/dsig/XMLObject.class */
public interface XMLObject extends XMLStructure {
    public static final String TYPE = "http://www.w3.org/2000/09/xmldsig#Object";

    List getContent();

    String getId();

    String getMimeType();

    String getEncoding();
}
