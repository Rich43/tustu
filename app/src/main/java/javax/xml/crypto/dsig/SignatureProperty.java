package javax.xml.crypto.dsig;

import java.util.List;
import javax.xml.crypto.XMLStructure;

/* loaded from: rt.jar:javax/xml/crypto/dsig/SignatureProperty.class */
public interface SignatureProperty extends XMLStructure {
    String getTarget();

    String getId();

    List getContent();
}
