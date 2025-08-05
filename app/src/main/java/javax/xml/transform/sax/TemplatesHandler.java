package javax.xml.transform.sax;

import javax.xml.transform.Templates;
import org.xml.sax.ContentHandler;

/* loaded from: rt.jar:javax/xml/transform/sax/TemplatesHandler.class */
public interface TemplatesHandler extends ContentHandler {
    Templates getTemplates();

    void setSystemId(String str);

    String getSystemId();
}
